package com.example.btl_android_n2.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.DAO.BookingDAO;
import com.example.btl_android_n2.DAO.NotificationDAO;
import com.example.btl_android_n2.DAO.ReviewDAO;
import com.example.btl_android_n2.DAO.RoomDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Booking;
import com.example.btl_android_n2.Models.Notification;
import com.example.btl_android_n2.Models.Review;
import com.example.btl_android_n2.Models.Room;
import com.example.btl_android_n2.Activity.RoomDetailActivity;
import com.example.btl_android_n2.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<Booking> bookings;
    private RoomDAO roomDAO;
    private Context context;
    private int userId;

    public BookingAdapter(Context context, int userId, List<Booking> bookings, RoomDAO roomDAO) {
        this.context = context;
        this.bookings = bookings;
        this.roomDAO = roomDAO;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Booking booking = bookings.get(position);

        // Lấy thông tin phòng từ RoomDAO
        Room room = roomDAO.getRoomById(booking.getRoomId());

        if (room != null) {
            holder.tvRoomName.setText(room.getRoomName());
            holder.tvRoomType.setText(room.getRoomType());

            // Hiển thị ảnh đầu tiên từ danh sách ảnh
            String firstImagePath = (room.getImage() != null && !room.getImage().isEmpty())
                    ? room.getImage().split(",")[0]
                    : "images/hotel5s_default.png"; // Thay thế bằng ảnh mặc định

            try {
                InputStream inputStream = context.getAssets().open(firstImagePath);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                holder.imgRoom.setImageDrawable(drawable);
            } catch (IOException e) {
                holder.imgRoom.setImageResource(R.drawable.vungtau); // Sử dụng ảnh mặc định
                Log.e("RoomAdapter", "Error loading image: " + firstImagePath, e);
            }
        }

        // Thiết lập dữ liệu cho các view khác
        holder.tvBookingCreatedDate.setText(booking.getBookingDate());
        holder.tvRoomPrice.setText(String.format("Giá: %,d VND", (int) booking.getTotalPrice()));

        // Kiểm tra ngày checkout
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        if (isBeforeDate(booking.getCheckOutDate(), currentDate)){
            holder.tvStatus.setText("Đã checkout");
        }
        else {
            // Thiết lập trạng thái đặt phòng
            holder.tvStatus.setText(booking.getStatus() ? "Đã đặt" : "Đã huỷ");
        }

        holder.tvStatus.setTextColor(
                booking.getStatus()
                        ? ContextCompat.getColor(context, R.color.green) // Màu xanh cho "Đã đặt"
                        : ContextCompat.getColor(context, R.color.red)   // Màu đỏ cho "Đã huỷ"
        );

        holder.tvDates.setText(booking.getCheckInDate() + " - " + booking.getCheckOutDate());

        // Kiểm tra nếu có thể huỷ
        if (isCancelable(booking.getCheckInDate())) {
            holder.imgViewCancelRoom.setVisibility(View.VISIBLE);
        } else {
            holder.imgViewCancelRoom.setVisibility(View.GONE);
        }

        // Xử lý sự kiện click huỷ
        holder.imgViewCancelRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng holder.getAdapterPosition() để lấy vị trí mới nhất
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) return;

                Booking currentBooking = bookings.get(currentPosition);

                // Hiển thị dialog xác nhận
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận huỷ phòng")
                        .setMessage("Bạn có muốn huỷ phòng này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Cập nhật trạng thái đặt phòng và phòng
                                currentBooking.setStatus(false);
                                room.setStatus("Còn trống");

                                BookingDAO bookingDAO = new BookingDAO(new DatabaseHelper(context));

                                // Cập nhật trạng thái đặt phòng và trạng thái phòng
                                boolean isBookingUpdated = bookingDAO.updateBookingStatus(currentBooking) > 0;
                                boolean isRoomUpdated = roomDAO.updateRoomStatus(room) > 0;

                                // Thêm thông báo
                                String pattern = "dd-MM-yyyy";
                                String dateInString =new SimpleDateFormat(pattern).format(new Date());
                                Notification notification = new Notification(0, userId, "Huỷ phòng thành công phòng " + room.getRoomName(), dateInString, 0);
                                NotificationDAO notificationDAO = new NotificationDAO(new DatabaseHelper(context));
                                boolean notiSuccess = notificationDAO.insertNotification(notification) > 0;

                                if (isBookingUpdated && isRoomUpdated && notiSuccess) {
                                    notifyItemChanged(currentPosition);
                                    Toast.makeText(context, "Đã huỷ phòng thành công.", Toast.LENGTH_SHORT).show();
                                } else {
                                    currentBooking.setStatus(true);
                                    room.setStatus("Đã đặt");
                                    Toast.makeText(context, "Không thể huỷ phòng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

        // Xử lý click để đánh giá
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin roomId từ booking
                int roomId = booking.getRoomId();

                // Lấy ngày hiện tại và tính toán ngày check-in, check-out
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();

                // Ngày check-in là ngày hiện tại + 1 ngày
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                String checkInDate = sdf.format(calendar.getTime());

                // Ngày check-out là ngày hiện tại + 2 ngày
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                String checkOutDate = sdf.format(calendar.getTime());

                // Chuyển đến RoomDetailActivity với các thông tin cần thiết
                Intent intent = new Intent(context, RoomDetailActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("checkInDate", checkInDate);
                intent.putExtra("checkOutDate", checkOutDate);

                // Bắt đầu activity
                context.startActivity(intent);
            }
        });

        holder.tvReviewComment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Kiểm tra ngày checkout
                        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        if (!isBeforeDate(booking.getCheckOutDate(), currentDate)) {
                            Toast.makeText(context, "Bạn chỉ có thể đánh giá sau khi đã check-out!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Hiển thị dialog đánh giá
                        showReviewDialog(booking);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingCreatedDate, tvRoomName, tvRoomType, tvRoomPrice, tvStatus, tvDates, tvReviewComment;
        ImageView imgViewCancelRoom, imgRoom;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBookingCreatedDate = itemView.findViewById(R.id.txBookingCreatedDate);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDates = itemView.findViewById(R.id.tvDates);
            tvReviewComment = itemView.findViewById(R.id.tvReviewComment);
            imgViewCancelRoom = itemView.findViewById(R.id.imgViewCancelRoom);
            imgRoom = itemView.findViewById(R.id.imgRoom);
        }
    }

    // Hàm kiểm tra nếu ngày checkInDate >= 2 ngày sau ngày hiện tại
    public boolean isCancelable(String checkInDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date checkInDate = sdf.parse(checkInDateStr);
            Date currentDate = new Date();

            // Cộng 2 ngày vào ngày hiện tại
            currentDate.setTime(currentDate.getTime() + (2 * 24 * 60 * 60 * 1000));

            return checkInDate.after(currentDate); // Kiểm tra nếu checkInDate sau 2 ngày
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isBeforeDate(String date1, String date2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            return d1.before(d2) || d1.equals(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showReviewDialog(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_review, null);
        builder.setView(view);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText etComment = view.findViewById(R.id.etComment);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating = ratingBar.getRating();
                String comment = etComment.getText().toString().trim();

                if (rating == 0 || comment.isEmpty()) {
                    Toast.makeText(context, "Vui lòng đánh giá và nhập bình luận!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo đối tượng Review
                Review review = new Review();
                review.setUserId(userId);
                review.setRoomId(booking.getRoomId());
                review.setRating(rating);
                review.setComment(comment);
                review.setCreatedDate(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

                // Thêm đánh giá vào database
                ReviewDAO reviewDAO = new ReviewDAO(new DatabaseHelper(context));
                if (reviewDAO.insertReview(review) > 0) {
                    Toast.makeText(context, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Không thể đánh giá, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Đóng dialog
            }
        });

    }

}

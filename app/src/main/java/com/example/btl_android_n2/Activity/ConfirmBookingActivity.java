package com.example.btl_android_n2.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android_n2.DAO.BookingDAO;
import com.example.btl_android_n2.DAO.BookingHistoryDAO;
import com.example.btl_android_n2.DAO.NotificationDAO;
import com.example.btl_android_n2.DAO.PaymentHistoryDAO;
import com.example.btl_android_n2.DAO.RoomDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Booking;
import com.example.btl_android_n2.Models.BookingHistory;
import com.example.btl_android_n2.Models.Notification;
import com.example.btl_android_n2.Models.PaymentHistory;
import com.example.btl_android_n2.Models.Room;
import com.example.btl_android_n2.R;
import com.example.btl_android_n2.Util.RoomUtil;
import com.example.btl_android_n2.Util.SessionManager;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmBookingActivity extends AppCompatActivity {

    private TextView tvRoomName, tvRoomAddress, tvCheckInDate, tvCheckOutDate, tvTotalPrice, tvPaymentMethod, tvFullName, tvPhoneNumber, tvEmail, tvRating;
    private Button btnConfirmBooking;
    private ImageView btnBack;
    private int roomId, userId;
    private String checkInDate, checkOutDate,fullName, phoneNumber, email, paymentMethod;
    private Room currentRoom;
    private SessionManager sessionManager;
    private RoomDAO roomDAO;
    private ImageView imgRoom;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            // Khởi tạo SessionManager và RoomDAO
            sessionManager = new SessionManager(this);
            roomDAO = new RoomDAO(new DatabaseHelper(this));

            initViews();
            getIntentData();

            currentRoom = roomDAO.getRoomById(roomId);
            updateUI();

            if(btnBack != null) {
                btnBack.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }
                );
            }

            btnConfirmBooking.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            confirmBooking();
                        }
                    }
            );

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Khởi tạo các View trong layout.
     */
    private void initViews() {
        imgRoom = findViewById(R.id.confirm_imgRoom); // ID của ImageView phòng
        tvRoomName = findViewById(R.id.confirm_tvRoomName);
        tvRoomAddress = findViewById(R.id.confirm_tvRoomAddress);
        tvCheckInDate = findViewById(R.id.confirm_tvCheckInDate);
        tvCheckOutDate = findViewById(R.id.confirm_tvCheckOutDate);
        tvTotalPrice = findViewById(R.id.confirm_tvTotalPrice);
        tvFullName = findViewById(R.id.confirm_etFullName);
        tvPhoneNumber = findViewById(R.id.confirm_etPhoneNumber);
        tvEmail = findViewById(R.id.confirm_etEmail);
        tvPaymentMethod = findViewById(R.id.confirm_tvPaymentMethod);
        btnConfirmBooking = findViewById(R.id.confirm_btnConfirmBooking);
        tvRating = findViewById(R.id.confirm_tvRating);
        btnBack = findViewById(R.id.confirm_imgBack);
    }

    private void getIntentData() {
        // Nhận thông tin từ Intent
        Intent intent = getIntent();
        roomId = intent.getIntExtra("roomId", -1);
        userId = sessionManager.getUserId();
        fullName = intent.getStringExtra("fullName");
        phoneNumber = intent.getStringExtra("phoneNumber");
        email = intent.getStringExtra("email");
        paymentMethod = intent.getStringExtra("paymentMethod");
        checkInDate = intent.getStringExtra("checkInDate");
        checkOutDate = intent.getStringExtra("checkOutDate");

        if (roomId == -1 || fullName == null || phoneNumber == null || email == null) {
            Toast.makeText(this, "Lỗi dữ liệu. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }

    /**
     * Cập nhật UI với thông tin nhận được từ Intent.
     */
    private void updateUI() {
        if (currentRoom != null) {
            loadRoomImage();
            // Hiển thị thông tin phòng
            tvRoomName.setText(currentRoom.getRoomName());
            tvRoomAddress.setText(currentRoom.getAddress());
            tvCheckInDate.setText("Nhận phòng: " + checkInDate);
            tvCheckOutDate.setText("Trả phòng: " + checkOutDate);
            tvRating.setText(String.format("%.1f ★", currentRoom.getReview()));

            // Tính tổng giá phòng
            totalPrice = RoomUtil.calculateTotalPrice(checkInDate, checkOutDate, currentRoom);
            DecimalFormat decimalFormat = new DecimalFormat();

            tvTotalPrice.setText("Tổng tiền: " + decimalFormat.format(totalPrice) + " VND");

            // Hiển thị thông tin khách hàng và thanh toán
            tvFullName.setText(fullName);
            tvPhoneNumber.setText(phoneNumber);
            tvEmail.setText(email);
            tvPaymentMethod.setText(paymentMethod);
        }
    }

    private void loadRoomImage() {
        // Hiển thị hình ảnh phòng 2
        String fistImgPath = currentRoom.getImage().split(",")[0];
        try {
            InputStream inputStream = getAssets().open(fistImgPath);
            Drawable drawable2 = Drawable.createFromStream(inputStream, null);
            imgRoom.setImageDrawable(drawable2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmBooking() {
        // Kiểm tra thông tin đầu vào
        if (currentRoom == null || checkInDate == null || checkOutDate == null || totalPrice == 0) {
            Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String dateBooking = sdf.format(currentDate);

        // Tạo đối tượng Booking và lưu vào cơ sở dữ liệu
        Booking booking = new Booking(0, userId, roomId, dateBooking, checkInDate, checkOutDate, totalPrice, true, "Không có yêu cầu");

        // Lưu Booking vào bảng `Bookings`
        BookingDAO bookingDAO = new BookingDAO(new DatabaseHelper(this));
        long isBookingSuccess = bookingDAO.insertBooking(booking);

        // Cập nhật trạng thái phòng trong bảng `Rooms` (đánh dấu phòng đã được đặt)
        currentRoom.setStatus("Booked");
        int isRoomUpdated = roomDAO.updateRoom(currentRoom);

        // Thêm mới lịch sử đặt phòng
        BookingHistory bookingHistory = new BookingHistory(0, userId, roomId, "Đặt phòng", dateBooking);
        BookingHistoryDAO bookingHistoryDAO = new BookingHistoryDAO(new DatabaseHelper(this));

        long bookingHistorySuccess = bookingHistoryDAO.insertBookingHistory(bookingHistory);

        // Thêm mới lịch sử thanh toán
        if(isBookingSuccess != -1) {
            int bookingId = (int) isBookingSuccess;
            PaymentHistory paymentHistory = new PaymentHistory(0, bookingId, dateBooking, totalPrice, tvPaymentMethod.getText().toString());
            PaymentHistoryDAO paymentHistoryDAO = new PaymentHistoryDAO(new DatabaseHelper(this));
            paymentHistoryDAO.insertPaymentHistory(paymentHistory);
        }else {
            return;
        }

        // Thêm thông báo
        Notification notification = new Notification(0, userId, "Đặt phòng thành công phòng " + currentRoom.getRoomName(), dateBooking, 0);
        NotificationDAO notificationDAO = new NotificationDAO(new DatabaseHelper(this));
        long notiSuccess = notificationDAO.insertNotification(notification);

        if (isBookingSuccess != -1 && isRoomUpdated != -1 && bookingHistorySuccess != -1 && notiSuccess != -1) {

            // Gửi email xác nhận đặt phòng trong nền
//            new BookingConfirmationEmailService(
//                    email, fullName, currentRoom.getRoomName(), checkInDate, checkOutDate, totalPrice
//            ).sendEmail(); // Sử dụng execute để chạy trong nền với AsyncTask

            Toast.makeText(this, "Booking successful!", Toast.LENGTH_SHORT).show();
            finish(); // Hoặc điều hướng đến màn hình khác
        } else {
            Toast.makeText(this, "Booking failed! Please try again.", Toast.LENGTH_SHORT).show();
            Log.e("BookingProcess", "Failed at some step of the booking process.");
        }

    }
}
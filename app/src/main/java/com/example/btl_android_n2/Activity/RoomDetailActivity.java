package com.example.btl_android_n2.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.Adapter.ReviewAdapter;
import com.example.btl_android_n2.DAO.ReviewDAO;
import com.example.btl_android_n2.Models.Review;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android_n2.DAO.RoomDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Room;
import com.example.btl_android_n2.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomDetailActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private TextView tvRoomName, tvRoomType, tvRoomPrice, tvRoomRating, tvRoomAddress, tvRoomDescriptionContent, tvCheckInTimeDetail, tvCheckOutTimeDetail, errorMessageTextView;
    private ImageView imgRoom, btnNext, btnPre, btnBack;
    private List<String> imageList = new ArrayList<>();
    private int currentImageIndex = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Button btnChoose;

    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private ReviewDAO reviewDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_detail);

        // Khởi tạo các view
        initControls();

        setDefaultDates();

        // Lấy RoomId từ Intent
        int roomId = getIntent().getIntExtra("roomId", -1);
        String checkInDate = getIntent().getStringExtra("checkInDate"); // Lấy ngày nhận
        String checkOutDate = getIntent().getStringExtra("checkOutDate"); // Lấy ngày trả

        if (roomId != -1) {
            loadRoomDetail(roomId, checkInDate, checkOutDate);
        }

        // Thiết lập bản đồ
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        getALlReviews(roomId);

        // Xử lý nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra tính hợp lệ của ngày nhận và ngày trả
                if (!areDatesValid(tvCheckInTimeDetail.getText().toString(), tvCheckOutTimeDetail.getText().toString())) {
                    return;  // Nếu ngày không hợp lệ, không thực hiện tiếp
                }

                // Chuyển sang màn hình Booking với các thông tin cần thiết
                Intent intent = new Intent(RoomDetailActivity.this, BookingActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("checkInDate", tvCheckInTimeDetail.getText().toString());
                intent.putExtra("checkOutDate", tvCheckOutTimeDetail.getText().toString());
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initControls() {
        tvRoomName = findViewById(R.id.tvRoomNameDetail);
        tvRoomType = findViewById(R.id.tvRoomTypeDetail);
        tvRoomPrice = findViewById(R.id.tvRoomPriceDetail);
        tvRoomRating = findViewById(R.id.tvRoomRatingDetail);
        tvRoomAddress = findViewById(R.id.tvRoomAddressDetail);
        tvRoomDescriptionContent = findViewById(R.id.tvRoomDescriptionDetail);
        btnBack = findViewById(R.id.btnBackSearch);
        imgRoom = findViewById(R.id.imgRoomDetail);
        btnNext = findViewById(R.id.btnNextDetail);
        btnPre = findViewById(R.id.btnPreDetail);
        btnChoose = findViewById(R.id.btnChoose);
        tvCheckInTimeDetail = findViewById(R.id.tvCheckInTimeDetail); // Ngày nhận
        tvCheckOutTimeDetail = findViewById(R.id.tvCheckOutTimeDetail); // Ngày trả
        errorMessageTextView = findViewById(R.id.errorMessageTextView);
        // Sự kiện cho "Ngày Nhận"
        tvCheckInTimeDetail.setOnClickListener(view -> showDatePickerDialog(tvCheckInTimeDetail));

        // Sự kiện cho "Ngày Trả"
        tvCheckOutTimeDetail.setOnClickListener(view -> showDatePickerDialog(tvCheckOutTimeDetail));
    }


    private void setDefaultDates() {
        Calendar calendar = Calendar.getInstance();

        // Định dạng ngày hiện tại cho "Ngày Nhận"
        String currentDate = dateFormat.format(calendar.getTime());
        tvCheckInTimeDetail.setText(currentDate);

        // Định dạng ngày hôm sau cho "Ngày Trả"
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String nextDate = dateFormat.format(calendar.getTime());
        tvCheckOutTimeDetail.setText(nextDate);
    }

    private void setupImageNavigation(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            imageList = Arrays.asList(imagePath.split(","));
        } else {
            imageList = Collections.singletonList("images/hotel5s_default.png"); // Ảnh mặc định
        }

        // Hiển thị ảnh đầu tiên
        loadImage(imageList.get(currentImageIndex));

        // Sự kiện bấm nút Previous
        btnPre.setOnClickListener(v -> {
            if (currentImageIndex > 0) {
                currentImageIndex--;
                loadImage(imageList.get(currentImageIndex));
            } else {
                Toast.makeText(this, "Đây là ảnh đầu tiên!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện bấm nút Next
        btnNext.setOnClickListener(v -> {
            if (currentImageIndex < imageList.size() - 1) {
                currentImageIndex++;
                loadImage(imageList.get(currentImageIndex));
            } else {
                Toast.makeText(this, "Đây là ảnh cuối cùng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRoomDetail(int roomId, String checkInDate, String checkOutDate) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            RoomDAO roomDAO = new RoomDAO(new DatabaseHelper(this));
            Room room = roomDAO.getRoomById(roomId);

            handler.post(() -> {
                if (room != null) {
                    tvRoomName.setText(room.getRoomName());
                    tvRoomType.setText(room.getRoomType());
                    tvRoomPrice.setText(String.format("Giá: %,d VND", (int) room.getPrice()));
                    tvRoomRating.setText(String.format("%.1f ★", room.getReview()));
                    tvRoomDescriptionContent.setText(room.getDescription());
                    tvRoomAddress.setText(room.getAddress());

                    tvCheckInTimeDetail.setText(checkInDate != null ? checkInDate : "Chọn ngày");
                    tvCheckOutTimeDetail.setText(checkOutDate != null ? checkOutDate : "Chọn ngày");

                    // Thiết lập ảnh và điều hướng
                    setupImageNavigation(room.getImage());
                }
            });
        });
    }

    private void loadImage(String imagePath) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Drawable drawable = null;
            try {
                InputStream inputStream = getAssets().open(imagePath);
                drawable = Drawable.createFromStream(inputStream, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Drawable finalDrawable = drawable;
            handler.post(() -> {
                if (finalDrawable != null) {
                    imgRoom.setImageDrawable(finalDrawable);
                } else {
                    imgRoom.setImageResource(R.drawable.ic_hotel); // Ảnh mặc định khi lỗi
                }
            });
        });
    }

    // Kiểm tra tính hợp lệ của ngày nhận và ngày trả
    private boolean areDatesValid(String checkInDate, String checkOutDate) {
        // Ẩn thông báo lỗi trước khi kiểm tra
        errorMessageTextView.setVisibility(View.GONE);

        // Kiểm tra xem người dùng đã chọn ngày hay chưa
        if (checkInDate == null || checkOutDate == null ||
                "Chọn ngày".equals(checkInDate) || "Chọn ngày".equals(checkOutDate)) {
            // Hiển thị thông báo lỗi nếu chưa chọn ngày
            errorMessageTextView.setVisibility(View.VISIBLE);
            errorMessageTextView.setText("Vui lòng chọn ngày nhận và trả phòng!");
            return false;  // Dừng thực thi hàm nếu chưa chọn ngày
        }

        try {
            Date checkIn = dateFormat.parse(checkInDate);
            Date checkOut = dateFormat.parse(checkOutDate);
            Date currentDate = new Date();

            // Kiểm tra: Ngày nhận phải lớn hơn ngày hiện tại và ngày trả phải lớn hơn ngày nhận
            if (checkIn.after(currentDate) && checkOut.after(checkIn)) {
                return true;
            } else {
                // Hiển thị thông báo lỗi nếu ngày không hợp lệ
                errorMessageTextView.setVisibility(View.VISIBLE);
                errorMessageTextView.setText("Ngày nhận phải lớn hơn ngày hiện tại và ngày trả phải lớn hơn ngày nhận.");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi nếu có lỗi trong việc phân tích ngày
            errorMessageTextView.setVisibility(View.VISIBLE);
            errorMessageTextView.setText("Có lỗi xảy ra, vui lòng thử lại!");
            return false;  // Trả về false nếu có lỗi
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        ProgressBar progressBar = findViewById(R.id.progressBar); // Khởi tạo ProgressBar
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar

        int roomId = getIntent().getIntExtra("roomId", -1);

        if (roomId != -1) {
            RoomDAO roomDAO = new RoomDAO(new DatabaseHelper(this));
            Room room = roomDAO.getRoomById(roomId);

            if (room != null) {
                String shortAddress = room.getAddress().split(",")[0];
                getLocationFromAddress(shortAddress, location -> {
                    progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi đã có kết quả

                    if (location != null) {
                        mMap.addMarker(new MarkerOptions().position(location).title(room.getRoomName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                    } else {
                        Toast.makeText(this, "Không tìm thấy địa chỉ trên bản đồ!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar nếu Room không tồn tại
            }
        } else {
            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar nếu RoomId không hợp lệ
        }
    }

    private void getLocationFromAddress(String address, OnLocationFoundListener listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            LatLng location = null;
            try {
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address result = addresses.get(0);
                    location = new LatLng(result.getLatitude(), result.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            LatLng finalLocation = location;
            handler.post(() -> {
                if (listener != null) {
                    listener.onLocationFound(finalLocation);
                }
            });
        });
    }

    /**
     * Hiển thị DatePickerDialog để chọn ngày.
     *
     * @param editText EditText sẽ hiển thị ngày đã chọn.
     */
    private void showDatePickerDialog(final TextView editText) {
        Calendar calendar = Calendar.getInstance();

        // Lấy giá trị ngày từ EditText nếu có, nếu không thì dùng ngày hiện tại
        String selectedDate = editText.getText().toString();
        if (!selectedDate.isEmpty()) {
            try {
                // Nếu EditText có giá trị, parse ngày từ EditText
                calendar.setTime(dateFormat.parse(selectedDate));
            } catch (Exception e) {
                // Nếu có lỗi khi phân tích (do định dạng không hợp lệ), dùng ngày hiện tại
                calendar = Calendar.getInstance();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Định dạng ngày thành "dd/MM/yyyy"
                    String date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(date);
                }, year, month, day);

        datePickerDialog.show();
        Log.d("MainActivity", "DatePickerDialog shown for: " + editText.getId());
    }

    // Listener interface for location results
    interface OnLocationFoundListener {
        void onLocationFound(LatLng location);
    }

    private void getALlReviews(int roomId){
        // Ánh xạ RecyclerView
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);

        // Thiết lập DAO
        reviewDAO = new ReviewDAO(new DatabaseHelper(this));

        // Lấy danh sách đánh giá từ DAO
        List<Review> reviewList = reviewDAO.getViewByRoomId(roomId);

        // Thiết lập RecyclerView
        reviewAdapter = new ReviewAdapter(this, reviewList);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
    }
}
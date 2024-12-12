package com.example.btl_android_n2.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android_n2.DAO.RoomDAO;
import com.example.btl_android_n2.DAO.UserDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Room;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.btl_android_n2.Models.User;
import com.example.btl_android_n2.R;
import com.example.btl_android_n2.Util.RoomUtil;
import com.example.btl_android_n2.Util.SessionManager;


public class BookingActivity extends AppCompatActivity {

    private ImageView imgRoom, btnBack;
    private TextView tvRoomName, tvRoomAddress, tvCheckInDate, tvCheckOutDate, tvRoomPrice, tvTotalPrice;
    private EditText etFullName, etPhoneNumber, etEmail;
    private RadioGroup paymentMethodGroup;
    private Button btnBookRoom;

    private Room currentRoom;

    private DecimalFormat decimalFormat;

    private String checkInDate;
    private String checkOutDate;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private SessionManager sessionManager;
    private int roomId, userLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);

        // Khởi tạo các View trong layout
        initViews();

        // Lấy RoomId, CheckInDate, CheckOutDate từ Intent
        initIntent();

        // Lấy thông tin phòng từ RoomDAO và hiển thị lên giao diện
        updateUI();

        // Thiết lập sự kiện khi click nút Đặt phòng
        btnBookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookRoom();
            }
        });

        // Xử lý nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Khởi tạo các View trong layout.
     */
    private void initViews() {
        imgRoom = findViewById(R.id.booking_imgRoom);
        tvRoomName = findViewById(R.id.booking_tvRoomName);
        tvRoomAddress = findViewById(R.id.booking_tvRoomAddress);
        tvCheckInDate = findViewById(R.id.booking_tvCheckInDate);
        tvCheckOutDate = findViewById(R.id.booking_tvCheckOutDate);
        tvRoomPrice = findViewById(R.id.booking_tvRoomPrice);
        tvTotalPrice = findViewById(R.id.booking_tvTotalPrice);
        etFullName = findViewById(R.id.booking_etFullName);
        etPhoneNumber = findViewById(R.id.booking_etPhoneNumber);
        etEmail = findViewById(R.id.booking_etEmail);
        paymentMethodGroup = findViewById(R.id.booking_paymentMethodGroup);
        btnBookRoom = findViewById(R.id.booking_btnBookRoom);
        btnBack = findViewById(R.id.booking_btnBack);
        decimalFormat = new DecimalFormat("#,###");
    }

    private void initIntent() {
        RoomDAO roomDAO = new RoomDAO(new DatabaseHelper(this));

        checkInDate = getIntent().getStringExtra("checkInDate");
        checkOutDate = getIntent().getStringExtra("checkOutDate");

        roomId = getIntent().getIntExtra("roomId", -1);

        // Lấy thông tin phòng theo roomId từ RoomDAO
        currentRoom = roomDAO.getRoomById(roomId);

        // Nếu không có giá trị, sử dụng ngày hiện tại và ngày hiện tại + 1
        if (checkInDate == null) {
            checkInDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        }
        if (checkOutDate == null) {
            // Sử dụng calendar lấy date
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            checkOutDate = simpleDateFormat.format(calendar.getTime());
        }
    }

    /**
     * Lấy thông tin phòng từ RoomDAO và hiển thị lên giao diện.
     */
    private void updateUI() {
        if (currentRoom != null) {
            // Hiển thị ảnh phòng
            loadRoomImage();

            // Hiển thị thông tin phòng
            tvRoomName.setText(currentRoom.getRoomName());
            tvRoomAddress.setText(currentRoom.getAddress());

            // Hiển thị ngày nhận, ngày trả
            tvCheckInDate.setText(checkInDate);
            tvCheckOutDate.setText(checkOutDate);

            // Hiển thị giá phòng
            tvRoomPrice.setText("Giá cho 1 đêm: " + decimalFormat.format(Math.round(currentRoom.getPrice())) + " VND");

            // Tính toán tổng tiền
            double totalPrice = RoomUtil.calculateTotalPrice(checkInDate, checkOutDate, currentRoom);
            tvTotalPrice.setText("Tổng tiền: " + decimalFormat.format(totalPrice) + " VND");

            // Hiển thị thông tin khách hàng
            loadUserInfo();
        }
    }

    /**
     * Hiển thị thông tin khách hàng
     */
    private void loadUserInfo() {
        int userId = sessionManager.getUserId(); // Lấy userId từ SessionManager

        // Lấy thông tin người dùng từ cơ sở dữ liệu (có thể dùng UserDAO)
        UserDAO userDAO = new UserDAO(new DatabaseHelper(this));

        User currentUser = userDAO.getUserById(userId); // Giả sử có phương thức getUserById trong UserDAO

        if (currentUser != null) {
            // Hiển thị thông tin người dùng lên UI
            // Hiển thị Full Name lên TextView
            etFullName.setText(currentUser.getFullName());

            etEmail.setText(currentUser.getEmail());

            etPhoneNumber.setText(currentUser.getPhoneNumber());

        }
    }

    /**
     * Tải và hiển thị ảnh phòng.
     * Giả sử ảnh là URL, bạn có thể thay thế bằng cách tải ảnh từ URL hoặc từ tài nguyên
     */
    public void loadRoomImage() {
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

    /**
     * Bắt sự kiện đặt phòng, khởi tạo Intent và truyền các thông tin cần thiết.
     */
    private void bookRoom() {
        // Lấy thông tin từ các view
        String fullName = etFullName.getText().toString();  // Họ tên khách hàng
        String phoneNumber = etPhoneNumber.getText().toString();  // Số điện thoại khách hàng
        String email = etEmail.getText().toString();  // Email khách hàng (nếu có)
        int userId = sessionManager.getUserId();

        // Lấy thông tin hình thức thanh toán từ RadioGroup
        int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
        RadioButton selectedPaymentMethod = findViewById(selectedPaymentMethodId);
        String paymentMethod = selectedPaymentMethod != null ? selectedPaymentMethod.getText().toString() : "";  // Hình thức thanh toán

        // Kiểm tra xem các thông tin cần thiết có hợp lệ không
        if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || paymentMethod.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Intent để chuyển sang màn xác nhận đặt phòng
        Intent intent = new Intent(BookingActivity.this, ConfirmBookingActivity.class);
        intent.putExtra("roomId", roomId);  // Truyền roomId
        intent.putExtra("userId", userId);
        intent.putExtra("fullName", fullName);  // Truyền họ tên khách hàng
        intent.putExtra("checkInDate", tvCheckInDate.getText().toString());
        intent.putExtra("checkOutDate", tvCheckOutDate.getText().toString());
        intent.putExtra("phoneNumber", phoneNumber);  // Truyền số điện thoại khách hàng
        intent.putExtra("email", email);  // Truyền email khách hàng
        intent.putExtra("paymentMethod", paymentMethod);  // Truyền hình thức thanh toán

        // Bắt đầu Activity xác nhận đặt phòng
        startActivity(intent);
    }

}
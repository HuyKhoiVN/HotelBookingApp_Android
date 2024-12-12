package com.example.btl_android_n2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android_n2.MainActivity;
import com.example.btl_android_n2.Models.User;
import com.example.btl_android_n2.R;
import com.example.btl_android_n2.Util.SessionManager;
import com.example.btl_android_n2.DAO.UserDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private UserDAO userDAO;
    private int userId;

    // Controls
    private TextView tvUsername, txtInvalidPassword;
    private EditText editName, editPhone, editEmail, editAddress, editDob, etPassword;
    private Button btnUpdate, btnLogout, btnPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        initDatbase();
        initControl();

        displayUserInfo();

        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateUserInfo();
                    }
                }
        );

        btnLogout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Xóa trạng thái đăng nhập
                        sessionManager.logout();

                        // Chuyển người dùng về màn hình đăng nhập
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);

                        // Kết thúc activity hiện tại
                        finish();
                    }
                }
        );

        setupPasswordChangeListener();
        setUpNavigaetView();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Khởi tạo các thành phần cơ bản
    private void initDatbase() {
        sessionManager = new SessionManager(this);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userDAO = new UserDAO(dbHelper);

        // Kiểm tra trạng thái đăng nhập
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        userId = sessionManager.getUserId();
    }

    // Khởi tạo control
    // Lấy các thành phần giao diện theo ID
    private void initControl() {
        tvUsername = findViewById(R.id.profile_tvUsername);
        txtInvalidPassword = findViewById(R.id.txtInvalidPassword);
        editName = findViewById(R.id.edit_user_name);
        editPhone = findViewById(R.id.edit_user_phone);
        editEmail = findViewById(R.id.edit_user_email);
        editAddress = findViewById(R.id.edit_user_address);
        editDob = findViewById(R.id.edit_user_dob);
        btnUpdate = findViewById(R.id.btnUpdateAccount);
        btnLogout = findViewById(R.id.btnLogout);
        btnPassword = findViewById(R.id.btnPassword);
        etPassword = findViewById(R.id.edit_user_password);
    }

    // Hiển thị thông tin người dùng từ cơ sở dữ liệu
    private void displayUserInfo() {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            tvUsername.setText(user.getUsername());
            editName.setText(user.getFullName());
            editPhone.setText(user.getPhoneNumber());
            editEmail.setText(user.getEmail());
            editAddress.setText(user.getAddress());
            editDob.setText(user.getDob());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }
    }

    // Cập nhật thông tin người dùng vào cơ sở dữ liệu
    private void updateUserInfo() {
        String fullName = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String dob = editDob.getText().toString().trim();

        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        User userUpdate = userDAO.getUserById(userId);

        User updatedUser = new
                User(userId,userUpdate.getUsername(),
                userUpdate.getPassword(),
                fullName,
                email,
                phone,
                userUpdate.getAvatar(),
                address,
                dob,
                userUpdate.getCreatedDate(),
                userUpdate.getLastLogin(),
                userUpdate.getIsActive());

        int rowsAffected = userDAO.updateUser(updatedUser);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupPasswordChangeListener() {
        // Ẩn thông báo lỗi mặc định
        txtInvalidPassword.setVisibility(View.GONE);

        // Lắng nghe thay đổi nội dung trong trường mật khẩu
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPassword = s.toString().trim();

                // Kiểm tra độ dài mật khẩu
                if (newPassword.length() > 3) {
                    btnPassword.setEnabled(true);
                    txtInvalidPassword.setVisibility(View.GONE);
                } else {
                    btnPassword.setEnabled(false);
                    txtInvalidPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý sự kiện nút "Đổi mật khẩu"
        btnPassword.setOnClickListener(v -> {
            String newPassword = etPassword.getText().toString().trim();

            if (newPassword.length() > 3) {
                int isUpdated = userDAO.updatePassword(userId, newPassword);
                if (isUpdated > 0) {
                    Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpNavigaetView() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Chuyển đến màn hình Home
                    Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Chuyển đến màn hình Lịch Sử
                    Intent historyIntent = new Intent(ProfileActivity.this, BookingHistoryActivity.class);
                    startActivity(historyIntent);
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    // Chuyển đến màn hình Thông Báo
                    Intent notificationIntent = new Intent(ProfileActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Chuyển đến màn hình Hồ Sơ
                    Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });
    }
}
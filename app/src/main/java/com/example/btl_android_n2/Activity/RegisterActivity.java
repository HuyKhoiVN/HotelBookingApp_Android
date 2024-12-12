package com.example.btl_android_n2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android_n2.DAO.UserDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android_n2.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etFullName, etEmailPhone, etPassword;
    private TextView tvErrorMessage;
    private UserDAO userDAO;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo các view và db
        initControl();

        // Đăng ký sự kiện chuyển tới màn hình đăng nhập
        setLoginClickListener();

        // Đăng ký sự kiện cho nút đăng ký
        setRegisterButtonClickListener();

        // Thêm padding cho màn hình
        setWindowInsets();
    }

    private void initControl() {
        etUsername = findViewById(R.id.regis_EtUsername);
        etFullName = findViewById(R.id.regis_EtFullName);
        etEmailPhone = findViewById(R.id.regis_EtEmailPhone);
        etPassword = findViewById(R.id.regis_EtPassword);
        tvErrorMessage = findViewById(R.id.regis_tvErrorMessage);

        btnRegister = findViewById(R.id.regis_btnRegister);
        userDAO = new UserDAO(new DatabaseHelper(this));
    }

    private void setLoginClickListener() {
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(v -> {
            // Chuyển đến màn hình Đăng nhập
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void setRegisterButtonClickListener() {
        btnRegister.setOnClickListener(v -> {
            if (isValidInput()) {
                // Kiểm tra sự tồn tại của username, email, và phone
                if (isFieldExist("Username", etUsername.getText().toString()) ||
                        isFieldExist("Email", etEmailPhone.getText().toString()) ) {
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText("Tên đăng nhập hoặc email đã tồn tại.");
                } else {
                    registerNewUser();
                }
            } else {
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText("Vui lòng điền đầy đủ và đúng định dạng thông tin.");
            }
        });
    }

    private void setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Hàm kiểm tra tất cả các trường nhập liệu
    private boolean isValidInput() {
        String username = etUsername.getText().toString();
        String fullName = etFullName.getText().toString();
        String emailPhone = etEmailPhone.getText().toString();
        String password = etPassword.getText().toString();

        // Kiểm tra các trường nhập liệu
        return isValidField(username, fullName, emailPhone, password);
    }

    // Hàm kiểm tra định dạng tất cả các trường
    private boolean isValidField(String username, String fullName, String emailPhone, String password) {
        // Kiểm tra họ tên phải có ít nhất 2 từ
        if (fullName.trim().split(" ").length < 2) return false;

        // Kiểm tra username chỉ chứa chữ cái thường và số
        username = username.toLowerCase(); // Chuyển về chữ cái thường
        if (!username.matches("[a-z0-9]+")) return false;

        // Kiểm tra mật khẩu phải có ít nhất 3 ký tự
        if (password.length() < 3) return false;

        // Kiểm tra email đúng định dạng
        if (!emailPhone.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) return false;

        return true;
    }

    private boolean isFieldExist(String fieldName, String fieldValue) {
        // Sử dụng phương thức của UserDAO để kiểm tra sự tồn tại của các trường trong DB
        return userDAO.isFieldExist(fieldName, fieldValue);
    }

    private void registerNewUser() {
        // Tạo đối tượng User mới từ thông tin người dùng nhập vào
        User newUser = createUser();

        // Chèn người dùng vào cơ sở dữ liệu
        long result = userDAO.insertUser(newUser);

        if (result > 0) {
            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình đăng nhập
        } else {
            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    private User createUser() {
        String username = etUsername.getText().toString();
        String fullName = etFullName.getText().toString();
        String emailPhone = etEmailPhone.getText().toString();
        String password = etPassword.getText().toString();

        return new User(
                0, // userId sẽ tự động sinh khi insert vào DB
                username,
                password,
                fullName,
                emailPhone, // Thêm mail
                "", // phone number null,
                "", // Avatar giả sử không có
                "", // Địa chỉ giả sử không có
                "", // Ngày sinh giả sử không có
                "", // Ngày tạo giả sử không có
                "", // Ngày đăng nhập cuối giả sử không có
                1 // Trạng thái người dùng: 1 - hoạt động
        );
    }
}

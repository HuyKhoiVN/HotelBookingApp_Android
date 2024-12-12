package com.example.btl_android_n2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.Adapter.NotificationAdapter;
import com.example.btl_android_n2.DAO.NotificationDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.MainActivity;
import com.example.btl_android_n2.Models.Notification;

import com.example.btl_android_n2.R;
import com.example.btl_android_n2.Util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private NotificationDAO notificationDAO;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Chuyển đến màn hình Home
                    Intent homeIntent = new Intent(NotificationActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Chuyển đến màn hình Lịch Sử
                    Intent historyIntent = new Intent(NotificationActivity.this, BookingHistoryActivity.class);
                    startActivity(historyIntent);
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    // Chuyển đến màn hình Thông Báo
                    Intent notificationIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Chuyển đến màn hình Hồ Sơ
                    Intent profileIntent = new Intent(NotificationActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });


        // Khởi tạo SessionManager
        SessionManager sessionManager = new SessionManager(this);

        // Kiểm tra trạng thái đăng nhập
        if (!sessionManager.isLoggedIn()) {
            // Nếu chưa đăng nhập, chuyển đến LoginActivity
            Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Đóng MainActivity để không quay lại màn hình chính nếu nhấn nút Back
        }

        int userId = sessionManager.getUserId();

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        LinearLayout linnerNoBooking = findViewById(R.id.linnerNoNotification);

        // Khởi tạo DAO và lấy dữ liệu
        notificationDAO = new NotificationDAO(new DatabaseHelper(this));
        notificationList = notificationDAO.getNotificationsByUserId(userId);

        if(notificationList == null || notificationList.isEmpty()) {
            linnerNoBooking.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            TextView txtNoBooking = findViewById(R.id.tvNoNotification);
            txtNoBooking.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }
        else {
            linnerNoBooking.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Thiết lập adapter và layout manager
            notificationAdapter = new NotificationAdapter(notificationList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(notificationAdapter);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
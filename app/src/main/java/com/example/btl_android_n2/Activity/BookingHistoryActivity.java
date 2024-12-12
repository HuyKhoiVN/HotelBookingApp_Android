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

import com.example.btl_android_n2.Adapter.BookingAdapter;
import com.example.btl_android_n2.DAO.BookingDAO;
import com.example.btl_android_n2.DAO.RoomDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.MainActivity;
import com.example.btl_android_n2.Models.Booking;

import com.example.btl_android_n2.R;
import com.example.btl_android_n2.Util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_history);


        recyclerView = findViewById(R.id.recyclerViewBookings);
        bookingDAO = new BookingDAO(new DatabaseHelper(this));
        roomDAO = new RoomDAO(new DatabaseHelper(this));
        LinearLayout linnerNoBooking = findViewById(R.id.linnerNoBooking);

        // Khởi tạo SessionManager
        SessionManager sessionManager = new SessionManager(this);

        // Kiểm tra trạng thái đăng nhập
        if (!sessionManager.isLoggedIn()) {
            // Nếu chưa đăng nhập, chuyển đến LoginActivity
            Intent intent = new Intent(BookingHistoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Đóng MainActivity để không quay lại màn hình chính nếu nhấn nút Back
        }

        int userId = sessionManager.getUserId();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Chuyển đến màn hình Home
                    Intent homeIntent = new Intent(BookingHistoryActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Chuyển đến màn hình Lịch Sử
                    Intent historyIntent = new Intent(BookingHistoryActivity.this, BookingHistoryActivity.class);
                    startActivity(historyIntent);
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    // Chuyển đến màn hình Thông Báo
                    Intent notificationIntent = new Intent(BookingHistoryActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Chuyển đến màn hình Hồ Sơ
                    Intent profileIntent = new Intent(BookingHistoryActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });

        bookingList = bookingDAO.getBookingsByUserId(userId);

        if(bookingList == null || bookingList.isEmpty()) {
            linnerNoBooking.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            TextView txtNoBooking = findViewById(R.id.tvNoBooking);
            txtNoBooking.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(BookingHistoryActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            linnerNoBooking.setVisibility(View.GONE);

            bookingAdapter = new BookingAdapter(this, userId, bookingList, roomDAO);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(bookingAdapter);
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
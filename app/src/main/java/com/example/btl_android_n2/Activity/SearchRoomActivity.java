package com.example.btl_android_n2.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android_n2.MainActivity;
import com.example.btl_android_n2.R;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.Adapter.RoomAdapter;
import com.example.btl_android_n2.Models.Room;
import com.example.btl_android_n2.Util.SearchCriteria;
import com.example.btl_android_n2.Util.RoomUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SearchRoomActivity extends AppCompatActivity {

    private EditText etLocationSearch, etPeopleNumberSearch;
    private Button btnSearchSearch, filterPrice, filterRating, filterAmenities;
    private RecyclerView recyclerViewRooms;
    TextView tvRoomCount;
    private boolean isPriceAscending = true;
    private boolean isRatingAscending = true;
    private boolean isAmenitiesAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_room);

        // Khởi tạo các control
        initControls();

        // Nhận dữ liệu từ MainActivity (nếu có)
        loadSearchCriteria();

        // Xử lý sự kiện nhấn nút tìm kiếm
        initEvents();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Chuyển đến màn hình Home
                    Intent homeIntent = new Intent(SearchRoomActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Chuyển đến màn hình Lịch Sử
                    Intent historyIntent = new Intent(SearchRoomActivity.this, BookingHistoryActivity.class);
                    startActivity(historyIntent);
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    // Chuyển đến màn hình Thông Báo
                    Intent notificationIntent = new Intent(SearchRoomActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Chuyển đến màn hình Hồ Sơ
                    Intent profileIntent = new Intent(SearchRoomActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });

        String location = getIntent().getStringExtra("location");
        String peopleNumberStr = getIntent().getStringExtra("peopleNumber");
        // Thực hiện tìm kiếm tự động nếu có dữ liệu
        if ((location != null && !location.isEmpty()) || (peopleNumberStr != null && !peopleNumberStr.isEmpty())) {
            performSearch();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initControls() {
        etLocationSearch = findViewById(R.id.etLocationSearch);
        etPeopleNumberSearch = findViewById(R.id.etPeopleNumberSearch);
        btnSearchSearch = findViewById(R.id.btnSearchSearch);
        recyclerViewRooms = findViewById(R.id.recyclerViewRoomsSearch);
        tvRoomCount = findViewById(R.id.tvRoomCount);
        filterPrice = findViewById(R.id.filter_price);
        filterRating = findViewById(R.id.filter_rating);
        filterAmenities = findViewById(R.id.filter_amenities);
    }

    private void loadSearchCriteria() {
        String location = getIntent().getStringExtra("location");
        String peopleNumberStr = getIntent().getStringExtra("peopleNumber");

        etLocationSearch.setText(location != null ? location : "");
        etPeopleNumberSearch.setText(peopleNumberStr != null ? peopleNumberStr : "");
    }

    private void initEvents() {
        btnSearchSearch.setOnClickListener(v -> performSearch());
        // Sắp xếp theo giá
        filterPrice.setOnClickListener(v -> {
            isPriceAscending = !isPriceAscending;
            sortRoomsByPrice(isPriceAscending);
            updateFilterBackground(filterPrice, isPriceAscending);
        });

        // Sắp xếp theo đánh giá
        filterRating.setOnClickListener(v -> {
            isRatingAscending = !isRatingAscending;
            sortRoomsByRating(isRatingAscending);
            updateFilterBackground(filterRating, isRatingAscending);
        });

        // Sắp xếp theo tiện ích (giả định sử dụng số lượng người như tiện ích)
        filterAmenities.setOnClickListener(v -> {
            isAmenitiesAscending = !isAmenitiesAscending;
            sortRoomsByAmenities(isAmenitiesAscending);
            updateFilterBackground(filterAmenities, isAmenitiesAscending);
        });
    }

    private void performSearch() {
        String location = etLocationSearch.getText().toString();
        int peopleNumber = etPeopleNumberSearch.getText().toString().isEmpty()
                ? 0
                : Integer.parseInt(etPeopleNumberSearch.getText().toString());

        // Tạo tiêu chí tìm kiếm
        SearchCriteria criteria = new SearchCriteria(location, peopleNumber);

        // Lấy danh sách phòng theo tiêu chí
        List<Room> filteredRooms = RoomUtil.searchRooms(this, criteria);

        // Hiển thị danh sách phòng
        RoomAdapter adapter = new RoomAdapter(this, filteredRooms);
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRooms.setAdapter(adapter);

        // Cập nhật tổng số phòng tìm được
        tvRoomCount.setText(String.format("Tổng số phòng: %d", filteredRooms.size()));
    }
    private void sortRoomsByPrice(boolean ascending) {
        List<Room> roomList = ((RoomAdapter) recyclerViewRooms.getAdapter()).getRoomList();
        if (roomList != null) {
            roomList.sort((room1, room2) -> ascending
                    ? Double.compare(room1.getPrice(), room2.getPrice())
                    : Double.compare(room2.getPrice(), room1.getPrice()));
            recyclerViewRooms.getAdapter().notifyDataSetChanged();
        }
    }

    private void sortRoomsByRating(boolean ascending) {
        List<Room> roomList = ((RoomAdapter) recyclerViewRooms.getAdapter()).getRoomList();
        if (roomList != null) {
            roomList.sort((room1, room2) -> ascending
                    ? Double.compare(room1.getReview(), room2.getReview())
                    : Double.compare(room2.getReview(), room1.getReview()));
            recyclerViewRooms.getAdapter().notifyDataSetChanged();
        }
    }


    private void sortRoomsByAmenities(boolean ascending) {
        List<Room> roomList = ((RoomAdapter) recyclerViewRooms.getAdapter()).getRoomList();
        if (roomList != null) {
            roomList.sort((room1, room2) -> ascending
                    ? Integer.compare(room1.getPeopleNumber(), room2.getPeopleNumber())
                    : Integer.compare(room2.getPeopleNumber(), room1.getPeopleNumber()));
            recyclerViewRooms.getAdapter().notifyDataSetChanged();
        }
    }


    private void updateFilterBackground(TextView filter, boolean isAscending) {
        int backgroundColor = isAscending
                ? getResources().getColor(R.color.ascending, null)
                : getResources().getColor(R.color.descending, null);
        filter.setBackgroundColor(backgroundColor);
    }

    public String getCheckInDate() {
        EditText etCheckInDateSearch = findViewById(R.id.etCheckInDateSearch);
        return etCheckInDateSearch.getText().toString();
    }

    public String getCheckOutDate() {
        EditText etCheckOutDateSearch = findViewById(R.id.etCheckOutDateSearch);
        return etCheckOutDateSearch.getText().toString();
    }

}
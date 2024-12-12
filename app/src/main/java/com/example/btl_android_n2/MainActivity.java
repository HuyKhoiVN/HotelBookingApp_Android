package com.example.btl_android_n2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.Activity.RoomDetailActivity;
import com.example.btl_android_n2.Adapter.RoomAdapter;
import com.example.btl_android_n2.DAO.RoomDAO;
import com.example.btl_android_n2.Models.Room;
import com.example.btl_android_n2.Activity.ProfileActivity;
import com.example.btl_android_n2.Activity.BookingHistoryActivity;
import com.example.btl_android_n2.Activity.LoginActivity;
import com.example.btl_android_n2.Activity.NotificationActivity;
import com.example.btl_android_n2.Activity.SearchRoomActivity;
import com.example.btl_android_n2.Util.RoomUtil;
import com.example.btl_android_n2.Util.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DatabaseHelper db;
    private EditText etCheckInDate, etCheckOutDate, etLocation, etPeopleNumber;
    private LinearLayout linearHotel1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private RoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);

        // Kiểm tra trạng thái đăng nhập
        if (!sessionManager.isLoggedIn()) {
            // Nếu chưa đăng nhập, chuyển đến LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Đóng MainActivity để không quay lại màn hình chính nếu nhấn nút Back
        }

        // Khởi tạo cơ sở dữ liệu
        initDatabase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Chuyển đến màn hình Home
                    Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    // Chuyển đến màn hình Lịch Sử
                    Intent historyIntent = new Intent(MainActivity.this, BookingHistoryActivity.class);
                    startActivity(historyIntent);
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    // Chuyển đến màn hình Thông Báo
                    Intent notificationIntent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Chuyển đến màn hình Hồ Sơ
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });

        // Khởi tạo các control
        initControls();

        // khởi tạo ngày
        setDefaultDates();

        // Khởi tạo các sự kiện
        initEvents();

        // Gọi hàm thêm dữ liệu
//        checkAndInsertRooms();

        // Hiển thị random room
        displayRandomRooms();

        // Hiển thị danh sách phòng
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRooms);
        displayRecyclerView(this, recyclerView, etCheckInDate.getText().toString(), etCheckOutDate.getText().toString());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Khởi tạo cơ sở dữ liệu.
     */
    private void initDatabase() {
        db = new DatabaseHelper(this);
        Toast.makeText(this, "UID: " + sessionManager.getUserId() + ", Database created successfully!", Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", "Database initialized.");
        db.getWritableDatabase();
    }

    /**
     * Khởi tạo các control từ file XML.
     */
    private void initControls() {
        etCheckInDate = findViewById(R.id.etCheckInDate);
        etCheckOutDate = findViewById(R.id.etCheckOutDate);
        linearHotel1 = findViewById(R.id.linnerHotelABC);
        etLocation = findViewById(R.id.etLocation);
        etPeopleNumber = findViewById(R.id.etPeopleNumber);
        Log.d("MainActivity", "Controls initialized.");
    }

    /**
     * Khởi tạo các sự kiện cho các control.
     */
    private void initEvents() {
        // Sự kiện cho "Ngày Nhận"
        etCheckInDate.setOnClickListener(view -> showDatePickerDialog(etCheckInDate));

        // Sự kiện cho "Ngày Trả"
        etCheckOutDate.setOnClickListener(view -> showDatePickerDialog(etCheckOutDate));

        // Sự kiện click vào LinearLayout "Hotel"
        linearHotel1.setOnClickListener(v -> navigateToRoomDetail());

        findViewById(R.id.btnSearch).setOnClickListener(v -> navigateToSearchRoom());


        Log.d("MainActivity", "Events initialized.");
    }

    private void setDefaultDates() {
        Calendar calendar = Calendar.getInstance();

        // Định dạng ngày hiện tại cho "Ngày Nhận"
        String currentDate = dateFormat.format(calendar.getTime());
        etCheckInDate.setText(currentDate);

        // Định dạng ngày hôm sau cho "Ngày Trả"
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String nextDate = dateFormat.format(calendar.getTime());
        etCheckOutDate.setText(nextDate);
    }


    /**
     * Hiển thị DatePickerDialog để chọn ngày.
     *
     * @param editText EditText sẽ hiển thị ngày đã chọn.
     */
    private void showDatePickerDialog(final EditText editText) {
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
                    updateDatesInAdapter();
                }, year, month, day);

        datePickerDialog.show();
        Log.d("MainActivity", "DatePickerDialog shown for: " + editText.getId());
    }

    /**
     * Điều hướng sang màn hình Search.
     */
    private void navigateToRoomDetail() {
        Intent intent = new Intent(MainActivity.this, RoomDetailActivity.class);
        startActivity(intent);
        Log.d("MainActivity", "Navigating to RoomDetail.");
    }

    /**
     * Kiểm tra đã có data trong room chưa
     */
    private void checkAndInsertRooms() {
        RoomDAO roomDAO = new RoomDAO(new DatabaseHelper(this));
        if (roomDAO.getAllRooms().isEmpty()) {
            RoomUtil.insertRandomRooms(this);
        } else {
            Log.d("MainActivity", "Rooms already exist in database. Skipping insertion.");
        }
    }

    private void navigateToSearchRoom() {
        Intent intent = new Intent(this, SearchRoomActivity.class);
        intent.putExtra("location", etLocation.getText().toString());
        intent.putExtra("peopleNumber", etPeopleNumber.getText().toString());
        startActivity(intent);
    }


    /**
     *  Hiển thị ngẫy nhiên 2 phòng
     */
    private void displayRandomRooms() {
        // Lấy danh sách 2 phòng ngẫu nhiên
        List<Room> randomRooms = RoomUtil.selectRandomRooms(this, 2);

        if (randomRooms.size() < 2) {
            Toast.makeText(this, "Không đủ phòng để hiển thị.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gán dữ liệu cho LinearLayout Hotel ABC
        Room room1 = randomRooms.get(0);

        ImageView imgHotel1 = findViewById(R.id.imgHotel1);
        TextView tvHotelName1 = findViewById(R.id.tvHotelName1);
        TextView tvHotelAddress1 = findViewById(R.id.tvHotelAddress1);

        // Hiển thị dữ liệu phòng 1
        tvHotelName1.setText(room1.getRoomName());
        tvHotelAddress1.setText(room1.getAddress());

        // Hiển thị hình ảnh phòng 1
        String firstImagePath1 = room1.getImage().split(",")[0];
        try {
            InputStream inputStream1 = getAssets().open(firstImagePath1);
            Drawable drawable1 = Drawable.createFromStream(inputStream1, null);
            imgHotel1.setImageDrawable(drawable1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gán dữ liệu cho LinearLayout Hotel XYZ
        Room room2 = randomRooms.get(1);

        ImageView imgHotel2 = findViewById(R.id.imgHotel2);
        TextView tvHotelName2 = findViewById(R.id.tvHotelName2);
        TextView tvHotelAddress2 = findViewById(R.id.tvHotelAddress2);

        // Hiển thị dữ liệu phòng 2
        tvHotelName2.setText(room2.getRoomName());
        tvHotelAddress2.setText(room2.getAddress());

        // Hiển thị hình ảnh phòng 2
        String firstImagePath2 = room2.getImage().split(",")[0];
        try {
            InputStream inputStream2 = getAssets().open(firstImagePath2);
            Drawable drawable2 = Drawable.createFromStream(inputStream2, null);
            imgHotel2.setImageDrawable(drawable2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiển thị ngẫy nhiên 5 phòng leen recyclerView
     */
    private void displayRecyclerView(Context context, RecyclerView recyclerView, String checkInDate, String checkOutDate) {
        List<Room> randomRooms = RoomUtil.selectRandomRooms(context, 5);
        adapter = new RoomAdapter(context, randomRooms, checkInDate, checkOutDate);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    // Sau khi chọn ngày, cập nhật ngày trong adapter
    private void updateDatesInAdapter() {
        String checkInDateChange = etCheckInDate.getText().toString();  // Lấy ngày Check-In từ EditText
        String checkOutDateChange = etCheckOutDate.getText().toString();  // Lấy ngày Check-Out từ EditText

        // Kiểm tra nếu ngày được chọn hợp lệ (không rỗng)
        if (!checkInDateChange.isEmpty() && !checkOutDateChange.isEmpty()) {
            // Cập nhật ngày cho adapter
            if (adapter != null) {
                adapter.updateDates(checkInDateChange, checkOutDateChange); // Gọi phương thức updateDates trong adapter
            }
        }
    }

}
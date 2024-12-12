package com.example.btl_android_n2.Util;

import android.database.sqlite.SQLiteDatabase;

import java.util.Random;

public class ProvinceUtil {

    // Tên các bảng (public static final để các lớp khác có thể sử dụng)
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_ROOMS = "Rooms";
    public static final String TABLE_BOOKINGS = "Bookings";
    public static final String TABLE_PAYMENT_HISTORY = "PaymentHistory";
    public static final String TABLE_FAVORITE_ROOMS = "FavoriteRooms";
    public static final String TABLE_NOTIFICATIONS = "Notifications";
    public static final String TABLE_REVIEWS = "Reviews";
    public static  final String TABLE_PROVINCES = "Provinces";
    public static  final String TABLE_BOOKING_HISTORY = "BookingHistory";
    public static void insertSampleData(SQLiteDatabase db) {
        // Thêm dữ liệu mẫu vào Users
        db.execSQL("INSERT INTO " + TABLE_USERS + "(Username, Password, FullName, Email, PhoneNumber, IsActive) VALUES " +
                "('admin', 'admin123', 'Administrator', 'admin@example.com', '0123456789', 1), " +
                "('user1', 'password1', 'Nguyễn Văn A', 'user1@example.com', '0987654321', 1), " +
                "('user2', 'password2', 'Trần Thị B', 'user2@example.com', '0912345678', 1)");

        // Thêm 64 tỉnh thành với số phòng ngẫu nhiên
        Random random = new Random();
        String[] provinces = {
                "Hà Nội", "Hồ Chí Minh", "Hải Phòng", "Đà Nẵng", "Cần Thơ",
                "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu",
                "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước",
                "Bình Thuận", "Cà Mau", "Cao Bằng", "Đắk Lắk", "Đắk Nông",
                "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang",
                "Hà Nam", "Hà Tĩnh", "Hải Dương", "Hậu Giang", "Hòa Bình",
                "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu",
                "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
                "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Quảng Bình",
                "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng",
                "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa",
                "Thừa Thiên Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang", "Vĩnh Long",
                "Vĩnh Phúc", "Yên Bái"
        };

        for (String province : provinces) {
            int availableRooms = random.nextInt(101) + 100; // Random từ 100 đến 200
            db.execSQL("INSERT INTO " + TABLE_PROVINCES + "(ProvinceName, AvailableRooms) VALUES ('" +
                    province + "', " + availableRooms + ")");
        }
    }
}

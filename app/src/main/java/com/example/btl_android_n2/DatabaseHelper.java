package com.example.btl_android_n2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HotelBooking.db";
    private static final int DATABASE_VERSION = 1;

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


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bật ràng buộc khóa ngoại
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Tạo bảng Users
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "UserId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Username TEXT UNIQUE, " +
                "Password TEXT, " +
                "FullName TEXT, " +
                "Email TEXT, " +
                "PhoneNumber TEXT, " +
                "Avatar TEXT, " +
                "Address TEXT, " +
                "DOB TEXT, " +
                "CreatedDate TEXT, " +
                "LastLogin TEXT, " +
                "IsActive INTEGER DEFAULT 1)");

        // Tạo bảng Province
        db.execSQL("CREATE TABLE " + TABLE_PROVINCES + " (" +
                "ProvinceId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ProvinceName TEXT NOT NULL, " +
                "AvailableRooms INTEGER DEFAULT 0)");

        // Tạo bảng Rooms
        db.execSQL("CREATE TABLE " + TABLE_ROOMS + " (" +
                "RoomId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "RoomName TEXT, " +
                "RoomType TEXT, " +
                "PeopleNumber INTEGER, " +
                "Price REAL, " +
                "Description TEXT, " +
                "Review REAL, " +
                "Status TEXT, " +
                "ProvinceId INTEGER, " +
                "Image TEXT, " + // Nếu cần thêm cột Image
                "Address TEXT, " + // Thêm Address
                "FOREIGN KEY (ProvinceId) REFERENCES Provinces(ProvinceId))");


        db.execSQL("CREATE TABLE " + TABLE_BOOKING_HISTORY + " (" +
                "HistoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserId INTEGER NOT NULL, " +
                "RoomId INTEGER NOT NULL, " +
                "UserAction TEXT NOT NULL, " +
                "ActionDate TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(UserId) REFERENCES Users(UserId), " +
                "FOREIGN KEY(RoomId) REFERENCES Rooms(RoomId))");

        // Tạo bảng Bookings
        db.execSQL("CREATE TABLE " + TABLE_BOOKINGS + " (" +
                "BookingId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserId INTEGER, " +
                "RoomId INTEGER, " +
                "BookingDate TEXT, " +
                "CheckInDate TEXT, " +
                "CheckOutDate TEXT, " +
                "TotalPrice REAL, " +
                "Status INTEGER DEFAULT 1 CHECK(Status IN (0, 1))," +
                "SpecialRequests TEXT, " +
                "FOREIGN KEY(UserId) REFERENCES " + TABLE_USERS + "(UserId), " +
                "FOREIGN KEY(RoomId) REFERENCES " + TABLE_ROOMS + "(RoomId))");

        // Tạo bảng PaymentHistory
        db.execSQL("CREATE TABLE " + TABLE_PAYMENT_HISTORY + " (" +
                "PaymentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "BookingId INTEGER, " +
                "PaymentDate TEXT, " +
                "PaymentAmount REAL, " +
                "PaymentMethod TEXT, " +
                "FOREIGN KEY(BookingId) REFERENCES " + TABLE_BOOKINGS + "(BookingId))");

        // Tạo bảng FavoriteRooms
        db.execSQL("CREATE TABLE " + TABLE_FAVORITE_ROOMS + " (" +
                "FavoriteId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserId INTEGER, " +
                "RoomId INTEGER, " +
                "CreatedDate TEXT, " +
                "FOREIGN KEY(UserId) REFERENCES " + TABLE_USERS + "(UserId), " +
                "FOREIGN KEY(RoomId) REFERENCES " + TABLE_ROOMS + "(RoomId))");

        // Tạo bảng Notifications
        db.execSQL("CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                "NotificationId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserId INTEGER, " +
                "Message TEXT, " +
                "CreatedDate TEXT, " +
                "IsRead INTEGER DEFAULT 0, " +
                "FOREIGN KEY(UserId) REFERENCES " + TABLE_USERS + "(UserId))");

        // Tạo bảng Reviews
        db.execSQL("CREATE TABLE " + TABLE_REVIEWS + " (" +
                "ReviewId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserId INTEGER, " +
                "RoomId INTEGER, " +
                "Rating REAL CHECK(Rating >= 0 AND Rating <= 5), " +
                "Comment TEXT, " +
                "CreatedDate TEXT, " +
                "FOREIGN KEY(UserId) REFERENCES " + TABLE_USERS + "(UserId), " +
                "FOREIGN KEY(RoomId) REFERENCES " + TABLE_ROOMS + "(RoomId))");

        // Tạo chỉ mục để tối ưu hóa truy vấn
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_username ON " + TABLE_USERS + " (Username)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_userid ON " + TABLE_BOOKINGS + " (UserId)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_roomid ON " + TABLE_BOOKINGS + " (RoomId)");

        Log.d("DatabaseHelper", "Database and tables created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Tạo lại cơ sở dữ liệu mới
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Bật tính năng ràng buộc khóa ngoại
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}

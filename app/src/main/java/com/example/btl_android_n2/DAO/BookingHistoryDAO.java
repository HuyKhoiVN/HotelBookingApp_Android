package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.BookingHistory;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryDAO {
    private SQLiteDatabase db;

    public BookingHistoryDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một lịch sử đặt phòng mới
    public long insertBookingHistory(BookingHistory bookingHistory) {
        ContentValues values = new ContentValues();
        values.put("UserId", bookingHistory.getUserId());
        values.put("RoomId", bookingHistory.getRoomId());
        values.put("UserAction", bookingHistory.getUserAction());
        values.put("ActionDate", bookingHistory.getActionDate());
        return db.insert(DatabaseHelper.TABLE_BOOKING_HISTORY, null, values);
    }

    // Read: Lấy thông tin lịch sử đặt phòng theo ID
    public BookingHistory getBookingHistoryById(int historyId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOOKING_HISTORY + " WHERE HistoryId = ?", new String[]{String.valueOf(historyId)});
        if (cursor != null && cursor.moveToFirst()) {
            BookingHistory bookingHistory = new BookingHistory(
                    cursor.getInt(cursor.getColumnIndexOrThrow("HistoryId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("UserAction")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ActionDate"))
            );
            cursor.close();
            return bookingHistory;
        }
        return null;
    }

    // Read: Lấy danh sách lịch sử đặt phòng của một người dùng
    public List<BookingHistory> getBookingHistoryByUserId(int userId) {
        List<BookingHistory> historyList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOOKING_HISTORY + " WHERE UserId = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                BookingHistory bookingHistory = new BookingHistory(
                        cursor.getInt(cursor.getColumnIndexOrThrow("HistoryId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("UserAction")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ActionDate"))
                );
                historyList.add(bookingHistory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }

    // Delete: Xóa lịch sử đặt phòng theo ID
    public boolean deleteBookingHistoryById(int historyId) {
        return db.delete(DatabaseHelper.TABLE_BOOKING_HISTORY, "HistoryId = ?", new String[]{String.valueOf(historyId)}) > 0;
    }
}

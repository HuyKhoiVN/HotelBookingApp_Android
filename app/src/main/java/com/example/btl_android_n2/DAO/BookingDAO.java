package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private SQLiteDatabase db;

    public BookingDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một lượt đặt phòng mới
    public long insertBooking(Booking booking) {
        ContentValues values = new ContentValues();
        values.put("UserId", booking.getUserId());
        values.put("RoomId", booking.getRoomId());
        values.put("BookingDate", booking.getBookingDate());
        values.put("CheckInDate", booking.getCheckInDate());
        values.put("CheckOutDate", booking.getCheckOutDate());
        values.put("TotalPrice", booking.getTotalPrice());
        values.put("Status", booking.getStatus() ? 1 : 0); // true -> 1, false -> 0
        values.put("SpecialRequests", booking.getSpecialRequests());
        return db.insert(DatabaseHelper.TABLE_BOOKINGS, null, values);
    }

    // Read: Lấy thông tin đặt phòng theo ID
    public Booking getBookingById(int bookingId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS + " WHERE BookingId = ?", new String[]{String.valueOf(bookingId)});
        if (cursor != null && cursor.moveToFirst()) {
            Booking booking = new Booking(
                    cursor.getInt(cursor.getColumnIndexOrThrow("BookingId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("BookingDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CheckInDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CheckOutDate")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("TotalPrice")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("Status")) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow("SpecialRequests"))
            );
            cursor.close();
            return booking;
        }
        return null;
    }

    // Read: Lấy danh sách tất cả lượt đặt phòng
    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS, null);
        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(cursor.getColumnIndexOrThrow("BookingId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("BookingDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CheckInDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CheckOutDate")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("TotalPrice")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("Status")) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow("SpecialRequests"))
                );
                bookingList.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookingList;
    }

    // Cập nhật trạng thái đặt phòng
    public int updateBookingStatus(Booking booking) {
        ContentValues values = new ContentValues();
        values.put("Status", booking.getStatus() ? 1 : 0); // Chỉ cập nhật trạng thái (1 là true, 0 là false)

        return db.update(DatabaseHelper.TABLE_BOOKINGS, values, "BookingId = ?", new String[]{String.valueOf(booking.getBookingId())});
    }


    // Delete: Xóa một lượt đặt phòng theo ID
    public boolean deleteBookingById(int bookingId) {
        return db.delete(DatabaseHelper.TABLE_BOOKINGS, "BookingId = ?", new String[]{String.valueOf(bookingId)}) > 0;
    }

    // Read: Lấy danh sách tất cả lượt đặt phòng của một người dùng theo UserId
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookingList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS + " WHERE UserId = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(cursor.getColumnIndexOrThrow("BookingId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("BookingDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CheckInDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CheckOutDate")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("TotalPrice")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("Status")) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow("SpecialRequests"))
                );
                bookingList.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookingList;
    }

}

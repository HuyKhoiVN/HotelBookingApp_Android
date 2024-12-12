package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private SQLiteDatabase db;

    public NotificationDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một thông báo mới
    public long insertNotification(Notification notification) {
        ContentValues values = new ContentValues();
        values.put("UserId", notification.getUserId());
        values.put("Message", notification.getMessage());
        values.put("CreatedDate", notification.getCreatedDate());
        values.put("IsRead", notification.getIsRead());
        return db.insert(DatabaseHelper.TABLE_NOTIFICATIONS, null, values);
    }

    // Read: Lấy thông tin một thông báo theo ID
    public Notification getNotificationById(int notificationId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NOTIFICATIONS + " WHERE NotificationId = ?", new String[]{String.valueOf(notificationId)});
        if (cursor != null && cursor.moveToFirst()) {
            Notification notification = new Notification(
                    cursor.getInt(cursor.getColumnIndexOrThrow("NotificationId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Message")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("IsRead"))
            );
            cursor.close();
            return notification;
        }
        return null;
    }

    // Read: Lấy danh sách tất cả thông báo của một người dùng
    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notificationList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NOTIFICATIONS + " WHERE UserId = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification(
                        cursor.getInt(cursor.getColumnIndexOrThrow("NotificationId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Message")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("IsRead"))
                );
                notificationList.add(notification);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notificationList;
    }

    // Update: Cập nhật trạng thái đọc của thông báo
    public int updateNotificationIsRead(int notificationId, int isRead) {
        ContentValues values = new ContentValues();
        values.put("IsRead", isRead); // 0 = chưa đọc, 1 = đã đọc
        return db.update(DatabaseHelper.TABLE_NOTIFICATIONS, values, "NotificationId = ?", new String[]{String.valueOf(notificationId)});
    }

    // Delete: Xóa một thông báo theo ID
    public boolean deleteNotification(int notificationId) {
        return db.delete(DatabaseHelper.TABLE_NOTIFICATIONS, "NotificationId = ?", new String[]{String.valueOf(notificationId)}) > 0;
    }

    // Delete: Xóa tất cả thông báo của một người dùng
    public boolean deleteNotificationsByUserId(int userId) {
        return db.delete(DatabaseHelper.TABLE_NOTIFICATIONS, "UserId = ?", new String[]{String.valueOf(userId)}) > 0;
    }
}

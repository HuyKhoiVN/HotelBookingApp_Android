package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private SQLiteDatabase db;

    public UserDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một người dùng mới
    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Password", user.getPassword());
        values.put("FullName", user.getFullName());
        values.put("Email", user.getEmail());
        values.put("PhoneNumber", user.getPhoneNumber());
        values.put("Avatar", user.getAvatar());
        values.put("Address", user.getAddress());
        values.put("DOB", user.getDob());
        values.put("CreatedDate", user.getCreatedDate());
        values.put("LastLogin", user.getLastLogin());
        values.put("IsActive", user.getIsActive());
        return db.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    // Read: Lấy thông tin một người dùng theo ID
    public User getUserById(int userId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE UserId = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("FullName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Avatar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Address")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DOB")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("LastLogin")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("IsActive"))
            );
            cursor.close();
            return user;
        }
        return null;
    }

    // Tìm người dùng theo tên đăng nhập hoặc email và mật khẩu
    public User getUserByUsernameAndPassword(String username, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE (Username = ? OR Email = ?) AND Password = ?",
                new String[]{username, username, password});

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("FullName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Avatar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Address")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DOB")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("LastLogin")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("IsActive"))
            );
            cursor.close();
            return user;
        }
        return null;
    }

    public boolean isFieldExist(String fieldName, String fieldValue) {
        // Câu truy vấn SQL động để kiểm tra trường có tồn tại không
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + fieldName + " = ?";

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query, new String[]{fieldValue});
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true; // Nếu tìm thấy dữ liệu, trường này đã tồn tại
        }
        return false; // Nếu không tìm thấy, trường này không tồn tại
    }


    // Read: Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("FullName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Avatar")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DOB")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LastLogin")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("IsActive"))
                );
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    // Update: Cập nhật thông tin người dùng
    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Password", user.getPassword());
        values.put("FullName", user.getFullName());
        values.put("Email", user.getEmail());
        values.put("PhoneNumber", user.getPhoneNumber());
        values.put("Avatar", user.getAvatar());
        values.put("Address", user.getAddress());
        values.put("DOB", user.getDob());
        values.put("LastLogin", user.getLastLogin());
        values.put("IsActive", user.getIsActive());
        return db.update(DatabaseHelper.TABLE_USERS, values, "UserId = ?", new String[]{String.valueOf(user.getUserId())});
    }

    // Update password: Cập nhật mật khẩu người dùng
    public int updatePassword(int userId, String newPassword) {
        ContentValues values = new ContentValues();
        values.put("Password", newPassword); // Thay "Password" bằng tên cột mật khẩu trong cơ sở dữ liệu

        // Cập nhật mật khẩu dựa vào UserId
        return db.update(DatabaseHelper.TABLE_USERS, values, "UserId = ?", new String[]{String.valueOf(userId)});
    }


    // Delete: Xóa một người dùng theo ID
    public boolean deleteUser(int userId) {
        return db.delete(DatabaseHelper.TABLE_USERS, "UserId = ?", new String[]{String.valueOf(userId)}) > 0;
    }
}

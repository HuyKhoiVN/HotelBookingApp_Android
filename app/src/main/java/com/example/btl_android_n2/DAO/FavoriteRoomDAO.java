package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.FavoriteRoom;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRoomDAO {
    private SQLiteDatabase db;

    public FavoriteRoomDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một phòng yêu thích mới
    public long insertFavoriteRoom(FavoriteRoom favoriteRoom) {
        ContentValues values = new ContentValues();
        values.put("UserId", favoriteRoom.getUserId());
        values.put("RoomId", favoriteRoom.getRoomId());
        values.put("CreatedDate", favoriteRoom.getCreatedDate());
        return db.insert(DatabaseHelper.TABLE_FAVORITE_ROOMS, null, values);
    }

    // Read: Lấy thông tin phòng yêu thích theo ID
    public FavoriteRoom getFavoriteRoomById(int favoriteId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FAVORITE_ROOMS + " WHERE FavoriteId = ?", new String[]{String.valueOf(favoriteId)});
        if (cursor != null && cursor.moveToFirst()) {
            FavoriteRoom favoriteRoom = new FavoriteRoom(
                    cursor.getInt(cursor.getColumnIndexOrThrow("FavoriteId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate"))
            );
            cursor.close();
            return favoriteRoom;
        }
        return null;
    }

    // Read: Lấy danh sách tất cả phòng yêu thích của một người dùng
    public List<FavoriteRoom> getFavoriteRoomsByUserId(int userId) {
        List<FavoriteRoom> favoriteRoomList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FAVORITE_ROOMS + " WHERE UserId = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                FavoriteRoom favoriteRoom = new FavoriteRoom(
                        cursor.getInt(cursor.getColumnIndexOrThrow("FavoriteId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate"))
                );
                favoriteRoomList.add(favoriteRoom);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteRoomList;
    }

    // Delete: Xóa một phòng yêu thích theo ID
    public boolean deleteFavoriteRoomById(int favoriteId) {
        return db.delete(DatabaseHelper.TABLE_FAVORITE_ROOMS, "FavoriteId = ?", new String[]{String.valueOf(favoriteId)}) > 0;
    }

    // Delete: Xóa tất cả phòng yêu thích của một người dùng
    public boolean deleteFavoriteRoomsByUserId(int userId) {
        return db.delete(DatabaseHelper.TABLE_FAVORITE_ROOMS, "UserId = ?", new String[]{String.valueOf(userId)}) > 0;
    }
}

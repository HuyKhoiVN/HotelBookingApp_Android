package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private SQLiteDatabase db;

    public RoomDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một phòng mới
    public long insertRoom(Room room) {
        ContentValues values = new ContentValues();
        values.put("RoomName", room.getRoomName());
        values.put("RoomType", room.getRoomType());
        values.put("PeopleNumber", room.getPeopleNumber());
        values.put("Price", room.getPrice());
        values.put("Description", room.getDescription());
        values.put("Review", room.getReview());
        values.put("Status", room.getStatus());
        values.put("ProvinceId", room.getProvinceId());
        values.put("Image", room.getImage());
        values.put("Address", room.getAddress());
        return db.insert(DatabaseHelper.TABLE_ROOMS, null, values);
    }

    // Read: Lấy thông tin một phòng theo ID
    public Room getRoomById(int roomId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROOMS + " WHERE RoomId = ?", new String[]{String.valueOf(roomId)});
        if (cursor != null && cursor.moveToFirst()) {
            Room room = new Room(
                    cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("RoomName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("RoomType")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("PeopleNumber")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("Review")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Status")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("ProvinceId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Image")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Address"))
            );
            cursor.close();
            return room;
        }
        return null;
    }

    // Read: Lấy danh sách tất cả các phòng
    public List<Room> getAllRooms() {
        List<Room> roomList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROOMS, null);
        if (cursor.moveToFirst()) {
            do {
                Room room = new Room(
                        cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("RoomName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("RoomType")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("PeopleNumber")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Review")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Status")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ProvinceId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Address"))
                );
                roomList.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }

    // Update: Cập nhật thông tin phòng
    public int updateRoom(Room room) {
        ContentValues values = new ContentValues();
        values.put("RoomName", room.getRoomName());
        values.put("RoomType", room.getRoomType());
        values.put("PeopleNumber", room.getPeopleNumber());
        values.put("Price", room.getPrice());
        values.put("Description", room.getDescription());
        values.put("Review", room.getReview());
        values.put("Status", room.getStatus());
        values.put("ProvinceId", room.getProvinceId());
        values.put("Image", room.getImage());
        values.put("Address", room.getAddress());
        return db.update(DatabaseHelper.TABLE_ROOMS, values, "RoomId = ?", new String[]{String.valueOf(room.getRoomId())});
    }

    // Cập nhật trạng thái phòng
    public int updateRoomStatus(Room room) {
        ContentValues values = new ContentValues();
        values.put("Status", room.getStatus()); // Chỉ cập nhật trạng thái

        return db.update(DatabaseHelper.TABLE_ROOMS, values, "RoomId = ?", new String[]{String.valueOf(room.getRoomId())});
    }


    // Delete: Xóa một phòng theo ID
    public boolean deleteRoom(int roomId) {
        return db.delete(DatabaseHelper.TABLE_ROOMS, "RoomId = ?", new String[]{String.valueOf(roomId)}) > 0;
    }
}

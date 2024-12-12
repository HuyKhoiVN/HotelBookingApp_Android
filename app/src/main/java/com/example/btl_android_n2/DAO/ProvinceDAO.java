package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Province;

import java.util.ArrayList;
import java.util.List;

public class ProvinceDAO {
    private SQLiteDatabase db;

    public ProvinceDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một tỉnh mới
    public long insertProvince(Province province) {
        ContentValues values = new ContentValues();
        values.put("ProvinceName", province.getProvinceName());
        values.put("AvailableRooms", province.getAvailableRooms());
        return db.insert(DatabaseHelper.TABLE_PROVINCES, null, values);
    }

    // Read: Lấy thông tin một tỉnh theo ID
    public Province getProvinceById(int provinceId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PROVINCES + " WHERE ProvinceId = ?", new String[]{String.valueOf(provinceId)});
        if (cursor != null && cursor.moveToFirst()) {
            Province province = new Province(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ProvinceId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ProvinceName")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("AvailableRooms"))
            );
            cursor.close();
            return province;
        }
        return null;
    }

    // Read: Lấy danh sách tất cả tỉnh
    public List<Province> getAllProvinces() {
        List<Province> provinceList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PROVINCES, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ProvinceId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ProvinceName")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("AvailableRooms"))
                );
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return provinceList;
    }

    // Update: Cập nhật thông tin tỉnh
    public int updateProvince(Province province) {
        ContentValues values = new ContentValues();
        values.put("ProvinceName", province.getProvinceName());
        values.put("AvailableRooms", province.getAvailableRooms());
        return db.update(DatabaseHelper.TABLE_PROVINCES, values, "ProvinceId = ?", new String[]{String.valueOf(province.getProvinceId())});
    }

    // Delete: Xóa một tỉnh theo ID
    public boolean deleteProvince(int provinceId) {
        return db.delete(DatabaseHelper.TABLE_PROVINCES, "ProvinceId = ?", new String[]{String.valueOf(provinceId)}) > 0;
    }
}

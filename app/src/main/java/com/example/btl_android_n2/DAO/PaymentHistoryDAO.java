package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.PaymentHistory;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryDAO {
    private SQLiteDatabase db;

    public PaymentHistoryDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một giao dịch thanh toán mới
    public long insertPaymentHistory(PaymentHistory paymentHistory) {
        ContentValues values = new ContentValues();
        values.put("BookingId", paymentHistory.getBookingId());
        values.put("PaymentDate", paymentHistory.getPaymentDate());
        values.put("PaymentAmount", paymentHistory.getPaymentAmount());
        values.put("PaymentMethod", paymentHistory.getPaymentMethod());
        return db.insert(DatabaseHelper.TABLE_PAYMENT_HISTORY, null, values);
    }

    // Read: Lấy thông tin một giao dịch thanh toán theo ID
    public PaymentHistory getPaymentHistoryById(int paymentId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PAYMENT_HISTORY + " WHERE PaymentId = ?", new String[]{String.valueOf(paymentId)});
        if (cursor != null && cursor.moveToFirst()) {
            PaymentHistory paymentHistory = new PaymentHistory(
                    cursor.getInt(cursor.getColumnIndexOrThrow("PaymentId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("BookingId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PaymentDate")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("PaymentAmount")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PaymentMethod"))
            );
            cursor.close();
            return paymentHistory;
        }
        return null;
    }

    // Read: Lấy danh sách tất cả giao dịch thanh toán
    public List<PaymentHistory> getAllPaymentHistories() {
        List<PaymentHistory> paymentHistoryList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PAYMENT_HISTORY, null);
        if (cursor.moveToFirst()) {
            do {
                PaymentHistory paymentHistory = new PaymentHistory(
                        cursor.getInt(cursor.getColumnIndexOrThrow("PaymentId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("BookingId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("PaymentDate")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("PaymentAmount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("PaymentMethod"))
                );
                paymentHistoryList.add(paymentHistory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return paymentHistoryList;
    }

    // Update: Cập nhật thông tin giao dịch thanh toán
    public int updatePaymentHistory(PaymentHistory paymentHistory) {
        ContentValues values = new ContentValues();
        values.put("BookingId", paymentHistory.getBookingId());
        values.put("PaymentDate", paymentHistory.getPaymentDate());
        values.put("PaymentAmount", paymentHistory.getPaymentAmount());
        values.put("PaymentMethod", paymentHistory.getPaymentMethod());
        return db.update(DatabaseHelper.TABLE_PAYMENT_HISTORY, values, "PaymentId = ?", new String[]{String.valueOf(paymentHistory.getPaymentId())});
    }

    // Delete: Xóa một giao dịch thanh toán theo ID
    public boolean deletePaymentHistory(int paymentId) {
        return db.delete(DatabaseHelper.TABLE_PAYMENT_HISTORY, "PaymentId = ?", new String[]{String.valueOf(paymentId)}) > 0;
    }
}

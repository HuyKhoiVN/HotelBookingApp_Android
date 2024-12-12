package com.example.btl_android_n2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private SQLiteDatabase db;

    public ReviewDAO(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    // Create: Thêm một đánh giá mới
    public long insertReview(Review review) {
        ContentValues values = new ContentValues();
        values.put("UserId", review.getUserId());
        values.put("RoomId", review.getRoomId());
        values.put("Rating", review.getRating());
        values.put("Comment", review.getComment());
        values.put("CreatedDate", review.getCreatedDate());
        return db.insert(DatabaseHelper.TABLE_REVIEWS, null, values);
    }

    // Read: Lấy thông tin một đánh giá theo ID
    public Review getReviewById(int reviewId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEWS + " WHERE ReviewId = ?", new String[]{String.valueOf(reviewId)});
        if (cursor != null && cursor.moveToFirst()) {
            Review review = new Review(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ReviewId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("Rating")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Comment")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate"))
            );
            cursor.close();
            return review;
        }
        return null;
    }

    public List<Review> getViewByRoomId(int roomId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE RoomId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(roomId)});

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ReviewId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Rating")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Comment")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate"))
                );
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }


    // Read: Lấy danh sách tất cả đánh giá
    public List<Review> getAllReviews() {
        List<Review> reviewList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REVIEWS, null);
        if (cursor.moveToFirst()) {
            do {
                Review review = new Review(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ReviewId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("RoomId")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Rating")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Comment")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate"))
                );
                reviewList.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviewList;
    }

    // Update: Cập nhật thông tin đánh giá
    public int updateReview(Review review) {
        ContentValues values = new ContentValues();
        values.put("UserId", review.getUserId());
        values.put("RoomId", review.getRoomId());
        values.put("Rating", review.getRating());
        values.put("Comment", review.getComment());
        values.put("CreatedDate", review.getCreatedDate());
        return db.update(DatabaseHelper.TABLE_REVIEWS, values, "ReviewId = ?", new String[]{String.valueOf(review.getReviewId())});
    }

    // Delete: Xóa một đánh giá theo ID
    public boolean deleteReview(int reviewId) {
        return db.delete(DatabaseHelper.TABLE_REVIEWS, "ReviewId = ?", new String[]{String.valueOf(reviewId)}) > 0;
    }
}

package com.example.btl_android_n2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.Models.Room;
import com.example.btl_android_n2.Activity.RoomDetailActivity;
import com.example.btl_android_n2.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;
    private String checkInDate;
    private String checkOutDate;

    public RoomAdapter(Context context, List<Room> roomList, String checkInDate, String checkOutDate) {
        this.context = context;
        this.roomList = roomList;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public RoomAdapter(Context context, List<Room> roomList) {
        this(context, roomList, null, null);
    }

    // Phương thức này được gọi khi ngày thay đổi
    public void updateDates(String checkInDate, String checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        notifyDataSetChanged(); // Làm mới RecyclerView sau khi thay đổi ngày
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getRoomName());
        holder.tvRoomType.setText(room.getRoomType());
        holder.tvRoomDescription.setText(room.getDescription());
        holder.tvRoomPrice.setText(String.format("Giá: %,d VND", (int) room.getPrice()));
        holder.tvRoomRating.setText(String.format("%.1f ★", room.getReview()));

        // Hiển thị ảnh đầu tiên từ danh sách ảnh
        String firstImagePath = (room.getImage() != null && !room.getImage().isEmpty())
                ? room.getImage().split(",")[0]
                : "images/hotel5s_default.png"; // Thay thế bằng ảnh mặc định

        try {
            InputStream inputStream = context.getAssets().open(firstImagePath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.imgRoom.setImageDrawable(drawable);
        } catch (IOException e) {
            holder.imgRoom.setImageResource(R.drawable.vungtau); // Sử dụng ảnh mặc định
            Log.e("RoomAdapter", "Error loading image: " + firstImagePath, e);
        }
        // Sự kiện click để điều hướng
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RoomDetailActivity.class);
            intent.putExtra("roomId", room.getRoomId());

            // Chỉ truyền ngày nhận và ngày trả nếu không phải null
            if (checkInDate != null) {
                intent.putExtra("checkInDate", checkInDate);
            }
            if (checkOutDate != null) {
                intent.putExtra("checkOutDate", checkOutDate);
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
        notifyDataSetChanged();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomType, tvRoomDescription, tvRoomPrice, tvRoomRating;
        ImageView imgRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomDescription = itemView.findViewById(R.id.tvRoomDescription);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomRating = itemView.findViewById(R.id.tvRoomRating);
            imgRoom = itemView.findViewById(R.id.imgRoom);
        }
    }
}

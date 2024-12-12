package com.example.btl_android_n2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.Models.Notification;
import com.example.btl_android_n2.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationList;
    private boolean isRead;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.tvMessage.setText(notification.getMessage());
        holder.tvCreatedDate.setText(notification.getCreatedDate());

        if(notification.getIsRead() == 1) {
            isRead = true;
        }
        else isRead = false;

        holder.checkBox.setChecked(isRead);

        // Optional: Xử lý sự kiện nếu cần
        holder.checkBox.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isRead = holder.checkBox.isChecked();
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvCreatedDate;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_Message);
            tvCreatedDate = itemView.findViewById(R.id.tv_createdDate);
            checkBox = itemView.findViewById(R.id.chkIsRead);
        }
    }
}
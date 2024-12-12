package com.example.btl_android_n2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.DAO.UserDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Review;
import com.example.btl_android_n2.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);

        UserDAO userDAO = new UserDAO(new DatabaseHelper(context));

        holder.tvCreatedDateReview.setText(review.getCreatedDate());
        holder.tvUserNameReview.setText(userDAO.getUserById(review.getUserId()).getFullName()); // Giả sử UserName từ UserId
        holder.tvRoomRating.setText(String.format("%.1f ★", review.getRating()));
        holder.tvComment.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCreatedDateReview, tvUserNameReview, tvRoomRating, tvComment;
        ImageView imgRoom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCreatedDateReview = itemView.findViewById(R.id.tv_createdDateReview);
            tvUserNameReview = itemView.findViewById(R.id.tvUserNameReview);
            tvRoomRating = itemView.findViewById(R.id.tvRoomRating);
            tvComment = itemView.findViewById(R.id.tvComment);
            imgRoom = itemView.findViewById(R.id.imgRoom);
        }
    }
}

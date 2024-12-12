package com.example.btl_android_n2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_android_n2.Models.Province;
import com.example.btl_android_n2.R;
import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {
    private List<Province> provinceList;

    public ProvinceAdapter(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    @NonNull
    @Override
    public ProvinceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvinceViewHolder holder, int position) {
        Province province = provinceList.get(position);
        holder.tvProvinceName.setText(province.getProvinceName());
        holder.tvAvailableRooms.setText("Số phòng khả dụng: " + province.getAvailableRooms());
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    static class ProvinceViewHolder extends RecyclerView.ViewHolder {
        TextView tvProvinceName, tvAvailableRooms;

        public ProvinceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProvinceName = itemView.findViewById(R.id.tvProvinceName);
            tvAvailableRooms = itemView.findViewById(R.id.tvAvailableRooms);
        }
    }
}

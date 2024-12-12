package com.example.btl_android_n2.Models;

public class Province {
    private int provinceId;       // ID của tỉnh/thành phố
    private String provinceName;  // Tên tỉnh/thành phố
    private int availableRooms;   // Số lượng phòng khả dụng trong tỉnh

    public Province(int provinceId, String provinceName, int availableRooms) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.availableRooms = availableRooms;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }
}


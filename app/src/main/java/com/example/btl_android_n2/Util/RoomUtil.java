package com.example.btl_android_n2.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_n2.Adapter.RoomAdapter;
import com.example.btl_android_n2.DAO.ProvinceDAO;
import com.example.btl_android_n2.DAO.RoomDAO;
import com.example.btl_android_n2.DatabaseHelper;
import com.example.btl_android_n2.Models.Room;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RoomUtil {

    /**
     * Khai báo random tên đường
     */
    private static final String[] streets = {
            "Nguyễn Trãi", "Lê Lợi", "Hồ Tùng Mậu", "Trần Hưng Đạo", "Nguyễn Huệ", "Hai Bà Trưng",
            "Phạm Ngũ Lão", "Đường 3/2", "Đường Võ Văn Kiệt", "Đường Nguyễn Văn Linh",
            "Đường Cách Mạng Tháng Tám", "Đường Lý Tự Trọng", "Đường Hoàng Diệu", "Đường Hùng Vương",
            "Đường Nguyễn Công Trứ", "Đường Phan Đình Phùng", "Đường Lý Nam Đế", "Đường Nguyễn Xí",
            "Đường Trần Nhật Duật", "Đường Võ Văn Tần", "Đường Lê Văn Lương", "Đường Tô Hiến Thành",
            "Đường Bạch Đằng", "Đường Tôn Đức Thắng", "Đường Phan Văn Hớn", "Đường Nguyễn Thị Minh Khai",
            "Đường Điện Biên Phủ", "Đường Lý Thường Kiệt", "Đường Xô Viết Nghệ Tĩnh"
    };

    /**
     * List quận
     */
    private static final String[] districts = {
            "Quận 1", "Quận 3", "Quận 7", "Bình Thạnh", "Tân Bình", "Phú Nhuận",
            "Cầu Giấy", "Thanh Xuân", "Hoàng Mai", "Long Biên", "Đống Đa", "Ba Đình",
            "Hải Châu", "Sơn Trà", "Ngũ Hành Sơn", "Cẩm Lệ", "Liên Chiểu", "Hòa Vang",
            "Kiến An", "Lê Chân", "Ngô Quyền", "Hồng Bàng", "Dương Kinh", "Đồ Sơn",
            "Ninh Kiều", "Cái Răng", "Bình Thủy", "Ô Môn", "Thốt Nốt", "Vĩnh Thạnh"
    };

    /**
     * Thêm phòng ngẫu nhiên vào db
     * @param context Context
     */
    public static void insertRandomRooms(Context context) {
        RoomDAO roomDAO = new RoomDAO(new DatabaseHelper(context));
        ProvinceDAO provinceDAO = new ProvinceDAO(new DatabaseHelper(context));
        String[] roomTypes = {"Phòng đơn", "Phòng đôi", "Căn hộ", "Homestay"};
        String[] descriptions = {
                "Phòng rộng rãi, view đẹp.",
                "Gần trung tâm, đầy đủ tiện nghi.",
                "Giá cả phải chăng, tiện lợi cho gia đình.",
                "Homestay yên tĩnh, phù hợp nghỉ dưỡng."
        };

        List<String> imageList = getAllImagesFromAssets(context);
        if (imageList.isEmpty()) {
            Toast.makeText(context, "Không có ảnh trong thư mục assets!", Toast.LENGTH_SHORT).show();
            return;
        }

        Random random = new Random();
        int successCount = 0;
        int failureCount = 0;

        for (int i = 0; i < 50; i++) {
            int provinceId;
            if (i < 10) {
                // Đảm bảo 10 phòng thuộc ProvinceId = 1
                provinceId = 1;
            } else {
                // Các phòng còn lại có ProvinceId ngẫu nhiên từ 2 đến 62
                provinceId = random.nextInt(61) + 2; // 2 đến 62
            }

            String provinceName = provinceDAO.getProvinceById(provinceId) != null
                    ? provinceDAO.getProvinceById(provinceId).getProvinceName()
                    : "Không xác định";

            String roomType = roomTypes[random.nextInt(roomTypes.length)];
            String description = descriptions[random.nextInt(descriptions.length)];
            double price = random.nextDouble() * 2000000 + 500000;
            double review = random.nextDouble() * 5;

            String address = String.format(
                    "%d %s, %s, %s",
                    random.nextInt(100) + 1,
                    streets[random.nextInt(streets.length)],
                    districts[random.nextInt(districts.length)],
                    provinceName
            );

            Collections.shuffle(imageList);
            String image = String.join(",", imageList.subList(0, 4));

            String status = "Còn phòng";

            Room room = new Room(0, "Phòng " + (i + 1), roomType, random.nextInt(4) + 1, price, description, review, status, provinceId, image, address);

            long result = roomDAO.insertRoom(room);
            if (result != -1) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        // Hiển thị thông báo tổng hợp
        Toast.makeText(context, "Thêm thành công: " + successCount + " phòng.\nThêm thất bại: " + failureCount + " phòng.", Toast.LENGTH_LONG).show();
    }

    private static List<String> getAllImagesFromAssets(Context context) {
        List<String> imageList = new ArrayList<>();
        try {
            String[] files = context.getAssets().list("images");
            if (files != null) {
                for (String file : files) {
                    imageList.add("images/" + file);
                }
            }
        } catch (IOException e) {
            Log.e("Assets", "Error reading asset files", e);
        }
        return imageList;
    }

    /**
     * Chọn ngẫu nhiên phòng
     * @param context context
     * @param limit số lượng
     * @return ds phòng
     */
    public static List<Room> selectRandomRooms(Context context, int limit) {
        RoomDAO roomDAO = new RoomDAO(new DatabaseHelper(context));
        List<Room> allRooms = roomDAO.getAllRooms();

        if (allRooms.size() <= limit) {
            return allRooms; // Trả về tất cả phòng nếu số lượng ít hơn hoặc bằng limit
        }

        Collections.shuffle(allRooms); // Trộn danh sách phòng ngẫu nhiên
        return allRooms.subList(0, limit); // Trả về số lượng phòng theo yêu cầu
    }

    /**
     * Tìm kiếm phòng
     * @param context context
     * @param criteria đối tượng search
     * @return list room search
     */
    public static List<Room> searchRooms(Context context, SearchCriteria criteria) {
        RoomDAO roomDAO = new RoomDAO(new DatabaseHelper(context));
        List<Room> allRooms = roomDAO.getAllRooms();
        List<Room> filteredRooms = new ArrayList<>();

        for (Room room : allRooms) {
            boolean matchesLocation = criteria.getLocation() == null || room.getAddress().contains(criteria.getLocation());
            boolean matchesPeople = criteria.getPeopleNumber() <= room.getPeopleNumber();
            if (matchesLocation && matchesPeople) {
                filteredRooms.add(room);
            }
        }

        return filteredRooms;
    }

    /**
     * Tính tổng tiền theo ngày nhận, ngày trả
     * @param checkInDateInput
     * @param checkOutDateInput
     * @return
     */
    public static double calculateTotalPrice(String checkInDateInput, String checkOutDateInput, Room currentRoom) {
        double totalPrice = 0;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Chuyển chuỗi ngày vào đối tượng Date
            Date checkIn = simpleDateFormat.parse(checkInDateInput);
            Date checkOut = simpleDateFormat.parse(checkOutDateInput);

            // Tính toán số ngày giữa checkIn và checkOut
            long diffInMillis = checkOut.getTime() - checkIn.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

            // Tính tổng tiền
            double pricePerDay = currentRoom.getPrice();
            totalPrice = diffInDays * pricePerDay;

            // Làm tròn tổng tiền về số nguyên
            totalPrice = Math.round(totalPrice);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalPrice;
    }
}

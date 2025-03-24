package com.example.prm392.repository;

<<<<<<< HEAD
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.prm392.DTO.MenuItemDTO;
import com.example.prm392.Dao.MenuItemsDao;
import com.example.prm392.database.AppDatabase;
=======
>>>>>>> main
import com.example.prm392.entity.MenuItems;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuItemsRepository {
<<<<<<< HEAD
    private MenuItemsDao menuItemsDao;
    private LiveData<List<MenuItems>> allMenuItems;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private LiveData<List<MenuItemDTO>> allMenuItemsWithRestaurant;
    public MenuItemsRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        menuItemsDao = db.menuItemsDao();
        allMenuItemsWithRestaurant = menuItemsDao.getAllMenuItemsWithRestaurant();
    }

    public LiveData<List<MenuItemDTO>> getAllMenuItemsWithRestaurant() {
        return allMenuItemsWithRestaurant;
    }

    public LiveData<List<MenuItems>> getAllMenuItems() {
        return allMenuItems;
=======
    private DatabaseReference databaseReference;

    public MenuItemsRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("menuItems");
>>>>>>> main
    }

    // Thêm món ăn vào Firebase
    public void insert(MenuItems menuItem) {
        String id = UUID.randomUUID().toString(); // Tạo ID ngẫu nhiên
        menuItem.setId(id);
        databaseReference.child(id).setValue(menuItem);
    }

<<<<<<< HEAD
    public void update(MenuItems menuItem) {
        executorService.execute(() -> menuItemsDao.update(menuItem));
    }

    public void delete(MenuItems menuItem) {
        executorService.execute(() -> menuItemsDao.delete(menuItem));
    }

    public void deleteMenuItemById(int id) {
        executorService.execute(() -> menuItemsDao.deleteById(id));
=======
    // Lấy danh sách món ăn theo restaurantId
    public void getMenuByRestaurant(int restaurantId, OnMenuLoadListener listener) {
        databaseReference.orderByChild("restaurantId").equalTo(restaurantId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<MenuItems> menuList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MenuItems menuItem = snapshot.getValue(MenuItems.class);
                            menuList.add(menuItem);
                        }
                        listener.onSuccess(menuList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
                    }
                });
    }

    // Tìm món ăn theo ID
    public void findById(String id, OnFindMenuItemListener listener) {
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    MenuItems menuItem = dataSnapshot.getValue(MenuItems.class);
                    listener.onSuccess(menuItem);
                } else {
                    listener.onFailure("Không tìm thấy món ăn");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
            }
        });
    }

    // 🔹 Interface callback cho getMenuByRestaurant
    public interface OnMenuLoadListener {
        void onSuccess(List<MenuItems> menuList);
        void onFailure(String errorMessage);
    }

    // 🔹 Interface callback cho findById
    public interface OnFindMenuItemListener {
        void onSuccess(MenuItems menuItem);
        void onFailure(String errorMessage);
>>>>>>> main
    }
}

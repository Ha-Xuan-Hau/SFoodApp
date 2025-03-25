package com.example.prm392.repository;

import com.example.prm392.entity.MenuItems;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuItemsRepository {
    private DatabaseReference databaseReference;

    public MenuItemsRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("menuItems");
    }

    // Thêm món ăn vào Firebase
    public void insert(MenuItems menuItem) {
        databaseReference.child(menuItem.getId()).setValue(menuItem);
    }

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
        Query query = databaseReference.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    MenuItems menuItem = dataSnapshot.getChildren().iterator().next().getValue(MenuItems.class);
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
    }
}

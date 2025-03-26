package com.example.prm392.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392.DTO.MenuItemDTO;
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

    public void insert(MenuItems menuItem) {
        String id = databaseReference.push().getKey();
        if (id != null) {
            menuItem.setId(id);
            databaseReference.child(id).setValue(menuItem).addOnSuccessListener(aVoid -> Log.d("Firebase", "Thêm món ăn thành công!"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Lỗi khi thêm món ăn!", e));;
        }
    }

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

    public LiveData<List<MenuItems>> getAllMenuItems() {
        MutableLiveData<List<MenuItems>> menuItemsLiveData = new MutableLiveData<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MenuItems> menuItemsList = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    MenuItems menuItem = itemSnapshot.getValue(MenuItems.class);
                    menuItemsList.add(menuItem);
                }
                menuItemsLiveData.setValue(menuItemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                menuItemsLiveData.setValue(null);
            }
        });

        return menuItemsLiveData;
    }

    // **Cập nhật món ăn**
    public void update(MenuItems menuItems) {
        String id = menuItems.getId();
        if (id != null) {
            databaseReference.child(id).setValue(menuItems);
        }
    }

    // **Xóa món ăn**
    public void delete(MenuItems menuItems) {
        String id = menuItems.getId();
        if (id != null) {
            databaseReference.child(id).removeValue();
        }
    }

    // **Xóa món ăn theo ID**
    public void deleteById(String id) {
        databaseReference.child(id).removeValue();
    }
    public interface OnMenuLoadListener {
        void onSuccess(List<MenuItems> menuList);
        void onFailure(String errorMessage);
    }

    public interface OnMenuItemsLoadedListener {
        void onMenuItemsLoaded(List<MenuItemDTO> menuItems);
    }


    // **Lấy danh sách menu items của một restaurant cụ thể**
    public LiveData<List<MenuItemDTO>> getMenuItemsByRestaurant(String restaurantId) {
        MutableLiveData<List<MenuItemDTO>> menuItemsLiveData = new MutableLiveData<>();
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference("restaurants");

        databaseReference.orderByChild("restaurantId").equalTo(restaurantId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot menuSnapshot) {
                        List<MenuItemDTO> menuItemDTOList = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : menuSnapshot.getChildren()) {
                            MenuItems menuItem = itemSnapshot.getValue(MenuItems.class);
                            if (menuItem != null) {
                                // Lấy thông tin nhà hàng
                                restaurantRef.child(restaurantId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot restaurantSnapshot) {
                                                String restaurantName = restaurantSnapshot.child("name").getValue(String.class);
                                                MenuItemDTO dto = new MenuItemDTO();
                                                dto.id = menuItem.getId();
                                                dto.menu_name = menuItem.getName();
                                                dto.price = menuItem.getPrice();
                                                dto.description = menuItem.getDescription();
                                                dto.restaurant_name = restaurantName;
                                                dto.uri = menuItem.getImageUrl();
                                                dto.restaurant_id = menuItem.getRestaurantId();
                                                menuItemDTOList.add(dto);
                                                menuItemsLiveData.setValue(menuItemDTOList);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {}
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        menuItemsLiveData.setValue(null);
                    }
                });

        return menuItemsLiveData;
    }


    // 🔹 Interface callback cho findById
    public interface OnFindMenuItemListener {
        void onSuccess(MenuItems menuItem);
        void onFailure(String errorMessage);
    }
}
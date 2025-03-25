package com.example.prm392.repository;

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

    // **Lấy danh sách menu items kèm thông tin restaurant**
    public LiveData<List<MenuItemDTO>> getAllMenuItemsWithRestaurant() {
        MutableLiveData<List<MenuItemDTO>> menuItemsLiveData = new MutableLiveData<>();
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance()
                .getReference("restaurants");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot menuSnapshot) {
                List<MenuItemDTO> menuItemDTOList = new ArrayList<>();
                for (DataSnapshot itemSnapshot : menuSnapshot.getChildren()) {
                    MenuItems menuItem = itemSnapshot.getValue(MenuItems.class);

                    if (menuItem != null) {
                        String restaurantId = menuItem.getRestaurantId();
                        restaurantRef.child(restaurantId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot restaurantSnapshot) {
                                        String restaurantName = restaurantSnapshot.child("email").getValue(String.class);
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


    public void getAllMenuItemsWithRestaurant(OnMenuItemsLoadedListener listener) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MenuItemDTO> menuList = new ArrayList<>();
                for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                    MenuItems menuItem = menuSnapshot.getValue(MenuItems.class);
                    if (menuItem != null) {
                        MenuItemDTO dto = new MenuItemDTO();
                        dto.id = menuItem.getId();
                        dto.menu_name = menuItem.getName();
                        dto.price = menuItem.getPrice();
                        dto.description = menuItem.getDescription();
                        dto.uri = menuItem.getImageUrl();
                        dto.restaurant_id = menuItem.getRestaurantId();
                        menuList.add(dto);
                    }
                }
                listener.onMenuItemsLoaded(menuList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onMenuItemsLoaded(new ArrayList<>());
            }
        });
    }

    // 🔹 Interface callback cho findById
    public interface OnFindMenuItemListener {
        void onSuccess(MenuItems menuItem);
        void onFailure(String errorMessage);
    }
}
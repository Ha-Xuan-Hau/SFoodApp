package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.entity.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

import java.util.List;

public class RestaurantRepository {
    private final DatabaseReference firebaseDatabase;

    public RestaurantRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Restaurants");
    }

    // Thêm nhà hàng vào Firebase
    public void insert(Restaurant restaurant, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(restaurant.getId()).setValue(restaurant);
    }

    // Lấy tất cả nhà hàng
    public void getAllRestaurants(ValueEventListener listener) {
        firebaseDatabase.addListenerForSingleValueEvent(listener);
    }

    // Cập nhật thông tin nhà hàng
    public void updateRestaurant(Restaurant restaurant, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(restaurant.getId()))
                .setValue(restaurant)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Xóa nhà hàng
    public void deleteRestaurant(int restaurantId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(restaurantId)).removeValue()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
}

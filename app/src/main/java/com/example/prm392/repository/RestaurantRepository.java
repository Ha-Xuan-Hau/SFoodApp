package com.example.prm392.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392.entity.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

import java.util.ArrayList;
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
    public LiveData<List<Restaurant>> getAllRestaurants() {
        MutableLiveData<List<Restaurant>> liveDataRestaurants = new MutableLiveData<>();

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Restaurant> restaurantList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Restaurant restaurant = data.getValue(Restaurant.class);
                    if (restaurant != null) {
                        restaurantList.add(restaurant);
                    }
                }
                liveDataRestaurants.setValue(restaurantList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                liveDataRestaurants.setValue(null);
            }
        });

        return liveDataRestaurants;
    }

    // Cập nhật thông tin nhà hàng
    public void updateRestaurant(Restaurant restaurant, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(restaurant.getId()))
                .setValue(restaurant)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public interface OnRestaurantLoadedListener {
        void onRestaurantLoaded(Restaurant restaurant);
    }

    public void getRestaurantById(String restaurantId, OnRestaurantLoadedListener listener) {
        firebaseDatabase.child(restaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Restaurant restaurant = snapshot.getValue(Restaurant.class);
                listener.onRestaurantLoaded(restaurant);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onRestaurantLoaded(null);
            }
        });
    }

    // Xóa nhà hàng
    public void deleteRestaurant(int restaurantId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(restaurantId)).removeValue()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
}
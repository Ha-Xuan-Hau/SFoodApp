package com.example.prm392.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392.entity.Restaurant;
import com.example.prm392.service.PasswordUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

    public void getByEmail(String email, OnFindUserListener listener) {
        // Thêm tag để dễ dàng lọc log
        final String TAG = "RestaurantRepository";

        Log.d(TAG, "Searching for restaurant with email: " + email);

        firebaseDatabase.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Log thông tin về snapshot
                        Log.d(TAG, "Snapshot exists: " + snapshot.exists());
                        Log.d(TAG, "Snapshot children count: " + snapshot.getChildrenCount());
                        Log.d(TAG, "Snapshot reference: " + snapshot.getRef().toString());

                        // Log toàn bộ dữ liệu snapshot
                        Object snapshotValue = snapshot.getValue();
                        Log.d(TAG, "Snapshot full data: " + (snapshotValue != null ? snapshotValue.toString() : "null"));

                        if(snapshot.exists()){
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                // Log thông tin chi tiết của từng child
                                Log.d(TAG, "Child key: " + childSnapshot.getKey());

                                try {
                                    Restaurant user = childSnapshot.getValue(Restaurant.class);
                                    Log.d(TAG, "Found user: " + user);

                                    // Log từng trường của user
                                    if (user != null) {
                                        Log.d(TAG, "User details:");
                                        Log.d(TAG, "- ID: " + user.getId());
                                        Log.d(TAG, "- Name: " + user.getName());
                                        Log.d(TAG, "- Email: " + user.getEmail());
                                        Log.d(TAG, "- Phone: " + user.getPhone());
                                    }

                                    listener.onSuccess(user);
                                    return;
                                } catch (Exception e) {
                                    // Log bất kỳ lỗi chuyển đổi nào
                                    Log.e(TAG, "Error converting snapshot to Restaurant", e);
                                }
                            }
                        }

                        // Log khi không tìm thấy người dùng
                        Log.d(TAG, "No user found with email: " + email);
                        listener.onFailure("Không tìm thấy người dùng với email này.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Log lỗi kết nối
                        Log.e(TAG, "Firebase Error: " + error.getMessage(), error.toException());
                        listener.onFailure("Lỗi kết nối Firebase: " + error.getMessage());
                    }
                });
    }

    public void Register(String name, String email, String phone, String password) {
        firebaseDatabase.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int newId = 1; // Mặc định nếu không có dữ liệu nào

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        int lastId = Integer.parseInt(snapshot.getKey()); // Lấy ID cuối cùng
                        newId = lastId + 1; // Cộng thêm 1
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi chuyển đổi ID: " + e.getMessage());
                    }
                }

                // Tạo user với ID mới
                Restaurant restaurant = new Restaurant(String.valueOf(newId),name, email,PasswordUtil.hashPassword(password),phone,"avtive",0);
                firebaseDatabase.child(String.valueOf(newId)).setValue(restaurant)
                        .addOnSuccessListener(aVoid -> System.out.println("Đăng ký thành công với ID: "))
                        .addOnFailureListener(e -> System.err.println("Lỗi đăng ký: " + e.getMessage()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Lỗi kết nối Firebase: " + databaseError.getMessage());
            }
        });
    }
    // Đăng nhập bằng email và mật khẩu
    public void login(String email, String password, OnLoginListener listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
        firebaseDatabase.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Restaurant user = snapshot.getValue(Restaurant.class);
                                if (PasswordUtil.checkPassword(password, user.getPass())) {
                                    listener.onSuccess(user);
                                    return;
                                }
                            }
                        }
                        listener.onFailure("Sai email hoặc mật khẩu!");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
                    }
                });
    }
    public void resetPassword(String email, String newPassword, OnPasswordChangeListener listener) {
        final String TAG = "RestaurantRepository";

        // Find user by email
        firebaseDatabase.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Hash the new password
                                String hashedNewPassword = PasswordUtil.hashPassword(newPassword);

                                // Update password in Firebase
                                snapshot.getRef().child("pass").setValue(hashedNewPassword)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "Password reset successfully for email: " + email);
                                            listener.onSuccess("Đặt lại mật khẩu thành công");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to reset password", e);
                                            listener.onFailure("Không thể đặt lại mật khẩu. Vui lòng thử lại.");
                                        });
                                return;
                            }
                        }

                        // If no user found
                        listener.onFailure("Không tìm thấy người dùng với email này");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Firebase error during password reset", databaseError.toException());
                        listener.onFailure("Lỗi kết nối: " + databaseError.getMessage());
                    }
                });
    }
    public interface OnLoginListener {
        void onSuccess(Restaurant user);
        void onFailure(String errorMessage);
    }
    public interface OnFindUserListener {
        void onSuccess(Restaurant user);
        void onFailure(String errorMessage);
    }
    public interface OnPasswordChangeListener {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
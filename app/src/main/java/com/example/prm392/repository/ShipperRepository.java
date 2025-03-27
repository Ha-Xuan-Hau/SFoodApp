package com.example.prm392.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.entity.Shipper;
import com.example.prm392.service.PasswordUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ShipperRepository {
    private final DatabaseReference shipperRef;

    public ShipperRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        shipperRef = database.getReference("Shippers");
    }

    // 🔹 Thêm shipper vào Firebase
    public void insertShipper(Shipper shipper) {
        shipperRef.child(shipper.getShipperId()).setValue(shipper);
    }

    // 🔹 Lấy thông tin shipper theo ID
    public void getShipperById(String shipperId, ValueEventListener listener) {
        shipperRef.child(String.valueOf(shipperId)).addListenerForSingleValueEvent(listener);
    }

    // 🔹 Cập nhật thông tin shipper
    public void updateShipper(Shipper shipper, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        shipperRef.child(String.valueOf(shipper.getShipperId()))
                .setValue(shipper)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // 🔹 Xóa shipper
    public void deleteShipper(int shipperId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        shipperRef.child(String.valueOf(shipperId)).removeValue()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // 🔹 Lấy danh sách tất cả shipper
    public void getAllShippers(ValueEventListener listener) {
        shipperRef.addListenerForSingleValueEvent(listener);
    }

    public void Register(String name, String email, String phone, String cccd, String password) {
        shipperRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
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
                Shipper shipper = new Shipper(String.valueOf(newId),name, phone, cccd, email, PasswordUtil.hashPassword(password),"avtive");
                shipperRef.child(String.valueOf(newId)).setValue(shipper)
                        .addOnSuccessListener(aVoid -> System.out.println("Đăng ký thành công với ID: "))
                        .addOnFailureListener(e -> System.err.println("Lỗi đăng ký: " + e.getMessage()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Lỗi kết nối Firebase: " + databaseError.getMessage());
            }
        });
    }


    public void getByEmail(String email, OnFindUserListener listener) {
        shipperRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                                Shipper user = dataSnapshot.getValue(Shipper.class);
                                listener.onSuccess(user);
                                return;
                            }
                        }
                        listener.onFailure("Không tìm thấy người dùng với email này.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFailure("Lỗi kết nối Firebase: " + error.getMessage());
                    }
                });
    }

    // Đăng nhập bằng email và mật khẩu
    public void login(String email, String password,OnLoginListener listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
        shipperRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Shipper user = snapshot.getValue(Shipper.class);
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
        final String TAG = "ShipperRepository";

        // Find user by email
        shipperRef.orderByChild("email").equalTo(email)
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
        void onSuccess(Shipper user);
        void onFailure(String errorMessage);
    }
    public interface OnFindUserListener {
        void onSuccess(Shipper user);
        void onFailure(String errorMessage);
    }
    public interface OnPasswordChangeListener {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}

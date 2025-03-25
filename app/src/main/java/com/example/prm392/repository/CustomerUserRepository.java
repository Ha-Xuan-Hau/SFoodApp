package com.example.prm392.repository;

import android.util.Log;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.service.PasswordUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class CustomerUserRepository {
    private DatabaseReference databaseReference;

    public CustomerUserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("customerUsers");
    }

    // Thêm khách hàng mới vào Firebase
    public void insert(CustomerUser customerUser) {
        String id = UUID.randomUUID().toString(); // Tạo ID ngẫu nhiên
        customerUser.setCustomerId(id);
        databaseReference.child(id).setValue(customerUser);
    }

    // Đăng nhập bằng email và mật khẩu
    public void login(String email, String password, OnLoginListener listener) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CustomerUser user = snapshot.getValue(CustomerUser.class);
                                if (PasswordUtil.checkPassword(password, user.getPassword())) {
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

    // Lấy khách hàng theo ID
    public void findById(String id, OnFindUserListener listener) {
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    CustomerUser user = dataSnapshot.getValue(CustomerUser.class);
                    listener.onSuccess(user);
                } else {
                    listener.onFailure("Không tìm thấy người dùng");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
            }
        });
    }

    // Interface callback cho login
    public interface OnLoginListener {
        void onSuccess(CustomerUser user);
        void onFailure(String errorMessage);
    }

    // Interface callback cho findById
    public interface OnFindUserListener {
        void onSuccess(CustomerUser user);
        void onFailure(String errorMessage);
    }

    public void Register(String name, String email, String phone, String address, String password) {
        databaseReference.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
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
                CustomerUser customerUser = new CustomerUser(String.valueOf(newId),name, email, phone, address, PasswordUtil.hashPassword(password),"");
                databaseReference.child(String.valueOf(newId)).setValue(customerUser)
                        .addOnSuccessListener(aVoid -> System.out.println("Đăng ký thành công với ID: "))
                        .addOnFailureListener(e -> System.err.println("Lỗi đăng ký: " + e.getMessage()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Lỗi kết nối Firebase: " + databaseError.getMessage());
            }
        });
    }


    public void getCustomerByEmail(String email, OnFindUserListener listener) {
        // Thêm tag để dễ dàng lọc log
        final String TAG = "CustomerUserRepository";

        Log.d(TAG, "Searching for customer with email: " + email);

        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Log thông tin về snapshot
                        Log.d(TAG, "Snapshot exists: " + dataSnapshot.exists());
                        Log.d(TAG, "Snapshot children count: " + dataSnapshot.getChildrenCount());
                        Log.d(TAG, "Snapshot reference: " + dataSnapshot.getRef().toString());

                        // Log toàn bộ dữ liệu snapshot
                        Object snapshotValue = dataSnapshot.getValue();
                        Log.d(TAG, "Snapshot full data: " + (snapshotValue != null ? snapshotValue.toString() : "null"));

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    CustomerUser user = snapshot.getValue(CustomerUser.class);

                                    // Log thông tin chi tiết của user
                                    Log.d(TAG, "Found user: " + user);

                                    // Log từng trường của user
                                    if (user != null) {
                                        Log.d(TAG, "User details:");
                                        Log.d(TAG, "- ID: " + user.getCustomerId());
                                        Log.d(TAG, "- Name: " + user.getFullName());
                                        Log.d(TAG, "- Email: " + user.getEmail());
                                        Log.d(TAG, "- Phone: " + user.getPhone());
                                    }

                                    listener.onSuccess(user);
                                    return;
                                } catch (Exception e) {
                                    // Log bất kỳ lỗi chuyển đổi nào
                                    Log.e(TAG, "Error converting snapshot to CustomerUser", e);
                                }
                            }
                        }

                        // Log khi không tìm thấy người dùng
                        Log.d(TAG, "No user found with email: " + email);
                        listener.onFailure("Không tìm thấy người dùng với email này.");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log lỗi kết nối
                        Log.e(TAG, "Firebase Error: " + databaseError.getMessage(), databaseError.toException());
                        listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
                    }
                });
    }
    public void resetPassword(String email, String newPassword, OnPasswordChangeListener listener) {
        final String TAG = "CustomerUserRepository";

        // Find user by email
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Hash the new password
                                String hashedNewPassword = PasswordUtil.hashPassword(newPassword);

                                // Update password in Firebase
                                snapshot.getRef().child("password").setValue(hashedNewPassword)
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
    public interface OnPasswordChangeListener {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}

package com.example.prm392.repository;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.service.PasswordUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
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
        databaseReference.child(customerUser.getCustomerId()).setValue(customerUser);
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
        Query query = databaseReference.orderByChild("customerId").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    CustomerUser user = dataSnapshot.getChildren().iterator().next().getValue(CustomerUser.class);
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
                CustomerUser customerUser = new CustomerUser(String.valueOf(newId),name, email, phone, address, password,"");
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
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CustomerUser user = snapshot.getValue(CustomerUser.class);
                                listener.onSuccess(user);
                                return;
                            }
                        }
                        listener.onFailure("Không tìm thấy người dùng với email này.");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
                    }
                });
    }
}

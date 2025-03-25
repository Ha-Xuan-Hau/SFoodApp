package com.example.prm392.repository;

import com.example.prm392.entity.OrderDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDetailRepository {
    private DatabaseReference databaseReference;

    public OrderDetailRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("orderDetails");
    }

    // Thêm OrderDetail vào Firebase
    public void insert(OrderDetail orderDetail) {
        databaseReference.child(orderDetail.getId()).setValue(orderDetail);
    }

    // Lấy OrderDetail theo ID
    public void findById(String id, OnFindOrderDetailListener listener) {
        Query query = databaseReference.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    OrderDetail orderDetail = dataSnapshot.getChildren().iterator().next().getValue(OrderDetail.class);
                    listener.onSuccess(orderDetail);
                } else {
                    listener.onFailure("Không tìm thấy đơn hàng chi tiết");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
            }
        });
    }

    // Lấy danh sách OrderDetail theo OrderId
    public void getOrderDetailsByOrderId(String orderId, OnOrderDetailsLoadListener listener) {
        databaseReference.orderByChild("orderId").equalTo(orderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<OrderDetail> orderDetailsList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            OrderDetail orderDetail = snapshot.getValue(OrderDetail.class);
                            orderDetailsList.add(orderDetail);
                        }
                        listener.onSuccess(orderDetailsList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFailure("Lỗi kết nối Firebase: " + databaseError.getMessage());
                    }
                });
    }

    // Interface callback cho findById
    public interface OnFindOrderDetailListener {
        void onSuccess(OrderDetail orderDetail);
        void onFailure(String errorMessage);
    }

    // Interface callback cho getOrderDetailsByOrderId
    public interface OnOrderDetailsLoadListener {
        void onSuccess(List<OrderDetail> orderDetails);
        void onFailure(String errorMessage);
    }
}

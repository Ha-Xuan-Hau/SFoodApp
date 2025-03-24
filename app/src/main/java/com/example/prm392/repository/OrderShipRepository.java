package com.example.prm392.repository;

import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.OrderShip;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class OrderShipRepository {
    private DatabaseReference firebaseDatabase;

    public OrderShipRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance(
                "https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app"
        ).getReference("ShipperEvaluations");

    }

    // Thêm đơn hàng vào Firebase
    public void insert(OrderShip orderShip, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        DatabaseReference orderRef = firebaseDatabase.push();
        orderShip.setOrderShipId(orderRef.getKey()); // Chuyển hash về số dương
        orderRef.setValue(orderShip)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Lấy danh sách đơn hàng theo Customer ID
    public void getOrdersByCustomer(int customerId, ValueEventListener listener) {
        firebaseDatabase.orderByChild("customerId").equalTo(customerId)
                .addListenerForSingleValueEvent(listener);
    }

    // Lấy danh sách đơn hàng theo Shipper ID và danh sách trạng thái
    public void getOrdersByShipper(Integer shipperId, List<String> statusList, ValueEventListener listener) {
        Query query = (shipperId == null)
                ? firebaseDatabase
                : firebaseDatabase.orderByChild("shipperId").equalTo(shipperId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<OrderShip> result = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    OrderShip order = data.getValue(OrderShip.class);
                    if (order != null && statusList.contains(order.getOrderStatus())) {
                        result.add(order);
                    }
                }
                listener.onDataChange(snapshot); // Trả kết quả về UI
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onCancelled(error);
            }
        });
    }

    // Tìm đơn hàng theo ID
    public void findById(String orderId, ValueEventListener listener) {
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    OrderShip order = data.getValue(OrderShip.class);
                    if (order != null && order.getOrderShipId().equals(orderId)) {
                        listener.onDataChange(data);
                        return;
                    }
                }
                listener.onDataChange(null); // Không tìm thấy
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onCancelled(error);
            }
        });
    }

    // Cập nhật trạng thái đơn hàng thành "Hoàn thành"
    public void completeOrder(String orderId, long completedAt, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(orderId))
                .child("orderStatus").setValue("Hoàn thành");
        firebaseDatabase.child(String.valueOf(orderId))
                .child("completedAt").setValue(completedAt)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Shipper nhận đơn hàng
    public void acceptOrder(String orderId, int shipperId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(orderId))
                .child("shipperId").setValue(shipperId)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Xóa đơn hàng
    public void deleteOrder(int orderId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(orderId)).removeValue()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
    public interface OnFindOrderShipListener {
        void onSuccess(OrderShip orderShip);
        void onFailure(String errorMessage);
    }

    public interface OnOrderShipLoadListener {
        void onSuccess(List<OrderShip> orderShipList);
        void onFailure(String errorMessage);
    }
}

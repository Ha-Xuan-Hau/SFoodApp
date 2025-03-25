package com.example.prm392.repository;

import android.util.Log;
import android.widget.Toast;

import com.example.prm392.ShipperActivity;
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
        ).getReference("OrderShip");

    }

    // Thêm đơn hàng vào Firebase
    public void insert(OrderShip orderShip) {
        firebaseDatabase.child(orderShip.getOrderShipId()).setValue(orderShip);
    }

    // Lấy danh sách đơn hàng theo Customer ID

    public void findById(String orderShipId, final OnFindOrderShipListener listener) {
        Query query = firebaseDatabase.child("OrderShip").orderByChild("orderShipId").equalTo(orderShipId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("FirebaseData", "Snapshot exists: " + snapshot.exists());
                if (snapshot.exists()) {
                    // Duyệt qua tất cả các children để kiểm tra đơn hàng
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        OrderShip orderShip = orderSnapshot.getValue(OrderShip.class);
                        Log.d("FirebaseData", "OrderShip found: " + orderShip);
                        if (orderShip != null) {
                            listener.onSuccess(orderShip);
                            return;
                        }
                    }
                    listener.onFailure("Không tìm thấy đơn hàng hợp lệ");
                } else {
                    listener.onFailure("Không tìm thấy đơn hàng với orderId: " + orderShipId);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onFailure("Lỗi khi kết nối Firebase: " + error.getMessage());
            }
        });
    }




    // Interface OnOrdersLoadedListener
    public interface OnOrdersLoadedListener {
        void onSuccess(List<OrderShip> orderShipList);  // Phương thức này sẽ được gọi khi tải dữ liệu thành công
        void onFailure(String message);  // Phương thức này sẽ được gọi khi có lỗi xảy ra
    }


    // Hàm getOrdersByShipper
    public void loadOrdersByShipperId(String shipperId, final OnOrdersLoadedListener listener) {
        if (shipperId == null || shipperId.isEmpty()) {
            listener.onFailure("Không tìm thấy shipperId!");
            return;
        }

        Query query = firebaseDatabase.orderByChild("shipperId").equalTo(shipperId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<OrderShip> orderShipList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    OrderShip order = data.getValue(OrderShip.class);
                    if (order != null) {
                        orderShipList.add(order);  // Thêm đơn hàng vào danh sách
                    }
                }
                // Trả kết quả về UI
                if (!orderShipList.isEmpty()) {
                    listener.onSuccess(orderShipList);
                } else {
                    listener.onFailure("Không có đơn hàng nào cho shipper");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onFailure("Lỗi khi tải dữ liệu: " + error.getMessage());
            }
        });
    }

    // Hàm tải các đơn hàng chưa có shipperId (đơn hàng cần chấp nhận)
    public void loadOrdersNeedAccept(final OnOrdersLoadedListener listener) {
        Query query = firebaseDatabase.orderByChild("shipperId").equalTo("");  // Tìm các đơn hàng chưa có shipperId

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<OrderShip> orderShipList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    OrderShip order = data.getValue(OrderShip.class);
                    if (order != null) {
                        orderShipList.add(order);  // Thêm đơn hàng vào danh sách
                    }
                }
                // Trả kết quả về UI
                if (!orderShipList.isEmpty()) {
                    listener.onSuccess(orderShipList);
                } else {
                    listener.onFailure("Không có đơn hàng cần chấp nhận!");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onFailure("Lỗi khi tải dữ liệu: " + error.getMessage());
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
    public void acceptOrder(String orderId, String shipperId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
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

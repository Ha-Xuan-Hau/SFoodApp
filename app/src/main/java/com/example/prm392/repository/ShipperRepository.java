package com.example.prm392.repository;

import com.example.prm392.entity.Shipper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

public class ShipperRepository {
    private final DatabaseReference shipperRef;

    public ShipperRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        shipperRef = database.getReference("Shippers");
    }

    // 🔹 Thêm shipper vào Firebase
    public void insertShipper(Shipper shipper, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String shipperId = String.valueOf(shipper.getShipperId());
        shipperRef.child(shipperId).setValue(shipper)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
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
}

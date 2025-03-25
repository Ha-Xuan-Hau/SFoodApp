package com.example.prm392.repository;

import com.example.prm392.entity.Payments;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

public class PaymentsRepository {
    private final DatabaseReference firebaseDatabase;

    public PaymentsRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Payments");
    }

    // Thêm thanh toán vào Firebase
    public void insert(Payments payment) {
        firebaseDatabase.child(payment.getId()).setValue(payment);

    }

    // Lấy thanh toán theo orderId
    public void getPaymentByOrder(int orderId, ValueEventListener listener) {
        firebaseDatabase.orderByChild("orderId").equalTo(orderId)
                .addListenerForSingleValueEvent(listener);
    }

    // Cập nhật trạng thái thanh toán
    public void updatePaymentStatus(int paymentId, String status, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(paymentId))
                .child("status").setValue(status)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Xóa thanh toán
    public void deletePayment(int paymentId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(paymentId)).removeValue()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
}

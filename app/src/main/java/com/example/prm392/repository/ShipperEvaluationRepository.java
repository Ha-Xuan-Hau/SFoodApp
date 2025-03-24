package com.example.prm392.repository;

import com.example.prm392.entity.ShipperEvaluation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

public class ShipperEvaluationRepository {
    private final DatabaseReference firebaseDatabase;

    public ShipperEvaluationRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ShipperEvaluations");
    }

    // Thêm đánh giá shipper vào Firebase
    public void insert(ShipperEvaluation evaluation, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        DatabaseReference evalRef = firebaseDatabase.push();
        evaluation.setId(evalRef.getKey()); // Chuyển hash thành số dương
        evalRef.setValue(evaluation)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Lấy đánh giá theo Order ID
    public void getEvaluationByOrder(String orderId, OnFindEvaluationListener listener) {
        firebaseDatabase.orderByChild("orderId").equalTo(orderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                ShipperEvaluation evaluation = data.getValue(ShipperEvaluation.class);
                                if (evaluation != null) {
                                    listener.onSuccess(evaluation);
                                    return; // Thoát ngay sau khi tìm thấy
                                }
                            }
                        }
                        listener.onFailure("Không tìm thấy đánh giá.");
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        listener.onFailure(error.getMessage());
                    }
                });
    }


    // Cập nhật đánh giá
    public void updateEvaluation(ShipperEvaluation evaluation, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(evaluation.getId()))
                .setValue(evaluation)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Xóa đánh giá
    public void deleteEvaluation(int evaluationId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseDatabase.child(String.valueOf(evaluationId)).removeValue()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
    public interface OnFindEvaluationListener {
        void onSuccess(ShipperEvaluation evaluation);
        void onFailure(String errorMessage);
    }

}

package com.example.prm392.fragment.cart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.Adapter.MyCartAdapter;
import com.example.prm392.model.Cart;
import com.example.prm392.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyCartsFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference db, ordersDb;
    private RecyclerView recyclerView;
    private MyCartAdapter cartAdapter;
    private List<Cart> cartModelList;
    TextView overtotalAmount;
    AppCompatButton buyBtn;

    public MyCartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("AddToCart").child(userId).child("CurrentUser");
        ordersDb = FirebaseDatabase.getInstance().getReference("Orders").child(userId);

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overtotalAmount = root.findViewById(R.id.textView6);
        buyBtn = root.findViewById(R.id.buy_now);

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String documentId = snapshot.getKey();
                    Cart cartModel = snapshot.getValue(Cart.class);
                    if (cartModel != null) {
                        cartModel.setDocumentId(documentId);
                        cartModelList.add(cartModel);
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });

        return root;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount", 0);
            overtotalAmount.setText("Total Bill: $" + totalBill);
        }
    };

    private void placeOrder() {
        if (cartModelList.isEmpty()) {
            return; // No items to place order
        }

        // Generate the order date
        String orderDate = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault()).format(new Date());

        // Calculate the total price
        float totalPrice = 0;
        for (Cart item : cartModelList) {
            totalPrice += item.getTotalPrice();
        }

        // Create OrderModel with order details
        Order order = new Order(orderDate, String.valueOf(totalPrice), cartModelList);

        // Generate a unique order ID and save the order to Firebase
        String orderId = ordersDb.push().getKey();
        if (orderId != null) {
            ordersDb.child(orderId).setValue(order)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Clear cart after placing order
                            db.removeValue().addOnCompleteListener(clearTask -> {
                                if (clearTask.isSuccessful()) {
                                    cartModelList.clear();
                                    cartAdapter.notifyDataSetChanged();
                                    overtotalAmount.setText("Total Bill: $0");

                                    // Display toast notification for successful purchase
                                    Toast.makeText(getActivity(), "Purchase successful!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
    }

}


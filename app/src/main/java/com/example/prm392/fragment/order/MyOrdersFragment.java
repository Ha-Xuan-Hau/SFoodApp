package com.example.prm392.fragment.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.Adapter.OrderAdapterUser;
import com.example.prm392.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference ordersDb;
    private RecyclerView recyclerView;
    private OrderAdapterUser orderAdapterUser;
    private List<Order> orderList;
    private TextView emptyOrderText;

    public void OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        ordersDb = FirebaseDatabase.getInstance().getReference("Orders").child(userId);

        recyclerView = root.findViewById(R.id.recycler_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        emptyOrderText = root.findViewById(R.id.empty_order_text);

        orderList = new ArrayList<>();
        orderAdapterUser = new OrderAdapterUser(getActivity(), orderList, order -> {
            // Handle click event
        });
        recyclerView.setAdapter(orderAdapterUser);

        loadOrders();

        return root;
    }

    private void loadOrders() {
        ordersDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Order order = snapshot.getValue(Order.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }
                    orderAdapterUser.notifyDataSetChanged();
                }

                // Show or hide empty order text
                if (orderList.isEmpty()) {
                    emptyOrderText.setVisibility(View.VISIBLE);
                } else {
                    emptyOrderText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
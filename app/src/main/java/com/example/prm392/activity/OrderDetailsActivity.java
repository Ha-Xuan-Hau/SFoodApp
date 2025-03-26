package com.example.prm392.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.Adapter.OrderItemAdapter;
import com.example.prm392.model.Cart;
import com.example.prm392.model.Order;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    private TextView orderIdText, totalPriceText;
    private RecyclerView orderItemsRecyclerView;
    private OrderItemAdapter orderItemAdapter;
    private List<Cart> orderItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Initialize views
        orderIdText = findViewById(R.id.order_id_text);
        totalPriceText = findViewById(R.id.total_price_text);
        orderItemsRecyclerView = findViewById(R.id.order_items_recyclerview);

        // Get passed order data from intent
        Order order = (Order) getIntent().getSerializableExtra("orderDetails");
        if (order != null) {
            orderIdText.setText("Order Date: " + order.getOrderDate());
            totalPriceText.setText("Total Price: $" + order.getTotalPrice());

            // Initialize RecyclerView
            orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            orderItemAdapter = new OrderItemAdapter(this, order.getListProduct());
            orderItemsRecyclerView.setAdapter(orderItemAdapter);
        }
    }
}

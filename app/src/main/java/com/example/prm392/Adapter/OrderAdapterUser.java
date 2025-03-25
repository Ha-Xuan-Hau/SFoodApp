package com.example.prm392.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.activity.OrderDetailsActivity;
import com.example.prm392.model.Order;

import java.util.List;

public class OrderAdapterUser extends RecyclerView.Adapter<OrderAdapterUser.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public OrderAdapterUser(Context context, List<Order> orderList, OnItemClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderDate.setText("Order Date: " + order.getOrderDate());
        holder.orderAmount.setText("Total Amount: $" + order.getTotalPrice());

        // Set click listener for order item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderDetails", order); // Pass order as a Serializable extra
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate, orderAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date);
            orderAmount = itemView.findViewById(R.id.order_amount);
        }
    }
}

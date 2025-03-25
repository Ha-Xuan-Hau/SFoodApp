package com.example.prm392.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.model.Cart;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private Context context;
    private List<Cart> itemList;

    public OrderItemAdapter(Context context, List<Cart> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_details, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Cart item = itemList.get(position);
        holder.itemName.setText(item.getProductName());
        holder.itemQuantity.setText("Quantity: " + item.getTotalQuantity());
        holder.itemPrice.setText("Price: $" + item.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity, itemPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemPrice = itemView.findViewById(R.id.item_price);
        }
    }
}

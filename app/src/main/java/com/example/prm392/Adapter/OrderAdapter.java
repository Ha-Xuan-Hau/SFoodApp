package com.example.prm392.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.OrderDetailActivity;
import com.example.prm392.R;
import com.example.prm392.entity.CustomerUser;
import com.example.prm392.entity.OrderShip;
import com.example.prm392.repository.CustomerUserRepository;
import com.example.prm392.repository.MenuItemsRepository;
import com.example.prm392.repository.OrderDetailRepository;
import com.example.prm392.repository.OrderShipRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderShip> orderShipList;
    private Context context;



    public OrderAdapter(List<OrderShip> orderShipList, Context context) {
        this.orderShipList = orderShipList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_ship, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderShip order = orderShipList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        holder.tvOrderId.setText("Mã đơn: " + order.getOrderShipId());
        holder.tvCustomerId.setText("Mã khách hàng: " + order.getCustomerId());
        holder.tvOrderStatus.setText("Trạng thái: " + order.getOrderStatus());
        holder.tvTotalPrice.setText("Tổng tiền: " + order.getTotalPrice() + "đ");
        holder.tvCreatedAt.setText("Ngày tạo: " +
                (order.getCreatedAt() > 0 ? sdf.format(order.getCreatedAt()) : "N/A"));

        if (order.getOrderStatus().equals("Đang giao")) {
            holder.tvOrderStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark));
        } else if (order.getOrderStatus().equals("Hoàn thành")) {
            holder.tvOrderStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvOrderStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        holder.cardOrderItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("ORDER_ID", order.getOrderShipId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderShipList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerId, tvOrderStatus, tvTotalPrice, tvCreatedAt;
        CardView cardOrderItem;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cardOrderItem = itemView.findViewById(R.id.cardOrderItem);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerId = itemView.findViewById(R.id.tvCustomerId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}


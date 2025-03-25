package com.example.prm392.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<Cart> cartModellList;


    public MyCartAdapter(Context context, List<Cart> cartModellList) {
        this.context = context;
        this.cartModellList = cartModellList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart model = cartModellList.get(position);
        holder.name.setText(cartModellList.get(position).getProductName());
        holder.price.setText(cartModellList.get(position).getProductPrice());
        holder.date.setText(cartModellList.get(position).getCurrentDate());
        holder.time.setText(cartModellList.get(position).getCurrentTime());
        holder.quantity.setText(cartModellList.get(position).getTotalQuantity());
        holder.totalPrice.setText(String.valueOf(cartModellList.get(position).getTotalPrice()));

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemFromDatabase(model, position);
            }
        });

        // Tính tổng giá trị
        int totalPrice = 0;
        for (Cart cart : cartModellList) {
            totalPrice += cart.getTotalPrice();
        }

        // Gửi dữ liệu tổng giá trị qua Intent
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        context.sendBroadcast(intent);
    }

    private void removeItemFromDatabase(Cart model, int position) {
        // Xóa mục khỏi Firebase Realtime Database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CartItems")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(model.getDocumentId());

        reference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xóa mục khỏi danh sách local và cập nhật RecyclerView
                cartModellList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartModellList.size());

                // Tính tổng giá trị mới và gửi broadcast
                int totalPrice = 0;
                for (Cart cart : cartModellList) {
                    totalPrice += cart.getTotalPrice();
                }
                Intent intent = new Intent("MyTotalAmount");
                intent.putExtra("totalAmount", totalPrice);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return cartModellList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, date, time, quantity, totalPrice;
        ImageView deleteItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            quantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
            deleteItem = itemView.findViewById(R.id.delete);
        }
    }
}

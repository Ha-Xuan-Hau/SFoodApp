package com.example.prm392.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.MenuItems;

import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemViewHolder> {
    private List<MenuItems> menuItemsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(MenuItems menuItem);
    }

    public MenuItemsAdapter(List<MenuItems> menuItemsList, OnItemClickListener listener) {
        this.menuItemsList = menuItemsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItems menuItem = menuItemsList.get(position);
        holder.name.setText(menuItem.getName());
        holder.price.setText(String.format("%.2f VND", menuItem.getPrice()));

        // Xóa item khi bấm nút delete
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(menuItem));
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView btnDelete;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(com.example.prm392.R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}

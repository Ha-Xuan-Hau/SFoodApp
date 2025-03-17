package com.example.prm392.Adaptor;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.MenuItems;

import java.util.ArrayList;
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
    public void filter(String query, String priceFilter) {
        List<MenuItems> filteredList = new ArrayList<>();

        for (MenuItems item : filteredList) {
            boolean matchesQuery = item.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesPrice = true;

            if (!priceFilter.equals("Tất cả")) {
                double price = item.getPrice();
                if (priceFilter.equals("Dưới 50K") && price >= 50000) {
                    matchesPrice = false;
                } else if (priceFilter.equals("50K - 100K") && (price < 50000 || price > 100000)) {
                    matchesPrice = false;
                } else if (priceFilter.equals("Trên 100K") && price <= 100000) {
                    matchesPrice = false;
                }
            }

            if (matchesQuery && matchesPrice) {
                filteredList.add(item);
            }
        }

        menuItemsList.clear();
        menuItemsList.addAll(filteredList);
        notifyDataSetChanged();
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

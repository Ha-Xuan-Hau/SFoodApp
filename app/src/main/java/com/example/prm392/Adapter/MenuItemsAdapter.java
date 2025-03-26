package com.example.prm392.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DTO.MenuItemDTO;
import com.example.prm392.MenuItemDetailActivity;
import com.example.prm392.R;
import com.example.prm392.entity.MenuItems;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemViewHolder> {
    private List<MenuItemDTO> menuItemsList;
    private OnItemClickListener listener;
    private Context context;
    public interface OnItemClickListener {
        void onDeleteClick(MenuItemDTO menuItem);
    }

    public MenuItemsAdapter(List<MenuItemDTO> menuItemsList, OnItemClickListener listener) {
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


    }


    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItemDTO menuItem = menuItemsList.get(position);
        holder.name.setText(menuItem.getMenu_name());
        holder.price.setText(String.format("%.2f VND", menuItem.getPrice()));

        //holder.imageView.setImageResource(R.drawable.bg_rounded_corner);

       // holder.imageView.setImageURI(Uri.parse(menuItem.getUri()));
        holder.itemView.setOnClickListener(v -> {
            Context itemContext = v.getContext();
            Intent intent = new Intent(itemContext, MenuItemDetailActivity.class);
            intent.putExtra("id", menuItem.getId());
            intent.putExtra("menu_name", menuItem.getMenu_name());
            intent.putExtra("price", menuItem.getPrice());
            intent.putExtra("description", menuItem.getDescription());
            intent.putExtra("restaurant_id", menuItem.getRestaurant_id());
            itemContext.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(menuItem));
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    public void updateList(List<MenuItemDTO> newList) {
        menuItemsList.clear();
        menuItemsList.addAll(newList);
        notifyDataSetChanged();
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, restaurant;
        ImageView btnDelete;
        ImageView imageView;
        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(com.example.prm392.R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}

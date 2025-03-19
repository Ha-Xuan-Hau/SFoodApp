package com.example.prm392;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Adaptor.MenuItemsAdapter;
import com.example.prm392.DTO.MenuItemDTO;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.viewmodel.MenuItemsViewModel;
import com.example.prm392.viewmodel.MenuItemsViewModelFactory;
import com.example.prm392.entity.MenuItems;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsActivity extends AppCompatActivity {
    private MenuItemsViewModel menuItemsViewModel;
    private MenuItemsAdapter adapter;
    private List<MenuItemDTO> menuItemsList = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private SearchView searchView;
    private Spinner spinnerFilterPrice;
    private List<MenuItemDTO> originalMenuItemsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_menu_items);
        Button btnAdd = findViewById(R.id.btn_add_menu_item);
        searchView = findViewById(R.id.search_view);
        spinnerFilterPrice = findViewById(R.id.spinner_filter_price);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuItemsAdapter(menuItemsList, menuItem -> {
            menuItemsViewModel.deleteMenuItem(menuItem);
            Toast.makeText(this, "Đã xóa món: " + menuItem.getMenu_name(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        menuItemsViewModel = new ViewModelProvider(this, new MenuItemsViewModelFactory(this))
                .get(MenuItemsViewModel.class);

        menuItemsViewModel.getAllMenuItemsWithRestaurant().observe(this, menuItems -> {
            originalMenuItemsList.clear();
            originalMenuItemsList.addAll(menuItems);
            menuItemsList.clear();
            menuItemsList.addAll(menuItems);
            adapter.notifyDataSetChanged();
        });

        btnAdd.setOnClickListener(v -> {
            showAddMenuItemDialog();
        });
        setupSearchView();
        setupPriceFilter();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });
    }

    private void setupPriceFilter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.price_filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterPrice.setAdapter(adapter);

        spinnerFilterPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterData(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterData(String searchText) {
        List<MenuItemDTO> filteredList = new ArrayList<>();
        String selectedPriceFilter = spinnerFilterPrice.getSelectedItem().toString();
        if (searchText.isEmpty() && selectedPriceFilter.equals("Tất cả")) {
            Log.d("FilterData", "Khôi phục danh sách gốc");
            adapter.updateList(new ArrayList<>(originalMenuItemsList));
            return;
        }
        for (MenuItemDTO item : menuItemsList) {
            boolean matchesSearch = searchText.isEmpty() || item.getMenu_name().toLowerCase().contains(searchText.toLowerCase());
            boolean matchesPrice = selectedPriceFilter.equals("Tất cả") ||
                    (selectedPriceFilter.equals("Dưới 50K") && item.getPrice() < 50000) ||
                    (selectedPriceFilter.equals("Trên 50K") && item.getPrice() >= 50000);

            if (matchesSearch && matchesPrice) {
                filteredList.add(item);
            }
        }

        adapter.updateList(filteredList); // Phương thức update trong Adapter
    }



    private void showAddMenuItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu_item, null);
        builder.setView(dialogView);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);
        EditText edtPrice = dialogView.findViewById(R.id.edtPrice);
        EditText edtImageUrl = dialogView.findViewById(R.id.edtImageUrl);
        Spinner selectRestaurant = dialogView.findViewById(R.id.select_restaurant);

        edtImageUrl.setFocusable(false);
        edtImageUrl.setOnClickListener(v -> openImagePicker());

        List<Restaurant> restaurantList = new ArrayList<>();
        List<String> restaurantNames = new ArrayList<>();

        menuItemsViewModel.getAllRestaurants().observe(this, restaurants -> {
            restaurantList.clear();
            restaurantList.addAll(restaurants);
            restaurantNames.clear();

            for (Restaurant restaurant : restaurants) {
                restaurantNames.add(restaurant.getEmail());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, restaurantNames);
            selectRestaurant.setAdapter(adapter);
        });

        builder.setTitle("Thêm Món Mới")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String description = edtDescription.getText().toString().trim();
                    String priceStr = edtPrice.getText().toString().trim();
                    String imageUrl = edtImageUrl.getText().toString().trim();

                    if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int price = Integer.parseInt(priceStr);

                    int selectedIndex = selectRestaurant.getSelectedItemPosition();
                    if (selectedIndex < 0 || selectedIndex >= restaurantList.size()) {
                        Toast.makeText(this, "Vui lòng chọn nhà hàng hợp lệ!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int restaurantId = restaurantList.get(selectedIndex).getId(); 

                    MenuItems newItem = new MenuItems(restaurantId, name, description, price, imageUrl,"available");
                    menuItemsViewModel.insert(newItem);

                    Toast.makeText(this, "Món mới đã được thêm!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



}


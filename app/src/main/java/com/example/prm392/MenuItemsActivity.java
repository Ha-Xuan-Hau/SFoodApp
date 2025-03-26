package com.example.prm392;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import com.example.prm392.Adapter.MenuItemsAdapter;
import com.example.prm392.DTO.MenuItemDTO;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.viewmodel.MenuItemsViewModel;
import com.example.prm392.viewmodel.MenuItemsViewModelFactory;
import com.example.prm392.entity.MenuItems;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        ImageButton btnOpenNav = findViewById(R.id.btn_open_nav);
        Intent intent = getIntent();
        String restaurantId = "1";
        btnOpenNav.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenu().add("Quản lý thông tin nhà hàng").setOnMenuItemClickListener(item -> {
                Intent intent2 = new Intent(this, UpdateRestaurantActivity.class);
                intent2.putExtra("restaurantId", restaurantId);
                startActivity(intent2);
                return true;
            });

            popupMenu.getMenu().add("Quản lý thực đơn").setOnMenuItemClickListener(item -> {
                Intent intent2 = new Intent(this, MenuItemsActivity.class);
                intent2.putExtra("restaurantId", restaurantId);
                startActivity(intent2);
                return true;
            });

            popupMenu.show();
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view_menu_items);
        Button btnAdd = findViewById(R.id.btn_add_menu_item);
        searchView = findViewById(R.id.search_view);
        spinnerFilterPrice = findViewById(R.id.spinner_filter_price);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuItemsAdapter(menuItemsList, menuItem -> {
            menuItemsViewModel.deleteMenuItem(menuItem.getId());
            Toast.makeText(this, "Đã xóa món: " + menuItem.getMenu_name(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        menuItemsViewModel = new ViewModelProvider (this).get(MenuItemsViewModel.class);

        menuItemsViewModel.fetchMenuItemsByRestaurant(restaurantId); // Gọi phương thức lấy dữ liệu theo restaurantId

        menuItemsViewModel.getAllMenuItemsWithRestaurant().observe(this, menuItems -> {
            originalMenuItemsList.clear();
            originalMenuItemsList.addAll(menuItems);
            menuItemsList.clear();
            menuItemsList.addAll(menuItems);
            adapter.notifyDataSetChanged();
        });

        btnAdd.setOnClickListener(v -> {
            showAddMenuItemDialog(restaurantId);
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


    private Uri selectedImageUri;

    private void showAddMenuItemDialog(String restautantId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu_item, null);
        builder.setView(dialogView);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);
        EditText edtPrice = dialogView.findViewById(R.id.edtPrice);

        Button btnSelectImage = dialogView.findViewById(R.id.btn_select_image);
        ImageView imgPhoto = dialogView.findViewById(R.id.img_photo);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        Uri selectedImageUri = null;

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });


        builder.setTitle("Thêm Món Mới")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String description = edtDescription.getText().toString().trim();
                    String priceStr = edtPrice.getText().toString().trim();
                    if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (selectedImageUri != null) {
                        // Upload the image to Firebase
                        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");
                        imageRef.putFile(selectedImageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String imageUrl = uri.toString();
                                        int price = Integer.parseInt(priceStr);
                                        MenuItems newItem = new MenuItems(restautantId, name, description, price, imageUrl,"available");
                                        menuItemsViewModel.insert(newItem);
                                        menuItemsViewModel.fetchMenuItemsByRestaurant(restautantId);
                                        Log.d("Image URL", "Uploaded Image URL: " + imageUrl);
                                    });
                                })
                                .addOnFailureListener(e -> Log.e("Error", "Image upload failed: " + e.getMessage()));
                    } else {
                        Log.e("Error", "No image selected");
                    }



                    Toast.makeText(this, "Món mới đã được thêm!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            selectedImageUri = data.getData();

            // Display the selected image in the ImageView
            if (selectedImageUri != null) {
                ImageView imgPhoto = findViewById(R.id.img_photo); // Reference the ImageView from the dialog
                imgPhoto.setImageURI(selectedImageUri); // Set the selected image URI
            } else {
                Log.e("Error", "Không có ảnh nào được chọn");
            }
        }
    }


}


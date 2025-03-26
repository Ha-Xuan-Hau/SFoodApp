package com.example.prm392;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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

import androidx.annotation.NonNull;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuItemsActivity extends AppCompatActivity {
    private MenuItemsViewModel menuItemsViewModel;
    private MenuItemsAdapter adapter;
    private List<MenuItemDTO> menuItemsList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private Spinner spinnerFilterPrice;
    private List<MenuItemDTO> originalMenuItemsList = new ArrayList<>();
    private ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);
        checkStoragePermission();
        ImageButton btnOpenNav = findViewById(R.id.btn_open_nav);
        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("restaurantId");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("menuItems");

        btnOpenNav.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenu().add("Quản lý thông tin nhà hàng").setOnMenuItemClickListener(item -> {
                Intent intent2 = new Intent(MenuItemsActivity.this, UpdateRestaurantActivity.class);
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

        menuItemsViewModel.fetchMenuItemsByRestaurant(restaurantId);

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

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 100);
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ cần quyền READ_MEDIA_IMAGES thay vì READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        } else {
            // Android 12 trở xuống
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
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

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        imgPhoto = dialogView.findViewById(R.id.img_photo);
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

                        String imageUrl = selectedImageUri.toString();
                        int price = Integer.parseInt(priceStr);
                        MenuItems newItem = new MenuItems(restautantId, name, description, price, imageUrl,"available");
                        String itemId = databaseReference.push().getKey();
                        if (itemId != null) {
                            databaseReference.child(itemId).setValue(newItem)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Thêm thành công!"))
                                    .addOnFailureListener(e -> Log.e("Firebase", "Lỗi khi thêm: " + e.getMessage()));
                            Toast.makeText(this, "Món mới đã được thêm!", Toast.LENGTH_SHORT).show();
                        }

                        // Upload the image to Firebase
                        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");
                        imageRef.putFile(selectedImageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        Log.d("Image URL", "Uploaded Image URL: " + imageUrl);
                                    });
                                })
                                .addOnFailureListener(e -> Log.e("Error", "Image upload failed: " + e.getMessage()));
                    } else {
                        Log.e("Error", "No image selected");
                    }

                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Quyền truy cập được cấp!");
            } else {
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (!showRationale) {
                    // Người dùng đã từ chối và chọn "Không hỏi lại"
                    showSettingsDialog();
                } else {
                    // Người dùng từ chối nhưng chưa chọn "Không hỏi lại"
                    Toast.makeText(this, "Bạn cần cấp quyền để chọn và tải ảnh!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yêu cầu quyền");
        builder.setMessage("Ứng dụng cần quyền để chọn ảnh. Vui lòng cấp quyền trong Cài đặt.");
        builder.setPositiveButton("Đi đến Cài đặt", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                Log.d("MenuItemsActivity", "Selected Image URI: " + selectedImageUri.toString());

                if (imgPhoto != null) {
                    imgPhoto.setImageURI(selectedImageUri);
                } else {
                    Log.e("MenuItemsActivity", "imgPhoto is null!");
                }
            }
        }
    }


}


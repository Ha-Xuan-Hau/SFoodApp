package com.example.prm392;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Adaptor.MenuItemsAdapter;
import com.example.prm392.viewmodel.MenuItemsViewModel;
import com.example.prm392.viewmodel.MenuItemsViewModelFactory;
import com.example.prm392.entity.MenuItems;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsActivity extends AppCompatActivity {
    private MenuItemsViewModel menuItemsViewModel;
    private MenuItemsAdapter adapter;
    private List<MenuItems> menuItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_menu_items);
        Button btnAdd = findViewById(R.id.btn_add_menu_item);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuItemsAdapter(menuItemsList, menuItem -> {
            menuItemsViewModel.delete(menuItem);
            Toast.makeText(this, "Đã xóa món: " + menuItem.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        menuItemsViewModel = new ViewModelProvider(this, new MenuItemsViewModelFactory(this))
                .get(MenuItemsViewModel.class);

        menuItemsViewModel.getAllMenuItems().observe(this, menuItems -> {
            menuItemsList.clear();
            menuItemsList.addAll(menuItems);
            adapter.notifyDataSetChanged();
        });

        btnAdd.setOnClickListener(v -> {
            showAddMenuItemDialog();
        });

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
        Spinner spnStatus = dialogView.findViewById(R.id.spnStatus);

        builder.setTitle("Thêm Món Mới")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    // Lấy dữ liệu từ các trường nhập
                    String name = edtName.getText().toString().trim();
                    String description = edtDescription.getText().toString().trim();
                    String priceStr = edtPrice.getText().toString().trim();
                    String imageUrl = edtImageUrl.getText().toString().trim();
                    String status = spnStatus.getSelectedItem().toString();

                    // Kiểm tra dữ liệu hợp lệ
                    if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || imageUrl.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int price = Integer.parseInt(priceStr);

                    // Tạo đối tượng món ăn mới
                    MenuItems newItem = new MenuItems(0, name, description, price, imageUrl, status);
                    menuItemsViewModel.insert(newItem);

                    Toast.makeText(this, "Món mới đã được thêm!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}


package com.example.prm392;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392.entity.MenuItems;
import com.example.prm392.viewmodel.MenuItemsViewModel;

import java.util.List;

public class MenuItemsActivity extends AppCompatActivity {
    private MenuItemsViewModel menuItemsViewModel;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        menuItemsViewModel = new ViewModelProvider(this).get(MenuItemsViewModel.class);

        editTextName = findViewById(R.id.edit_text_name);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPrice = findViewById(R.id.edit_text_price);
        buttonSave = findViewById(R.id.button_save);

        buttonSave.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());

            MenuItems menuItems = new MenuItems();
            menuItems.setName(name);
            menuItems.setDescription(description);
            menuItems.setPrice(price);

            menuItemsViewModel.insert(menuItems);
        });

        // Lấy danh sách MenuItems và cập nhật UI
        List<MenuItems> menuItemsList = menuItemsViewModel.getAllMenuItems();
        // Cập nhật UI với danh sách MenuItems
    }
}


package com.example.prm392.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.prm392.entity.MenuItems;
import com.example.prm392.repository.MenuItemsRepository;

import java.util.List;

public class MenuItemsViewModel extends AndroidViewModel {
    private MenuItemsRepository repository;
    private List<MenuItems> allMenuItems;

    public MenuItemsViewModel(Application application) {
        super(application);
        repository = new MenuItemsRepository(application);
        allMenuItems = repository.getAllMenuItems();
    }

    public void insert(MenuItems menuItems) {
        repository.insert(menuItems);
    }

    public List<MenuItems> getAllMenuItems() {
        return allMenuItems;
    }

    public void update(MenuItems menuItems) {
        repository.update(menuItems);
    }

    public void delete(MenuItems menuItems) {
        repository.delete(menuItems);
    }
}

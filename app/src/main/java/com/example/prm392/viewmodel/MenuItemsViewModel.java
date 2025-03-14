package com.example.prm392.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392.entity.MenuItems;
import com.example.prm392.repository.MenuItemsRepository;

import java.util.List;

public class MenuItemsViewModel extends ViewModel {
    private MenuItemsRepository repository;
    private final LiveData<List<MenuItems>> allMenuItems;
    public MenuItemsViewModel(Context context) {
        repository = new MenuItemsRepository(context);
        allMenuItems = repository.getAllMenuItems();
    }

    public void insert(MenuItems menuItems) {
        repository.insert(menuItems);
    }

    public LiveData<List<MenuItems>> getAllMenuItems() {
        return allMenuItems;
    }

    public void update(MenuItems menuItems) {
        repository.update(menuItems);
    }

    public void delete(MenuItems menuItems) {
        repository.delete(menuItems);
    }
}

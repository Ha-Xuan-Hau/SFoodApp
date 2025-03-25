package com.example.prm392.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392.DTO.MenuItemDTO;
import com.example.prm392.entity.MenuItems;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.repository.MenuItemsRepository;
import com.example.prm392.repository.RestaurantRepository;

import java.util.List;

public class MenuItemsViewModel extends ViewModel {
    private MenuItemsRepository repository;
    private RestaurantRepository restaurantRepository;
    private MutableLiveData<List<MenuItemDTO>> allMenuItemsWithRestaurant = new MutableLiveData<>();

    public MenuItemsViewModel() {
        repository = new MenuItemsRepository();
        restaurantRepository = new RestaurantRepository();
        fetchAllMenuItemsWithRestaurant();
    }

    // Thêm món ăn mới vào Firebase
    public void insert(MenuItems menuItems) {
        repository.insert(menuItems);
    }

    // Lấy danh sách tất cả món ăn kèm thông tin nhà hàng
    private void fetchAllMenuItemsWithRestaurant() {
        repository.getAllMenuItemsWithRestaurant(new MenuItemsRepository.OnMenuItemsLoadedListener() {
            @Override
            public void onMenuItemsLoaded(List<MenuItemDTO> menuItems) {
                allMenuItemsWithRestaurant.setValue(menuItems);
            }
        });
    }

    public LiveData<List<MenuItemDTO>> getAllMenuItemsWithRestaurant() {
        return allMenuItemsWithRestaurant;
    }

    // Cập nhật món ăn
    public void update(MenuItems menuItems) {
        repository.update(menuItems);
    }

    // Xóa món ăn
    public void deleteMenuItem(String menuItemId) {
        repository.deleteById(menuItemId);
    }

    // Lấy danh sách tất cả nhà hàng
    public LiveData<List<Restaurant>> getAllRestaurants() {
        return restaurantRepository.getAllRestaurants();
    }
}


package com.example.prm392.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
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
    private LiveData<List<MenuItemDTO>> allMenuItemsWithRestaurant;
    public MenuItemsViewModel(Context context) {
        repository = new MenuItemsRepository(context);
        restaurantRepository = new RestaurantRepository(context);
        allMenuItemsWithRestaurant = repository.getAllMenuItemsWithRestaurant();
    }

    public void insert(MenuItems menuItems) {
        repository.insert(menuItems);
    }

    public LiveData<List<MenuItemDTO>> getAllMenuItemsWithRestaurant() {
        return allMenuItemsWithRestaurant;
    }

    public void update(MenuItems menuItems) {
        repository.update(menuItems);
    }

    public void deleteMenuItem(MenuItemDTO menuItemDTO) {
        repository.deleteMenuItemById(menuItemDTO.getId());
    }

    public LiveData<List<Restaurant>> getAllRestaurants() {
        return restaurantRepository.getAllRestaurantsName();
    }
}

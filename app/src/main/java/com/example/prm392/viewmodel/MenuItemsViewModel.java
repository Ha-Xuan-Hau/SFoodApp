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
    private final MenuItemsRepository repository;
    private final RestaurantRepository restaurantRepository;
    private MutableLiveData<List<MenuItemDTO>> allMenuItemsWithRestaurant;

    public MenuItemsViewModel() {
        repository = new MenuItemsRepository();
        restaurantRepository = new RestaurantRepository();
        allMenuItemsWithRestaurant = new MutableLiveData<>();
    }

    public void insert(MenuItems menuItems) {
        repository.insert(menuItems);
    }

    public void fetchMenuItemsByRestaurant(String restaurantId) {
        allMenuItemsWithRestaurant = (MutableLiveData<List<MenuItemDTO>>) repository.getMenuItemsByRestaurant(restaurantId);
    }

    public LiveData<List<MenuItemDTO>> getAllMenuItemsWithRestaurant() {
        return allMenuItemsWithRestaurant;
    }

    public void update(MenuItems menuItems) {
        repository.update(menuItems);
    }

    public void deleteMenuItem(String menuItemId) {
        repository.deleteById(menuItemId);
    }

    public LiveData<List<Restaurant>> getAllRestaurants() {
        return restaurantRepository.getAllRestaurants();
    }
}



package com.example.prm392.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prm392.Dao.CustomerUserDao;
import com.example.prm392.Dao.MenuItemsDao;
import com.example.prm392.Dao.OrderDetailDao;
import com.example.prm392.Dao.OrderShipDao;
import com.example.prm392.Dao.PaymentsDao;
import com.example.prm392.Dao.RestaurantDao;
import com.example.prm392.Dao.ShipperDao;
import com.example.prm392.Dao.ShipperEvaluationDao;
import com.example.prm392.entity.CustomerUser;
import com.example.prm392.entity.MenuItems;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.OrderShip;
import com.example.prm392.entity.Payments;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.entity.Shipper;
import com.example.prm392.entity.ShipperEvaluation;

@Database(entities = {
        Restaurant.class,
        MenuItems.class,
        Payments.class,
        Shipper.class,
        CustomerUser.class,
        OrderShip.class,
        ShipperEvaluation.class,
        OrderDetail.class
},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract RestaurantDao restaurantDao();
    public abstract MenuItemsDao menuItemsDao();
    public abstract PaymentsDao paymentsDao();
    public abstract ShipperDao shipperDao();
    public abstract CustomerUserDao customerUserDao();
    public abstract OrderShipDao orderShipDao();
    public abstract ShipperEvaluationDao shipperEvaluationDao();
    public abstract OrderDetailDao orderDetailDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


package com.example.prm392;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Adapter.OrderAdapter;
import com.example.prm392.entity.OrderShip;
import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.CustomerUserRepository;
import com.example.prm392.repository.MenuItemsRepository;
import com.example.prm392.repository.OrderDetailRepository;
import com.example.prm392.repository.OrderShipRepository;
import com.example.prm392.repository.RestaurantRepository;
import com.example.prm392.repository.ShipperEvaluationRepository;
import com.example.prm392.repository.ShipperRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ShipperActivity extends AppCompatActivity implements OrderShipRepository.OnOrdersLoadedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navUseCase, navAccount;
    private ImageButton btnMenu;
    private ImageView ivAvatar;
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private OrderShipRepository orderShipRepository;
    private CustomerUserRepository customerUserRepository;
    private RestaurantRepository restaurantRepository;
    private MenuItemsRepository menuItemsRepository;
    private OrderDetailRepository orderDetailRepository;
    private ShipperRepository shipperRepository;
    private ShipperEvaluationRepository shipperEvaluationRepository;
    private String email;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);

        ShipperRepository shipperRepository = new ShipperRepository();

        Intent intent = getIntent();
        Integer shipperId = intent.getIntExtra("shipperId",1);

        // Ánh xạ view
        drawerLayout = findViewById(R.id.drawer_layout);
        navUseCase = findViewById(R.id.nav_usecase);
        navAccount = findViewById(R.id.nav_account);
        btnMenu = findViewById(R.id.btn_menu);
        ivAvatar = findViewById(R.id.ivAvatar);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // Đặt chiều rộng menu là 1/4 màn hình
        DrawerLayout.LayoutParams paramsUseCase = (DrawerLayout.LayoutParams) navUseCase.getLayoutParams();
        paramsUseCase.width = screenWidth / 2;
        navUseCase.setLayoutParams(paramsUseCase);

        DrawerLayout.LayoutParams paramsAccount = (DrawerLayout.LayoutParams) navAccount.getLayoutParams();
        paramsAccount.width = screenWidth / 2;
        navAccount.setLayoutParams(paramsAccount);

        // Mở menu Use Case khi nhấn vào nút menu
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        // Mở menu Account khi nhấn vào avatar
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        DatabaseReference shipperRef = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Shippers")
                .child(String.valueOf(shipperId));

        shipperRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Shipper shipper = snapshot.getValue(Shipper.class);
                    Log.d("ShipperActivity", "Shipper: " + (shipper != null ? shipper.getFullName() : "NULL"));

                    if (shipper != null) {
                        NavigationView navigationView = findViewById(R.id.nav_account);
                        View headerView = navigationView.getHeaderView(0);
                        TextView usernameTextView = headerView.findViewById(R.id.nav_header_username);
                        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);
                        email = shipper.getEmail();
                        usernameTextView.setText(shipper.getFullName());
                        emailTextView.setText(shipper.getEmail());
                    } else {
                        Toast.makeText(ShipperActivity.this, "Không tìm thấy shipper!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShipperActivity.this, "Không tìm thấy shipper!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Lỗi Firebase: " + error.getMessage());
            }
        });


        // Xử lý sự kiện chọn menu Use Case
        navUseCase.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // Xử lý khi người dùng chọn "Trang chủ"
                if (itemId == R.id.nav_home) {
                    Toast.makeText(ShipperActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();

                    // Xử lý khi người dùng chọn "Đơn hàng của bạn"
                } else if (itemId == R.id.nav_your_ordership) {
                    Toast.makeText(ShipperActivity.this, "Đơn hàng của bạn", Toast.LENGTH_SHORT).show();
                    // Trạng thái đơn hàng của shipper: "Đang giao", "Đã hủy"
                    String statusList = "Đang giao";
                    // Gọi loadOrders với shipperId và statusList
                    loadOrders(shipperId, statusList);

                    // Xử lý khi người dùng chọn "Chờ xác nhận"
                } else if (itemId == R.id.nav_accept_ordership) {
                    Toast.makeText(ShipperActivity.this, "Chờ xác nhận", Toast.LENGTH_SHORT).show();
                    // Trạng thái đơn hàng "Đang giao"
                    List<String> statusList = Arrays.asList("Đang giao");
                    // Gọi loadOrders với shipperId và statusList
                    orderShipRepository.loadOrdersNeedAccept(new OrderShipRepository.OnOrdersLoadedListener() {
                        @Override
                        public void onSuccess(List<OrderShip> orderShipList) {
                            OrderAdapter orderAdapter = new OrderAdapter(orderShipList, ShipperActivity.this);
                            RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);  // Thay R.id.recyclerView bằng id thực tế của RecyclerView trong layout của bạn
                            recyclerView.setLayoutManager(new LinearLayoutManager(ShipperActivity.this));  // Đảm bảo set LayoutManager cho RecyclerView
                            recyclerView.setAdapter(orderAdapter);  // Đặt adapter cho RecyclerView
                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    });

                    // Xử lý khi người dùng chọn "Đánh giá"
                } else if (itemId == R.id.nav_evaluator) {
                    Toast.makeText(ShipperActivity.this, "Đánh giá", Toast.LENGTH_SHORT).show();
                    // Trạng thái đơn hàng "Hoàn thành"
                    String statusList = "Hoàn thành";
                    // Gọi loadOrders với shipperId và statusList
                    loadOrders(shipperId, statusList);
                }

                // Đóng thanh navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        // Xử lý sự kiện chọn menu Account
        navAccount.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                Intent intent1 = new Intent(ShipperActivity.this, ProfileSetupActivity.class);

                // Kiểm tra shipperId trước khi truy vấn Firebase
                if (shipperId == null) {
                    Toast.makeText(ShipperActivity.this, "Không tìm thấy shipperId!", Toast.LENGTH_SHORT).show();
                    return true;
                }

                DatabaseReference shipperRef1 = FirebaseDatabase.getInstance()
                        .getReference("Shippers")
                        .child(String.valueOf(shipperId));


                shipperRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Shipper shipper = snapshot.getValue(Shipper.class);
                            if (shipper != null) {
                                intent1.putExtra("shipperId", shipper.getShipperId());
                                intent1.putExtra("fullName", shipper.getFullName());
                                intent1.putExtra("email", shipper.getEmail());
                                intent1.putExtra("cccd", shipper.getCccd());
                                intent1.putExtra("phone", shipper.getPhone());
                                startActivity(intent1);
                            } else {
                                Toast.makeText(ShipperActivity.this, "Lỗi dữ liệu shipper!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ShipperActivity.this, "Không tìm thấy shipper!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("Firebase", "Lỗi Firebase: " + error.getMessage());
                        Toast.makeText(ShipperActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            if(itemId == R.id.nav_changePass){
                // Kiểm tra shipperId trước khi truy vấn Firebase
                if (shipperId == null) {
                    Toast.makeText(ShipperActivity.this, "Không tìm thấy shipperId!", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Intent intent1 = new Intent(ShipperActivity.this, NewPasswordActivity.class);
                intent1.putExtra("email", email);
                changePass.launch(intent1);
            }
            if (itemId == R.id.nav_logout) {
                Intent intent1 = new Intent(ShipperActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
            }


            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });



        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        orderShipRepository = new OrderShipRepository();

        String statusList = "Đang giao";
        loadOrders(shipperId,statusList);


    }
    private void loadOrders(Integer shipperId, String statusList) {
        if (shipperId == null) {
            Toast.makeText(ShipperActivity.this, "Không tìm thấy shipperId!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm getOrdersByShipper và truyền 'this' làm listener
        orderShipRepository.loadOrdersByShipperId("" + shipperId, new OrderShipRepository.OnOrdersLoadedListener() {
            @Override
            public void onSuccess(List<OrderShip> orderShipList) {
                for(OrderShip o:orderShipList){
                    if(o.getOrderStatus().equals(statusList)){
                        OrderAdapter orderAdapter = new OrderAdapter(orderShipList, ShipperActivity.this);
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);  // Thay R.id.recyclerView bằng id thực tế của RecyclerView trong layout của bạn
                        recyclerView.setLayoutManager(new LinearLayoutManager(ShipperActivity.this));  // Đảm bảo set LayoutManager cho RecyclerView
                        recyclerView.setAdapter(orderAdapter);  // Đặt adapter cho RecyclerView
                    }
                }
                // Tạo adapter và gán vào RecyclerView

            }

            @Override
            public void onFailure(String message) {
                // Xử lý khi có lỗi
                Toast.makeText(ShipperActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onSuccess(List<OrderShip> orderShipList) {

    }

    @Override
    public void onFailure(String message) {

    }
    private final ActivityResultLauncher<Intent> changePass = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                }
            }
    );
}
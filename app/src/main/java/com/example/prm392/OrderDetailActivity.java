package com.example.prm392;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.entity.MenuItems;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.OrderShip;
import com.example.prm392.entity.ShipperEvaluation;
import com.example.prm392.repository.CustomerUserRepository;
import com.example.prm392.repository.MenuItemsRepository;
import com.example.prm392.repository.OrderDetailRepository;
import com.example.prm392.repository.OrderShipRepository;
import com.example.prm392.repository.ShipperEvaluationRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView fullName, phone, address, productName, description, quantity, price, quantityPrice, totalPrice, orderShipId, createdAt, completedAt,reviewTextView;
    private Button btnCompleteOrder, btnAcceptOrder;
    private CustomerUserRepository customerUserRepository;
    private MenuItemsRepository menuItemsRepository;
    private OrderDetailRepository orderDetailRepository;
    private OrderShipRepository orderShipRepository;
    private ShipperEvaluationRepository shipperEvaluationRepository;
    private OrderShip orderShip;  // Biến toàn cục để lưu trạng thái đơn hàng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        String orderId = getIntent().getStringExtra("ORDER_ID");

        fullName = findViewById(R.id.full_name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        productName = findViewById(R.id.product_name);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        quantityPrice = findViewById(R.id.quantity_price);
        totalPrice = findViewById(R.id.total_price);
        orderShipId = findViewById(R.id.orderShipId);
        createdAt = findViewById(R.id.createdAt);
        completedAt = findViewById(R.id.completedAt);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);
        btnAcceptOrder = findViewById(R.id.btnAcceptOrder);
        reviewTextView = findViewById(R.id.reviewTextView);


        new Thread(() -> {
            customerUserRepository = new CustomerUserRepository();
            menuItemsRepository = new MenuItemsRepository();
            orderDetailRepository = new OrderDetailRepository();
            orderShipRepository = new OrderShipRepository();
            shipperEvaluationRepository = new ShipperEvaluationRepository();

            CountDownLatch latch = new CountDownLatch(4); // Chờ 4 dữ liệu: orderShip, customerUser, orderDetail, menuItems

            final OrderShip[] orderShip = {null};
            final CustomerUser[] customerUser = {null};
            final OrderDetail[] orderDetail = {null};
            final MenuItems[] menuItems = {null};
            final ShipperEvaluation[] evaluation = {null};

            // 1. Lấy OrderShip
            orderShipRepository.findById(orderId, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        orderShip[0] = snapshot.getValue(OrderShip.class);
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    latch.countDown();
                }
            });

            try {
                latch.await(); // Đợi OrderShip tải xong
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (orderShip[0] == null) {
                runOnUiThread(() -> Toast.makeText(OrderDetailActivity.this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show());
                return;
            }

            // 2. Lấy CustomerUser
            DatabaseReference customerRef = FirebaseDatabase.getInstance()
                    .getReference("CustomerUsers")
                    .child(String.valueOf(orderShip[0].getCustomerId()));

            customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        customerUser[0] = snapshot.getValue(CustomerUser.class);
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    latch.countDown();
                }
            });

            // 3. Lấy OrderDetail
            orderDetailRepository.findById(orderShip[0].getOrderDetailId(), new OrderDetailRepository.OnFindOrderDetailListener() {
                @Override
                public void onSuccess(OrderDetail result) {
                    orderDetail[0] = result;
                    latch.countDown();
                }

                @Override
                public void onFailure(String errorMessage) {
                    latch.countDown();
                }
            });

            // 4. Lấy MenuItems
            menuItemsRepository.findById(orderDetail[0].getMenuItemsId(), new MenuItemsRepository.OnFindMenuItemListener() {
                @Override
                public void onSuccess(MenuItems result) {
                    menuItems[0] = result;
                    latch.countDown();
                }

                @Override
                public void onFailure(String errorMessage) {
                    latch.countDown();
                }
            });

            // 3. Chờ dữ liệu từ MenuItems tải xong
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 4. Kiểm tra evaluation[0] trước khi gọi getEvaluationByOrder
            if (evaluation[0] != null) {
                shipperEvaluationRepository.getEvaluationByOrder(evaluation[0].getId(), new ShipperEvaluationRepository.OnFindEvaluationListener() {
                    @Override
                    public void onSuccess(ShipperEvaluation result) {
                        evaluation[0] = result;
                        runOnUiThread(() -> updateUI(orderShip[0], customerUser[0], orderDetail[0], menuItems[0], evaluation[0]));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> updateUI(orderShip[0], customerUser[0], orderDetail[0], menuItems[0], null));
                    }
                });
            } else {
                // Nếu evaluation chưa có, cập nhật UI ngay với giá trị null
                runOnUiThread(() -> updateUI(orderShip[0], customerUser[0], orderDetail[0], menuItems[0], null));
            }
        }).start();

// Phương thức cập nhật UI

        // Xử lý khi bấm vào nút Hoàn thành
        btnCompleteOrder.setOnClickListener(v -> new Thread(() -> {
            if (orderShip != null) {
                long completedAtTime = System.currentTimeMillis(); // Lấy thời gian hoàn thành
                orderShipRepository.completeOrder(orderShip.getOrderShipId(), completedAtTime,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                runOnUiThread(() -> {
                                    Toast.makeText(OrderDetailActivity.this, "Đơn hàng đã hoàn thành!", Toast.LENGTH_SHORT).show();
                                });
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(OrderDetailActivity.this, "Lỗi khi cập nhật đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                );


                runOnUiThread(() -> {
                    btnCompleteOrder.setVisibility(View.GONE); // Ẩn nút sau khi hoàn thành
                    completedAt.setText("Thời gian hoàn thành: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(completedAtTime));
                    Toast.makeText(OrderDetailActivity.this, "Đơn hàng đã hoàn thành!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start());

        // Xử lý khi bấm vào nút Nhận đơn
        btnAcceptOrder.setOnClickListener(v -> new Thread(() -> {
            if (orderShip != null) {
                int shipperId = getCurrentShipperId(); // Lấy ID của shipper hiện tại
                orderShipRepository.acceptOrder(orderShip.getOrderShipId(), shipperId,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                runOnUiThread(() -> {
                                    Toast.makeText(OrderDetailActivity.this, "Shipper đã nhận đơn hàng!", Toast.LENGTH_SHORT).show();
                                });
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(OrderDetailActivity.this, "Lỗi khi cập nhật shipper: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                );


                runOnUiThread(() -> {
                    btnAcceptOrder.setVisibility(View.GONE); // Ẩn nút sau khi nhận đơn
                    Toast.makeText(OrderDetailActivity.this, "Nhận đơn thành công!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start());

        ImageView btnBack = findViewById(R.id.btnBack1);
        btnBack.setOnClickListener(v -> finish());
    }

    // Hàm giả định lấy shipperId của shipper hiện tại (bạn cần thay thế bằng cách lấy ID thực tế từ tài khoản shipper đang đăng nhập)
    private int getCurrentShipperId() {
        return 1; // Giả sử ID shipper hiện tại là 1, thay bằng cách lấy từ SharedPreferences hoặc database
    }
    private void updateUI(OrderShip orderShip, CustomerUser customerUser, OrderDetail orderDetail, MenuItems menuItems, ShipperEvaluation evaluation) {
        if (customerUser != null) {
            fullName.setText(customerUser.getFullName());
            phone.setText(customerUser.getPhone());
            address.setText(customerUser.getAddress());
        }

        if (menuItems != null) {
            productName.setText(menuItems.getName());
            description.setText(menuItems.getDescription());
            price.setText("đ" + menuItems.getPrice());
        }

        if (orderDetail != null) {
            quantity.setText("x" + orderDetail.getQuantity());
            quantityPrice.setText("Tổng tiền hàng: ₫" + orderDetail.getPrice());
            totalPrice.setText("Thành tiền: ₫" + (orderDetail.getPrice() + 16050));
        }

        if (orderShip != null) {
            orderShipId.setText("Mã đơn hàng: " + orderShip.getOrderShipId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            createdAt.setText("Thời gian đặt hàng: " + dateFormat.format(orderShip.getCreatedAt()));

            if (!orderShip.getShipperId().isEmpty() ) {
                if ("Đang giao".equals(orderShip.getOrderStatus())) {
                    btnCompleteOrder.setVisibility(View.VISIBLE);
                } else {
                    btnCompleteOrder.setVisibility(View.GONE);
                    completedAt.setText("Thời gian hoàn thành: " + dateFormat.format(orderShip.getCompletedAt()));
                }
            } else {
                btnAcceptOrder.setVisibility(View.VISIBLE);
            }
        }

        if (evaluation != null) {
            reviewTextView.setText("⭐ " + evaluation.getStarRate() + " sao\n" + evaluation.getReview());
            reviewTextView.setVisibility(View.VISIBLE);
        } else {
            reviewTextView.setVisibility(View.GONE);
        }
    }
}

package com.example.prm392;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.entity.MenuItems;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.OrderShip;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView fullName, phone, address, productName, description, quantity, price, quantityPrice, totalPrice, orderShipId, createdAt, completedAt, reviewTextView;
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

        // Initialize views
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

        // Initialize repositories
        customerUserRepository = new CustomerUserRepository();
        menuItemsRepository = new MenuItemsRepository();
        orderDetailRepository = new OrderDetailRepository();
        orderShipRepository = new OrderShipRepository();
        shipperEvaluationRepository = new ShipperEvaluationRepository();

        loadOrderData(orderId);
    }

    private void loadOrderData( String orderId) {
        // Using ExecutorService for background thread handling
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Perform Firebase query here
                orderShipRepository.findById(orderId, new OrderShipRepository.OnFindOrderShipListener() {
                    @Override
                    public void onSuccess(final OrderShip orderShip) {
                        // Ensure orderShip is not null before accessing its properties
                        if (orderShip != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateUIOrderShip(orderShip);
                                }
                            });
                            loadCustomerUser(orderShip.getCustomerId());
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OrderDetailActivity.this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(OrderDetailActivity.this, "Lỗi khi tải đơn hàng: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }



    private void loadCustomerUser(final String customerId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                customerUserRepository.findById(customerId, new CustomerUserRepository.OnFindUserListener() {
                    @Override
                    public void onSuccess(final CustomerUser user) {
                        // Run UI update on the main thread
                        if (orderShip != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateUICustomerUser(user);

                                }
                            });
                            loadOrderDetail(orderShip.getOrderDetailId());
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OrderDetailActivity.this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(final String errorMessage) {
                        // Run error handling on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Show failure message on UI thread
                            }
                        });
                    }
                });
            }
        });
    }

    private void loadOrderDetail(final String orderDetailId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                orderDetailRepository.findById(orderDetailId, new OrderDetailRepository.OnFindOrderDetailListener() {
                    @Override
                    public void onSuccess(final OrderDetail result) {
                        // Run UI update on the main thread
                        if (orderShip != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateUIOrderDetail(result);

                                }
                            });
                            loadMenuItems(result.getMenuItemsId());
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OrderDetailActivity.this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(final String errorMessage) {
                        // Run error handling on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(OrderDetailActivity.this, "Lỗi khi tải chi tiết đơn hàng!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void loadMenuItems(final String menuItemId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                menuItemsRepository.findById(menuItemId, new MenuItemsRepository.OnFindMenuItemListener() {
                    @Override
                    public void onSuccess(final MenuItems result) {
                        // Run UI update on the main thread
                        if (orderShip != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateUIMenuItems(result);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OrderDetailActivity.this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(final String errorMessage) {
                        // Run error handling on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(OrderDetailActivity.this, "Lỗi khi tải sản phẩm!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }


    // Update UI methods
    private void updateUIOrderShip(OrderShip orderShip) {
        if (orderShip != null) {
            orderShipId.setText("Mã đơn hàng: " + orderShip.getOrderShipId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            createdAt.setText("Thời gian đặt hàng: " + dateFormat.format(orderShip.getCreatedAt()));

            if (!orderShip.getShipperId().isEmpty()) {
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
    }

    private void updateUICustomerUser(CustomerUser customerUser) {
        if (customerUser != null) {
            fullName.setText(customerUser.getFullName());
            phone.setText(customerUser.getPhone());
            address.setText(customerUser.getAddress());
        }
    }

    private void updateUIOrderDetail(OrderDetail orderDetail) {
        if (orderDetail != null) {
            quantity.setText("x" + orderDetail.getQuantity());
            quantityPrice.setText("Tổng tiền hàng: ₫" + orderDetail.getPrice());
            totalPrice.setText("Thành tiền: ₫" + (orderDetail.getPrice() + 16050)); // Include any extra charges
        }
    }

    private void updateUIMenuItems(MenuItems menuItems) {
        if (menuItems != null) {
            productName.setText(menuItems.getName());
            description.setText(menuItems.getDescription());
            price.setText("đ" + menuItems.getPrice());
        }
    }


    // Handle button clicks
//    private void setupButtons() {
//        btnCompleteOrder.setOnClickListener(v -> completeOrder());
//        btnAcceptOrder.setOnClickListener(v -> acceptOrder());
//    }

//    private void completeOrder() {
//        if (orderShip != null) {
//            long completedAtTime = System.currentTimeMillis();
//            orderShipRepository.completeOrder(orderShip.getOrderShipId(), completedAtTime,
//                    new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(OrderDetailActivity.this, "Đơn hàng đã hoàn thành!", Toast.LENGTH_SHORT).show();
//                            btnCompleteOrder.setVisibility(View.GONE);
//                            completedAt.setText("Thời gian hoàn thành: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(completedAtTime));
//                        }
//                    },
//                    new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(OrderDetailActivity.this, "Lỗi khi cập nhật đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//        }
//    }

//    private void acceptOrder() {
//        if (orderShip != null) {
//            String shipperId = "1";
//            orderShipRepository.acceptOrder(orderShip.getOrderShipId(), shipperId,
//                    new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(OrderDetailActivity.this, "Shipper đã nhận đơn hàng!", Toast.LENGTH_SHORT).show();
//                            btnAcceptOrder.setVisibility(View.GONE);
//                        }
//                    },
//                    new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(OrderDetailActivity.this, "Lỗi khi cập nhật shipper: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//        }
//    }

}


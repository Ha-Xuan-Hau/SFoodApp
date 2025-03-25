package com.example.prm392.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.Adapter.ProductDetailsAdapter;
import com.example.prm392.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListProductsActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<Product> viewAllModelList;
    ProductDetailsAdapter productDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAllModelList = new ArrayList<>();
        productDetailsAdapter = new ProductDetailsAdapter(this, viewAllModelList);
        recyclerView.setAdapter(productDetailsAdapter);

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        String mode = getIntent().getStringExtra("mode");
        String type = getIntent().getStringExtra("type");
        Query query = (type != null)
                ? databaseReference.orderByChild("type").equalTo(type)
                : databaseReference.orderByChild("mode").equalTo(mode);

        // Fetch data from Firebase based on type
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewAllModelList.clear(); // Clear the list before adding new data

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);
                    String mode = snapshot.child("mode").getValue(String.class);
                    String imgUrl = snapshot.child("img_url").getValue(String.class);
                    // Handle `rating` and `price` fields, checking for both String and Long types
                    String rating = snapshot.child("rating").getValue() != null ? snapshot.child("rating").getValue().toString() : null;
                    String price = snapshot.child("price").getValue() != null ? snapshot.child("price").getValue().toString() : null;

                    if (name != null && rating != null && description != null && imgUrl != null && price != null) {
                        Product product = new Product();
                        product.setName(name);
                        product.setRating(rating);
                        product.setDescription(description);
                        product.setType(type);
                        product.setImg_url(imgUrl);
                        product.setPrice(price);
                        product.setMode(mode);
                        viewAllModelList.add(product);
                    }
                }

                productDetailsAdapter.notifyDataSetChanged(); // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}

package com.example.prm392.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.activity.ListProductsActivity;
import com.example.prm392.Adapter.HomeAdapter;
import com.example.prm392.Adapter.PopularProductsAdapters;
import com.example.prm392.Adapter.RecommendedProductsAdapter;
import com.example.prm392.constant.Constant;
import com.example.prm392.model.HomeCategory;
import com.example.prm392.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ScrollView scrollView;
    ProgressBar progressBar;
    // popular item
    RecyclerView popularRec, homeCatRec, recommendedRec;
    List<Product> productList;
    PopularProductsAdapters popularProductsAdapters;
    DatabaseReference popularDatabaseReference;
    // home category
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;
    // recommended
    List<Product> recommendedModelList;
    RecommendedProductsAdapter recommendedProductsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        popularRec = root.findViewById(R.id.pop_rec);
        homeCatRec = root.findViewById(R.id.explore_rec);
        recommendedRec = root.findViewById(R.id.recommended_rec);
        scrollView = root.findViewById(R.id.scroll_view);
        progressBar = root.findViewById(R.id.progressbar);


        TextView viewAllPopular = root.findViewById(R.id.view_all_popular);
        viewAllPopular.setOnClickListener(v -> showPopularProducts());

        TextView viewAllRecommended = root.findViewById(R.id.view_all_recommended);
        viewAllRecommended.setOnClickListener(v -> showRecommendedProducts());

        // popular items
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        productList = new ArrayList<>();
        popularProductsAdapters = new PopularProductsAdapters(getActivity(), productList);
        popularRec.setAdapter(popularProductsAdapters);
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendedModelList = new ArrayList<>();
        recommendedProductsAdapter = new RecommendedProductsAdapter(getActivity(), recommendedModelList);
        recommendedRec.setAdapter(recommendedProductsAdapter);

        // Initialize DatabaseReference for popular items
        popularDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        // Add ValueEventListener to fetch data for popular items from Firebase
        popularDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recommendedModelList.clear();
                productList.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract data from snapshot
                    String productName = snapshot.child("name").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String discount = snapshot.child("discount").getValue(String.class);
                    String imgUrl = snapshot.child("img_url").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);
                    String mode = snapshot.child("mode").getValue(String.class);
                    String rating = snapshot.child("rating").getValue(String.class);
                    String price = snapshot.child("price").getValue(String.class);

                    // Check for null values
                    if (productName != null && description != null && discount != null && imgUrl != null && type != null && rating != null) {
                        Product product = new Product();
                        product.setName(productName);
                        product.setRating(rating);
                        product.setDescription(description);
                        product.setDiscount(discount);
                        product.setPrice(price);
                        product.setType(type);
                        product.setImg_url(imgUrl);
                        product.setMode(mode);
                        if(mode != null && mode.equals(Constant.POPULAR_PRODUCT)) {
                            productList.add(product);
                        } else {
                            recommendedModelList.add(product);
                        }
                    }
                }
                popularProductsAdapters.notifyDataSetChanged(); // Notify adapter of data change
                progressBar.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // explore items
        homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(), categoryList);
        homeCatRec.setAdapter(homeAdapter);

        // Initialize DatabaseReference for home categories
        DatabaseReference homeDatabaseReference = FirebaseDatabase.getInstance().getReference().child("HomeCategory");

        // Add ValueEventListener to fetch data for home categories from Firebase
        homeDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract data from snapshot
                    String productName = snapshot.child("name").getValue(String.class);
                    String imgUrl = snapshot.child("img_url").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);

                    // Check for null values
                    if (productName != null && imgUrl != null && type != null) {
                        // Create a HomeCategory object with extracted properties
                        HomeCategory homeCategory = new HomeCategory(productName, imgUrl, type);
                        categoryList.add(homeCategory); // Add the model to the list
                    }
                }
                homeAdapter.notifyDataSetChanged(); // Notify adapter of data change
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        return root;
    }

    private void showPopularProducts() {
        // Navigate to a new fragment or activity showing the full list of popular products
        // For example, you might start an activity or navigate to another fragment
        Intent intent = new Intent(getActivity(), ListProductsActivity.class);
        intent.putExtra("mode", "Popular");
        startActivity(intent);
    }

    private void showRecommendedProducts() {
        // Similar to popular products, navigate to the recommended products list
        Intent intent = new Intent(getActivity(), ListProductsActivity.class);
        intent.putExtra("mode", "Recommended");
        startActivity(intent);
    }
}

package com.example.prm392.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.activity.ProductDetailsActivity;
import com.example.prm392.model.Product;

import java.util.List;

public class RecommendedProductsAdapter extends RecyclerView.Adapter<RecommendedProductsAdapter.ViewHolder> {

    Context context;
    List<Product> list;

    public RecommendedProductsAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentProduct = list.get(position);
        Glide.with(context).load(currentProduct.getImg_url()).into(holder.imageView);
        holder.name.setText(currentProduct.getName());
        holder.description.setText(currentProduct.getDescription());
        holder.rating.setText(currentProduct.getRating());

        // Set click listener to open ProductDetailsActivity with the selected product details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productDetails", currentProduct); // Pass product as a Serializable extra
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,description,rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.rec_img);
            name = itemView.findViewById(R.id.rec_name);
            description = itemView.findViewById(R.id.rec_dec);
            rating = itemView.findViewById(R.id.rec_rating);
        }
    }
}

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

public class PopularProductsAdapters extends RecyclerView.Adapter<PopularProductsAdapters.ViewHolder> {
    private Context context;
    private List<Product> productList;

    public PopularProductsAdapters(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        // Load product image, name, rating, description, and discount
        Glide.with(context).load(currentProduct.getImg_url()).into(holder.popImg);
        holder.name.setText(currentProduct.getName());
        holder.rating.setText(currentProduct.getRating());
        holder.description.setText(currentProduct.getDescription());
        holder.discount.setText(currentProduct.getDiscount());

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
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView popImg;
        TextView name, description, rating, discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            popImg=itemView.findViewById(R.id.pop_img);
            name=itemView.findViewById(R.id.pop_name);
            description=itemView.findViewById(R.id.pop_des);
            discount=itemView.findViewById(R.id.pop_discount);
            rating=itemView.findViewById(R.id.pop_rating);
        }
    }
}

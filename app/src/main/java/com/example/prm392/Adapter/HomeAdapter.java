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
import com.bumptech.glide.request.RequestOptions;
import com.example.prm392.R;
import com.example.prm392.activity.ListProductsActivity;
import com.example.prm392.model.HomeCategory;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Context context;
    List<HomeCategory> categoryList;

    public HomeAdapter(Context context, List<HomeCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cat_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Kiểm tra nếu position hợp lệ (Không vượt quá phạm vi)
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        // Tạo RequestOptions với listener cho quá trình tải hình ảnh
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.diet)  // Đặt hình ảnh placeholder trong quá trình tải
                .error(R.drawable.error_image);  // Đặt hình ảnh khi tải thất bại

        // Tải hình ảnh từ URL bằng Glide và áp dụng RequestOptions
        Glide.with(context)
                .load(categoryList.get(position).getImg_url())
                .apply(requestOptions)
                .into(holder.catImg);

        // Đặt tên danh mục
        holder.name.setText(categoryList.get(position).getName());

        // Sử dụng holder.getAdapterPosition() để lấy vị trí chính xác
        holder.itemView.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                // Tạo Intent và truyền thông tin
                Intent intent = new Intent(context, ListProductsActivity.class);
                intent.putExtra("type", categoryList.get(clickedPosition).getName());
                context.startActivity(intent);
            }
        });
    }


    // Phương thức này trả về hình ảnh lỗi dựa trên vị trí của mục danh mục

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.home_cat_img);
            name = itemView.findViewById(R.id.home_cat_name);
        }
    }
}

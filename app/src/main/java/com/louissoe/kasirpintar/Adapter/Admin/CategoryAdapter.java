package com.louissoe.kasirpintar.Adapter.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.louissoe.kasirpintar.Model.Category;
import com.louissoe.kasirpintar.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.lay_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category row = categories.get(position);
        holder.nama.setText(row.nama_category);
        holder.aktif.setText("Product Aktif: " + row.product_aktif);
        holder.takaktif.setText("Product Tidak Aktif: " + row.product_takaktif);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
class CategoryViewHolder extends RecyclerView.ViewHolder{
    TextView nama, aktif, takaktif;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        nama = itemView.findViewById(R.id.cat_nama);
        aktif = itemView.findViewById(R.id.cat_aktif);
        takaktif = itemView.findViewById(R.id.cat_takaktif);
    }
}

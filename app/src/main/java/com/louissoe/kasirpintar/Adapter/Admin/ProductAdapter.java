package com.louissoe.kasirpintar.Adapter.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.louissoe.kasirpintar.Listener.ProductListener;
import com.louissoe.kasirpintar.Model.Product;
import com.louissoe.kasirpintar.PubClass;
import com.louissoe.kasirpintar.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewholder> {
    Context context;
    List<Product> products;
    ProductListener listener;
    PubClass pubs = new PubClass();

    public ProductAdapter(Context context, List<Product> productsList, ProductListener listener) {
        this.context = context;
        this.products = productsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewholder(LayoutInflater.from(context).inflate(R.layout.lay_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewholder holder, int position) {
        Product row = products.get(position);
        holder.tv_name.setText(row.nama_barang);

        String newPrice = pubs.hargaFormat(Integer.parseInt(row.harga_barang));
        holder.tv_price.setText("Rp. " +newPrice);

        holder.tv_stock.setText(row.stok);
        holder.tv_status.setText(row.status_barang);
        holder.lay_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(row.id_barang);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

class ProductViewholder extends RecyclerView.ViewHolder {
    RelativeLayout lay_product;
    TextView tv_name, tv_price, tv_stock, tv_status;

    public ProductViewholder(@NonNull View itemView) {
        super(itemView);
        lay_product = itemView.findViewById(R.id.lay_product);
        tv_name = itemView.findViewById(R.id.product_name);
        tv_price = itemView.findViewById(R.id.product_price);
        tv_stock = itemView.findViewById(R.id.product_stock);
        tv_status = itemView.findViewById(R.id.product_status);
    }
}
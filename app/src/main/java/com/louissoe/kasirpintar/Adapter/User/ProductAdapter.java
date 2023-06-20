package com.louissoe.kasirpintar.Adapter.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.louissoe.kasirpintar.Model.Product;
import com.louissoe.kasirpintar.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductDataViewholder>{
    Context context;
    List<Product> products;

    public ProductAdapter(Context context, List<Product> productsList) {
        this.context = context;
        this.products = productsList;
    }

    @NonNull
    @Override
    public ProductDataViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductDataViewholder(LayoutInflater.from(context).inflate(R.layout.lay_product, parent, false));
    }



    @Override
    public void onBindViewHolder(ProductDataViewholder holder, int position) {
        Product row = products.get(position);
        holder.tv_name.setText(row.getNama_barang());
        holder.tv_price.setText(String.valueOf(row.getHarga_barang()));
        holder.tv_stok.setText(row.getStok());
        holder.tv_status.setText(row.getStatus_barang());
        holder.lay_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String id = row.getId_barang();

            }
        });
        //        String url  = products.get(position).getFoto_barang();
//        ImageRequest imgReq = new ImageRequest(url, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//                holder.product_image.setImageBitmap(response);
//            }
//        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        Volley.newRequestQueue(context).add(imgReq);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}


class ProductDataViewholder extends RecyclerView.ViewHolder {
    RelativeLayout lay_product;
    TextView tv_name, tv_price, tv_stok, tv_status;

    public ProductDataViewholder(@NonNull View itemView) {
        super(itemView);
        lay_product = itemView.findViewById(R.id.lay_product);
        tv_name = itemView.findViewById(R.id.product_name);
        tv_price = itemView.findViewById(R.id.product_price);
        tv_stok = itemView.findViewById(R.id.product_stock);
        tv_status = itemView.findViewById(R.id.product_status);
    }
}

package com.louissoe.kasirpintar.Fragment.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.louissoe.kasirpintar.Adapter.User.ProductAdapter;
import com.louissoe.kasirpintar.DetailProductActivity;
import com.louissoe.kasirpintar.FormProduct;
import com.louissoe.kasirpintar.Listener.ProductListener;
import com.louissoe.kasirpintar.Model.Product;
import com.louissoe.kasirpintar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    ProgressDialog dialog;

    ProductAdapter adapter;
    RecyclerView rv_product;
    Spinner sp_status, sp_category, sp_product;
    List<String> tags = new ArrayList<>();
    SearchView sv_product;
    ViewGroup root;
    RecyclerView.LayoutManager lay;
    RequestQueue queue;
    ArrayList<Product> arr = new ArrayList<>();
    String apiKey = "https://9b99-103-189-201-211.ngrok-free.app/";
    FloatingActionButton fab_add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_product, container, false);
        dialog = new ProgressDialog(this.getActivity());
        dialog.setTitle("Loading");


        rv_product = root.findViewById(R.id.rv_product);
        lay = new GridLayoutManager(this.getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rv_product.setLayoutManager(lay);
        rv_product.setHasFixedSize(true);

        sp_status = root.findViewById(R.id.sp_status);
        sp_category = root.findViewById(R.id.sp_category);
        sp_product = root.findViewById(R.id.sp_product);
        sv_product = root.findViewById(R.id.sv_product);

        fab_add = root.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FormProduct.class);
                i.putExtra("id","");
                startActivity(i);
            }
        });

        getData();

        return root;
    }
    void getData(){
        queue = Volley.newRequestQueue(this.getActivity());
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,apiKey + "api/databarang", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int count = 0;
                    while (count < response.length()) {
                        JSONObject obj = response.getJSONObject(count);
                        Product model = new Product(obj.getString("id_barang"), obj.getString("nama_barang"), obj.getString("stok"), obj.getInt("id_kategory"), obj.getString("nama_kategory"), obj.getString("status_barang"),obj.getString("barcode"), obj.getString("harga_barang"), obj.getString("foto_barang"));
                        arr.add(model);
                        count++;

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("ERROR DATA", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(req);

        adapter = new ProductAdapter(this.getActivity(), arr, onClick);
        rv_product.setAdapter(adapter);
    }
    private ProductListener onClick = new ProductListener() {
        @Override
        public void onClickListener(String id) {
            startActivity(new Intent(getActivity(), DetailProductActivity.class).putExtra("id", id));
        }
    };
}
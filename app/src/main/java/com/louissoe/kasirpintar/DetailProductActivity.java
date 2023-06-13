package com.louissoe.kasirpintar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.louissoe.kasirpintar.Fragment.User.ProductFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailProductActivity extends AppCompatActivity {

    RequestQueue queue;
    TextView product_name, product_price, product_stok, product_kategory;
    Button edit, delete;
    String nama, harga, stok, kategory, id_barang;
    String apiKey = "https://9b99-103-189-201-211.ngrok-free.app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_product);
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_stok = findViewById(R.id.product_stock);
        product_kategory= findViewById(R.id.product_kategory);
        getData();

        edit = findViewById(R.id.btn_edit);
        delete = findViewById(R.id.btn_delete);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailProductActivity.this, FormProduct.class);
                i.putExtra("id",id_barang);
                startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(DetailProductActivity.this);
                build.setTitle("Hapus Product");
                build.setMessage("Apakah Anda Yakin Ingin Menghapus " + nama  + " ?");
                build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        queue =  Volley.newRequestQueue(DetailProductActivity.this);
                        StringRequest req = new StringRequest(Request.Method.DELETE, apiKey + "api/databarang/" + id_barang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(DetailProductActivity.this, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DetailProductActivity.this, ProductFragment.class));
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                       });
                        queue.add(req);
                    }
                });
                build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = build.create();
                dialog.show();
            }
        });

    }
    void getData(){
        queue = Volley.newRequestQueue(this);
        String id =  getIntent().getStringExtra("id");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, apiKey + "api/databarang/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    id_barang = id;
                    nama =  response.getString("nama_barang");
                    harga = response.getString("harga_barang");
                    stok = response.getString("stok");
                    kategory = response.getString("nama_kategory");
                    product_name.setText(nama);
                    product_price.setText(harga);
                    product_stok.setText(stok + " Tersisa");
                    product_kategory.setText(kategory);


                } catch (JSONException e) {
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
    }
}
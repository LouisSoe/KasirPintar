package com.louissoe.kasirpintar.Fragment.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.louissoe.kasirpintar.Adapter.Admin.ProductAdapter;
import com.louissoe.kasirpintar.DetailProductActivity;
import com.louissoe.kasirpintar.FormProduct;
import com.louissoe.kasirpintar.Listener.ProductListener;
import com.louissoe.kasirpintar.Model.Product;
import com.louissoe.kasirpintar.PubClass;
import com.louissoe.kasirpintar.R;
import com.louissoe.kasirpintar.ScannerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String token;
    ArrayList<String> idCat = new ArrayList<>();
    ArrayList<String> nameCat = new ArrayList<>();
    int posCategory;
    String searchQuery, selectedStatus, selectedCategory ,selectedFilter= "";
    String newFilter = "asc";
    String newStatus;
    ArrayAdapter<String> categoryAdapter;
    ArrayList<Product> arr = new ArrayList<>();
    FloatingActionButton fab_add;
    PubClass pubs = new PubClass();
    SharedPreferences pref;
    ImageButton scanner;

    @Override
    public void onResume() {
        super.onResume();
        fetchData("", "", "", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_product, container, false);
        dialog = new ProgressDialog(this.getActivity());
        dialog.setTitle("Loading");

        pref = getActivity().getSharedPreferences("DataKasir", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        rv_product = root.findViewById(R.id.rv_product);
        lay = new GridLayoutManager(this.getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rv_product.setLayoutManager(lay);
        rv_product.setHasFixedSize(true);

        sp_status = root.findViewById(R.id.sp_status);
        sp_category = root.findViewById(R.id.sp_category);
        sp_product = root.findViewById(R.id.sp_product);
        sv_product = root.findViewById(R.id.sv_product);
        scanner = root.findViewById(R.id.btn_scanner);

        getCategory();

        scanner.setOnClickListener(scannerClicked);
        sp_status.setOnItemSelectedListener(statusChanged);
        sp_product.setOnItemSelectedListener(productChanged);
        sp_category.setOnItemSelectedListener(categoryChanged);
        sv_product.setOnQueryTextListener(searchChanged);

        fab_add = root.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FormProduct.class);
                i.putExtra("id","");
                startActivity(i);
            }
        });



        fetchData("", "", "", "");
        return root;
    }
    void getCategory(){
        queue = Volley.newRequestQueue(getActivity());
        String url = pubs.apiUrl() + "api/kategory";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                idCat.add(0, "0");
                nameCat.add(0, "Semua");
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String name = obj.getString("nama_kategory");
                        String id = obj.getString("id_kategory");
                        nameCat.add(name);
                        idCat.add(id);
                        categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nameCat);
                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_category.setAdapter(categoryAdapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(req);
    }

    View.OnClickListener scannerClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(), ScannerActivity.class));
        }
    };
    SearchView.OnQueryTextListener searchChanged = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchQuery = sv_product.getQuery().toString().trim();
            fetchData(searchQuery, newStatus, selectedCategory, newFilter);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            searchQuery = sv_product.getQuery().toString().trim();
            fetchData(searchQuery, newStatus, selectedCategory,newFilter);
            return true;
        }
    };

    AdapterView.OnItemSelectedListener productChanged = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedFilter = sp_product.getSelectedItem().toString().toLowerCase();
            switch (selectedFilter){
                case "a -> z":
                    newFilter = "asc";
                    break;
                case "z -> a":
                    newFilter = "desc";
                    break;
                default:
                    newFilter = "asc";
                    break;

            }
            fetchData(searchQuery, newStatus, selectedCategory, newFilter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    AdapterView.OnItemSelectedListener categoryChanged = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(idCat.isEmpty()){

            }else{
                selectedCategory = idCat.get(position);
                fetchData(searchQuery, newStatus, selectedCategory, newFilter);

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener statusChanged = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedStatus = sp_status.getSelectedItem().toString().toLowerCase();
            switch (selectedStatus){
                case "tersedia":
                    newStatus = "aktif";
                    break;
                case "stok kosong":
                    newStatus = "tidak_aktif";
                    break;
                default: newStatus = "semua";
            }
            fetchData(searchQuery, newStatus, selectedCategory, newFilter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private void fetchData(String queries, String status, String category, String filterProduct) {
        String url = pubs.apiUrl() + "api/databarang";

        StringBuilder filter = new StringBuilder();

        if (!TextUtils.isEmpty(queries)) {
            filter.append("search=" + queries);
        }

        if (status != null && !TextUtils.isEmpty(status) && !status.equalsIgnoreCase("semua")) {
            if (filter.length() > 0) {
                filter.append("&");
            }

            filter.append("status_barang=" + status);
        }

        if (category != null && !TextUtils.isEmpty(category) && !category.equalsIgnoreCase("0")) {
            if (filter.length() > 0) {
                filter.append("&");
            }
            filter.append("id_kategory=" + category);
        }
        if (filterProduct != null && !TextUtils.isEmpty(filterProduct) && !filterProduct.equalsIgnoreCase("semua")) {
            if (filter.length() > 0) {
                filter.append("&");
            }

            filter.append("urutkan=" + filterProduct);
        }

        String finalUrl = url;
        if (filter.length() > 0) {
            finalUrl += "?" + filter.toString();
        }

        queue = Volley.newRequestQueue(this.getActivity());

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, finalUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int count = 0;
                    arr.clear();
                    while (count < response.length()) {
                        JSONObject obj = response.getJSONObject(count);
                        Product model = new Product(obj.getString("id_barang"), obj.getString("nama_barang"), obj.getString("stok"), obj.getString("status_barang"), obj.getString("harga_barang"));
                        arr.add(model);
                        count++;
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
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
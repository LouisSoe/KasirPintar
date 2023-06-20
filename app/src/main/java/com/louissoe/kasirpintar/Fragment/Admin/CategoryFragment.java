package com.louissoe.kasirpintar.Fragment.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.louissoe.kasirpintar.Adapter.Admin.CategoryAdapter;
import com.louissoe.kasirpintar.Adapter.Admin.ProductAdapter;
import com.louissoe.kasirpintar.Model.Category;
import com.louissoe.kasirpintar.Model.Product;
import com.louissoe.kasirpintar.PubClass;
import com.louissoe.kasirpintar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CategoryFragment extends Fragment {

    ViewGroup root;
    SearchView sv_category;
    RecyclerView rv_category;
    RequestQueue queue;
    JsonArrayRequest req;
    String fixedUrl, searchQuery, token;
    ArrayList<Category> arr = new ArrayList<>();
    CategoryAdapter adapter;
    SharedPreferences pref;
    RecyclerView.LayoutManager lay;
    PubClass pubs = new PubClass();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);
        sv_category = root.findViewById(R.id.sv_category);
        rv_category = root.findViewById(R.id.rv_category);

        lay = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rv_category.setLayoutManager(lay);
        rv_category.setHasFixedSize(true);


        pref = getActivity().getSharedPreferences("DataKasir", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        sv_category.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = sv_category.getQuery().toString().toLowerCase();
                getData(searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = sv_category.getQuery().toString().toLowerCase();
                getData(searchQuery);
                return true;
            }
        });
        getData("");
        return root;
    }

    void getData(String query){
        queue = Volley.newRequestQueue(this.getActivity());

        if(query == "" || sv_category.getQuery().length() == 0){
            fixedUrl = pubs.apiUrl() + "api/kategory";
        }else{
            fixedUrl = pubs.apiUrl() + "api/kategory?search="+query;
        }


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,fixedUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int count = 0;
                    arr.clear();
                    while (count < response.length()) {
                        JSONObject obj = response.getJSONObject(count);
                        JSONObject jumlah = obj.getJSONObject("countdata");
                        int aktif = jumlah.getInt("aktif");
                        int tidak = jumlah.getInt("tidak_aktif");
                        Category model = new Category(obj.getString("nama_kategory"), aktif,tidak);
                        arr.add(model);
                        count++;
                    }
                    Log.i("sdat", response.toString());
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(req);

        adapter = new CategoryAdapter(this.getActivity(), arr);
        rv_category.setAdapter(adapter);
    }
}
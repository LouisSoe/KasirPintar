package com.louissoe.kasirpintar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.louissoe.kasirpintar.Fragment.Admin.ProductFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormProduct extends AppCompatActivity {

    Spinner in_kategory;
    Button action;
    Switch in_status;
    EditText in_nama, in_harga, in_barcode, in_stok, in_beli;
    String id, nama, harga, beli,barcode, stok, kategory, status, kat_nama, kat_id, selectedId, fixedUrl, token;
    int reqMethod;
    SharedPreferences pref;
    PubClass pubs = new PubClass();
     ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String>   idList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_product);

        pref = getSharedPreferences("DataKasir", MODE_PRIVATE);
        token = pref.getString("token", "");

        action = findViewById(R.id.btn_action);
        in_nama = findViewById(R.id.in_nama);
        in_harga = findViewById(R.id.in_harga);
        in_stok = findViewById(R.id.in_stok);
        in_barcode = findViewById(R.id.in_barcode);
        in_kategory = findViewById(R.id.in_kategory);
        in_status = findViewById(R.id.in_status);
        in_beli = findViewById(R.id.in_hargabeli);
        id = getIntent().getStringExtra("id");

        in_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    status = "aktif";
                        in_status.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(FormProduct.this, R.color.checked)));
                }else{
                    status = "tidak_aktif";
                    in_status.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(FormProduct.this, R.color.unchecked)));
                }

            }
        });

        getKategory();
        validateMethod();


        in_kategory.setOnItemSelectedListener(kategorySelected);

        action.setOnClickListener(actionClicked);
    }

    AdapterView.OnItemSelectedListener kategorySelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(idList.isEmpty()){

            }else {
                in_kategory.setSelection(position);
                selectedId = idList.get(position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener actionClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            queue = Volley.newRequestQueue(FormProduct.this);
            JSONObject obj = new JSONObject();
            try {
                obj.put("nama_barang", in_nama.getText());
                obj.put("stok", in_stok.getText());
                obj.put("id_kategory", Integer.parseInt(selectedId));
                obj.put("harga_barang", in_harga.getText());
                obj.put("harga_pembelian", in_beli.getText());
                obj.put("barcode", in_barcode.getText());
                obj.put("status_barang", status);
                obj.put("foto_barang", null);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            StringRequest reqAct = new StringRequest(reqMethod, fixedUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");
                        if (message.equals("Success")) {
                            showDialog("Berhasil Mengubah Data");
                            finish();
                        }

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
                public byte[] getBody() throws AuthFailureError {
                    return obj.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            queue.add(reqAct);
        }
    };
    private void validateMethod() {
        if(!id.isEmpty()){
            reqMethod = Request.Method.PUT;
            fixedUrl = pubs.apiUrl() + "api/databarang/" + id;

            queue = Volley.newRequestQueue(FormProduct.this);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, fixedUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        nama =  response.getString("nama_barang");
                        harga = response.getString("harga_barang");
                        beli = response.getString("harga_pembelian");
                        stok = response.getString("stok");
                        kategory = response.getString("id_kategory");
                        Log.e("data", kategory);
                        barcode = response.getString("barcode");
                        status = response.getString("status_barang");

                        in_nama.setText(nama);
                        in_harga.setText(harga);
                        in_beli.setText(beli);
                        in_stok.setText(stok);
                        in_barcode.setText(barcode);

                        if(status.equals("aktif")){
                            in_status.setChecked(true);
                            in_status.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(FormProduct.this, R.color.checked)));
                        }else{
                            in_status.setChecked(false);
                            in_status.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(FormProduct.this, R.color.unchecked)));
                        }

                        int indexSpinner = idList.indexOf(kategory);
                        in_kategory.setSelection(indexSpinner);

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
        }else{
            reqMethod = Request.Method.POST;
            fixedUrl = pubs.apiUrl() + "api/databarang";


        }
    }

    private void getKategory() {
        queue = Volley.newRequestQueue(FormProduct.this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, pubs.apiUrl() + "api/kategory", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                   nameList.clear();
                   idList.clear();
                    for (int i = 0; i < response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        kat_nama = obj.optString("nama_kategory");
                        kat_id = String.valueOf(obj.getInt("id_kategory"));
                        nameList.add(kat_nama);
                        idList.add(kat_id);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(FormProduct.this, android.R.layout.simple_spinner_item, nameList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        in_kategory.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
    void showDialog(String message){
        AlertDialog.Builder build = new AlertDialog.Builder(FormProduct.this);
        build.setTitle("Pesan");
        build.setMessage(message);
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = build.create();
        dialog.show();
    }

}
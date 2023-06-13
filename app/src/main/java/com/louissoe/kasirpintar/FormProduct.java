package com.louissoe.kasirpintar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class FormProduct extends AppCompatActivity {

    Spinner in_kategory;
    Button action;
    Switch in_status;
    EditText in_nama, in_harga, in_barcode, in_stok;
    String id, nama, harga, barcode, stok, kategory, status, kat_nama, kat_id, selectedId, fixedUrl;
    int reqMethod;
    String apiKey = "https://9b99-103-189-201-211.ngrok-free.app/";
    private ArrayList<String> nameList, idList;
    private ArrayAdapter<String> spinnerAdapter;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_product);

        action = findViewById(R.id.btn_action);
        in_nama = findViewById(R.id.in_nama);
        in_harga = findViewById(R.id.in_harga);
        in_stok = findViewById(R.id.in_stok);
        in_barcode = findViewById(R.id.in_barcode);
        in_kategory = findViewById(R.id.in_kategory);
        in_status = findViewById(R.id.in_status);
        id = getIntent().getStringExtra("id");

        getKategory();
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

        if(!id.isEmpty()){
            reqMethod = Request.Method.PATCH;
            fixedUrl = apiKey + "api/databarang/" + id;

            queue = Volley.newRequestQueue(FormProduct.this);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, apiKey + "api/databarang/" + id, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        nama =  response.getString("nama_barang");
                        harga = response.getString("harga_barang");
                        stok = response.getString("stok");
                        kategory = response.getString("id_kategory");
                        barcode = response.getString("barcode");
                        status = response.getString("status_barang");

                        in_nama.setText(nama);
                        in_harga.setText(harga);
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
            });
            queue.add(req);
        }else{
            reqMethod = Request.Method.POST;
            fixedUrl = apiKey + "api/databarang";

        }

        in_kategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedId = idList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue = Volley.newRequestQueue(FormProduct.this);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("nama_barang", in_nama.getText());
                    obj.put("stok", in_stok.getText());
                    obj.put("id_kategory", Integer.parseInt(selectedId));
                    obj.put("harga_barang", in_harga.getText());
                    obj.put("barcode", in_barcode.getText());
                    obj.put("status_barang", "aktif");
                    obj.put("foto_barang", null);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Log.e("data", obj.toString());
                StringRequest req = new StringRequest(reqMethod, fixedUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
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
                };
                queue.add(req);
            }
        });
    }

    private void getKategory() {
        queue = Volley.newRequestQueue(FormProduct.this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, apiKey + "api/kategory", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    nameList = new ArrayList<>();
                    idList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        kat_nama = obj.getString("nama_kategory");
                        kat_id = String.valueOf(obj.getInt("id_kategory"));
                        nameList.add(kat_nama);
                        idList.add(kat_id);
                    }

                    spinnerAdapter = new ArrayAdapter<>(FormProduct.this, android.R.layout.simple_spinner_item,nameList);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    in_kategory.setAdapter(spinnerAdapter);

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
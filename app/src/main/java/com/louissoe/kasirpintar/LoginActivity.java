package com.louissoe.kasirpintar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText usr, pwd;
    String username, password;
    Button login;
    SharedPreferences pref;
    PubClass pubClass = new PubClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usr = findViewById(R.id.usr);
        pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.login);

        pref = getSharedPreferences("DataKasir", MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck();
            }
        });
    }

    private void loginCheck() {
        username = String.valueOf(usr.getText());
        password = String.valueOf(pwd.getText());

        RequestQueue queue = Volley.newRequestQueue(this);
        if(!username.isEmpty() && !password.isEmpty()){
            JSONObject user = new JSONObject();
            try {
                user.put("email", username);
                user.put("password", password);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,pubClass.apiUrl() + "api/login", user, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject data = response.getJSONObject("User");
                        String namaUser  = data.getString("nama_pengguna");
                        String idUser = data.getString("id_user");
                        String token = response.getString("token").toString();

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("idUser", idUser);
                        editor.putString("namaUser", namaUser);
                        editor.putString("token", token);
                        editor.putBoolean("isLogin", true);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

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
            Toast.makeText(this, "Isi Username dan Password", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.louissoe.kasirpintar.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.louissoe.kasirpintar.LoginActivity;
import com.louissoe.kasirpintar.PubClass;
import com.louissoe.kasirpintar.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    PubClass pubs = new PubClass();
    SharedPreferences pref;
    TextView sayHi, tgl;
    Button logout;
    String token;
    RequestQueue queue;
    StringRequest req;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        pref = requireActivity().getSharedPreferences("DataKasir", MODE_PRIVATE);
        token = pref.getString("token", "");

        logout = root.findViewById(R.id.logoutBtn);
        sayHi = root.findViewById(R.id.sayHi);
        tgl = root.findViewById(R.id.tanggal);
        String url = pubs.apiUrl();
        tgl.setText(pubs.getDate());
        queue = Volley.newRequestQueue(this.getActivity());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = new StringRequest(Request.Method.POST, url + "api/logout", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String message = obj.getString("message");
                            Log.i("msf", message);
                            if(message.equals("Berhasil Logout")){
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("isLogin", false);
                                editor.clear().apply();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
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
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };
                queue.add(req);
            }
        });
        boolean check = pref.getBoolean("isLogin", false);
        Log.e("isLogin: ", String.valueOf(check));
        if(pref.getBoolean("isLogin", false) == false){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else{
            sayHi.setText("Hai, "+pref.getString("namaUser","User"));
        }
        return root;
    }
}
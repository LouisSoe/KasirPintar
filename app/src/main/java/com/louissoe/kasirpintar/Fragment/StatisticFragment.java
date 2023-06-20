package com.louissoe.kasirpintar.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.louissoe.kasirpintar.PubClass;
import com.louissoe.kasirpintar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticFragment extends Fragment {

    ViewGroup root;
    BarChart barPenjualan;
    PieChart pieTotal;
    RequestQueue queue;
    JsonObjectRequest req;
    String url, output, total, token;
    SimpleDateFormat inputFormat, outputFormat;
    PubClass pubs = new PubClass();
    int totalDays;
    ArrayList<BarEntry> dataBar = new ArrayList<>();
    ArrayList<PieEntry> dataPie = new ArrayList<>();

    SharedPreferences pref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_statistic, null);
        barPenjualan = root.findViewById(R.id.barPenjualan);
        pieTotal = root.findViewById(R.id.pieTotal);

        pref = getActivity().getSharedPreferences("DataKasir", MODE_PRIVATE);
        token = pref.getString("token", "");

         url = pubs.apiUrl();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            totalDays = currentDate.lengthOfMonth();
        }
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");
         outputFormat = new SimpleDateFormat("dd");

         getData();


        return root;
    }
    void getData(){
        queue = Volley.newRequestQueue(getActivity());
        req = new JsonObjectRequest(Request.Method.GET, url + "api/chart/penjualan", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String tgl = obj.getString("date");
                        total = obj.getString("total");
                        Date date = inputFormat.parse(tgl);
                        output = outputFormat.format(date);

                        dataBar.add(new BarEntry(Integer.parseInt(output), Integer.parseInt(total)));
                    }
                    BarDataSet barDS = new BarDataSet(dataBar, "Penjualan Perhari");
                    BarData barData = new BarData();
                    Description desc = new Description();
                    barPenjualan.setDescription(desc);
                    barDS.setColor(ColorTemplate.MATERIAL_COLORS[3], 255);
                    barDS.setValueTextColor(Color.BLACK);
                    barData.addDataSet(barDS);

                    barPenjualan.setData(barData);
                    barPenjualan.setFitBars(true);
                    barPenjualan.animateY(2000);
                    barPenjualan.invalidate();

                    String totalBulanan = response.getString("total_bulanan");
                    String totalMingguan = response.getString("total_mingguan");

                    dataPie.add(new PieEntry(Integer.parseInt(totalMingguan), "Penghasilan Minggu Ini"));
                    dataPie.add(new PieEntry(Integer.parseInt(totalBulanan), "Penghasilan Bulan Ini"));
                    PieDataSet pieDS = new PieDataSet(dataPie, "Daftar Penjualan");
                    pieDS.setValueTextSize(15f);
                    pieDS.setValueTextColor(Color.BLACK);

                    PieData pieData = new PieData();
                    pieData.addDataSet(pieDS);
                    pieTotal.setData(pieData);
                    pieTotal.animateY(2000);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
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

}
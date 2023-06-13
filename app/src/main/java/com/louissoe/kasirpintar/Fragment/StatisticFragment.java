package com.louissoe.kasirpintar.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.louissoe.kasirpintar.R;

import java.util.ArrayList;

public class StatisticFragment extends Fragment {

    ViewGroup root;
    BarChart barPenjualan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_statistic, null);
        barPenjualan = root.findViewById(R.id.barPenjualan);

        BarDataSet barDS = new BarDataSet(barVal(), "Tingkat Penjualan");
        BarData barData = new BarData();
        barData.addDataSet(barDS);

        barPenjualan.setData(barData);
        barPenjualan.invalidate();

        return root;
    }

    private ArrayList<BarEntry> barVal(){
        ArrayList<BarEntry> dataValue = new ArrayList<BarEntry>();
        dataValue.add(new BarEntry(0,3));
        dataValue.add(new BarEntry(1,7));
        dataValue.add(new BarEntry(2,4));
        dataValue.add(new BarEntry(3,10));
        return dataValue;
    }
}
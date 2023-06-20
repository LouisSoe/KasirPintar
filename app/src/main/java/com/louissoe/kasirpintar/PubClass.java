package com.louissoe.kasirpintar;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PubClass {
    @Nullable
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
    Date tgl = new Date();
    public String getDate(){ return sdf.format(tgl);}
    public String apiUrl(){
        return "https://8a4a-103-189-201-211.ngrok-free.app/";
    }
    public String hargaFormat(int harga){
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("in", "ID"));
        return nf.format(harga);
    }
}

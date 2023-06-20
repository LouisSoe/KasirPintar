package com.louissoe.kasirpintar.Model;

public class Category {
    public Integer id;
    public String nama_category;
    public Integer product_aktif;
    public Integer product_takaktif;

    public Category( String nama_category, Integer product_aktif, Integer product_takaktif) {
        this.nama_category = nama_category;
        this.product_aktif = product_aktif;
        this.product_takaktif = product_takaktif;
    }
}

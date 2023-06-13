package com.louissoe.kasirpintar.Model;

public class Product{
    public String id_barang;
    public String nama_barang;
    public String stok;
    public int id_kategory;
    public String nama_kategory;
    public String status_barang;
    public String barcode;
    public String harga_barang;
    public String foto_barang;

    public Product(String id_barang, String nama_barang, String stok, int id_kategory, String nama_kategory, String status_barang, String barcode, String harga_barang, String foto_barang) {
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.stok = stok;
        this.id_kategory = id_kategory;
        this.nama_kategory = nama_kategory;
        this.status_barang = status_barang;
        this.barcode = barcode;
        this.harga_barang = harga_barang;
        this.foto_barang = foto_barang;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public int getId_kategory() {
        return id_kategory;
    }

    public void setId_kategory(int id_kategory) {
        this.id_kategory = id_kategory;
    }

    public String getNama_kategory() {
        return nama_kategory;
    }

    public void setNama_kategory(String nama_kategory) {
        this.nama_kategory = nama_kategory;
    }

    public String getStatus_barang() {
        return status_barang;
    }

    public void setStatus_barang(String status_barang) {
        this.status_barang = status_barang;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(String harga_barang) {
        this.harga_barang = harga_barang;
    }

    public String getFoto_barang() {
        return foto_barang;
    }

    public void setFoto_barang(String foto_barang) {
        this.foto_barang = foto_barang;
    }
}

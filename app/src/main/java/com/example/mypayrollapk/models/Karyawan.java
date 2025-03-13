package com.example.mypayrollapk.models;

import java.io.Serializable;

public class Karyawan implements Serializable { // Implement Serializable

    private String nik;
    private String nama;
    private String alamat;
    private String jabatan;
    private String tanggalMasuk;

    // Constructor
    public Karyawan() {
        // Kosongkan constructor jika tidak ada parameter
    }

    public Karyawan(String nik, String nama, String alamat, String jabatan, String tanggalMasuk) {
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.jabatan = jabatan;
        this.tanggalMasuk = tanggalMasuk;
    }

    // Getter dan Setter
    public String getNik() {
        return nik;
    }
    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {this.nama = nama;}

//    public String getPassword() {
//        return password;
//    }
//    public void setPassword(String password) {
//        this.password = password;
//    }
    public String getAlamat() {
        return alamat;
    }
    public void setAlamat(String alamat) {this.alamat = alamat;}

    public String getJabatan() {
        return jabatan;
    }
    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(String tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    // Mengoverride metode toString() untuk menampilkan nama karyawan di Spinner
    @Override
    public String toString() {
        return this.nama+ " (" + this.nik + ")";  // Menampilkan nama karyawan
    }
}
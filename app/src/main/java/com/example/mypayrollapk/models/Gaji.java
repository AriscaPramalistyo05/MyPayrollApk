package com.example.mypayrollapk.models;

public class Gaji {
    private String nik;
    private int bulan;
    private int tahun;
    private double gajiPokok;
    private double lembur;
    private double uangMakan;
    private double totalGaji;

    public Gaji(String nik, int bulan, int tahun, double gajiPokok, double lembur, double uangMakan, double totalGaji) {
        this.nik = nik;
        this.bulan = bulan;
        this.tahun = tahun;
        this.gajiPokok = gajiPokok;
        this.lembur = lembur;
        this.uangMakan = uangMakan;
        this.totalGaji = totalGaji;
    }

    // Getter dan Setter
    public String getNik() {
        return nik;
    }

    public int getBulan() {
        return bulan;
    }

    public int getTahun() {
        return tahun;
    }

    public double getGajiPokok() {
        return gajiPokok;
    }

    public double getLembur() {
        return lembur;
    }

    public double getUangMakan() {
        return uangMakan;
    }

    public double getTotalGaji() {
        return totalGaji;
    }
}
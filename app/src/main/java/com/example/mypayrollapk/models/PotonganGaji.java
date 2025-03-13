package com.example.mypayrollapk.models;

public class PotonganGaji {
    private int id;
    private String namaPotongan;
    private double nominal;

    public PotonganGaji(int id, String namaPotongan, double nominal) {
        this.id = id;
        this.namaPotongan = namaPotongan;
        this.nominal = nominal;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public String getNamaPotongan() {
        return namaPotongan;
    }

    public double getNominal() {
        return nominal;
    }
}
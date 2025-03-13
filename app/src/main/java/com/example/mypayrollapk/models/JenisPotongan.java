package com.example.mypayrollapk.models;

public class JenisPotongan {
    private int id;
    private String namaPotongan;
    private double persentasePotongan;

    // Constructor
    public JenisPotongan(int id, String namaPotongan, double persentasePotongan) {
        this.id = id;
        this.namaPotongan = namaPotongan;
        this.persentasePotongan = persentasePotongan;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaPotongan() {
        return namaPotongan;
    }

    public void setNamaPotongan(String namaPotongan) {
        this.namaPotongan = namaPotongan;
    }

    public double getPersentasePotongan() {
        return persentasePotongan;
    }

    public void setPersentasePotongan(double persentasePotongan) {
        this.persentasePotongan = persentasePotongan;
    }

    // Override toString() untuk menampilkan data dalam bentuk String
    @Override
    public String toString() {
        return namaPotongan;  // Jangan tambahkan persentase di sini
    }

}
package com.example.mypayrollapk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypayrollapk.R;
import com.example.mypayrollapk.adapters.PotonganGajiAdapter;
import com.example.mypayrollapk.database.DatabaseHelper;
import com.example.mypayrollapk.models.JenisPotongan;
import com.example.mypayrollapk.models.Karyawan;
import com.example.mypayrollapk.models.PotonganGaji;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class KelolaGajiActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerKaryawan, spinnerJenisPotongan;
    private TextInputEditText etUangMakan, etLembur;
    private TextView tvTotalGaji;
    private RecyclerView listPotonganGaji;
    private MaterialButton btnTambahPotongan, btnSimpan;

    private DatabaseHelper dbHelper;
    private List<Karyawan> karyawanList;
    private List<JenisPotongan> jenisPotonganList;
    private List<PotonganGaji> potonganGajiList;
    private PotonganGajiAdapter potonganGajiAdapter;

    private String selectedNik;
    private double gajiPokok = 0;
    private double totalPotongan = 0;
    private double totalGaji = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_gaji);

        // Inisialisasi view
        spinnerKaryawan = findViewById(R.id.spinner_karyawan);
        etUangMakan = findViewById(R.id.et_uang_makan);
        etLembur = findViewById(R.id.et_lembur);
        spinnerJenisPotongan = findViewById(R.id.spinner_jenis_potongan);
        btnTambahPotongan = findViewById(R.id.btn_tambah_potongan);
        listPotonganGaji = findViewById(R.id.list_potongan_gaji);
        listPotonganGaji.setLayoutManager(new LinearLayoutManager(this)); // Tambahkan ini
        tvTotalGaji = findViewById(R.id.tv_total_gaji);
        btnSimpan = findViewById(R.id.btn_simpan_gaji);

        // Inisialisasi database
        dbHelper = new DatabaseHelper(this);

        // Load data karyawan dan jenis potongan
        loadKaryawanData();
        loadJenisPotonganData();

        // Listener untuk Spinner Karyawan
            spinnerKaryawan.setOnItemClickListener((parent, view, position, id) -> {
                selectedNik = karyawanList.get(position).getNik();
                hitungDanTampilkanData();
            });

        // Listener untuk tombol Tambah Potongan
        btnTambahPotongan.setOnClickListener(v -> {
            tambahPotongan();
        });

        // Listener untuk tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            simpanDataGaji();
        });
    }

    // Method untuk memuat data karyawan ke Spinner
    private void loadKaryawanData() {
        karyawanList = dbHelper.getAllKaryawan();
        ArrayAdapter<Karyawan> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, karyawanList);
        spinnerKaryawan.setAdapter(adapter);
    }

    // Method untuk memuat data jenis potongan ke Spinner
    private void loadJenisPotonganData() {
        jenisPotonganList = dbHelper.getAllJenisPotongan();

        if (jenisPotonganList == null || jenisPotonganList.isEmpty()) {
            Toast.makeText(this, "Tidak ada data jenis potongan", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<JenisPotongan> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jenisPotonganList);
        spinnerJenisPotongan.setAdapter(adapter);
    }


    // Method untuk menambahkan potongan ke daftar
    private void tambahPotongan() {
        String selectedJenisPotonganText = spinnerJenisPotongan.getText().toString().trim();
        if (selectedJenisPotonganText.isEmpty()) {
            Toast.makeText(this, "Pilih jenis potongan terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cari jenis potongan yang cocok berdasarkan nama
        JenisPotongan selectedJenisPotongan = null;
        for (JenisPotongan jenisPotongan : jenisPotonganList) {
            if (jenisPotongan.getNamaPotongan().equalsIgnoreCase(selectedJenisPotonganText)) {
                selectedJenisPotongan = jenisPotongan;
                break;
            }
        }

        if (selectedJenisPotongan == null) {
            Toast.makeText(this, "Jenis potongan tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        double nominalPotongan = gajiPokok * (selectedJenisPotongan.getPersentasePotongan() / 100);
        potonganGajiList.add(new PotonganGaji(0, selectedJenisPotongan.getNamaPotongan(), nominalPotongan));

        potonganGajiAdapter.notifyDataSetChanged();
        hitungTotalGaji();
    }

    // Method untuk menghitung dan menampilkan data
    private void hitungDanTampilkanData() {
        // Ambil gaji pokok berdasarkan jabatan
        String jabatan = dbHelper.getJabatanByNik(selectedNik);
        gajiPokok = dbHelper.getGajiPokokByJabatan(jabatan);

        // Pastikan potonganGajiList tidak null
        if (potonganGajiList == null) {
            potonganGajiList = new ArrayList<>();
        } else {
            potonganGajiList.clear();
        }

        // Inisialisasi Adapter
        potonganGajiAdapter = new PotonganGajiAdapter(this, potonganGajiList, () -> hitungTotalGaji());
        listPotonganGaji.setAdapter(potonganGajiAdapter);

        // Hitung total gaji
        hitungTotalGaji();
    }

    // Method untuk menghitung total gaji
    private void hitungTotalGaji() {
        if (potonganGajiList == null) {
            potonganGajiList = new ArrayList<>(); // Hindari NullPointerException
        }

        double uangMakan = 0;
        double lembur = 0;

        try {
            uangMakan = Double.parseDouble(etUangMakan.getText().toString());
            lembur = Double.parseDouble(etLembur.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Masukkan nilai yang valid untuk uang makan dan lembur", Toast.LENGTH_SHORT).show();
        }

        // Hitung total potongan
        totalPotongan = 0;
        for (PotonganGaji potongan : potonganGajiList) {
            totalPotongan += potongan.getNominal();
        }

        // Hitung total gaji
        totalGaji = gajiPokok + (lembur * 20000) + uangMakan - totalPotongan;
        tvTotalGaji.setText(formatRupiah(totalGaji));
    }

    private String formatRupiah(double amount) {
        // Membuat format dengan pemisah ribuan
        NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp" + format.format(amount);  // Menambahkan "Rp" di depan
    }

    // Method untuk menyimpan data gaji ke database
    private void simpanDataGaji() {
        // Ambil bulan dan tahun saat ini
        Calendar calendar = Calendar.getInstance();
        int bulan = calendar.get(Calendar.MONTH) + 1; // Bulan dimulai dari 0
        int tahun = calendar.get(Calendar.YEAR);

        // Ambil nilai uang makan dan lembur
        double uangMakan = 0;
        double lembur = 0;

        try {
            uangMakan = Double.parseDouble(etUangMakan.getText().toString());
            lembur = Double.parseDouble(etLembur.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Masukkan nilai yang valid untuk uang makan dan lembur", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan data gaji ke database
        boolean isSuccess = dbHelper.insertGaji(selectedNik, bulan, tahun, gajiPokok, lembur, uangMakan, totalGaji);
        if (isSuccess) {
            Toast.makeText(this, "Data gaji berhasil disimpan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Gagal menyimpan data gaji", Toast.LENGTH_SHORT).show();
        }
    }
}
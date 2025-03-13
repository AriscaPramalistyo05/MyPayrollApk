package com.example.mypayrollapk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class IzinActivity extends AppCompatActivity {

    private TextView tvNama,tvNik,tvJenis;
    private EditText etKeterangan;
    private Button btnSubmit;
    private String nik;
    private String nama;
    private String jenis;
    private String status;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin);

        tvJenis = findViewById(R.id.tv_jenis);
        tvNama = findViewById(R.id.tv_nama);
        tvNik = findViewById(R.id.tv_nik);
        etKeterangan = findViewById(R.id.et_keterangan);
        btnSubmit = findViewById(R.id.btn_submit_permohonan);

        // Get data passed from AbsensiUserActivity
        Intent intent = getIntent();
        nik = intent.getStringExtra("nik");
        nama = intent.getStringExtra("nama");
        jenis = intent.getStringExtra("jenis");

        // Set status sesuai jenis permohonan
        tvNik.setText("NIK: " + nik);
        tvNama.setText("Nama: " + nama);
        if (jenis != null) {
            status = "Pending";
            if (jenis.equalsIgnoreCase("Sakit")) {
                tvJenis.setText("Jenis: Sakit");
            } else if (jenis.equalsIgnoreCase("Izin")) {
                tvJenis.setText("Jenis: Izin");
            } else if (jenis.equalsIgnoreCase("Cuti")) {
                tvJenis.setText("Jenis: Cuti");
            }
        }

        // Open Database
        db = openOrCreateDatabase("AbsensiDB", MODE_PRIVATE, null);

        // Button untuk submit permohonan
        btnSubmit.setOnClickListener(v -> {
            String keterangan = etKeterangan.getText().toString();
            savePermohonan(keterangan);
        });
    }

    // Fungsi untuk menyimpan permohonan izin ke database
    private void savePermohonan(String keterangan) {
        ContentValues values = new ContentValues();
        values.put("nik", nik);
        values.put("nama", nama);
        values.put("tanggal", getCurrentDate());
        values.put("jenis", jenis);
        values.put("status", status);
        values.put("keterangan", keterangan);

        long result = db.insert("permohonan", null, values);

        if (result != -1) {
            Toast.makeText(this, "Permohonan berhasil diajukan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal mengajukan permohonan", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi untuk mengambil tanggal saat ini
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());  // Memastikan menggunakan konstruktor yang benar
    }
}

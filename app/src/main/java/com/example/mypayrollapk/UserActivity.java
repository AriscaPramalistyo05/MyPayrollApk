package com.example.mypayrollapk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypayrollapk.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private TextView txtNama, txtNik, txtSaldo;
    private MaterialButton logout;
    private RecyclerView recyclerViewRiwayatAbsensi;
    private DatabaseHelper databaseHelper;
    private RiwayatAbsensiAdapter riwayatAbsensiAdapter;
    private List<RiwayatAbsensi> riwayatAbsensiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inisialisasi elemen UI
        txtNama = findViewById(R.id.txtNama);
        txtNik = findViewById(R.id.txtNik);
        txtSaldo = findViewById(R.id.txtSaldo);
        logout = findViewById(R.id.logout);
        recyclerViewRiwayatAbsensi = findViewById(R.id.recyclerViewRiwayatAbsensi);

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Ambil data dari Intent
        String nama = getIntent().getStringExtra("nama");
        String nik = getIntent().getStringExtra("nik");

        // Set data ke TextView
        txtNama.setText("Hi, " + nama);
        txtNik.setText("NIK: " + nik);

        // Tampilkan riwayat absensi
        tampilkanRiwayatAbsensi(nik);

        // Tampilkan gaji yang diterima
        tampilkanGaji(nik);

        // Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    // Method untuk menampilkan riwayat absensi
    private void tampilkanRiwayatAbsensi(String nik) {
        riwayatAbsensiList = new ArrayList<>();

        // Ambil data dari database
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT tanggal, shift, jam_masuk, jam_pulang FROM absensi WHERE nik = ? ORDER BY tanggal DESC",
                new String[]{nik}
        );

        if (cursor.moveToFirst()) {
            do {
                String tanggal = cursor.getString(0);
                String shift = cursor.getString(1);
                String jamMasuk = cursor.getString(2);
                String jamPulang = cursor.getString(3);

                // Tambahkan ke list
                riwayatAbsensiList.add(new RiwayatAbsensi(tanggal, shift, jamMasuk, jamPulang));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Set adapter untuk RecyclerView
        riwayatAbsensiAdapter = new RiwayatAbsensiAdapter(riwayatAbsensiList);
        recyclerViewRiwayatAbsensi.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRiwayatAbsensi.setAdapter(riwayatAbsensiAdapter);
    }

    // Method untuk menampilkan gaji yang diterima
    private void tampilkanGaji(String nik) {
        // Ambil data gaji dari database
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT total_gaji FROM gaji WHERE nik = ? ORDER BY tahun DESC, bulan DESC LIMIT 1",
                new String[]{nik}
        );

        if (cursor.moveToFirst()) {
            double totalGaji = cursor.getDouble(0);
            txtSaldo.setText("Rp. " + String.format("%,.2f", totalGaji)); // Format dengan 2 desimal
        } else {
            txtSaldo.setText("Rp. 0"); // Jika tidak ada data gaji
        }
        cursor.close();
    }
}
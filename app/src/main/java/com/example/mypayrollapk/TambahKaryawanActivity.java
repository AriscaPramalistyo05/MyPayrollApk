package com.example.mypayrollapk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypayrollapk.database.DatabaseHelper;

import java.util.regex.Pattern;

public class TambahKaryawanActivity extends AppCompatActivity {

    private EditText etNik, etNama, etAlamat, etJabatan,etPassword;
    private TextView tvTanggalMasuk;
    private Button btnSimpan, btnPilihTanggal;
    private DatabaseHelper databaseHelper;
    private String karyawanId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_karyawan);

        // Inisialisasi View
        etNik = findViewById(R.id.et_nik);
        etNama = findViewById(R.id.et_nama);
        etPassword = findViewById(R.id.et_password);
        etAlamat = findViewById(R.id.et_alamat);
        etJabatan = findViewById(R.id.et_jabatan);
        tvTanggalMasuk = findViewById(R.id.tv_tanggal_masuk);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnPilihTanggal = findViewById(R.id.btn_pilih_tanggal);

        databaseHelper = new DatabaseHelper(this);

        // Ambil data karyawanId dari Intent jika sedang mengedit
        karyawanId = getIntent().getStringExtra("karyawan_id");
        if (karyawanId != null) {
            loadKaryawanData(karyawanId);
        }

        // Pilih tanggal masuk
        btnPilihTanggal.setOnClickListener(v -> showDatePicker());

        // Simpan data karyawan
        btnSimpan.setOnClickListener(v -> saveKaryawan());
    }

    private void showDatePicker() {
        // Buat instance DatePickerFragment dan tambahkan listener
        DatePickerFragment datePicker = new DatePickerFragment((view, year, month, day) -> {
            String tanggal = year + "-" + (month + 1) + "-" + day; // Format tanggal (YYYY-MM-DD)
            tvTanggalMasuk.setText(tanggal); // Set ke TextView
        });
        // Tampilkan DatePickerFragment
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void saveKaryawan() {
        // Ambil inputan dari form
        String nik = etNik.getText().toString().trim();
        String nama = etNama.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();
        String jabatan = etJabatan.getText().toString().trim();
        String tanggalMasuk = tvTanggalMasuk.getText().toString().trim();

        // Validasi input
        if (nik.isEmpty() || nama.isEmpty() || alamat.isEmpty() || jabatan.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi panjang NIK (7 digit) menggunakan regex
        if (!Pattern.matches("\\d{7}", nik)) {
            Toast.makeText(this, "NIK harus berupa angka 7 digit!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Jika tanggal masuk tidak dipilih, biarkan database menggunakan default
        if (tanggalMasuk.isEmpty()) {
            tanggalMasuk = null; // Database akan mengatur default tanggal
        }

        // Langkah 1: Cek apakah NIK sudah ada di tabel users
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE nik = ?", new String[]{nik});
        if (cursor.getCount() > 0) {
            // NIK sudah ada
            Toast.makeText(this, "NIK sudah terdaftar!", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        // Langkah 2: Menambahkan data ke tabel karyawan
        ContentValues values = new ContentValues();
        values.put("nik", nik);
        values.put("nama", nama);
        values.put("password", password);
        values.put("alamat", alamat);
        values.put("jabatan", jabatan);
//        values.put("lokasi",lokasi);
        if (tanggalMasuk != null) {
            values.put("tanggal_masuk", tanggalMasuk);
        }
        values.put("role", "user"); // Menyimpan role default sebagai 'user'

        // Simpan data ke tabel karyawan
        if (karyawanId == null) {
            // Insert data baru
            long id = databaseHelper.getWritableDatabase().insert("karyawan", null, values);
            if (id > 0) {
                // Tambahkan juga ke tabel users
                ContentValues userValues = new ContentValues();
                userValues.put("nik", nik);
                userValues.put("nama", nama);
                userValues.put("password", password);
                userValues.put("role", "user");

                long userId = databaseHelper.getWritableDatabase().insert("users", null, userValues);
                if (userId > 0) {
                    Toast.makeText(this, "Karyawan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Gagal menambahkan karyawan ke tabel users!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Gagal menambahkan karyawan! Pastikan NIK unik.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update data karyawan
            int rows = databaseHelper.getWritableDatabase().update("karyawan", values, "nik=?", new String[]{karyawanId});
            if (rows > 0) {
                // Update juga tabel users
                ContentValues userValues = new ContentValues();
                userValues.put("nik", nik);
                userValues.put("nama", nama);

                int userRows = databaseHelper.getWritableDatabase().update("users", userValues, "nik=?", new String[]{karyawanId});
                if (userRows > 0) {
                    Toast.makeText(this, "Data karyawan berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Gagal memperbarui data di tabel users!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Gagal memperbarui karyawan!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadKaryawanData(String id) {
        // Ambil data dari database berdasarkan NIK
        String query = "SELECT * FROM karyawan WHERE nik=?";
        try (Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(query, new String[]{id})) {
            if (cursor.moveToFirst()) {
                // Pastikan kolom tersedia sebelum mengambil data
                int nikIndex = cursor.getColumnIndex("nik");
                int namaIndex = cursor.getColumnIndex("nama");
                int passwordIndex = cursor.getColumnIndex("password");
                int alamatIndex = cursor.getColumnIndex("alamat");
//                int lokasiIndex = cursor.getColumnIndex("lokasi");
                int jabatanIndex = cursor.getColumnIndex("jabatan");
                int tanggalMasukIndex = cursor.getColumnIndex("tanggal_masuk");

                // Set nilai hanya jika kolom ditemukan
                if (nikIndex >= 0) etNik.setText(cursor.getString(nikIndex));
                if (namaIndex >= 0) etNama.setText(cursor.getString(namaIndex));
                if (passwordIndex >= 0) etPassword.setText(cursor.getString(passwordIndex));
                if (alamatIndex >= 0) etAlamat.setText(cursor.getString(alamatIndex));
//                if (lokasiIndex >= 0) etLokasi.setText(cursor.getString(lokasiIndex));
                if (jabatanIndex >= 0) etJabatan.setText(cursor.getString(jabatanIndex));
                if (tanggalMasukIndex >= 0) tvTanggalMasuk.setText(cursor.getString(tanggalMasukIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memuat data karyawan!", Toast.LENGTH_SHORT).show();
        }
    }

}

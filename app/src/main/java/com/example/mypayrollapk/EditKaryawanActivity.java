package com.example.mypayrollapk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypayrollapk.database.DatabaseHelper;
import com.example.mypayrollapk.models.Karyawan;

public class EditKaryawanActivity extends AppCompatActivity {

    private EditText etNik, etNama, etAlamat, etJabatan;
    private Button btnUpdate;
    private DatabaseHelper db;
    private Karyawan karyawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_karyawan);

        etNik = findViewById(R.id.et_nik);
        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etJabatan = findViewById(R.id.et_jabatan);
        btnUpdate = findViewById(R.id.btn_update);

        db = new DatabaseHelper(this);
        karyawan = (Karyawan) getIntent().getSerializableExtra("karyawan");

        if (karyawan != null) {
            etNik.setText(karyawan.getNik());
            etNama.setText(karyawan.getNama());
            etAlamat.setText(karyawan.getAlamat());
            etJabatan.setText(karyawan.getJabatan());
        }

        btnUpdate.setOnClickListener(view -> {
            String nik = etNik.getText().toString();
            String nama = etNama.getText().toString();
            String alamat = etAlamat.getText().toString();
            String jabatan = etJabatan.getText().toString();

            if (nik.isEmpty() || nama.isEmpty() || alamat.isEmpty() || jabatan.isEmpty()) {
                Toast.makeText(this, "Data harus diisi semua", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.updateKaryawan(nik, nama, alamat, jabatan)) {
                // Update data di tabel 'users'
                if (db.updateUser(nik, nama)) {
                    Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                    // Setelah data diperbarui, perbarui tampilan RecyclerView
                    setResult(RESULT_OK);
                    finish(); // Kembali ke aktivitas utama setelah update
                } else {
                    Toast.makeText(this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to update employee data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

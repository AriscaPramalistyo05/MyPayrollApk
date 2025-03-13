package com.example.mypayrollapk;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private TextView txtAdmin;
    private ImageView logout;
    private Button btnKelolaKaryawan, btnKelolaGajiKaryawan, btnKelolaAbsensiKaryawan, btnKelolaTunjangan, btnKelolaLaporan, btnTambahAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inisialisasi elemen UI
        txtAdmin = findViewById(R.id.txt_admin);
        logout = findViewById(R.id.logout);
        RelativeLayout btnKelolaKaryawan = findViewById(R.id.btn_kelola_karyawan);
        RelativeLayout btnKelolaGajiKaryawan = findViewById(R.id.btn_kelola_gaji_karyawan);
        RelativeLayout btnKelolaAbsensiKaryawan = findViewById(R.id.btn_kelola_absensi_karyawan);
//        RelativeLayout btnKelolaTunjangan = findViewById(R.id.btn_kelola_tunjangan);
//        RelativeLayout btnKelolaLaporan = findViewById(R.id.btn_kelola_laporan);
//        RelativeLayout btnTambahAdmin = findViewById(R.id.btn_tambah_admin);

        // Ambil data nama admin dari Intent
        String namaAdmin = getIntent().getStringExtra("nama");
        txtAdmin.setText(namaAdmin);

        // Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Navigasi ke KelolaDataActivity
        btnKelolaKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, KelolaDataActivity.class);
                startActivity(intent);
            }
        });

        // Navigasi ke KelolaGajiActivity
        btnKelolaGajiKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, KelolaGajiActivity.class);
                startActivity(intent);
            }
        });

        // Navigasi ke KelolaAbsensiActivity
        btnKelolaAbsensiKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, KelolaAbsensiActivity.class);
                startActivity(intent);
            }
        });

//        // Navigasi ke KelolaTunjanganActivity
//        btnKelolaTunjangan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AdminActivity.this, KelolaTunjanganActivity.class);
//                startActivity(intent);
//            }
//        });


//        // Navigasi ke KelolaAdminActivity
//        btnTambahAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AdminActivity.this, KelolaAdminActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}

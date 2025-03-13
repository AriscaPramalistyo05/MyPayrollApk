package com.example.mypayrollapk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypayrollapk.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private EditText editNik, editPassword;
    private Switch activeSwitch;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Inisialisasi elemen UI */
        editNik = findViewById(R.id.editNik);
        editPassword = findViewById(R.id.editPassword);
        activeSwitch = findViewById(R.id.activeSwitch);
        btnLogin = findViewById(R.id.btn_login);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Event klik tombol login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nik = editNik.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (nik.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "NIK dan Password harus diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Periksa ke database
                Cursor cursor = dbHelper.login(nik, password);
                if (cursor != null && cursor.moveToFirst()) {
                    String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                    String nama = cursor.getString(cursor.getColumnIndexOrThrow("nama"));

                    // Simpan data pengguna untuk digunakan di halaman berikutnya
                    Intent intent;
                    if (role.equals("admin")) {
                        intent = new Intent(MainActivity.this, AdminActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, UserActivity.class);
                    }
                    intent.putExtra("nik", nik);
                    intent.putExtra("nama", nama);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Login gagal! Periksa NIK atau Password.", Toast.LENGTH_SHORT).show();
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }


}
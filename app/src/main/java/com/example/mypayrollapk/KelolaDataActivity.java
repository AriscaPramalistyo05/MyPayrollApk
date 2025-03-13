package com.example.mypayrollapk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import java.text.ParseException;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypayrollapk.adapters.KaryawanAdapter;
import com.example.mypayrollapk.database.DatabaseHelper;
import com.example.mypayrollapk.models.Karyawan;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class KelolaDataActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KaryawanAdapter adapter;
    private ArrayList<Karyawan> karyawanList;
    private DatabaseHelper db;
    private ImageButton btnTambahData;
    private Button btnImportKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_data);

        recyclerView = findViewById(R.id.list_karyawan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnTambahData = findViewById(R.id.btn_tambah_data);
        btnImportKaryawan = findViewById(R.id.btn_import_karyawan);

        db = new DatabaseHelper(this);
        karyawanList = new ArrayList<>();

        loadData();

        btnTambahData.setOnClickListener(view -> {
            Intent intent = new Intent(KelolaDataActivity.this, TambahKaryawanActivity.class);
            startActivity(intent);
        });

        btnImportKaryawan.setOnClickListener(view -> openFileChooser());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            importExcelData(fileUri);  // Proses impor data dari file Excel
            loadData();  // Muat ulang data setelah data diperbarui (akan memperbarui tampilan)
        }
    }





    private void importExcelData(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);  // Ambil sheet pertama
            SQLiteDatabase database = db.getWritableDatabase();
            database.beginTransaction();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                try {
                    String nik = row.getCell(0).getCellType() == CellType.NUMERIC ?
                            String.valueOf((long) row.getCell(0).getNumericCellValue()) :
                            row.getCell(0).getStringCellValue().trim();

                    if (!isValidNIK(nik)) continue;

                    Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM karyawan WHERE nik = ?", new String[]{nik});
                    cursor.moveToFirst();
                    int count = cursor.getInt(0);
                    cursor.close();

                    if (count > 0) continue;

                    String nama = row.getCell(1).getStringCellValue().trim();
                    String alamat = row.getCell(2).getStringCellValue().trim();
                    String jabatan = row.getCell(3).getStringCellValue().trim();
                    String tanggalMasuk = row.getCell(4).getCellType() == CellType.NUMERIC ?
                            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(row.getCell(4).getDateCellValue()) :
                            row.getCell(4).getStringCellValue().trim();

                    if (!isValidDate(tanggalMasuk)) continue;

                    ContentValues karyawanValues = new ContentValues();
                    karyawanValues.put("nik", nik);
                    karyawanValues.put("nama", nama);
                    karyawanValues.put("alamat", alamat);
                    karyawanValues.put("jabatan", jabatan);
                    karyawanValues.put("tanggal_masuk", tanggalMasuk);

                    database.insert("karyawan", null, karyawanValues);

                    String password = "123456";
                    ContentValues userValues = new ContentValues();
                    userValues.put("nik", nik);
                    userValues.put("nama", nama);
                    userValues.put("password", password);
                    userValues.put("role", "user");

                    database.insert("users", null, userValues);
                } catch (Exception e) {
                    Log.e("Import Excel", "Error on row " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            database.setTransactionSuccessful();
            database.endTransaction();
            workbook.close();
            Toast.makeText(this, "Data imported successfully", Toast.LENGTH_SHORT).show();
            loadData();

        } catch (Exception e) {
            Toast.makeText(this, "Failed to import data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadData() {
        karyawanList = db.getAllKaryawan();
        if (karyawanList != null && !karyawanList.isEmpty()) {
            adapter = new KaryawanAdapter(karyawanList, this,
                    new ItemClickListener() {
                        @Override
                        public void onItemClick(Karyawan karyawan, int position) {
                            Intent intent = new Intent(KelolaDataActivity.this, EditKaryawanActivity.class);
                            intent.putExtra("karyawan", karyawan);
                            startActivity(intent);
                        }
                    },
                    new ItemClickListener() {
                        @Override
                        public void onItemClick(Karyawan karyawan, int position) {
                            deleteKaryawan(karyawan, position);
                        }
                    }
            );
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No employee data available", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteKaryawan(Karyawan karyawan, int position) {
        if (db.hapusKaryawan(karyawan.getNik()) && db.hapusUser(karyawan.getNik())) {
            karyawanList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(this, "Karyawan deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete Karyawan", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidNIK(String nik) {
        return nik != null && nik.length() == 7;
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
package com.example.mypayrollapk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mypayrollapk.models.Karyawan;

public class DBHelper extends SQLiteOpenHelper {

    // Nama dan versi database
    private static final String DATABASE_NAME = "payroll.db";
    private static final int DATABASE_VERSION = 1;

    // Nama tabel dan kolom
    private static final String TABLE_KARYAWAN = "karyawan";
    private static final String COL_NIK = "nik";
    private static final String COL_NAMA = "nama";
    private static final String COL_ALAMAT = "alamat";
    private static final String COL_JABATAN = "jabatan";
    private static final String COL_TANGGAL_MASUK = "tanggal_masuk";

    // Konstruktor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel karyawan
        String CREATE_TABLE_KARYAWAN = "CREATE TABLE " + TABLE_KARYAWAN + " (" +
                COL_NIK + " TEXT PRIMARY KEY, " +
                COL_NAMA + " TEXT, " +
                COL_ALAMAT + " TEXT, " +
                COL_JABATAN + " TEXT, " +
                COL_TANGGAL_MASUK + " TEXT)";
        db.execSQL(CREATE_TABLE_KARYAWAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Menghapus tabel jika ada perubahan versi database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KARYAWAN);
        onCreate(db);
    }

    // Menambahkan data karyawan ke database
    public void addKaryawan(Karyawan karyawan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NIK, karyawan.getNik());
        values.put(COL_NAMA, karyawan.getNama());
        values.put(COL_ALAMAT, karyawan.getAlamat());
        values.put(COL_JABATAN, karyawan.getJabatan());
        values.put(COL_TANGGAL_MASUK, karyawan.getTanggalMasuk());

        // Menyimpan data karyawan
        db.insert(TABLE_KARYAWAN, null, values);
        db.close();
    }

    // Menghapus karyawan berdasarkan NIK
    public boolean deleteKaryawan(String nik) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_KARYAWAN, COL_NIK + " = ?", new String[]{nik});
        db.close();
        return result > 0;  // Mengembalikan true jika penghapusan berhasil
    }
}

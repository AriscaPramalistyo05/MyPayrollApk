package com.example.mypayrollapk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mypayrollapk.models.JenisPotongan;
import com.example.mypayrollapk.models.Karyawan;
import com.example.mypayrollapk.models.PotonganGaji;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pegawai.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        insertDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < 11) {
//            db.execSQL("DROP TABLE IF EXISTS jenis_potongan");
//            db.execSQL("DROP TABLE IF EXISTS potongan_gaji");
//            createTables(db); // Re-create tables
//
//            insertDefaultData(db);
//        }
//    }

    // Membuat tabel database
    private void createTables(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        // Membuat tabel lainnya
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, nik INTEGER UNIQUE CHECK(LENGTH(nik) = 7), nama TEXT, password TEXT, role TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS karyawan (id INTEGER PRIMARY KEY AUTOINCREMENT, nik INTEGER UNIQUE CHECK(LENGTH(nik) = 7), nama TEXT, alamat TEXT, jabatan TEXT, tanggal_masuk DATE DEFAULT CURRENT_DATE, role TEXT DEFAULT 'user', FOREIGN KEY(nik) REFERENCES users(nik) ON DELETE CASCADE)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS absensi (id INTEGER PRIMARY KEY AUTOINCREMENT, nik INTEGER CHECK(LENGTH(nik) = 7), nama TEXT, shift TEXT, tanggal TEXT, jam_masuk TEXT, jam_pulang TEXT, keterangan TEXT, FOREIGN KEY(nik) REFERENCES karyawan(nik) ON DELETE CASCADE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS gaji (id INTEGER PRIMARY KEY AUTOINCREMENT, nik INTEGER CHECK(LENGTH(nik) = 7), bulan INTEGER, tahun INTEGER, gaji_pokok REAL, lembur REAL, uang_makan REAL,  total_gaji REAL, FOREIGN KEY(nik) REFERENCES karyawan(nik) ON DELETE CASCADE)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS laporan (id INTEGER PRIMARY KEY AUTOINCREMENT, nik INTEGER CHECK(LENGTH(nik) = 7), nama TEXT, aduan TEXT, tanggal TEXT, FOREIGN KEY(nik) REFERENCES karyawan(nik) ON DELETE CASCADE)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS admin (id INTEGER PRIMARY KEY AUTOINCREMENT, nik INTEGER UNIQUE CHECK(LENGTH(nik) = 7), nama TEXT, alamat TEXT, password TEXT, jabatan TEXT, role TEXT DEFAULT 'admin')");

        // Tambahkan tabel jenis potongan
        db.execSQL("CREATE TABLE IF NOT EXISTS jenis_potongan (id INTEGER PRIMARY KEY AUTOINCREMENT, nama_potongan TEXT NOT NULL, persentase_potongan REAL NOT NULL)");

        // Tambahkan tabel potongan_gaji
        db.execSQL("CREATE TABLE IF NOT EXISTS potongan_gaji (id INTEGER PRIMARY KEY AUTOINCREMENT, nik TEXT NOT NULL, id_jenis_potongan INTEGER NOT NULL, nominal REAL NOT NULL, FOREIGN KEY (nik) REFERENCES karyawan(nik) ON DELETE CASCADE, FOREIGN KEY (id_jenis_potongan) REFERENCES jenis_potongan(id) ON DELETE CASCADE)");

        // Tambahkan tabel gaji_jabatan untuk gaji pokok berdasarkan jabatan
        db.execSQL("CREATE TABLE IF NOT EXISTS gaji_jabatan (jabatan TEXT PRIMARY KEY, gaji_pokok REAL)");
    }

    // Memasukkan data default
    private void insertDefaultData(SQLiteDatabase db) {
        db.execSQL("INSERT OR IGNORE INTO users (nik, nama, password, role) VALUES (1000001, 'Admin', 'punyaadmin', 'admin')");
        db.execSQL("INSERT OR IGNORE INTO users (nik, nama, password, role) VALUES (2510001, 'User', '123456', 'user')");

        // Insert some default jenis potongan
        db.execSQL("INSERT OR IGNORE INTO jenis_potongan (nama_potongan, persentase_potongan) VALUES ('BPJS', 2.5)");
        db.execSQL("INSERT OR IGNORE INTO jenis_potongan (nama_potongan, persentase_potongan) VALUES ('Pajak Penghasilan', 5.0)");
        db.execSQL("INSERT OR IGNORE INTO jenis_potongan (nama_potongan, persentase_potongan) VALUES ('Denda', 1.0)");
        db.execSQL("INSERT OR IGNORE INTO jenis_potongan (nama_potongan, persentase_potongan) VALUES ('Potongan PPh 21', 3.0)");
    }

//    public void clearUsers() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM users WHERE nik NOT IN (1000001, 2510001)"); // Hapus semua kecuali Admin dan User default
//    }
//    public void clearKaryawan(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM karyawan WHERE nik ");
//    }
//
//    public void clearAbsensi(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM absensi WHERE nik ");
//    }

    /* ============================
       FUNGSI MANIPULASI KARYAWAN
       ============================ */
    public boolean insertKaryawan(String nik, String nama, String alamat, String jabatan) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String defaultPassword = "123456"; // Password default

            ContentValues karyawanValues = new ContentValues();
            karyawanValues.put("nik", nik);
            karyawanValues.put("nama", nama);
            karyawanValues.put("alamat", alamat);
            karyawanValues.put("jabatan", jabatan);

            long karyawanResult = db.insert("karyawan", null, karyawanValues);
            if (karyawanResult == -1) return false;

            ContentValues userValues = new ContentValues();
            userValues.put("nik", nik);
            userValues.put("nama", nama);
            userValues.put("password", defaultPassword);
            userValues.put("role", "user");

            long userResult = db.insert("users", null, userValues);
            if (userResult == -1) return false;

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Karyawan> getAllKaryawan() {
        ArrayList<Karyawan> karyawanList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nik, nama, alamat, jabatan, tanggal_masuk FROM karyawan", null);

        if (cursor.moveToFirst()) {
            do {
                Karyawan karyawan = new Karyawan(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                karyawanList.add(karyawan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return karyawanList;
    }

    // Ambil jabatan karyawan berdasarkan NIK
    public String getJabatanByNik(String nik) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT jabatan FROM karyawan WHERE nik = ?", new String[]{nik});
        String jabatan = "";
        if (cursor.moveToFirst()) {
            jabatan = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return jabatan;
    }

    public boolean updateKaryawan(String nik, String nama, String alamat, String jabatan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("nama", nama);
        contentValues.put("alamat", alamat);
        contentValues.put("jabatan", jabatan);

        int result = db.update("karyawan", contentValues, "nik = ?", new String[]{nik});
        return result > 0; // Jika berhasil update
    }


    public boolean hapusKaryawan(String nik) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("karyawan", "nik = ?", new String[]{nik});
        return result > 0; // Jika berhasil dihapus
    }

    public boolean hapusUser(String nik) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("users", "nik = ?", new String[]{nik});
        return result > 0; // Jika berhasil dihapus
    }

    /* ============================
       FUNGSI MANIPULASI ABSENSI
       ============================ */

    // Hitung jumlah hari hadir berdasarkan NIK
//    public int getJumlahHariHadir(String nik) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(
//                "SELECT COUNT(*) FROM absensi WHERE nik = ? AND keterangan = 'Hadir'",
//                new String[]{nik}
//        );
//
//        int jumlahHariHadir = 0;
//        if (cursor.moveToFirst()) {
//            jumlahHariHadir = cursor.getInt(0);
//        }
//        cursor.close();
//        db.close();
//        return jumlahHariHadir;
//    }

    /* ============================
       FUNGSI AUTENTIKASI PENGGUNA
       ============================ */
    public Cursor login(String nik, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE nik = ? AND password = ?", new String[]{nik, password});
    }

    public boolean updateUser(String nik, String nama) {
        ContentValues values = new ContentValues();
        values.put("nama", nama);
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.update("users", values, "nik = ?", new String[]{nik});
        return rowsAffected > 0;
    }

       /* ============================
       FUNGSI MANIPULASI JENIS POTONGAN
       ============================ */

    // Ambil semua jenis potongan
    public List<JenisPotongan> getAllJenisPotongan() {
        List<JenisPotongan> jenisPotonganList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM jenis_potongan", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String namaPotongan = cursor.getString(cursor.getColumnIndexOrThrow("nama_potongan"));
                double persentase = cursor.getDouble(cursor.getColumnIndexOrThrow("persentase_potongan"));
                jenisPotonganList.add(new JenisPotongan(id, namaPotongan, persentase));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return jenisPotonganList;
    }

    /* ============================
       FUNGSI MANIPULASI POTONGAN GAJI
       ============================ */

    // Tambahkan potongan gaji
    public boolean insertPotonganGaji(String nik, int idJenisPotongan, double nominal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nik", nik);
        values.put("id_jenis_potongan", idJenisPotongan);
        values.put("nominal", nominal);

        long result = db.insert("potongan_gaji", null, values);
        db.close();
        return result != -1;
    }

    // Ambil gaji pokok berdasarkan jabatan
    public double getGajiPokokByJabatan(String jabatan) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT gaji_pokok FROM gaji_jabatan WHERE jabatan = ?", new String[]{jabatan});
        double gajiPokok = 0;
        if (cursor.moveToFirst()) {
            gajiPokok = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return gajiPokok;
    }


    // Ambil semua potongan gaji berdasarkan NIK
    public List<PotonganGaji> getPotonganGajiByNik(String nik) {
        List<PotonganGaji> potonganGajiList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT pg.id, jp.nama_potongan, pg.nominal FROM potongan_gaji pg " +
                        "JOIN jenis_potongan jp ON pg.id_jenis_potongan = jp.id WHERE pg.nik = ?",
                new String[]{nik}
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String namaPotongan = cursor.getString(cursor.getColumnIndexOrThrow("nama_potongan"));
                double nominal = cursor.getDouble(cursor.getColumnIndexOrThrow("nominal"));
                potonganGajiList.add(new PotonganGaji(id, namaPotongan, nominal));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return potonganGajiList;
    }

    // Hapus potongan gaji berdasarkan ID
    public boolean deletePotonganGaji(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("potongan_gaji", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }


    /* ============================
       FUNGSI MANIPULASI GAJI
       ============================ */

    // Simpan data gaji
    public boolean insertGaji(String nik, int bulan, int tahun, double gajiPokok, double lembur, double uangMakan, double totalGaji) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nik", nik);
        values.put("bulan", bulan);
        values.put("tahun", tahun);
        values.put("gaji_pokok", gajiPokok);
        values.put("lembur", lembur);
        values.put("uang_makan", uangMakan);
        values.put("total_gaji", totalGaji);

        long result = db.insert("gaji", null, values);
        db.close();
        return result != -1;
    }

    // Ambil data gaji berdasarkan NIK, bulan, dan tahun
    public Cursor getGajiByNik(String nik, int bulan, int tahun) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM gaji WHERE nik = ? AND bulan = ? AND tahun = ?",
                new String[]{nik, String.valueOf(bulan), String.valueOf(tahun)}
        );
    }

}


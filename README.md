# Projek Aplikasi Penggajian Karyawan dengan Android Studio & SQLite

Selamat datang di proyek **Aplikasi Penggajian Karyawan**! Aplikasi ini dirancang untuk memudahkan pengelolaan data karyawan, perhitungan gaji, dan pengelolaan kehadiran dengan menggunakan Android Studio dan SQLite. 

## Deskripsi Proyek

Aplikasi ini memungkinkan pengguna untuk:
- Menginput data karyawan secara manual atau melalui impor dari file Excel.
- Mengelola kehadiran karyawan dengan sistem otomatis yang memudahkan proses input.
- Menghitung gaji karyawan secara otomatis berdasarkan kehadiran dan tunjangan kinerja.

## Fitur Utama

- **Input Data Karyawan**: 
  - Menyediakan fitur untuk memasukkan data karyawan secara manual.
  - Dukungan impor data dari file Excel dengan validasi integritas data.

- **Sistem Kehadiran**: 
  - Mengimpor kehadiran karyawan dari file Excel ke database SQL dengan automasi format yang mudah.

- **Perhitungan Gaji Otomatis**: 
  - Algoritma perhitungan gaji yang otomatis berdasarkan data kehadiran dan tunjangan kinerja.

## Teknologi yang Digunakan

- **Android Studio**: IDE untuk pengembangan aplikasi Android.
- **SQLite**: Database ringan untuk menyimpan data lokal.
- **Java**: Bahasa pemrograman untuk pengembangan aplikasi Android.

## Struktur Proyek

Berikut adalah struktur direktori proyek:

```
MyPayrollApk/
│
├── app/                          # Folder aplikasi
│   ├── src/                      # Sumber kode aplikasi
│   │   ├── main/                 # Kode utama
│   │   │   ├── java/             # Kode Java/Kotlin
│   │   │   ├── res/              # Resource (layout, drawable, etc.)
│   │   └── test/                 # Unit tests
│   │
│   ├── build.gradle               # File build Gradle untuk modul aplikasi
│   └── AndroidManifest.xml        # File manifest aplikasi
│
├── README.md                     # Dokumentasi proyek (file ini)
└── .gitignore                    # File untuk mengabaikan file tertentu di Git
```

## Cara Menjalankan Proyek

1. Clone repositori ini:
   ```bash
   git clone https://github.com/AriscaPramalistyo05/MyPayrollApk.git
   ```
2. Buka proyek di Android Studio.
3. Pastikan semua dependensi telah diunduh.
4. Jalankan aplikasi di emulator atau perangkat Android Anda.

## Kontribusi

Kami sangat menghargai kontribusi dari komunitas! Jika Anda ingin berkontribusi, silakan ikuti langkah-langkah berikut:

1. Fork repositori ini.
2. Buat branch baru:
   ```bash
   git checkout -b fitur-baru
   ```
3. Lakukan perubahan Anda dan commit:
   ```bash
   git commit -m "Menambahkan fitur baru"
   ```
4. Push branch Anda ke GitHub:
   ```bash
   git push origin fitur-baru
   ```
5. Ajukan Pull Request ke branch `main`.

## Kontak

Jika Anda memiliki pertanyaan atau saran terkait proyek ini, jangan ragu untuk menghubungi kami melalui email: **ariscapramalistyo05@gmail.com**

---

Terima kasih telah menggunakan Aplikasi Penggajian Karyawan! Kami berharap aplikasi ini dapat membantu dalam pengelolaan data karyawan dan perhitungan gaji secara efisien. 😊

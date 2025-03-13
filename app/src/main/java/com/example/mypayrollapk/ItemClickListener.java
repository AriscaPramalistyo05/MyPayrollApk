package com.example.mypayrollapk;

import com.example.mypayrollapk.models.Karyawan;

// Jadikan ItemClickListener sebagai interface
public interface ItemClickListener {
    void onItemClick(Karyawan karyawan, int position); // Method untuk menangani klik
}

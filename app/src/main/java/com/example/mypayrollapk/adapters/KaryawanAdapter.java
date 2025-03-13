package com.example.mypayrollapk.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypayrollapk.EditKaryawanActivity;
import com.example.mypayrollapk.KelolaDataActivity;
import com.example.mypayrollapk.R;
import com.example.mypayrollapk.database.DatabaseHelper;
import com.example.mypayrollapk.models.Karyawan;
import com.example.mypayrollapk.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.KaryawanViewHolder> {

    private List<Karyawan> karyawanList;
    private Context context;
    private DatabaseHelper dbHelper;
    private ItemClickListener clickListener;
    private ItemClickListener deleteListener;

    // Konstruktor yang menerima listener
    public KaryawanAdapter(ArrayList<Karyawan> karyawanList, Context context, ItemClickListener clickListener, ItemClickListener deleteListener) {
        this.karyawanList = karyawanList;
        this.context = context;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
        this.dbHelper = new DatabaseHelper(context);
    }



    // ViewHolder untuk item RecyclerView
    public static class KaryawanViewHolder extends RecyclerView.ViewHolder {
        TextView tvNik, tvNama, tvAlamat, tvJabatan, tvTanggalMasuk;
        Button btnEdit, btnHapus;

        public KaryawanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNik = itemView.findViewById(R.id.tv_nik);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvJabatan = itemView.findViewById(R.id.tv_jabatan);
            tvTanggalMasuk = itemView.findViewById(R.id.tv_tanggal_masuk);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnHapus = itemView.findViewById(R.id.btn_hapus);
        }
    }

    // Mengikat data dengan ViewHolder
    @Override
    public void onBindViewHolder(@NonNull KaryawanViewHolder holder, int position) {
        Karyawan karyawan = karyawanList.get(position);

        // Menampilkan data pada TextView
        holder.tvNik.setText(karyawan.getNik());
        holder.tvNama.setText(karyawan.getNama());
        holder.tvAlamat.setText(karyawan.getAlamat());
        holder.tvJabatan.setText(karyawan.getJabatan());
        holder.tvTanggalMasuk.setText(karyawan.getTanggalMasuk());

        // Tombol Edit
        holder.btnEdit.setOnClickListener(v -> {
            // Panggil listener untuk klik edit
            clickListener.onItemClick(karyawan, position);
        });

        // Tombol Hapus
        holder.btnHapus.setOnClickListener(v -> {
            Log.d("KaryawanAdapter", "Hapus button clicked for position: " + position);  // Tambahkan log
            deleteListener.onItemClick(karyawan, position);
        });
    }

    // Menyediakan ViewHolder baru untuk setiap item
    @NonNull
    @Override
    public KaryawanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout untuk item RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_karyawan, parent, false);
        return new KaryawanViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return karyawanList.size();
    }

    // Update data yang ada di adapter
    public void setData(List<Karyawan> newList) {
        this.karyawanList = newList;
        notifyDataSetChanged();
    }
}

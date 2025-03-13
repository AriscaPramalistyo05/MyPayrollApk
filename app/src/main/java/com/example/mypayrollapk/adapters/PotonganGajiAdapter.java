package com.example.mypayrollapk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypayrollapk.KelolaGajiActivity;
import com.example.mypayrollapk.R;
import com.example.mypayrollapk.models.PotonganGaji;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PotonganGajiAdapter extends RecyclerView.Adapter<PotonganGajiAdapter.ViewHolder> {

    private Context context;
    private List<PotonganGaji> potonganGajiList;
    private OnItemDeleteListener deleteListener;

    // Perbaiki konstruktor dengan menerima OnItemDeleteListener
    public PotonganGajiAdapter(Context context, List<PotonganGaji> potonganGajiList, OnItemDeleteListener deleteListener) {
        this.context = context;
        this.potonganGajiList = potonganGajiList;
        this.deleteListener = deleteListener;
    }


    public interface OnItemDeleteListener {
        void onItemDeleted();
    }

    public PotonganGajiAdapter(Context context, List<PotonganGaji> potonganGajiList) {
        this.context = context;
        this.potonganGajiList = potonganGajiList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_potongan_gaji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PotonganGaji potongan = potonganGajiList.get(position);

        holder.tvNamaPotongan.setText(potongan.getNamaPotongan());
        holder.tvNominalPotongan.setText(formatRupiah(potongan.getNominal()));

        // Fungsi tombol hapus
        holder.btnHapus.setOnClickListener(v -> {
            potonganGajiList.remove(position);
            notifyDataSetChanged(); // Gunakan ini untuk menghindari error update list
            deleteListener.onItemDeleted();  // Update total gaji setelah dihapus
            Toast.makeText(context, "Potongan dihapus", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return potonganGajiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaPotongan, tvNominalPotongan;
        ImageButton btnHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaPotongan = itemView.findViewById(R.id.tv_nama_potongan);
            tvNominalPotongan = itemView.findViewById(R.id.tv_nominal_potongan);
            btnHapus = itemView.findViewById(R.id.btn_hapus_potongan);
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(amount);
    }
}

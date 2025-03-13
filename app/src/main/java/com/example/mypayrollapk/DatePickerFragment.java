package com.example.mypayrollapk;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// Kelas DatePickerFragment untuk menampilkan dialog pemilihan tanggal
public class DatePickerFragment extends DialogFragment {

    private final DatePickerDialog.OnDateSetListener listener;

    // Konstruktor untuk menerima listener saat tanggal dipilih
    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Gunakan tanggal saat ini sebagai tanggal default
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Kembalikan instance DatePickerDialog dengan listener
        return new DatePickerDialog(requireContext(), listener, year, month, day);
    }
}

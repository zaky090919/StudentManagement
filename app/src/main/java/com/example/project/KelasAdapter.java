package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class KelasAdapter extends ArrayAdapter<Kelas> {
    private Context context;
    private List<Kelas> kelasList;
    private DatabaseHelper dbHelper;

    public KelasAdapter(Context context, List<Kelas> kelasList) {
        super(context, 0, kelasList);
        this.context = context;
        this.kelasList = kelasList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_kelas, parent, false);
        }

        Kelas kelas = kelasList.get(position);

        TextView tvNamaKelas = convertView.findViewById(R.id.tvNamaKelas);
        TextView tvNamaDosen = convertView.findViewById(R.id.tvNamaDosen);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdate);


        // Menampilkan data kelas di TextView
        tvNamaKelas.setText(kelas.getNamaKelas());
        tvNamaDosen.setText(kelas.getNamaDosen());

        // Tombol Update
        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditKelasActivity.class);
            intent.putExtra("KELAS_ID", kelas.getId()); // Kirim ID kelas untuk update
            context.startActivity(intent);
        });



        // Pastikan untuk mengembalikan convertView yang sudah diproses
        return convertView; // Kembalikan view untuk item yang sudah di-update
    }
}

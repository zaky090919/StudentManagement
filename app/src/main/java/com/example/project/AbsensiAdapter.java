package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.Spinner;
import android.widget.ArrayAdapter; // Untuk ArrayAdapter
import androidx.annotation.NonNull; // Untuk NonNull
import android.widget.AdapterView;





public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.AbsensiViewHolder> {

    private List<Siswa> siswaList;
    private OnKeteranganChangeListener onKeteranganChangeListener;

    // Constructor
    public AbsensiAdapter(List<Siswa> siswaList, OnKeteranganChangeListener onKeteranganChangeListener) {
        this.siswaList = siswaList;
        this.onKeteranganChangeListener = onKeteranganChangeListener;
    }

    @NonNull
    @Override
    public AbsensiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absen, parent, false);
        return new AbsensiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsensiViewHolder holder, int position) {
        Siswa siswa = siswaList.get(position);
        holder.namaTextView.setText(siswa.getNama());
        holder.jurusanTextView.setText(siswa.getJurusan());
        holder.genderTextView.setText(siswa.getGender());

        // Isi spinner dengan data keterangan
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                holder.itemView.getContext(),
                R.array.keterangan_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerKeterangan.setAdapter(adapter);

        // Atur keterangan sesuai data
        if (siswa.getKeterangan() != null) {
            holder.spinnerKeterangan.setSelection(adapter.getPosition(siswa.getKeterangan()));
        }

        // Set listener untuk perubahan keterangan
        holder.spinnerKeterangan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedKeterangan = parentView.getItemAtPosition(position).toString();
                if (!selectedKeterangan.equals(siswa.getKeterangan())) {
                    siswa.setKeterangan(selectedKeterangan); // Update keterangan pada objek siswa
                    onKeteranganChangeListener.onKeteranganChanged(siswa); // Kirim data yang diubah ke activity
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }

    public static class AbsensiViewHolder extends RecyclerView.ViewHolder {
        TextView namaTextView, jurusanTextView, genderTextView;
        Spinner spinnerKeterangan;

        public AbsensiViewHolder(@NonNull View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.namaTextView);
            jurusanTextView = itemView.findViewById(R.id.jurusanTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            spinnerKeterangan = itemView.findViewById(R.id.spinnerKeterangan);
        }
    }

    // Interface untuk mendeteksi perubahan keterangan
    public interface OnKeteranganChangeListener {
        void onKeteranganChanged(Siswa siswa);
    }
    public void updateData(List<Siswa> newSiswaList) {
        this.siswaList.clear(); // Kosongkan daftar lama
        this.siswaList.addAll(newSiswaList); // Tambahkan daftar baru
        notifyDataSetChanged(); // Beritahu adapter untuk memperbarui tampilan
    }

}

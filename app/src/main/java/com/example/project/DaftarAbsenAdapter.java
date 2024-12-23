package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DaftarAbsenAdapter extends RecyclerView.Adapter<DaftarAbsenAdapter.ViewHolder> {

    private Context context;
    private List<Siswa> siswaList;
    private String[] keteranganArray;

    public DaftarAbsenAdapter(Context context, List<Siswa> siswaList) {
        this.context = context;
        this.siswaList = siswaList;
        this.keteranganArray = context.getResources().getStringArray(R.array.keterangan_array);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item
        View view = LayoutInflater.from(context).inflate(R.layout.item_daftar_absen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get data siswa at current position
        Siswa siswa = siswaList.get(position);

        // Set TextView data
        holder.namaTextView.setText(siswa.getNama());
        holder.jurusanTextView.setText(siswa.getJurusan());
        holder.genderTextView.setText(siswa.getGender());

        // Set Spinner untuk keterangan absensi
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, keteranganArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerKeterangan.setAdapter(spinnerAdapter);

        // Set Spinner default item
        holder.spinnerKeterangan.setSelection(getKeteranganPosition(siswa.getKeterangan())); // Set the spinner position to match the keterangan

        // Set listener untuk perubahan pilihan keterangan
        holder.spinnerKeterangan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Ambil keterangan yang dipilih dan set ke Siswa
                String keterangan = keteranganArray[position];
                siswa.setKeterangan(keterangan); // Update Siswa's keterangan
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Tidak ada pilihan
            }
        });
    }

    private int getKeteranganPosition(String keterangan) {
        // Find the position of the given keterangan in the array
        for (int i = 0; i < keteranganArray.length; i++) {
            if (keteranganArray[i].equals(keterangan)) {
                return i;
            }
        }
        return 0; // Default to "Hadir" if not found
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaTextView, jurusanTextView, genderTextView;
        Spinner spinnerKeterangan;

        public ViewHolder(View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.namaTextView);
            jurusanTextView = itemView.findViewById(R.id.jurusanTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            spinnerKeterangan = itemView.findViewById(R.id.spinnerKeterangan);
        }
    }
}

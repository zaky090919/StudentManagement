package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder> {

    private Context context;
    private List<Siswa> siswaList;
    private DatabaseHelper dbHelper;

    public SiswaAdapter(Context context, List<Siswa> siswaList) {
        this.context = context;
        this.siswaList = siswaList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public SiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_siswa, parent, false);
        return new SiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SiswaViewHolder holder, int position) {
        Siswa siswa = siswaList.get(position);

        holder.tvNama.setText(siswa.getNama());
        holder.tvKelas.setText(siswa.getKelas(dbHelper));
        holder.tvTempatLahir.setText(siswa.getTempatLahir());
        holder.tvTanggalLahir.setText(siswa.getTanggalLahir());
        holder.tvAlamat.setText(siswa.getAlamat());
        holder.tvJurusan.setText(siswa.getJurusan());
        holder.tvGender.setText(siswa.getGender());

        // Edit button click
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditSiswaActivity.class);
            intent.putExtra("SiswaId", siswa.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }

    public static class SiswaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvKelas, tvTempatLahir, tvTanggalLahir, tvAlamat, tvJurusan, tvGender;
        Button btnEdit;

        public SiswaViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvKelas = itemView.findViewById(R.id.tvKelas);
            tvTempatLahir = itemView.findViewById(R.id.tvTempatLahir);
            tvTanggalLahir = itemView.findViewById(R.id.tvTanggalLahir);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);
            tvGender = itemView.findViewById(R.id.tvGender);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}

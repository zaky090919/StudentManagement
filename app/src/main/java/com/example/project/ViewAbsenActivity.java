package com.example.project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;


public class ViewAbsenActivity extends AppCompatActivity {

    private Spinner spinnerKelas;
    private EditText etTanggal;
    private TextView tvHari;
    private Button btnCari, btnEdit;
    private RecyclerView recyclerViewAbsensi;

    private AbsensiAdapter absensiAdapter;
    private DatabaseHelper databaseHelper;
    private List<Siswa> siswaList;
    private List<Kelas> kelasList; // List untuk menyimpan data kelas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_absen);

        // Inisialisasi View
        spinnerKelas = findViewById(R.id.spinnerKelas);
        etTanggal = findViewById(R.id.etTanggal);
        tvHari = findViewById(R.id.tvHari);
        btnCari = findViewById(R.id.btnCari);
        btnEdit = findViewById(R.id.btnEdit);
        recyclerViewAbsensi = findViewById(R.id.recyclerViewAbsensi);

        recyclerViewAbsensi.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Muat daftar kelas ke Spinner
        loadKelasData();

        // Pilih tanggal menggunakan DatePicker
        etTanggal.setOnClickListener(v -> showDatePicker());

        // Tombol Cari
        btnCari.setOnClickListener(v -> {
            int selectedKelasIndex = spinnerKelas.getSelectedItemPosition();
            if (selectedKelasIndex >= 0 && !kelasList.isEmpty()) {
                int selectedKelasId = kelasList.get(selectedKelasIndex).getId(); // ID kelas
                String tanggal = etTanggal.getText().toString();

                if (!tanggal.isEmpty()) {
                    siswaList = databaseHelper.getSiswaByKelas(selectedKelasId);
                    List<HashMap<String, String>> absensiData = databaseHelper.getDataByTanggal(tanggal);

                    // Gabungkan data siswa dan absensi
                    List<Siswa> filteredSiswa = filterSiswaByTanggal(siswaList, absensiData);

                    if (filteredSiswa.isEmpty()) {
                        // Jika tidak ada data untuk tanggal itu
                        Toast.makeText(this, "Data tidak ada di database untuk tanggal itu", Toast.LENGTH_SHORT).show();

                        // Bersihkan tampilan RecyclerView
                        if (absensiAdapter == null) {
                            absensiAdapter = new AbsensiAdapter(new ArrayList<>(), this::onKeteranganChanged);
                            recyclerViewAbsensi.setAdapter(absensiAdapter);
                        } else {
                            absensiAdapter.updateData(new ArrayList<>());
                        }
                    } else {
                        // Tampilkan data di RecyclerView
                        if (absensiAdapter == null) {
                            absensiAdapter = new AbsensiAdapter(filteredSiswa, this::onKeteranganChanged);
                            recyclerViewAbsensi.setAdapter(absensiAdapter);
                        } else {
                            absensiAdapter.updateData(filteredSiswa);
                        }
                    }
                } else {
                    Toast.makeText(this, "Pilih tanggal terlebih dahulu!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Pilih kelas terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });


        // Tombol Edit
        // Tombol Edit
        btnEdit.setOnClickListener(v -> {
            boolean allUpdated = true; // Untuk melacak apakah semua data berhasil diperbarui
            String tanggal = etTanggal.getText().toString(); // Ambil tanggal dari EditText

            if (tanggal.isEmpty()) {
                Toast.makeText(ViewAbsenActivity.this, "Pilih tanggal terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Siswa siswa : siswaList) {
                if (siswa.getKeterangan() != null) {
                    boolean updated = databaseHelper.updateKeterangan(siswa.getId(), tanggal, siswa.getKeterangan());
                    if (!updated) {
                        allUpdated = false; // Jika salah satu pembaruan gagal
                    }
                }
            }

            // Tampilkan pesan berdasarkan hasil pembaruan
            if (allUpdated) {
                Toast.makeText(ViewAbsenActivity.this, "Semua data absensi berhasil diperbarui", Toast.LENGTH_SHORT).show();

                // Pindah ke ManageAbsenActivity
                Intent intent = new Intent(ViewAbsenActivity.this, ManageAbsenActivity.class);
                startActivity(intent);
                finish(); // Tutup activity ini jika tidak diperlukan lagi
            } else {
                Toast.makeText(ViewAbsenActivity.this, "Beberapa data gagal diperbarui", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadKelasData() {
        kelasList = databaseHelper.getAllKelas();
        List<String> kelasNames = new ArrayList<>();

        // Ambil nama kelas untuk ditampilkan di Spinner
        for (Kelas kelas : kelasList) {
            kelasNames.add(kelas.getNamaKelas());
        }

        if (kelasNames.isEmpty()) {
            kelasNames.add("Tidak ada kelas tersedia");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kelasNames);
        spinnerKelas.setAdapter(adapter);
    }

    private void showDatePicker() {
        // Tampilkan DatePickerDialog untuk memilih tanggal
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Format tanggal dan tampilkan di EditText
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etTanggal.setText(selectedDate);

            // Dapatkan nama hari berdasarkan tanggal
            calendar.set(year1, month1, dayOfMonth);
            String selectedDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
            tvHari.setText("Hari: " + selectedDay);
        }, year, month, day);
        datePickerDialog.show();
    }

    private String getDayName(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
    }

    private List<Siswa> filterSiswaByTanggal(List<Siswa> siswaList, List<HashMap<String, String>> absensiData) {
        List<Siswa> filteredList = new ArrayList<>();
        for (Siswa siswa : siswaList) {
            for (HashMap<String, String> absensi : absensiData) {
                if (absensi.get("nama").equals(siswa.getNama())) {
                    siswa.setKeterangan(absensi.get("keterangan"));
                    filteredList.add(siswa);
                    break;
                }
            }
        }
        return filteredList;
    }

    // Listener untuk perubahan keterangan
    private void onKeteranganChanged(Siswa siswa) {
        // This will be used to trigger the update when the keterangan changes
    }
}

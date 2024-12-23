package com.example.project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddAbsensiActivity extends AppCompatActivity {
    private Spinner spinnerKelas;
    private RecyclerView recyclerViewSiswa;
    private EditText etTanggal;
    private TextView tvHari;
    private Button saveButton;

    private DaftarAbsenAdapter adapter;
    private DatabaseHelper dbHelper; // Database helper untuk operasi database
    private List<Siswa> siswaList = new ArrayList<>();
    private List<Kelas> kelasList = new ArrayList<>();

    private String selectedDate = ""; // Untuk menyimpan tanggal yang dipilih
    private String selectedDay = "";  // Untuk menyimpan nama hari yang dipilih

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_absensi);

        spinnerKelas = findViewById(R.id.spinnerKelas);
        recyclerViewSiswa = findViewById(R.id.recyclerViewSiswa);
        etTanggal = findViewById(R.id.etTanggal);
        tvHari = findViewById(R.id.tvHari);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new DatabaseHelper(this);

        // Load data kelas ke spinner
        loadKelasData();

        // Setup RecyclerView
        adapter = new DaftarAbsenAdapter(this, siswaList);
        recyclerViewSiswa.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSiswa.setAdapter(adapter);

        // Spinner on item selected listener
        spinnerKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Ambil kelas yang dipilih
                Kelas selectedKelas = kelasList.get(position);
                loadSiswaByKelas(selectedKelas.getId()); // Muat siswa berdasarkan kelas
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                siswaList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        // EditText untuk memilih tanggal
        etTanggal.setOnClickListener(v -> showDatePicker());

        // Tombol Save
        saveButton.setOnClickListener(v -> saveAbsensi());
    }

    private void showDatePicker() {
        // Get today's date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set up DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Format selected date and display it in EditText
            selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etTanggal.setText(selectedDate);

            // Get the day of the week based on the selected date
            calendar.set(year1, month1, dayOfMonth);
            selectedDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
            tvHari.setText("Hari: " + selectedDay);
        }, year, month, day);

        // Set the minimum date to today to prevent selecting past dates
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }


    private void loadKelasData() {
        kelasList = dbHelper.getAllKelas(); // Pastikan Anda punya metode ini
        List<String> namaKelas = new ArrayList<>();
        for (Kelas kelas : kelasList) {
            namaKelas.add(kelas.getNamaKelas());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaKelas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKelas.setAdapter(adapter);
    }

    private void loadSiswaByKelas(int kelasId) {
        siswaList.clear();
        siswaList.addAll(dbHelper.getSiswaByKelas(kelasId)); // Pastikan metode ini ada di DatabaseHelper
        adapter.notifyDataSetChanged();
    }

    private void saveAbsensi() {
        if (selectedDate.isEmpty() || selectedDay.isEmpty()) {
            Toast.makeText(this, "Harap pilih tanggal terlebih dahulu.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek jika absensi untuk tanggal yang dipilih sudah ada
        if (isAbsensiAlreadySaved(selectedDate)) {
            Toast.makeText(this, "Data absensi sudah disimpan untuk tanggal ini.", Toast.LENGTH_SHORT).show();
            return; // Keluar dari metode jika data sudah ada
        }

        // Menyimpan data absensi
        for (Siswa siswa : siswaList) {
            String keterangan = siswa.getKeterangan();  // Dapatkan keterangan absensi

            long result = dbHelper.insertAbsensi(siswa.getId(), selectedDate, selectedDay, keterangan);
            if (result == -1) {
                Toast.makeText(this, "Gagal menyimpan absen untuk " + siswa.getNama(), Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(this, "Data absensi berhasil disimpan!", Toast.LENGTH_SHORT).show();
        // Pindah ke halaman Manage Absen Activity
        Intent intent = new Intent(AddAbsensiActivity.this, ManageAbsenActivity.class);
        startActivity(intent);
        finish(); // Tutup halaman AddAbsensiActivity
    }

    private boolean isAbsensiAlreadySaved(String date) {
        // Cek apakah data absensi sudah ada untuk tanggal yang dipilih
        return dbHelper.isAbsensiExist(date); // Metode ini akan mengembalikan true jika absensi sudah ada untuk tanggal tersebut
    }


}

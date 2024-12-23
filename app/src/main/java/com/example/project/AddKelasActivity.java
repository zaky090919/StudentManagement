package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class AddKelasActivity extends AppCompatActivity {

    private EditText etNamaKelas, etNamaDosen;
    private Button btnAddKelas;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kelas);

        etNamaKelas = findViewById(R.id.etNamaKelas);
        etNamaDosen = findViewById(R.id.etNamaDosen);
        btnAddKelas = findViewById(R.id.btnAddKelas);
        dbHelper = new DatabaseHelper(this);

        btnAddKelas.setOnClickListener(v -> {
            String namaKelas = etNamaKelas.getText().toString();
            String namaDosen = etNamaDosen.getText().toString();

            if (namaKelas.isEmpty() || namaDosen.isEmpty()) {
                Toast.makeText(AddKelasActivity.this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            Kelas kelas = new Kelas(namaKelas, namaDosen);
            long result = dbHelper.addKelas(kelas);

            if (result > 0) {
                Toast.makeText(AddKelasActivity.this, "Kelas berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddKelasActivity.this, ListKelasActivity.class);
                startActivity(intent); // Memulai ListKelasActivity lagi untuk memastikan data terbaru ditampilkan
                finish(); // Menutup AddKelasActivity
            } else {
                Toast.makeText(AddKelasActivity.this, "Gagal menambahkan kelas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


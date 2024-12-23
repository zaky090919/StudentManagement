package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

public class EditKelasActivity extends AppCompatActivity {

    private EditText editTextNamaKelas, editTextNamaDosen;
    private Button buttonUpdateKelas;
    private DatabaseHelper dbHelper;
    private int kelasId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kelas);

        // Initialize views
        editTextNamaKelas = findViewById(R.id.editTextNamaKelas);
        editTextNamaDosen = findViewById(R.id.editTextNamaDosen);
        buttonUpdateKelas = findViewById(R.id.buttonUpdateKelas);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Assuming kelasId is passed from previous activity as an extra
        kelasId = getIntent().getIntExtra("KELAS_ID", -1);

        // Load current data of Kelas
        loadKelasData(kelasId);

        // Handle update button click
        buttonUpdateKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKelas();
            }
        });
    }

    private void loadKelasData(int kelasId) {
        Kelas kelas = dbHelper.getKelasById(kelasId);

        if (kelas != null) {
            editTextNamaKelas.setText(kelas.getNamaKelas());
            editTextNamaDosen.setText(kelas.getNamaDosen());
        } else {
            Toast.makeText(this, "Kelas not found", Toast.LENGTH_SHORT).show();
            finish(); // Tutup activity jika data tidak ditemukan
        }
    }

    private void updateKelas() {
        String namaKelas = editTextNamaKelas.getText().toString().trim();
        String namaDosen = editTextNamaDosen.getText().toString().trim();

        if (namaKelas.isEmpty() || namaDosen.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Kelas object to update
        Kelas updatedKelas = new Kelas(kelasId, namaKelas, namaDosen);

        // Call the method to update the database
        boolean isUpdated = dbHelper.updateKelas(updatedKelas);

        if (isUpdated) {
            Toast.makeText(this, "Kelas updated successfully", Toast.LENGTH_SHORT).show();

            // After successful update, navigate to ListKelasActivity
            Intent intent = new Intent(EditKelasActivity.this, ListKelasActivity.class);
            startActivity(intent);
            finish(); // Close current activity
        } else {
            Toast.makeText(this, "Failed to update Kelas", Toast.LENGTH_SHORT).show();
        }
    }
}
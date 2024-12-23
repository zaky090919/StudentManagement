package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ListKelasActivity extends AppCompatActivity {

    private ListView listViewKelas;
    private Button btnAddKelas;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kelas);

        listViewKelas = findViewById(R.id.listViewKelas);
        btnAddKelas = findViewById(R.id.btnAddKelas);
        dbHelper = new DatabaseHelper(this);

        // Mengambil daftar kelas dari database
        List<Kelas> kelasList = dbHelper.getAllKelas();

        // Menggunakan KelasAdapter untuk menampilkan nama kelas dan nama dosen dalam ListView
        KelasAdapter kelasAdapter = new KelasAdapter(this, kelasList);
        listViewKelas.setAdapter(kelasAdapter);

        // Tombol untuk menambah kelas
        btnAddKelas.setOnClickListener(v -> {
            Intent intent = new Intent(ListKelasActivity.this, AddKelasActivity.class);
            startActivity(intent);
        });
        // On item click of ListView in ListKelasActivity
        listViewKelas.setOnItemClickListener((parent, view, position, id) -> {
            // Get the Kelas object at the clicked position
            Kelas selectedKelas = (Kelas) parent.getItemAtPosition(position);


        });

    }
}

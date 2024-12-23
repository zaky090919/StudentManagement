package com.example.project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewSiswaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SiswaAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Siswa> siswaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_siswa);

        recyclerView = findViewById(R.id.recyclerViewSiswa);
        dbHelper = new DatabaseHelper(this);

        // Ambil data siswa dari database
        siswaList = dbHelper.getAllSiswa();

        if (siswaList.isEmpty()) {
            Toast.makeText(this, "Tidak ada siswa", Toast.LENGTH_SHORT).show();
        } else {
            // Setup RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new SiswaAdapter(this, siswaList);
            recyclerView.setAdapter(adapter);
        }
    }
}

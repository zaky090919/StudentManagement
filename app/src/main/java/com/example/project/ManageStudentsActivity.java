package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageStudentsActivity extends AppCompatActivity {

    private Button btnTambahSiswa, btnDaftarSiswa, btnDaftarKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        // Initialize views
        btnTambahSiswa = findViewById(R.id.btnTambahSiswa);
        btnDaftarSiswa = findViewById(R.id.btnDaftarSiswa);


        // Set listener for the "Tambah Siswa" button to navigate to Add/Edit Student Activity
        btnTambahSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Add/Edit Student Activity
                Intent intent = new Intent(ManageStudentsActivity.this, AddSiswaActivity.class);
                startActivity(intent);
            }
        });

        // Set listener for the "Daftar Siswa" button to navigate to List of Students Activity
        btnDaftarSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the activity that shows the list of students (e.g., ViewSiswaActivity)
                Intent intent = new Intent(ManageStudentsActivity.this, ViewSiswaActivity.class);
                startActivity(intent);
            }
        });


    }
}

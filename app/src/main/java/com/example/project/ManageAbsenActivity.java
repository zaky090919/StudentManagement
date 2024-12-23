package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageAbsenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_absen);

        Button btnAddAbsen = findViewById(R.id.btnAddAbsen);
        Button btnViewAbsen = findViewById(R.id.btnViewAbsen);

        btnAddAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageAbsenActivity.this, AddAbsensiActivity.class);
                startActivity(intent);
            }
        });

        btnViewAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageAbsenActivity.this, ViewAbsenActivity.class);
                startActivity(intent);
            }
        });

    }
}

package com.example.project;

import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageEmployeesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeesAdapter adapter;
    private DatabaseHelper dbHelper;
    private ArrayList<User> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        recyclerView = findViewById(R.id.recyclerViewEmployees);
        dbHelper = new DatabaseHelper(this);

        // Fetch employee data from the database
        employeeList = dbHelper.getAllUsers();

        if (employeeList.isEmpty()) {
            Toast.makeText(this, "Tidak ada karyawan", Toast.LENGTH_SHORT).show();
        } else {
            // Setup RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new EmployeesAdapter(employeeList, this);
            recyclerView.setAdapter(adapter);
        }

        // Add Employee Button functionality
        findViewById(R.id.btnAddEmployee).setOnClickListener(v -> {
            // Launch the SignupActivity for adding a new employee
            startActivity(new Intent(ManageEmployeesActivity.this, SignupActivity.class));
        });
    }
}

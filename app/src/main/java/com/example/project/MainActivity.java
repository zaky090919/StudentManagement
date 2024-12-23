package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get username from the session
        SessionManager sessionManager = new SessionManager(this);
        String username = sessionManager.getUsername();

        // Referencing Views
        ImageView userIcon = findViewById(R.id.user_icon);
        TextView userName = findViewById(R.id.user_name);
        Button btnManageStudents = findViewById(R.id.btn_manage_students);
        Button btnManageAttendance = findViewById(R.id.btn_manage_attendance);
        ImageButton btnLogout = findViewById(R.id.btn_logout);
        Button btnManageClasses = findViewById(R.id.btn_manage_classes);
        Button btnManageEmployees = findViewById(R.id.btn_manage_employees); // Added reference for manage employees button

        // Set username in TextView
        if (username != null) {
            userName.setText(username);
        } else {
            userName.setText("Nama User");
        }

        // Logout action with confirmation
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog for logout confirmation
                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Logout")
                        .setMessage("Apakah Anda yakin ingin logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // If Yes is clicked, logout the user
                            sessionManager.logout(); // Clear the session
                            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(logoutIntent);
                            finish(); // Close the current activity (MainActivity)
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Do nothing if No is clicked, just dismiss the dialog
                            dialog.dismiss();
                        })
                        .show(); // Show the dialog
            }
        });

        // Navigation to Manage Students Activity
        btnManageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageStudentsActivity.class);
                startActivity(intent);
            }
        });

        // Navigation to Manage Attendance Activity
        btnManageAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageAbsenActivity.class);
                startActivity(intent);
            }
        });

        // Navigation to List Classes Activity
        btnManageClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListKelasActivity.class);
                startActivity(intent);
            }
        });

        // Navigation to Manage Employees Activity (Added functionality)
        btnManageEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageEmployeesActivity.class);
                startActivity(intent);
            }
        });
    }
}

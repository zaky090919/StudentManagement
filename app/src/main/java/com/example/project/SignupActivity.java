package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText editUsername, editPassword, editConfirmPassword;
    Button btnRegister;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);

        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.insertUser(username, password)) {
                Toast.makeText(SignupActivity.this, "Pendaftaran berhasil! Silakan login.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, ManageEmployeesActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignupActivity.this, "Pendaftaran gagal! Username sudah digunakan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

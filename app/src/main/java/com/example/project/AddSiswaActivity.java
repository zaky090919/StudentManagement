package com.example.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class AddSiswaActivity extends AppCompatActivity {
    EditText etNama, etTempatLahir, etTanggalLahir, etAlamat, etJurusan;
    RadioGroup rgGender;
    Spinner spinnerKelas;
    Button btnAddSiswa;
    DatabaseHelper dbHelper;

    // Variable to hold the selected Kelas ID
    private int selectedKelasId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_siswa);

        etNama = findViewById(R.id.etNama);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etAlamat = findViewById(R.id.etAlamat);
        etJurusan = findViewById(R.id.etJurusan);
        rgGender = findViewById(R.id.rgGender);
        spinnerKelas = findViewById(R.id.spinnerKelas);
        btnAddSiswa = findViewById(R.id.btnAddSiswa);

        dbHelper = new DatabaseHelper(this);

        // Load Kelas into Spinner
        loadKelasToSpinner();

        // Setup DatePicker for Tanggal Lahir
        etTanggalLahir.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Show the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddSiswaActivity.this,
                    (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        // Set the selected date into the EditText
                        String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etTanggalLahir.setText(selectedDate);
                    },
                    year, month, dayOfMonth
            );

            datePickerDialog.show();
        });

        btnAddSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etNama.getText().toString();
                String tempatLahir = etTempatLahir.getText().toString();
                String tanggalLahir = etTanggalLahir.getText().toString();
                String alamat = etAlamat.getText().toString();
                String jurusan = etJurusan.getText().toString();

                // Pengecekan data tidak boleh kosong
                if (nama.isEmpty() || tempatLahir.isEmpty() || tanggalLahir.isEmpty() ||
                        alamat.isEmpty() || jurusan.isEmpty() || rgGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(AddSiswaActivity.this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show();
                    return;  // Menghentikan proses jika ada data yang kosong
                }

                // Menentukan gender berdasarkan radio button yang dipilih
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                String gender = selectedGenderId == R.id.rbMale ? "Laki-laki" : "Perempuan";

                // Membuat objek Siswa
                Siswa siswa = new Siswa(0, nama, selectedKelasId, tempatLahir, tanggalLahir, alamat, jurusan, gender);

                // Menambahkan siswa ke dalam database
                long result = dbHelper.addSiswa(siswa);

                if (result > 0) {  // Jika data berhasil ditambahkan
                    Toast.makeText(AddSiswaActivity.this, "Data siswa berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                    // Navigasi kembali ke ManageStudentsActivity
                    Intent intent = new Intent(AddSiswaActivity.this, ManageStudentsActivity.class);
                    startActivity(intent);
                    finish();  // Menutup aktivitas ini agar tidak bisa kembali lagi dengan tombol back
                } else {  // Jika gagal menambahkan data
                    Toast.makeText(AddSiswaActivity.this, "Gagal menambahkan data siswa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to load Kelas into Spinner
    private void loadKelasToSpinner() {
        List<Kelas> kelasList = dbHelper.getAllKelas();
        if (kelasList != null && !kelasList.isEmpty()) {
            // Create an ArrayAdapter for the Spinner
            ArrayAdapter<Kelas> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kelasList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerKelas.setAdapter(adapter);

            // Set an item selected listener to get the selected Kelas ID
            spinnerKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selectedKelasId = kelasList.get(position).getId();  // Get the ID of the selected Kelas
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    selectedKelasId = -1;  // Set to -1 if no selection is made
                }
            });
        }
    }
}

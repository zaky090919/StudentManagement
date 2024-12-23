package com.example.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditSiswaActivity extends AppCompatActivity {

    private EditText edtNama, edtTempatLahir, edtTanggalLahir, edtAlamat, edtJurusan;
    private Spinner spinnerKelas;
    private RadioGroup radioGroupGender;
    private Button btnSaveSiswa;
    private DatabaseHelper dbHelper;
    private int siswaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_siswa);

        // Initialize views
        edtNama = findViewById(R.id.edtNama);
        edtTempatLahir = findViewById(R.id.edtTempatLahir);
        edtTanggalLahir = findViewById(R.id.edtTanggalLahir);
        edtAlamat = findViewById(R.id.edtAlamat);
        edtJurusan = findViewById(R.id.edtJurusan);
        spinnerKelas = findViewById(R.id.spinnerKelas);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        btnSaveSiswa = findViewById(R.id.btnSaveSiswa);
        dbHelper = new DatabaseHelper(this);

        // Get the student ID from the intent
        Intent intent = getIntent();
        siswaId = intent.getIntExtra("SiswaId", -1);

        if (siswaId != -1) {
            // Load the student data from the database
            loadSiswaData(siswaId);
        }

        // Set up the save button
        btnSaveSiswa.setOnClickListener(v -> {
            // Save the updated student data
            updateSiswaData();
        });

        // Set up the DatePicker for Tanggal Lahir field
        edtTanggalLahir.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Show the DatePickerDialog with the current date
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditSiswaActivity.this,
                    (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        // Set the selected date into the EditText
                        String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        edtTanggalLahir.setText(selectedDate);
                    },
                    year, month, dayOfMonth
            );

            datePickerDialog.show();
        });
    }

    // Method to load the student data into the EditText fields
    private void loadSiswaData(int siswaId) {
        Siswa siswa = dbHelper.getSiswaById(siswaId); // Get the student by ID from the database

        if (siswa != null) {
            edtNama.setText(siswa.getNama());
            edtTempatLahir.setText(siswa.getTempatLahir());
            edtTanggalLahir.setText(siswa.getTanggalLahir());
            edtAlamat.setText(siswa.getAlamat());
            edtJurusan.setText(siswa.getJurusan());

            // Use getKelas() method to get the class name from kelasId
            String kelasName = siswa.getKelas(dbHelper); // Get the Kelas name using the DatabaseHelper

            // Set the Kelas in the spinner
            ArrayAdapter<String> kelasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dbHelper.getAllKelasNames());
            kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerKelas.setAdapter(kelasAdapter);

            // Set the selected Kelas position in the spinner
            int kelasPosition = kelasAdapter.getPosition(kelasName); // Find the position of the kelas in the spinner
            spinnerKelas.setSelection(kelasPosition);

            // Set the gender based on the database value
            if (siswa.getGender().equals("Laki-laki")) {
                radioGroupGender.check(R.id.rbMale);
            } else {
                radioGroupGender.check(R.id.rbFemale);
            }
        }
    }

    // Method to update the student data in the database
    private void updateSiswaData() {
        String nama = edtNama.getText().toString();
        String kelas = spinnerKelas.getSelectedItem().toString(); // Get selected class name
        String tempatLahir = edtTempatLahir.getText().toString();
        String tanggalLahir = edtTanggalLahir.getText().toString();
        String alamat = edtAlamat.getText().toString();
        String jurusan = edtJurusan.getText().toString();

        // Get selected gender from RadioGroup
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);
        String gender = selectedGenderRadioButton.getText().toString();

        // Extract kelasId from the selected kelas string (assuming the format is "id-nama kelas")
        String[] kelasParts = kelas.split("-"); // Split the string by "-"
        int kelasId = Integer.parseInt(kelasParts[0].trim()); // Get the first part (ID) and convert to int

        // Update the data in the database
        Siswa siswa = new Siswa(siswaId, nama, kelasId, tempatLahir, tanggalLahir, alamat, jurusan, gender);
        boolean isUpdated = dbHelper.updateSiswa(siswa);

        if (isUpdated) {
            Toast.makeText(this, "Data siswa berhasil diupdate", Toast.LENGTH_SHORT).show();

            // Intent to navigate to ViewSiswaActivity
            Intent intent = new Intent(EditSiswaActivity.this, ViewSiswaActivity.class);
            intent.putExtra("SiswaId", siswaId); // Pass siswaId if needed
            startActivity(intent); // Start ViewSiswaActivity

            finish(); // Close the current activity (EditSiswaActivity)
        } else {
            Toast.makeText(this, "Gagal mengupdate data siswa", Toast.LENGTH_SHORT).show();
        }
    }
}

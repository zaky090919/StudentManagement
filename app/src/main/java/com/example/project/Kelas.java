package com.example.project;

public class Kelas {
    private int id;
    private String namaKelas;
    private String namaDosen;

    // Constructor tanpa ID (untuk menambahkan kelas baru)
    public Kelas(String namaKelas, String namaDosen) {
        this.namaKelas = namaKelas;
        this.namaDosen = namaDosen;
    }

    // Constructor dengan ID (untuk kelas yang sudah ada di database)
    public Kelas(int id, String namaKelas, String namaDosen) {
        this.id = id;
        this.namaKelas = namaKelas;
        this.namaDosen = namaDosen;
    }

    // Getter untuk ID
    public int getId() {
        return id;
    }

    // Setter untuk ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter untuk namaKelas
    public String getNamaKelas() {
        return namaKelas;
    }

    // Setter untuk namaKelas
    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    // Getter untuk namaDosen
    public String getNamaDosen() {
        return namaDosen;
    }

    // Setter untuk namaDosen
    public void setNamaDosen(String namaDosen) {
        this.namaDosen = namaDosen;
    }

    // Override toString untuk menampilkan hanya nama kelas
    @Override
    public String toString() {
        return namaKelas; // Hanya nama kelas yang akan ditampilkan
    }
}

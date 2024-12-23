package com.example.project;

public class Absen {
    private int absenId;
    private int siswaId;
    private String tanggal;
    private String hari;
    private String keterangan;
    private String namaSiswa;
    private String jurusan;
    private String gender;

    // Default constructor
    public Absen() {}

    // Constructor with all fields
    public Absen(int absenId, int siswaId, String tanggal, String hari, String keterangan,
                 String namaSiswa, String jurusan, String gender) {
        this.absenId = absenId;
        this.siswaId = siswaId;
        this.tanggal = tanggal;
        this.hari = hari;
        this.keterangan = keterangan;
        this.namaSiswa = namaSiswa;
        this.jurusan = jurusan;
        this.gender = gender;
    }

    // Getters and Setters
    public int getAbsenId() {
        return absenId;
    }

    public void setAbsenId(int absenId) {
        this.absenId = absenId;
    }

    public int getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(int siswaId) {
        this.siswaId = siswaId;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

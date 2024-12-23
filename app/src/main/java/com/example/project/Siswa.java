package com.example.project;

public class Siswa {
    private int id;
    private String nama;
    private int kelasId;  // Menyimpan ID kelas
    private String tempatLahir;
    private String tanggalLahir;
    private String alamat;
    private String jurusan;
    private String gender;
    private String keterangan;

    // Constructor
    public Siswa(int id, String nama, int kelasId, String tempatLahir, String tanggalLahir, String alamat, String jurusan, String gender) {
        this.id = id;
        this.nama = nama;
        this.kelasId = kelasId;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.jurusan = jurusan;
        this.gender = gender;
    }
    public Siswa() {
    }

    public String getKelas(DatabaseHelper dbHelper) {
        Kelas kelas = dbHelper.getKelasById(this.kelasId); // Mengambil objek Kelas berdasarkan kelasId
        if (kelas != null) {
            return kelas.getNamaKelas(); // Kembalikan nama kelas
        } else {
            return "Kelas tidak ditemukan"; // Jika tidak ditemukan
        }
    }


    // Getter dan Setter untuk kelasId
    public int getKelasId() {
        return kelasId;
    }


    public void setKelasId(int kelasId) {
        this.kelasId = kelasId;
    }


    // Metode untuk mendapatkan nama kelas


    // Getter dan Setter untuk atribut lainnya
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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

    // Konstruktor baru dengan 3 parameter untuk absensi
    public Siswa(String nama, String jurusan, String gender) {
        this.nama = nama;
        this.jurusan = jurusan;
        this.gender = gender;
        // Anda bisa memberikan nilai default untuk parameter lainnya, jika diperlukan
    }
    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
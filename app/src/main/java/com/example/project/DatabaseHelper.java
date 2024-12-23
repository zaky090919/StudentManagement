package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.database.SQLException;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student_management.db";
    private static final int DATABASE_VERSION = 4;

    // Tabel Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Tabel Siswa
    private static final String TABLE_SISWA = "siswa";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAMA = "nama";
    private static final String COLUMN_KELAS = "kelas";
    private static final String COLUMN_TEMPAT_LAHIR = "tempat_lahir";
    private static final String COLUMN_TANGGAL_LAHIR = "tanggal_lahir";
    private static final String COLUMN_ALAMAT = "alamat";
    private static final String COLUMN_JURUSAN = "jurusan";
    private static final String COLUMN_GENDER = "gender";

    //tabel kelas
    private static final String TABLE_KELAS = "kelas";
    private static final String COLUMN_KELAS_ID = "kelas_id";
    private static final String COLUMN_NAMA_KELAS = "nama_kelas";
    private static final String COLUMN_NAMA_DOSEN = "nama_dosen";

    // Tabel Absen
    private static final String TABLE_ABSEN = "absen";
    private static final String COLUMN_ABSEN_ID = "absen_id";
    private static final String COLUMN_SISWA_ID_FK = "siswa_id"; // Foreign Key
    private static final String COLUMN_TANGGAL = "tanggal";
    private static final String COLUMN_HARI = "hari";
    private static final String COLUMN_KETERANGAN = "keterangan";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // Create Kelas Table
        String CREATE_KELAS_TABLE = "CREATE TABLE " + TABLE_KELAS + " (" +
                COLUMN_KELAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA_KELAS + " TEXT, " +
                COLUMN_NAMA_DOSEN + " TEXT)";
        db.execSQL(CREATE_KELAS_TABLE);

        // Create Siswa Table with foreign key to Kelas
        String CREATE_TABLE_SISWA = "CREATE TABLE " + TABLE_SISWA + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA + " TEXT, " +
                COLUMN_KELAS + " INTEGER, " +
                COLUMN_TEMPAT_LAHIR + " TEXT, " +
                COLUMN_TANGGAL_LAHIR + " TEXT, " +
                COLUMN_ALAMAT + " TEXT, " +
                COLUMN_JURUSAN + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_KELAS + ") REFERENCES " + TABLE_KELAS + "(" + COLUMN_KELAS_ID + "))";
        db.execSQL(CREATE_TABLE_SISWA);

        // Create table SQL query for absen
        String CREATE_TABLE_ABSEN = "CREATE TABLE " + TABLE_ABSEN + " ("
                + COLUMN_ABSEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SISWA_ID_FK + " INTEGER, "
                + COLUMN_TANGGAL + " TEXT, "
                + COLUMN_HARI + " TEXT, "
                + COLUMN_KETERANGAN + " TEXT, "
                + "FOREIGN KEY (" + COLUMN_SISWA_ID_FK + ") REFERENCES " + TABLE_SISWA + "(" + COLUMN_ID + "));";
        db.execSQL(CREATE_TABLE_ABSEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all tables if database is upgraded
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SISWA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KELAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSEN);
        onCreate(db);
    }

    // ==== FUNGSI UNTUK USERS ====

    // Fungsi untuk menambahkan user
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Fungsi untuk autentikasi user
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();
        return isAuthenticated;
    }

    // Function to delete a user by ID
    public void deleteUserById(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Delete query
            String whereClause = COLUMN_USER_ID + " = ?";
            String[] whereArgs = {String.valueOf(userId)};

            // Execute delete
            db.delete(TABLE_USERS, whereClause, whereArgs);
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace to log
        } finally {
            db.close();
        }
    }
    // Function to get all users (Usernames)
    public ArrayList<User> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_USERS;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Check if the columns exist
                int userIdColumnIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                int usernameColumnIndex = cursor.getColumnIndex(COLUMN_USERNAME);

                // Ensure the column index is valid (>= 0)
                if (userIdColumnIndex != -1 && usernameColumnIndex != -1) {
                    do {
                        int userId = cursor.getInt(userIdColumnIndex);
                        String username = cursor.getString(usernameColumnIndex);

                        // Create a User object and add it to the list
                        User user = new User(userId, username);
                        users.add(user);
                    } while (cursor.moveToNext());
                } else {
                    Log.e("Database", "One or more columns not found.");
                }

                cursor.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return users;
    }



    // ==== FUNGSI UNTUK SISWA ====

    // Fungsi untuk menambahkan data siswa
    public long addSiswa(Siswa siswa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, siswa.getNama());
        values.put(COLUMN_KELAS, siswa.getKelasId());  // Gunakan getKelasId() untuk mendapatkan ID kelas
        values.put(COLUMN_TEMPAT_LAHIR, siswa.getTempatLahir());
        values.put(COLUMN_TANGGAL_LAHIR, siswa.getTanggalLahir());
        values.put(COLUMN_ALAMAT, siswa.getAlamat());
        values.put(COLUMN_JURUSAN, siswa.getJurusan());
        values.put(COLUMN_GENDER, siswa.getGender());

        // Insert data and return the row ID or -1 if insert failed
        long result = db.insert(TABLE_SISWA, null, values);
        db.close();
        return result;
    }

    public List<Siswa> getAllSiswa() {
        List<Siswa> siswaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_SISWA, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int namaIndex = cursor.getColumnIndex(COLUMN_NAMA);
                    int kelasIndex = cursor.getColumnIndex(COLUMN_KELAS);
                    int tempatLahirIndex = cursor.getColumnIndex(COLUMN_TEMPAT_LAHIR);
                    int tanggalLahirIndex = cursor.getColumnIndex(COLUMN_TANGGAL_LAHIR);
                    int alamatIndex = cursor.getColumnIndex(COLUMN_ALAMAT);
                    int jurusanIndex = cursor.getColumnIndex(COLUMN_JURUSAN);
                    int genderIndex = cursor.getColumnIndex(COLUMN_GENDER);

                    if (idIndex != -1 && namaIndex != -1 && kelasIndex != -1 && tempatLahirIndex != -1
                            && tanggalLahirIndex != -1 && alamatIndex != -1 && jurusanIndex != -1 && genderIndex != -1) {

                        int id = cursor.getInt(idIndex);
                        String nama = cursor.getString(namaIndex);
                        int kelasId = cursor.isNull(kelasIndex) ? -1 : cursor.getInt(kelasIndex);
                        String tempatLahir = cursor.isNull(tempatLahirIndex) ? "" : cursor.getString(tempatLahirIndex);
                        String tanggalLahir = cursor.isNull(tanggalLahirIndex) ? "" : cursor.getString(tanggalLahirIndex);
                        String alamat = cursor.isNull(alamatIndex) ? "" : cursor.getString(alamatIndex);
                        String jurusan = cursor.isNull(jurusanIndex) ? "" : cursor.getString(jurusanIndex);
                        String gender = cursor.isNull(genderIndex) ? "" : cursor.getString(genderIndex);

                        siswaList.add(new Siswa(id, nama, kelasId, tempatLahir, tanggalLahir, alamat, jurusan, gender));
                    } else {
                        Log.e("DatabaseHelper", "Column index not found.");
                    }
                } while (cursor.moveToNext());
            } else {
                Log.e("DatabaseHelper", "Cursor is null or empty.");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error reading siswa data", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return siswaList;
    }



    // === Fungsi untuk meng-update data siswa ===
    public boolean updateSiswa(Siswa siswa) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues to hold updated data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, siswa.getNama());
        values.put(COLUMN_KELAS, siswa.getKelasId());  // Menggunakan getKelasId() alih-alih getKelas()
        values.put(COLUMN_TEMPAT_LAHIR, siswa.getTempatLahir());
        values.put(COLUMN_TANGGAL_LAHIR, siswa.getTanggalLahir());
        values.put(COLUMN_ALAMAT, siswa.getAlamat());
        values.put(COLUMN_JURUSAN, siswa.getJurusan());
        values.put(COLUMN_GENDER, siswa.getGender());

        // Update record where ID matches
        int rowsAffected = db.update(TABLE_SISWA, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(siswa.getId())});
        db.close();

        return rowsAffected > 0; // Return true if one or more rows were affected
    }

    // Method to get a student by ID
    public Siswa getSiswaById(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Siswa siswa = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    TABLE_SISWA,  // Use the constant for the table name
                    new String[]{COLUMN_ID, COLUMN_NAMA, COLUMN_KELAS, COLUMN_TEMPAT_LAHIR, COLUMN_TANGGAL_LAHIR,
                            COLUMN_ALAMAT, COLUMN_JURUSAN, COLUMN_GENDER},
                    COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null
            );

            // Ensure cursor is not null and contains data
            if (cursor != null && cursor.moveToFirst()) {
                // Log column names for debugging
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    Log.d("DatabaseHelper", "Column: " + columnName);
                }

                // Fetch student data from the cursor
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                int namaIndex = cursor.getColumnIndexOrThrow(COLUMN_NAMA);
                int kelasIndex = cursor.getColumnIndexOrThrow(COLUMN_KELAS);
                int tempatLahirIndex = cursor.getColumnIndexOrThrow(COLUMN_TEMPAT_LAHIR);
                int tanggalLahirIndex = cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_LAHIR);
                int alamatIndex = cursor.getColumnIndexOrThrow(COLUMN_ALAMAT);
                int jurusanIndex = cursor.getColumnIndexOrThrow(COLUMN_JURUSAN);
                int genderIndex = cursor.getColumnIndexOrThrow(COLUMN_GENDER);

                // Convert kelas value from String to Integer
                int kelasId = Integer.parseInt(cursor.getString(kelasIndex));  // Convert String to int

                // Create a new Siswa object
                siswa = new Siswa(
                        cursor.getInt(idIndex),
                        cursor.getString(namaIndex),
                        kelasId,  // Pass the converted integer
                        cursor.getString(tempatLahirIndex),
                        cursor.getString(tanggalLahirIndex),
                        cursor.getString(alamatIndex),
                        cursor.getString(jurusanIndex),
                        cursor.getString(genderIndex)
                );
            } else {
                Log.e("DatabaseHelper", "Cursor is null or empty.");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while fetching data", e);
        } finally {
            // Ensure the cursor and database are closed properly in the finally block
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return siswa;  // Return the Siswa object, which may be null if no data was found
    }


    // ==== FUNGSI UNTUK KELAS ====

    // Fungsi untuk menambahkan kelas
    public long addKelas(Kelas kelas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_KELAS, kelas.getNamaKelas());
        values.put(COLUMN_NAMA_DOSEN, kelas.getNamaDosen());

        long id = db.insert(TABLE_KELAS, null, values);
        db.close();
        return id;
    }

    // Fungsi untuk mendapatkan semua kelas
    public List<Kelas> getAllKelas() {
        List<Kelas> kelasList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            cursor = db.query(TABLE_KELAS,
                    new String[]{COLUMN_KELAS_ID, COLUMN_NAMA_KELAS, COLUMN_NAMA_DOSEN},
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(COLUMN_KELAS_ID);
                    int namaKelasIndex = cursor.getColumnIndex(COLUMN_NAMA_KELAS);
                    int namaDosenIndex = cursor.getColumnIndex(COLUMN_NAMA_DOSEN);

                    if (idIndex != -1 && namaKelasIndex != -1 && namaDosenIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String namaKelas = cursor.getString(namaKelasIndex);
                        String namaDosen = cursor.getString(namaDosenIndex);

                        Kelas kelas = new Kelas(id, namaKelas, namaDosen);
                        kelasList.add(kelas);
                    } else {
                        Log.e("DatabaseHelper", "One or more columns are missing in the cursor");
                    }
                } while (cursor.moveToNext());
            } else {
                Log.d("DatabaseHelper", "No classes found or cursor is empty");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching kelas", e);
        } finally {
            // Pastikan cursor dan database ditutup
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return kelasList;
    }

    // Method tambahan untuk konversi langsung ke List<String>
    public List<String> getAllKelasNames() {
        List<Kelas> kelasList = getAllKelas();
        List<String> kelasNamesList = new ArrayList<>();

        for (Kelas kelas : kelasList) {
            // Tambahkan id, nama kelas, dan nama dosen
            kelasNamesList.add(kelas.getId() + "-" + kelas.getNamaKelas() + " (Dosen: " + kelas.getNamaDosen() + ")");
        }

        return kelasNamesList;
    }

    public long deleteKelas(int kelasId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Menghapus kelas berdasarkan kelas_id
        long result = db.delete(TABLE_KELAS, COLUMN_KELAS_ID + " = ?", new String[]{String.valueOf(kelasId)});
        db.close();
        return result; // Mengembalikan jumlah baris yang terhapus
    }

    // === Fungsi untuk meng-update data kelas ===
    public boolean updateKelas(Kelas kelas) {
        // Get writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues to hold the updated data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_KELAS, kelas.getNamaKelas());
        values.put(COLUMN_NAMA_DOSEN, kelas.getNamaDosen());

        // Perform the update operation on the database where the ID matches
        int rowsAffected = db.update(TABLE_KELAS, values, COLUMN_KELAS_ID + " = ?",
                new String[]{String.valueOf(kelas.getId())});

        // Close the database connection after the operation is complete
        db.close();

        // Return true if one or more rows were affected, meaning the update was successful
        return rowsAffected > 0;
    }

    // Method to get Kelas by ID
    public Kelas getKelasById(int kelasId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Kelas kelas = null;

        Cursor cursor = db.query(
                TABLE_KELAS, // Gunakan konstanta untuk nama tabel
                new String[]{COLUMN_KELAS_ID, COLUMN_NAMA_KELAS, COLUMN_NAMA_DOSEN},
                COLUMN_KELAS_ID + "=?",
                new String[]{String.valueOf(kelasId)},
                null, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KELAS_ID));
                    String namaKelas = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_KELAS));
                    String namaDosen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_DOSEN));

                    kelas = new Kelas(id, namaKelas, namaDosen);
                }
            } finally {
                cursor.close(); // Ensure cursor is closed in the finally block
            }
        }

        db.close(); // Close database connection
        return kelas; // Return kelas object, even if it's null
    }


    public List<Siswa> getSiswaByKelas(int kelasId) {
        List<Siswa> siswaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SISWA + " WHERE " + COLUMN_KELAS + "=?", new String[]{String.valueOf(kelasId)});

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int namaIndex = cursor.getColumnIndex(COLUMN_NAMA);
            int jurusanIndex = cursor.getColumnIndex(COLUMN_JURUSAN);
            int genderIndex = cursor.getColumnIndex(COLUMN_GENDER);

            if (idIndex != -1 && namaIndex != -1 && jurusanIndex != -1 && genderIndex != -1) {
                do {
                    Siswa siswa = new Siswa();
                    siswa.setId(cursor.getInt(idIndex));
                    siswa.setNama(cursor.getString(namaIndex));
                    siswa.setJurusan(cursor.getString(jurusanIndex));
                    siswa.setGender(cursor.getString(genderIndex));
                    siswaList.add(siswa);
                } while (cursor.moveToNext());
            } else {
                Log.e("DatabaseError", "One or more columns not found in Cursor.");
            }
        }
        cursor.close();
        return siswaList;
    }

    // ==== FUNGSI UNTUK ABSENSI ====
    // Insert Absensi Data
    public long insertAbsensi(int siswaId, String tanggal, String hari, String keterangan) {
        SQLiteDatabase db = this.getWritableDatabase();  // Mendapatkan database yang dapat ditulis
        ContentValues values = new ContentValues();

        // Menambahkan nilai ke ContentValues
        values.put(DatabaseHelper.COLUMN_SISWA_ID_FK, siswaId);
        values.put(DatabaseHelper.COLUMN_TANGGAL, tanggal);
        values.put(DatabaseHelper.COLUMN_HARI, hari);
        values.put(DatabaseHelper.COLUMN_KETERANGAN, keterangan);

        // Menyisipkan data ke tabel absen
        long id = db.insert(DatabaseHelper.TABLE_ABSEN, null, values);  // Lakukan operasi penyisipan

        db.close();  // Menutup koneksi database
        return id;  // Mengembalikan ID yang disisipkan (absen_id)
    }



    // Method to fetch attendance data based on a specific date
    public List<HashMap<String, String>> getDataByTanggal(String tanggal) {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                "s." + COLUMN_NAMA + " AS nama, " +
                "s." + COLUMN_JURUSAN + " AS jurusan, " +
                "s." + COLUMN_GENDER + " AS gender, " +
                "a." + COLUMN_KETERANGAN + " AS keterangan " +
                "FROM " + TABLE_ABSEN + " a " +
                "JOIN " + TABLE_SISWA + " s " +
                "ON a." + COLUMN_SISWA_ID_FK + " = s." + COLUMN_ID + " " +
                "WHERE a." + COLUMN_TANGGAL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{tanggal});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("nama", cursor.getString(cursor.getColumnIndexOrThrow("nama")));
                map.put("jurusan", cursor.getString(cursor.getColumnIndexOrThrow("jurusan")));
                map.put("gender", cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                map.put("keterangan", cursor.getString(cursor.getColumnIndexOrThrow("keterangan")));
                dataList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }
    public boolean updateKeterangan(int siswaId, String tanggal, String keterangan) {
        SQLiteDatabase db = null;
        boolean isSuccess = false;

        try {
            db = this.getWritableDatabase();

            // Membuat konten yang akan diupdate
            ContentValues values = new ContentValues();
            values.put(COLUMN_KETERANGAN, keterangan);

            // Melakukan update berdasarkan siswa_id dan tanggal
            String whereClause = COLUMN_SISWA_ID_FK + " = ? AND " + COLUMN_TANGGAL + " = ?";
            String[] whereArgs = { String.valueOf(siswaId), tanggal };

            int rowsAffected = db.update(TABLE_ABSEN, values, whereClause, whereArgs);
            isSuccess = rowsAffected > 0; // Pembaruan berhasil jika ada baris yang diperbarui
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close(); // Pastikan database selalu ditutup
            }
        }

        return isSuccess;
    }
    public boolean isAbsensiExist(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_ABSEN + " WHERE " + COLUMN_TANGGAL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});

        boolean exists = false;
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                exists = true;  // Data absensi ada untuk tanggal ini
            }
        }
        cursor.close();
        return exists;
    }

}
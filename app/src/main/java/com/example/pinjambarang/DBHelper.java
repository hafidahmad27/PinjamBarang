package com.example.pinjambarang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String database_name = "db_peminjaman";
    public static final String table_name = "tb_peminjaman";

    public static final String row_id = "_id";
    public static final String row_nama = "Nama";
    public static final String row_barang = "Barang";
    public static final String row_keperluan = "Keperluan";
    public static final String row_tglPinjam = "tanggal_pinjam";
    public static final String row_tglKembali = "tanggal_kembali";

    private SQLiteDatabase db;

    public DBHelper(Context context){
        super(context, database_name, null, 2);
        db = getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db){
        String query = "CREATE TABLE "+table_name+"("+row_id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +row_nama+" TEXT,"
                +row_barang+" TEXT,"
                +row_keperluan+" TEXT,"
                +row_tglPinjam+" TEXT,"
                +row_tglKembali+" TEXT)";
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int x){
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
    }

    public Cursor allData(){
        Cursor curs = db.rawQuery("SELECT * FROM "+table_name, null);
        return curs;
    }

    public Cursor oneData(Long id){
        Cursor curs = db.rawQuery("SELECT * FROM "+table_name+" WHERE "+ row_id+"="+id, null);
        return curs;
    }

    public void insertData(ContentValues values){
        db.insert(table_name, null, values);
    }

    public void updateData(ContentValues values, long id){
        db.update(table_name, values, row_id+"="+id, null);
    }

    public void deleteData(long id){
        db.delete(table_name, row_id+"="+id, null);
    }
}
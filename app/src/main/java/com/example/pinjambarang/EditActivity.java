package com.example.pinjambarang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    DBHelper helper;
    EditText etNama, etKeperluan, etTglPinjam, etTglKembali, etStatus;
    Spinner spListBarang;
    Button btnPengembalian;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelper(this);
        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        etNama = (EditText) findViewById(R.id.etNama_Edit);
        spListBarang = (Spinner) findViewById(R.id.spListBarang_Edit);
        etKeperluan = (EditText) findViewById(R.id.etKeperluan_Edit);
        etTglPinjam = (EditText) findViewById(R.id.etTglPinjam_Edit);
        etTglKembali = (EditText) findViewById(R.id.etTglKembali_Edit);
        etStatus = (EditText) findViewById(R.id.etStatus_Edit);
        btnPengembalian = (Button) findViewById(R.id.btnPengembalian_Edit);

        Calendar cal = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        etTglPinjam.setText(dateFormatter.format(cal.getTime()));
        etTglKembali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog();
            }
        });
        btnPengembalian.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Cursor cursor = helper.oneData(id);
                if (cursor.moveToFirst()) {
                    String tglKembali = etTglKembali.getText().toString().trim();
                    String status = "Dikembalikan";

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setMessage("Yakin ingin memproses pengembalian?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put(DBHelper.row_tglKembali, tglKembali);
                            values.put(DBHelper.row_status, status);
                            helper.updateData(values, id);

                            Toast.makeText(EditActivity.this, "Berhasil memproses pengembalian", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        getData();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                etTglKembali.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if (cursor.moveToFirst()){
            String nama = cursor.getString(cursor.getColumnIndex(DBHelper.row_nama));
            String barang = cursor.getString(cursor.getColumnIndex(DBHelper.row_barang));
            String keperluan = cursor.getString(cursor.getColumnIndex(DBHelper.row_keperluan));
            String tglPinjam = cursor.getString(cursor.getColumnIndex(DBHelper.row_tglPinjam));
            String tglKembali = cursor.getString(cursor.getColumnIndex(DBHelper.row_tglKembali));
            String status = cursor.getString(cursor.getColumnIndex(DBHelper.row_status));

            etNama.setText(nama);

            if (barang.equals("Tablet")){
                spListBarang.setSelection(0);
            } else if (barang.equals("Laptop")){
                spListBarang.setSelection(1);
            }

            etKeperluan.setText(keperluan);
            etTglPinjam.setText(tglPinjam);
            etTglKembali.setText(tglKembali);
            etStatus.setText(status);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        String status = etStatus.getText().toString().trim();

        if (status.equals("Dikembalikan")){
            etNama.setEnabled(false);
            spListBarang.setEnabled(false);
            etKeperluan.setEnabled(false);
            etTglKembali.setEnabled(false);
            btnPengembalian.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_edit){
            String nama = etNama.getText().toString().trim();
            String listBarang = spListBarang.getSelectedItem().toString().trim();
            String keperluan = etKeperluan.getText().toString().trim();
            String tglPinjam = etTglPinjam.getText().toString().trim();

            ContentValues values = new ContentValues();
            values.put(DBHelper.row_nama, nama);
            values.put(DBHelper.row_barang, listBarang);
            values.put(DBHelper.row_keperluan, keperluan);
            values.put(DBHelper.row_tglPinjam, tglPinjam);

            if (nama.equals("") || listBarang.equals("") || keperluan.equals("") || tglPinjam.equals("")){
                Toast.makeText(EditActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT);
            } else {
                helper.updateData(values, id);
                Toast.makeText(EditActivity.this, "Data Terupdate", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (item.getItemId() == R.id.delete_edit){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
            builder.setMessage("Data ini akan dihapus.");
            builder.setCancelable(true);
            builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    helper.deleteData(id);
                    Toast.makeText(EditActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.pinjambarang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    DBHelper helper;
    EditText etNomor, etNama, etKeterangan, etTglPinjam, etStatus;
    Spinner spListBarang;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelper(this);
        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        etNomor = (EditText) findViewById(R.id.etNomor_Edit);
        etNama = (EditText) findViewById(R.id.etNama_Edit);
        spListBarang = (Spinner) findViewById(R.id.spListBarang_Edit);
        etKeterangan = (EditText) findViewById(R.id.etKeterangan_Edit);
        etTglPinjam = (EditText) findViewById(R.id.etTglPinjam_Edit);
        etStatus = (EditText) findViewById(R.id.etStatus_Edit);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        etTglPinjam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog();
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
                etTglPinjam.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if (cursor.moveToFirst()){
            String nomor = cursor.getString(cursor.getColumnIndex(DBHelper.row_nomor));
            String nama = cursor.getString(cursor.getColumnIndex(DBHelper.row_nama));
            String barang = cursor.getString(cursor.getColumnIndex(DBHelper.row_barang));
            String keterangan = cursor.getString(cursor.getColumnIndex(DBHelper.row_keterangan));
            String tglPinjam = cursor.getString(cursor.getColumnIndex(DBHelper.row_tglPinjam));
            String status = cursor.getString(cursor.getColumnIndex(DBHelper.row_status));

            etNomor.setText(nomor);
            etNama.setText(nama);

            if (barang.equals("Tablet")){
                spListBarang.setSelection(0);
            } else if (barang.equals("Laptop")){
                spListBarang.setSelection(1);
            }

            etKeterangan.setText(keterangan);
            etTglPinjam.setText(tglPinjam);
            etStatus.setText(status);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_edit){
                String nomor = etNomor.getText().toString().trim();
                String nama = etNama.getText().toString().trim();
                String listBarang = spListBarang.getSelectedItem().toString().trim();
                String keterangan = etKeterangan.getText().toString().trim();
                String tglPinjam = etTglPinjam.getText().toString().trim();
                String status = etStatus.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_nomor, nomor);
                values.put(DBHelper.row_nama, nama);
                values.put(DBHelper.row_barang, listBarang);
                values.put(DBHelper.row_keterangan, keterangan);
                values.put(DBHelper.row_tglPinjam, tglPinjam);
                values.put(DBHelper.row_status, status);

                if (nomor.equals("") || nama.equals("") || listBarang.equals("") || keterangan.equals("") || tglPinjam.equals("") || status.equals("")){
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
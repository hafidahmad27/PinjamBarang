package com.example.pinjambarang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    DBHelper helper;
    EditText etNomor, etNama, etKeterangan, etTglPinjam, etStatus;
    Spinner spListBarang;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        etNomor = (EditText) findViewById(R.id.etNomor_Add);
        etNama = (EditText) findViewById(R.id.etNama_Add);
        spListBarang = (Spinner) findViewById(R.id.spListBarang_Add);
        etKeterangan = (EditText) findViewById(R.id.etKeterangan_Add);
        etTglPinjam = (EditText) findViewById(R.id.etTglPinjam_Add);
        etStatus = (EditText) findViewById(R.id.etStatus_Add);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        etTglPinjam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog();
            }
        });
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_add){
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
                    Toast.makeText(AddActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    helper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
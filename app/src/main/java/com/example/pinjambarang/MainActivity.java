package com.example.pinjambarang;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pinjambarang.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    DBHelper helper;
    LayoutInflater inflater;
    View dialogView;
    TextView tvNama, tvBarang, tvKeperluan, tvTglPinjam, tvTglKembali, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        helper = new DBHelper(this);
        listView = (ListView)findViewById(R.id.list_data);
        listView.setOnItemClickListener(this);
    }

    public void setListView(){
        Cursor cursor = helper.allData();
        Adapter adpt = new Adapter(this, cursor, 1);
        listView.setAdapter(adpt);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long x) {
        TextView getId = (TextView)view.findViewById(R.id.listID);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pilih Opsi");

        String[] options = {"Detail", "Edit", "Delete"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        final AlertDialog.Builder viewData = new AlertDialog.Builder(MainActivity.this);
                        inflater = getLayoutInflater();
                        dialogView = inflater.inflate(R.layout.view_data, null);
                        viewData.setView(dialogView);
                        viewData.setTitle("Detail Data");

                        tvNama = (TextView) dialogView.findViewById(R.id.tvNama);
                        tvBarang = (TextView) dialogView.findViewById(R.id.tvBarang);
                        tvKeperluan = (TextView) dialogView.findViewById(R.id.tvKeperluan);
                        tvTglPinjam = (TextView) dialogView.findViewById(R.id.tvTglPinjam);
                        tvTglKembali = (TextView) dialogView.findViewById(R.id.tvTglKembali);
                        tvStatus = (TextView) dialogView.findViewById(R.id.tvStatus);

                        tvNama      .setText("Nama Peminjam\t\t\t\t\t: " + cur.getString(cur.getColumnIndex(DBHelper.row_nama)));
                        tvBarang    .setText("Barang Yg Dipinjam\t\t: " + cur.getString(cur.getColumnIndex(DBHelper.row_barang)));
                        tvKeperluan .setText("Keperluan\t\t\t\t\t\t\t\t\t\t\t\t: " + cur.getString(cur.getColumnIndex(DBHelper.row_keperluan)));
                        tvTglPinjam .setText("Tgl. Pinjam\t\t\t\t\t\t\t\t\t\t\t: " + cur.getString(cur.getColumnIndex(DBHelper.row_tglPinjam)));
                        tvTglKembali.setText("Tgl. Kembali\t\t\t\t\t\t\t\t\t\t: " + cur.getString(cur.getColumnIndex(DBHelper.row_tglKembali)));
                        tvStatus.    setText("Status\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t: " + cur.getString(cur.getColumnIndex(DBHelper.row_status)));

                        viewData.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewData.show();
                }
                switch (which) {
                    case 1:
                        Intent iddata = new Intent(MainActivity.this, EditActivity.class);
                        iddata.putExtra(DBHelper.row_id, id);
                        startActivity(iddata);
                }
                switch (which) {
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Data ini akan dihapus");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(MainActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }
}
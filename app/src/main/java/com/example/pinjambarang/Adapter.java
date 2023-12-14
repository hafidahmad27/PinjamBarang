package com.example.pinjambarang;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Adapter extends CursorAdapter {
    private LayoutInflater layoutInflater;
    public Adapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup){
        View v = layoutInflater.inflate(R.layout.row_data, viewGroup, false);
        MyHolder holder = new MyHolder();
        holder.listID = (TextView)v.findViewById(R.id.listID);
        holder.listBarang = (TextView) v.findViewById(R.id.listBarang);
        holder.listNama = (TextView)v.findViewById(R.id.listNama);
        holder.listTglPinjam = (TextView)v.findViewById(R.id.listTglPinjam);
        holder.listTglKembali = (TextView)v.findViewById(R.id.listTglKembali);
        holder.listStatus = (TextView)v.findViewById(R.id.listStatus);
        v.setTag(holder);
        return v;
    }

    public void bindView(View view, Context context, Cursor cursor){
        MyHolder holder = (MyHolder)view.getTag();

        holder.listID.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_id)));
        holder.listBarang.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_barang)));
        holder.listNama.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_nama)));
        holder.listTglPinjam.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_tglPinjam)));
        holder.listTglKembali.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_tglKembali)));
        holder.listStatus.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_status)));
    }

    class MyHolder {
        TextView listID;
        TextView listBarang;
        TextView listNama;
        TextView listTglPinjam;
        TextView listTglKembali;
        TextView listStatus;
    }
}
package com.example.pinjambarang;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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
        holder.listNama = (TextView)v.findViewById(R.id.listNama);
        v.setTag(holder);
        return v;
    }

    public void bindView(View view, Context context, Cursor cursor){
        MyHolder holder = (MyHolder)view.getTag();

        holder.listID.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_id)));
        holder.listNama.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_nama)));
    }

    class MyHolder{
        TextView listID;
        TextView listNama;
    }
}
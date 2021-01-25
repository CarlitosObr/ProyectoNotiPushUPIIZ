package com.example.notipushupiiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    Context contexto;
    ArrayList<notificacion> notificaciones;
    private int diseno;
    private static final String url = "https://image.flaticon.com/icons/png/512/1342/1342316.png";

    public Adaptador(Context contexto, ArrayList<notificacion> notificaciones, int diseno) {
        this.contexto = contexto;
        this.notificaciones = notificaciones;
        this.diseno = diseno;
    }
    @Override
    public int getCount() {
        return notificaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return notificaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vD = convertView;
        if (vD == null) {
            LayoutInflater li = LayoutInflater.from(contexto);
            vD = li.inflate(diseno, null);
        }
        ImageView im = vD.findViewById(R.id.iv1);
        Picasso.get()
                .load(url)
                .resize(40,40)
                .centerCrop()
                .into(im);
        TextView nombre = vD.findViewById(R.id.name);
        nombre.setText(notificaciones.get(position).getTitulo());
        return vD;
    }
}

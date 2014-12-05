package com.example.aitor.inmobiliaria;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aitor on 02/12/2014.
 */
public class AdaptdorInmueble extends ArrayAdapter<Inmueble> {

    private Context contexto;
    private int recurso;
    private ArrayList<Inmueble> lista;
    private LayoutInflater i;


    public AdaptdorInmueble(Context context, int resource, ArrayList<Inmueble> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.recurso = resource;
        this.lista = objects;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        public TextView tvlocalidad, tvdireccion, tvprecio;
        public ImageView img;
        public int posicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("LOG", lista.size() + "");
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.img = (ImageView) convertView.findViewById(R.id.imagen);
            vh.tvlocalidad = (TextView) convertView.findViewById(R.id.localidad);
            vh.tvdireccion = (TextView) convertView.findViewById(R.id.direccion);
            vh.tvprecio = (TextView) convertView.findViewById(R.id.precio);

            convertView.setTag(vh);
        } else {

            vh = (ViewHolder) convertView.getTag();
        }

        Inmueble obj = (Inmueble) getItem(position);
        vh.posicion = position;
        if(obj.getTipo().compareTo("Casa")==0) {
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.casa));
        }else if(obj.getTipo().compareTo("Piso")==0) {
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.piso));
        }else if(obj.getTipo().compareTo("Local")==0) {
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.local));
        }else if(obj.getTipo().compareTo("Cochera")==0) {
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.cochera));
        }
        vh.tvlocalidad.setText(obj.getLocalidad() + "");
        vh.tvdireccion.setText(obj.getDireccion() + "");
        vh.tvprecio.setText(obj.getPrecio() + "");
        return convertView;
    }
}

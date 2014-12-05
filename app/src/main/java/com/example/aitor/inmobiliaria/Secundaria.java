package com.example.aitor.inmobiliaria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;


public class Secundaria extends Activity {
private FragmentoFotos fdos;
    private int mover=0;
    private ImageView img;
private ArrayList <String> imagenesParaMostrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundaria);
        fdos=(FragmentoFotos)getFragmentManager().findFragmentById(R.id.fragment4);
        img=(ImageView)findViewById(R.id.imageView);
        Inmueble obj =(Inmueble)getIntent().getParcelableExtra("inmueble");
        listarImg(obj.getLocalidad(), obj.getId());
    }
    public  void listarImg(String localidad,String id){
        imagenesParaMostrar=new ArrayList<String>();
        File f = new File(getExternalFilesDir(null),"");
        File file[] = f.listFiles();
        int si=0;
        String nombreArchivo=localidad+"_"+id;
        Log.d("Files", "Size: " + file.length);
        for (int i=0; i < file.length; i++)
        {
            if(nombreArchivo.compareTo(file[i].getName().substring(0,nombreArchivo.length()))==0) {
                imagenesParaMostrar.add(file[i].getName());
            }
        }
        mostrarEnImageView(imagenesParaMostrar);
    }

    public void atras(View v){
        mover--;
        mostrarEnImageView(imagenesParaMostrar);
    }
    public void delante(View v){
        Log.v("hola","hola");
        mover++;
        mostrarEnImageView(imagenesParaMostrar);
    }

    public void mostrarEnImageView(ArrayList<String>imagenes){

        if(mover>imagenes.size()-1 || mover<0) {
            mover=0;
            img.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir(null) + "/" + imagenes.get(mover)));
        }else{
            img.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir(null) + "/" + imagenes.get(mover)));
        }

    }




}

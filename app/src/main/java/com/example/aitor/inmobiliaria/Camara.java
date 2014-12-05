package com.example.aitor.inmobiliaria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by aitor on 05/12/2014.
 */
public class Camara extends Activity {

    private static final int CAMARA=0;
    String comprobarLocalidad,idinmueble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camara();
    }

    public void camara(){
            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMARA);
        //comprobarLocalidad=localidad;
        //idinmueble=id;
    }



    public void onActivityResult(int pet, int res, Intent data) {
        if (res == RESULT_OK && pet == CAMARA) {
            String fecha="";
            Bitmap foto = (Bitmap)data.getExtras().get("data");
            FileOutputStream salida;
            Calendar c = Calendar.getInstance();
            fecha += c.get(Calendar.YEAR)+"_";fecha+=c.get(Calendar.MONTH)+"_";fecha+=c.get(Calendar.DATE)+"_";
            fecha+=c.get(Calendar.HOUR)+"_";fecha+=c.get(Calendar.MINUTE)+"_";fecha+=c.get(Calendar.SECOND)+".";
            try {
                salida = new FileOutputStream(getExternalFilesDir(null)+"/"+comprobarLocalidad+"_"+idinmueble+"_"+fecha+"jpg");
                foto.compress(Bitmap.CompressFormat.JPEG, 90,salida);
                mostrarNombreImagen(getExternalFilesDir(null)+"/"+comprobarLocalidad+"_"+idinmueble+"_"+fecha+"jpg");
            } catch (FileNotFoundException e) {
            }
        }
    }

    public void mostrarNombreImagen(String s){
        Insertar i=new Insertar();
        String verNombre[]=s.split("/");
    //    i.setText(verNombre[verNombre.length-1]+"\n");
    }
}

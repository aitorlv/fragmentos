package com.example.aitor.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Collections;


public class Insertar extends Activity {
 private String idinmueble,comprobarLocalidad,tipo;
 private EditText etlocalidad,etdireccion,etprecio;
 private Spinner lista;
 private TextView tvimagen;
 private static final int CAMARA=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_insertar);
        Bundle b =this.getIntent().getExtras();
        if(b!=null) {
            idinmueble=b.getString("id");
        }
        if(savedInstanceState!=null){
            tvimagen.append(savedInstanceState.getString("imagenes"));
        }
        lista=(Spinner)findViewById(R.id.tipoin);
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lista.setAdapter(adapterTipo);

        lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipo=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void insertar(View v){
        etlocalidad=(EditText)findViewById(R.id.localidadin);
        etdireccion=(EditText)findViewById(R.id.direccionin);
        etprecio=(EditText)findViewById(R.id.precioin);
        String localidad,direccion,precio;
        Inmueble obj;
        localidad=etlocalidad.getText().toString();
        direccion=etdireccion.getText().toString();
        precio=etprecio.getText().toString()+"€";
        obj=new Inmueble(idinmueble,localidad,direccion,tipo,precio);

        try {
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("obj", obj);
            i.putExtra("inmueble",bundle);
            setResult(Activity.RESULT_OK, i);

        }catch (InternalError e){
            Intent i=new Intent();
            setResult(Activity.RESULT_CANCELED,i);
        }
        finish();
    }

    public void cancelar(View v){

            Intent i = new Intent();
            setResult(Activity.RESULT_CANCELED, i);
            finish();

    }


    public void abrirCamara(View v){
        etlocalidad=(EditText)findViewById(R.id.localidadin);
        comprobarLocalidad=etlocalidad.getText().toString();
        if(comprobarLocalidad.compareTo("")!=0) {
            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMARA);
        }else{
            Toast.makeText(this,"Rellena los datos antes",Toast.LENGTH_SHORT).show();
        }
    }

   /* public void setText(String s){
        tvimagen=(TextView)findViewById(R.id.tvimagen);
        tvimagen.setText(s);
    }*/

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
        tvimagen=(TextView)findViewById(R.id.tvimagen);
        String verNombre[]=s.split("/");
        tvimagen.append(verNombre[verNombre.length-1]+"\n");
    }

}

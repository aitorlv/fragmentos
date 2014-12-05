package com.example.aitor.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;


public class Modificar extends Activity {
    private EditText etmod1, etmod2, etmod3, etmod4;
    private String id,tipo;
    private TextView tvimagen;
    private Spinner lista;
    private String comprobarLocalidad;
    private static final int CAMARA=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        Bundle b = this.getIntent().getParcelableExtra("objetoinmueble");
        Inmueble obj=null;
        if (b != null) {
            obj = b.getParcelable("obj");
            mostrarValores(obj);
        }
         String[] valorestipo=new String[]{"Casa","Piso","Local","Cochera"};

        lista=(Spinner)findViewById(R.id.tipomod);
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lista.setAdapter(adapterTipo);
        for(int i=0;i<valorestipo.length;i++){
            if(valorestipo[i].compareTo(obj.getTipo())==0){
                lista.setSelection(i);
            }
        }
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


    public void mostrarValores(Inmueble obj) {
        id=obj.getId();
        etmod1 = (EditText) findViewById(R.id.localidadmod);
        etmod2 = (EditText) findViewById(R.id.direccionmod);
        etmod4 = (EditText) findViewById(R.id.preciomod);
        etmod1.setText(obj.getLocalidad().toString());
        etmod2.setText(obj.getDireccion().toString());
        etmod4.setText(obj.getPrecio().toString());

    }

    public void modificar(View v) {
        String localidad,direccion,precio;
        Inmueble obj;
        localidad = etmod1.getText().toString();
        direccion = etmod2.getText().toString();
        precio = etmod4.getText().toString();

          obj = new Inmueble(id,localidad, direccion, tipo,precio);

            try {
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", obj);
                i.putExtra("inmueble",bundle);
                setResult(Activity.RESULT_OK, i);

            }catch (InternalError e){
                Intent i=new Intent();
                setResult(Activity.RESULT_OK,i);
            }
            finish();

        }



    public void cancelar(View v){
        Intent i=new Intent();
        setResult(Activity.RESULT_CANCELED,i);
        finish();
    }

    public void camara(View v){
        comprobarLocalidad=etmod1.getText().toString();
        if(comprobarLocalidad.compareTo("")!=0) {
            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMARA);
        }else{
            Toast.makeText(this, "Rellena los datos antes", Toast.LENGTH_SHORT).show();
        }
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
                salida = new FileOutputStream(getExternalFilesDir(null)+"/"+comprobarLocalidad+"_"+id+"_"+fecha+"jpg");
                foto.compress(Bitmap.CompressFormat.JPEG, 90,salida);
                mostrarNombreImagen(getExternalFilesDir(null)+"/"+comprobarLocalidad+"_"+id+"_"+fecha+"jpg");
            } catch (FileNotFoundException e) {
            }
        }
    }

    public void mostrarNombreImagen(String s){
        tvimagen=(TextView)findViewById(R.id.tvmod);
        String verNombre[]=s.split("/");
        tvimagen.append(verNombre[verNombre.length-1]+"\n");
    }
}

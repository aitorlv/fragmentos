package com.example.aitor.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class Principal extends Activity {
    private static final int INSERTAR=0;
    private static final int MODIFICAR=1;
    private final int ACTIVIDAD2=2;
    private int indicemod;
    private FragmentoFotos fdos;
    private int mover=0;
    private ImageView img;
    private ArrayList <String> imagenesParaMostrar;
    private Context contexto;
    private ArrayList<Inmueble> valores=new ArrayList<Inmueble>();
    private AdaptdorInmueble ad=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("soy","oncreate");
        setContentView(R.layout.activity_principal);
        contexto=getApplicationContext();
       final ListView lv=(ListView)findViewById(R.id.listaInmueble);
            valores = Archivo.leerArchivo(getApplicationContext());


        ad=new AdaptdorInmueble(this,R.layout.detalle_inmueble,valores);
        lv.setAdapter(ad);


        fdos=(FragmentoFotos)getFragmentManager().findFragmentById(R.id.fragment3);
        final boolean horizontal=fdos!=null && fdos.isInLayout();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Inmueble t=(Inmueble)lv.getItemAtPosition(position);
                if(horizontal){
                    img=(ImageView)findViewById(R.id.imageView);
                    listarImg(t.getLocalidad(),t.getId());
                }else{
                    Intent i=new Intent(contexto,Secundaria.class);
                    i.putExtra("inmueble", t);
                    startActivityForResult(i,ACTIVIDAD2);
                }
            }
        });
        registerForContextMenu(lv);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("inmueble",valores);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_insertar) {
            insertarInmueble();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Object o = info.targetView.getTag();
        AdaptdorInmueble.ViewHolder vh = (AdaptdorInmueble.ViewHolder) o;
        if (id == R.id.action_modificar) {
            modificarItem(vh);
        } else if (id == R.id.action_borrar) {
            borrarItem(vh);
        }
        return super.onContextItemSelected(item);
    }



    public void insertarInmueble() {

        Intent intentInsertar = new Intent(this, Insertar.class);
        Bundle b=new Bundle();
        b.putString("id",idMayor()+"");
        intentInsertar.putExtras(b);
        startActivityForResult(intentInsertar, INSERTAR);
    }

    public void modificarItem(AdaptdorInmueble.ViewHolder vh){
        Intent i =new Intent(this,Modificar.class);
        Bundle b=new Bundle();
        indicemod=vh.posicion;
        Inmueble obj=(Inmueble)valores.get(vh.posicion);
        b.putParcelable("obj",obj);
        i.putExtra("objetoinmueble",b);
        startActivityForResult(i,MODIFICAR);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            Bundle b;
            Inmueble obj;
            switch (requestCode){
                case INSERTAR:
                    b=data.getParcelableExtra("inmueble");
                    obj=(Inmueble)b.getParcelable("obj");
                    tostada(obj+"");
                    valores.add(obj);
                    System.out.println(valores.size());
                    Collections.sort(valores);
                    ad.notifyDataSetChanged();
                    Archivo.guardarArchivo(getApplicationContext(), valores);
                    tostada("Datos insertados");
                    Log.v("soy","guardar");
                    break;
                case MODIFICAR:
                    b=data.getParcelableExtra("inmueble");
                    obj=(Inmueble)b.getParcelable("obj");
                    valores.set(indicemod,obj);
                    Collections.sort(valores);
                    ad.notifyDataSetChanged();
                    Archivo.guardarArchivo(getApplicationContext(), valores);
                    tostada("Datos modificados");
                    break;
                case ACTIVIDAD2:
                   // fdos.setTexto(data.getStringExtra("eres"));
            }
        }
    }

    public void borrarItem(final AdaptdorInmueble.ViewHolder vh){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        final View view=inflater.inflate(R.layout.dialogo_borrar,null);
        alert.setMessage("¿Borrar " + vh.tvdireccion.getText() + " de la lista?");
        alert.setView(view);
        //alert.setCancelable(false);
        alert.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valores.remove(vh.posicion);
                ad.notifyDataSetChanged();
                Archivo.guardarArchivo(getApplicationContext(), valores);
                tostada("Datos borrados");
            }
        });
        alert.setNegativeButton("Cancelar",null);
        alert.show();
    }

    public int idMayor(){
        int mayor=-1;
        Inmueble obj;
        for(int i=0;i<valores.size();i++){
            obj=valores.get(i);
            if(mayor<Integer.valueOf(obj.getId())){
                mayor=Integer.valueOf(obj.getId());
            }
        }
        return mayor+1;
    }

    public  void tostada(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
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

    public void mostrarEnImageView(ArrayList<String>imagenes) {

        if (mover > imagenes.size() - 1 || mover < 0) {
            mover = 0;
            img.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir(null) + "/" + imagenes.get(mover)));
        } else {
            img.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir(null) + "/" + imagenes.get(mover)));
        }
    }
}

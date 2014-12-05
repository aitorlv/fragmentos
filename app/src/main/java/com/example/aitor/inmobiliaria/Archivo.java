package com.example.aitor.inmobiliaria;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.example.aitor.inmobiliaria.Inmueble;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by aitor on 03/12/2014.
 */
public class Archivo {



    public static void guardarArchivo(Context contexto, ArrayList<Inmueble> valores) {
        FileOutputStream fxml = null;
        try {
            fxml = new FileOutputStream(new File(contexto.getExternalFilesDir(null), "archivo.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "Inmuebles");
            for (int i = 0; i < valores.size(); i++) {
                Inmueble obj = (Inmueble) valores.get(i);
                docxml.startTag(null, "Inmueble");
                docxml.startTag(null, "id");
                docxml.text(obj.getId());
                docxml.endTag(null, "id");
                docxml.startTag(null, "localidad");
                docxml.text(obj.getLocalidad());
                docxml.endTag(null, "localidad");
                docxml.startTag(null, "precio");
                docxml.text(obj.getPrecio());
                docxml.endTag(null, "precio");
                docxml.startTag(null, "direccion");
                docxml.text(obj.getDireccion());
                docxml.endTag(null, "direccion");
                docxml.startTag(null, "tipo");
                docxml.text(obj.getTipo());
                docxml.endTag(null, "tipo");
                docxml.endTag(null, "Inmueble");
            }
            docxml.endDocument();
            docxml.flush();
            fxml.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //LEER EL ARCHIVO

    //si tenemos permisos lee el archivo creado anterioromente
    public static ArrayList<Inmueble> leerArchivo(Context contexto) {
            String id="", localidad = "", precio = "", direccion = "", tipo = "",imagen;
            int radio = 0;
            ArrayList<Inmueble> valoresArchivo = new ArrayList<Inmueble>();
            XmlPullParser leerxml = Xml.newPullParser();
            try {
                leerxml.setInput(new FileInputStream(new File(contexto.getExternalFilesDir(null), "archivo.xml")), "utf-8");
                int evento = leerxml.getEventType();

                while (evento != XmlPullParser.END_DOCUMENT) {
                    if (evento == XmlPullParser.START_TAG) {
                        String etiqueta = leerxml.getName();
                        if(etiqueta.compareTo("id")==0){
                            id = leerxml.nextText();
                        }else if (etiqueta.compareTo("localidad") == 0) {
                            localidad = leerxml.nextText();
                        } else if (etiqueta.compareTo("precio") == 0) {
                            precio = leerxml.nextText();
                        } else if (etiqueta.compareTo("direccion") == 0) {
                            direccion = leerxml.nextText();
                        }else if (etiqueta.compareTo("tipo") == 0) {
                            tipo = leerxml.nextText();
                            valoresArchivo.add(new Inmueble(id,localidad, direccion, tipo,precio));
                        }

                    }
                    evento = leerxml.next();
                }


            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return valoresArchivo;

    }
}



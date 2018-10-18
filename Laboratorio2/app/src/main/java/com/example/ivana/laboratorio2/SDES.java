package com.example.ivana.laboratorio2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SDES extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    int opcion=0;
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL =1;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL =1;
    Button Ruta,Cifrar,Descifrar;
    EditText Llave;
    TextView MostrarRuta,MostrarPasos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdes);
        Ruta = (Button) findViewById(R.id.Boton_RutaSDES);
        Cifrar= (Button) findViewById(R.id.Boton_CifrarTextoSDES);
        Descifrar= (Button) findViewById(R.id.Boton_DescifrarTextoSDES);
        MostrarRuta = (TextView) findViewById(R.id.MostrarRutaSDES);
        MostrarPasos=(TextView) findViewById(R.id.TextView_MostrarCifradoSDES);
        Llave =(EditText) findViewById(R.id.Texto_Key);

        Ruta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //region Escoger ruta
                MostrarRuta.setText("");
                MostrarPasos.setText("");
                opcion=0;
                performFileSearch();
                //endregion
            }
        });

        Cifrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //region Cifrar
                MostrarRuta.setText("");
                MostrarPasos.setText("");
                opcion=1;
                performFileSearch();
                //endregion
            }
        });
        Descifrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //region Descifrar
                MostrarRuta.setText("");
                MostrarPasos.setText("");
                opcion=2;
                performFileSearch();
                //endregion
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);
                }
                String actual = "";

                //region Obtener ruta para almacenar archivo
                if (opcion == 0) {
                    Cifrado c = new Cifrado();
                    String ruta = "/storage/emulated/0/" + path;
                    String[] nuevaRuta = ruta.split("/");
                    for (int i = 0; i < nuevaRuta.length; i++) {

                        int tempo = nuevaRuta.length - 1;
                        if (tempo != i) {
                            actual += nuevaRuta[i] + "/";
                        }
                    }
                    c.CrearArchivo("/storage/emulated/0/Download/RutaArchivo.txt", actual);
                    MostrarRuta.setText("Ruta de almacenamiento de archivos: "+actual);
                }
                //endregion

                //region Cifrar
                if(opcion ==1)
                {
                    AlgortimoSDES s = new AlgortimoSDES(1);
                    String ruta = s.Leer();
                    String name= ("/storage/emulated/0/"+path);
                    String[] d = name.split("/");
                    String name2 = d[5].substring(0,d[5].length()-4);
                    String mensaje = Leer("/storage/emulated/0/" + path);
                    boolean condicion = s.ValidarKey(Llave.getText().toString());

                    if(Llave.getText().toString().length()==10&&ruta.length()>0&&mensaje.length()>0&&condicion==false)
                    {
                        try
                        {
                        int K = Integer.parseInt(Llave.getText().toString());
                        AlgortimoSDES sdes = new AlgortimoSDES(K);
                        String[] cifrado = new String[3];
                        cifrado =sdes.CifrarMensaje(K, mensaje);
                        MostrarPasos.setText("Mensaje original: "+mensaje+"\nPasos Cifrado: \n"+cifrado[2]+"\nMensaje Cifrado: "+ cifrado[0]);
                        String rutaSalida = ruta +name2+".scif";
                        sdes.CrearArchivo(rutaSalida,cifrado[0],cifrado[1]);
                        MostrarRuta.setText(rutaSalida);
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }

                    }
                    if(Llave.getText().toString().length()==0)
                    {
                        MostrarPasos.setText("Debe de ingresar una llave");
                    }

                    if(ruta.length()==0)
                    {
                        MostrarPasos.setText("Necesita una ruta de almacenamiento para guardar sus archivos");
                    }
                    if(Llave.getText().toString().length()> 0&&Llave.getText().toString().length()<10)
                    {
                        MostrarPasos.setText("La llave ingresada de "+ Llave.getText().toString().length()+"bits, ingrese una llave nuevamente de 10 bits.");
                        Llave.setText("");
                    }
                    if(Llave.getText().toString().length()>10)
                    {
                        MostrarPasos.setText("Llave demasiado grande, ingrese una nuevamente");
                        Llave.setText("");
                    }
                    if(condicion == true)
                    {
                        MostrarPasos.setText("La llave solo debe de contener 0 y 1");
                        Llave.setText("");
                    }

                }
                // endregion

                //region Descifrar
                if(opcion ==2)
                {
                    AlgortimoSDES s = new AlgortimoSDES(1);
                    String ruta = s.Leer();
                    String name= ("/storage/emulated/0/"+path);
                    String[] d = name.split("/");
                    String name2 = d[5].substring(0,d[5].length()-4);
                    String mensaje = Leer("/storage/emulated/0/" + path);
                    boolean condicion = s.ValidarKey(Llave.getText().toString());
                    if(Llave.getText().toString().length()==10&&ruta.length()>0&&mensaje.length()>0&&condicion==false)
                    {

                    }

                    if(Llave.getText().toString().length()==0)
                    {
                        MostrarPasos.setText("Debe de ingresar una llave");
                    }

                    if(ruta.length()==0)
                    {
                        MostrarPasos.setText("Necesita una ruta de almacenamiento para guardar sus archivos");
                    }
                    if(Llave.getText().toString().length()> 0&&Llave.getText().toString().length()<10)
                    {
                        MostrarPasos.setText("La llave ingresada de "+ Llave.getText().toString().length()+"bits, ingrese una llave nuevamente de 10 bits.");
                        Llave.setText("");
                    }
                    if(Llave.getText().toString().length()>10)
                    {
                        MostrarPasos.setText("Llave demasiado grande, ingrese una nuevamente");
                        Llave.setText("");
                    }
                    if(condicion == true)
                    {
                        MostrarPasos.setText("La llave solo debe de contener 0 y 1");
                        Llave.setText("");
                    }
                }
                // endregion
            }
        }
    }
    private void performFileSearch() {
        if(opcion==1)
        {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
        if(opcion ==2 || opcion ==0) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
    }
    private String leerTextFile (String input){
        File file = new File(Environment.getExternalStorageDirectory(), input);
        StringBuilder text = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linea;
            while((linea = br.readLine())!=null){
                text.append(linea);
                text.append("\n");
            }
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return text.toString();
    }
    public static String Leer(String archivo)
    {
        String textoArchivo = "";
        try
        {
            String cadenaArchivo;
            String temp="";
            FileReader filereader = new FileReader(archivo);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            while((cadenaArchivo = bufferedreader.readLine())!=null) {
                temp = temp + cadenaArchivo;
            }
            bufferedreader.close();
            textoArchivo = temp;
        }catch(Exception e){
            e.printStackTrace();
        }
        return textoArchivo;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private void CheckPermission()
    {
        if(ContextCompat.checkSelfPermission(SDES.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(SDES.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(SDES.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_WRITE_EXTERNAL);
            }
        }
    }
    private void CheckPermission2()
    {
        if(ContextCompat.checkSelfPermission(SDES.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(SDES.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(SDES.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_READ_EXTERNAL);
            }
        }
    }

}

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ZigZag extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    int opcion=0;
    TextView MostrarRuta,MostrarCifrado;
    Button EscogerRuta,Cifrar,CifrarArchivo,Descifrar;
    EditText Grado,Mensaje;
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL =1;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zig_zag);
        MostrarRuta=(TextView) findViewById(R.id.MostrarRuta);
        EscogerRuta =(Button) findViewById(R.id.Boton_Ruta);
        CifrarArchivo =(Button) findViewById(R.id.Boton_CifrarTexto);
        Cifrar =(Button) findViewById(R.id.Boton_Cifrar);
        Descifrar =(Button) findViewById(R.id.Boton_DescifrarTexto);
        MostrarCifrado=(TextView) findViewById(R.id.TextView_MostrarCifrado);
        Grado = (EditText)findViewById(R.id.Texto_Grado);
        Mensaje=(EditText)findViewById(R.id.Texto_Cifrar);
        CheckPermission();
        CheckPermission2();

        EscogerRuta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //region Escoger ruta
                MostrarRuta.setText("");
                MostrarCifrado.setText("");
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
                //region Cifrar mensaje
                MostrarRuta.setText("");
                MostrarCifrado.setText("");
                Cifrado cifrar = new Cifrado();
                Scanner s = new Scanner(System.in);
                String mensaje = Mensaje.getText().toString();
                String txt = mensaje.toUpperCase();
                String g = Grado.getText().toString();
                String ruta = cifrar.Leer();

                if(ruta.length()>0&&g.length()>0&&mensaje.length()>0)
                {
                    int grado = Integer.parseInt(g);
                    char[][] M = new char[grado][txt.length()];
                    cifrar.CrearMatriz( txt ,grado, M);
                    String mensajeCifrado = cifrar.CifrarMensaje(M,grado, txt.length());
                    String rutaSalida = ruta +txt+".cif";
                    cifrar.CrearArchivo(rutaSalida,mensajeCifrado);
                    MostrarCifrado.setText(mensajeCifrado);
                    MostrarRuta.setText(rutaSalida);
                    Mensaje.setText("");
                    Grado.setText("");
                }

                if(ruta.length()==0)
                {
                    MostrarCifrado.setText("Debe de escoger una ruta de almacenamiento");
                }
                if(g.length()==0)
                {
                    MostrarCifrado.setText("Debe de ingresar el grado de zig zag");
                }
                if(mensaje.length()==0)
                {
                    MostrarCifrado.setText("Debe de escribir un mensaje a cifrar");
                }

//endregion
            }
        });
        CifrarArchivo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //region Escoger ruta
                MostrarRuta.setText("");
                MostrarCifrado.setText("");
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
                MostrarCifrado.setText("");
                opcion=2;
                performFileSearch();
                //endregion
            }
        });

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
                    MostrarCifrado.setText(actual);
                }
                //endregion
                //region Cifrar texto
                if (opcion == 1) {
                    Cifrado cifrar = new Cifrado();
                    Scanner s = new Scanner(System.in);
                    String mensaje = Leer("/storage/emulated/0/" + path);
                    String g = Grado.getText().toString();
                    String txt = mensaje.toUpperCase();
                    String ruta = cifrar.Leer();
                    String name= ("/storage/emulated/0/"+path);
                    String[] d = name.split("/");
                    String name2 = d[5].substring(0,d[5].length()-4);
                    MostrarRuta.setText(name2);


                    if(ruta.length()>0&&g.length()>0&&mensaje.length()>0)
                    {
                        int grado = Integer.parseInt(g);
                        char[][] M = new char[grado][txt.length()];
                        cifrar.CrearMatriz( txt ,grado, M);
                        String mensajeCifrado = cifrar.CifrarMensaje(M,grado, txt.length());
                        String rutaSalida = ruta +name2+".cif";
                        cifrar.CrearArchivo(rutaSalida,mensajeCifrado);
                        MostrarCifrado.setText(mensajeCifrado);
                        MostrarRuta.setText(rutaSalida);
                        Mensaje.setText("");
                        Grado.setText("");
                    }

                    if(ruta.length()==0)
                    {
                        MostrarCifrado.setText("Debe de escoger una ruta de almacenamiento");
                    }
                    if(g.length()==0)
                    {
                        MostrarCifrado.setText("Debe de ingresar el grado de zig zag");
                    }
                    if(mensaje.length()==0)
                    {
                        MostrarCifrado.setText("Debe de escribir un mensaje a cifrar");
                    }
                }

                    //endregion
                //region Decifrar texto
                    if (opcion == 2)
                    {
                        if(path.endsWith(".cif"))
                        {
                            Descifrado Descifrado =new Descifrado();
                            String g = Grado.getText().toString();
                            String mensaje = Leer("/storage/emulated/0/" + path);
                            String ruta = Descifrado.Leer();
                            String name= ("/storage/emulated/0/"+path);
                            String[] d = name.split("/");
                            String name2 = d[5].substring(0,d[5].length()-4);

                            if(ruta.length()>0&&g.length()>0&&mensaje.length()>0)
                            {
                                int grado = Integer.parseInt(g);
                                String txt1 = mensaje.toUpperCase();
                                char[][] M1 = new char[grado][txt1.length()];
                                Descifrado.CrearMatriz(txt1 ,  grado , M1);
                                String MensajeDecifrado = Descifrado.MensajeDecifrado(M1, grado, txt1.length());
                                Descifrado.CrearArchivo(ruta+name2+".txt",MensajeDecifrado);
                                MostrarCifrado.setText(MensajeDecifrado);
                                MostrarRuta.setText(ruta+name2+".txt");

                            }
                            if(ruta.length()==0)
                            {
                                MostrarCifrado.setText("Debe de escoger una ruta de almacenamiento");
                            }
                            if(g.length()==0)
                            {
                                MostrarCifrado.setText("Debe de ingresar el grado de zig zag");
                            }
                            if(mensaje.length()==0)
                            {
                                MostrarCifrado.setText("Debe de escribir un mensaje a descifrar");
                            }
                        }
                        else
                        {
                            MostrarCifrado.setText("Archivo incorrecto");
                        }
                    }
                    //endregion
                }
            }
    }
    //leer el txt File
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

    public String Leer(String archivo)
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
        if(ContextCompat.checkSelfPermission(ZigZag.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(ZigZag.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(ZigZag.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_WRITE_EXTERNAL);
            }
        }
    }
    private void CheckPermission2()
    {
        if(ContextCompat.checkSelfPermission(ZigZag.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(ZigZag.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(ZigZag.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_READ_EXTERNAL);
            }
        }
    }


}

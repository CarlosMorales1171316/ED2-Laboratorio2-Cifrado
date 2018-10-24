package com.example.ivana.laboratorio2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public class DescifrarRSAVista extends AppCompatActivity {

    Button Ruta,Escogerkey1,Escogerkey2,Descifrar,Regresar;
    TextView MostrarLlaves;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    int opcion=0;
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL =1;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descifrar_rsavista);

        Ruta = (Button) findViewById(R.id.Boton_RutaCifrarRSA1);
        Regresar = (Button) findViewById(R.id.Boton_Regresar);
        Escogerkey1 = (Button) findViewById(R.id.Boton_EscojerLlavePrivada1);
        Escogerkey2 = (Button) findViewById(R.id.Boton_EscojerLlavePublica2);
        Descifrar = (Button) findViewById(R.id.Boton_DescifrarRSA);
        MostrarLlaves = (TextView) findViewById(R.id.Txt_MostrarCifradoRSA1);

        Regresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Borrar();
                Intent intent = new Intent(DescifrarRSAVista.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Ruta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MostrarLlaves.setText("");
                opcion=0;
                performFileSearch();
            }
        });

        Escogerkey1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MostrarLlaves.setText("");
                opcion=2;
                performFileSearch();
            }
        });
        Escogerkey2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MostrarLlaves.setText("");
                opcion=3;
                performFileSearch();
            }
        });

        Descifrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MostrarLlaves.setText("");
                opcion=1;
                performFileSearch();
            }
        });

    }
    private void CheckPermission()
    {
        if(ContextCompat.checkSelfPermission(DescifrarRSAVista.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(DescifrarRSAVista.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(DescifrarRSAVista.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_WRITE_EXTERNAL);
            }
        }
    }
    private void CheckPermission2()
    {
        if(ContextCompat.checkSelfPermission(DescifrarRSAVista.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(DescifrarRSAVista.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(DescifrarRSAVista.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_READ_EXTERNAL);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                    ArrayList<String> Escribir = new ArrayList<>();
                    Escribir.add(actual);
                    EscribirArchivo(Escribir, "/storage/emulated/0/Download/RutaArchivo.txt");
                    MostrarLlaves.setText("Ruta de almacenamiento de archivo descifrado: "+actual);
                }
                //endregion
                //region Obtener llave privada
                if(opcion ==2)
                {
                    if(path.endsWith(".key")) {
                        ArrayList<String> Escribir = new ArrayList<>();
                        ArrayList<String> Escribir2 = new ArrayList<>();
                        AlgortimoSDES s = new AlgortimoSDES(1);
                        String ruta1 = s.Leer();
                        Escribir = LeerArchivos(new File("/storage/emulated/0/Download/RutaArchivo.txt"));
                        if(Escribir.size() <1||Escribir.size()>1)
                        {
                            MostrarLlaves.setText("Debe de escoger ruta de almacenamiento");
                        }
                        if (ruta1.length() > 0 && Escribir.size() ==1) {
                            Escribir.clear();
                            String ruta2 = "/storage/emulated/0/" + path;
                            Escribir.add(ruta1);
                            Escribir2 = LeerArchivos(new File(ruta2));
                            for (String d : Escribir2) {
                                Escribir.add(d);
                            }
                            MostrarLlaves.setText("Llave privada " + Escribir2.get(0));
                            EscribirArchivo(Escribir, "/storage/emulated/0/Download/RutaArchivo.txt");
                        }
                        if (ruta1.length() == 0) {
                            MostrarLlaves.setText("Debe de ingresar una ruta de almacenamiento");
                        }
                    }
                    else
                    {
                        MostrarLlaves.setText("");
                        MostrarLlaves.setText("Archivo no compatible");
                    }
                }
                //endregion
                //region llave publica
                if(opcion==3)
                {
                    if (path.endsWith(".key")) {
                        ArrayList<String> Escribir = new ArrayList<>();
                        ArrayList<String> Escribir2 = new ArrayList<>();
                        AlgortimoSDES s = new AlgortimoSDES(1);
                        String ruta1 = s.Leer();
                        Escribir = LeerArchivos(new File("/storage/emulated/0/Download/RutaArchivo.txt"));
                        if(Escribir.size() <2||Escribir.size()>2)
                        {
                            MostrarLlaves.setText("Debe de ingresar llave privada");
                        }
                        if (ruta1.length() > 0 && Escribir.size() ==2)
                        {
                            String ruta2 = "/storage/emulated/0/" + path;
                            Escribir2 = LeerArchivos(new File(ruta2));
                            for (String d : Escribir2) {
                                Escribir.add(d);
                            }
                            MostrarLlaves.setText("Llave publica " + Escribir2.get(0));
                            EscribirArchivo(Escribir, "/storage/emulated/0/Download/RutaArchivo.txt");
                        }
                        if (ruta1.length() == 0) {
                            MostrarLlaves.setText("Debe de ingresar una ruta de almacenamiento");
                        }
                    } else {

                        MostrarLlaves.setText("");
                        MostrarLlaves.setText("Archivo no compatible");
                    }
                }
                //endregion
                //region Descifrado
                if(opcion==1) {
                    if (path.endsWith("rsacif")) {
                        AlgortimoSDES s = new AlgortimoSDES(1);
                        ArrayList<String> Cifrado = new ArrayList<>();
                        ArrayList<String> Leer = new ArrayList<>();
                        Cifrado = LeerArchivos(new File("/storage/emulated/0/Download/RutaArchivo.txt"));
                        String ruta2 = "/storage/emulated/0/" + path;
                        Leer = LeerArchivos(new File(ruta2));
                        if (Leer.size() > 0&&Cifrado.size()>0) {
                            String ruta = Cifrado.get(0);
                            String m = Leer.get(0);
                            String llave1 = Cifrado.get(1);
                            String llave2 = Cifrado.get(2);
                            String llavePrivada = llave1.substring(1, llave1.length() - 1);
                            String llavePublica = llave2.substring(1, llave2.length() - 1);
                            String[] tempo = llavePrivada.split(",");
                            String[] tempo2 = llavePublica.split(",");
                            int E = Integer.parseInt(tempo2[1]);
                            int N = Integer.parseInt(tempo[0]);
                            BigInteger n = new BigInteger(Integer.toString(N));
                            BigInteger e = new BigInteger(Integer.toString(E));

                            DescifradoRSA decoder = new DescifradoRSA(n, e, m);
                            String[] Descifrado = decoder.Descifrar();
                            String name = ("/storage/emulated/0/" + path);
                            String[] d = name.split("/");
                            String name2 = d[5].substring(0, d[5].length() - 7);
                            String rutaSalida = ruta + name2 + "Des.txt";
                            MostrarLlaves.setText(Descifrado[0] + "\nRuta archivo: " + rutaSalida);
                            Leer.clear();
                            Leer.add(Descifrado[1]);
                            EscribirArchivo(Leer, rutaSalida);
                            Borrar();
                        }
                        if(Cifrado.size()==0)
                        {
                            MostrarLlaves.setText("");
                            MostrarLlaves.setText("Debe de ingresar ruta de almacenamiento");

                        }
                    }
                    else
                    {
                        MostrarLlaves.setText("");
                        MostrarLlaves.setText("Archivo no compatible");
                    }
                }
                //endregion
            }
        }
    }
    private void performFileSearch()
         {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
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
    public ArrayList<String> LeerArchivos(File archivo){

        ArrayList<String> temp = new ArrayList<String>();
        try
        {
            String cadenaArchivo;
            FileReader filereader = new FileReader(archivo);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            while((cadenaArchivo = bufferedreader.readLine())!=null) {
                temp.add(cadenaArchivo);
            }
            bufferedreader.close();

        }catch(Exception e){

        }
        return temp;
    }
    public void EscribirArchivo(ArrayList<String> Escribir,String ruta)
    {
        try
        {
            if (!new File(ruta).exists())
            {
                new File(ruta).createNewFile();
            }
            FileWriter fw = new FileWriter(ruta);
            BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ruta),true),"UTF8"));
            fw.write("");
            for (int i = 0; i < Escribir.size(); i++)
            {
                if(i+1!= Escribir.size())
                {
                    out.write(Escribir.get(i)+System.getProperty("line.separator"));
                }
                else
                {
                    out.write(Escribir.get(i));
                }
            }

            out.close();
            fw.close();
        }
        catch(IOException e)
        {

        }
    }
    private void Borrar()
    {
        try
        {
            String ruta ="/storage/emulated/0/Download/RutaArchivo.txt";
            FileWriter fw = new FileWriter(ruta);
            fw.write("");
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

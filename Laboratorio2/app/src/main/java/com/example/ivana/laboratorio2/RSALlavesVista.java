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
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

public class RSALlavesVista extends AppCompatActivity {

    Button Cifrar,Ruta,GenerarLlaves;
    EditText P,Q,E;
    TextView MostrarLlaves;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    int opcion=0;
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL =1;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rs);
        Cifrar = (Button) findViewById(R.id.Boton_CifrarVista);
        Ruta = (Button) findViewById(R.id.Boton_RutaRSA);
        GenerarLlaves =(Button) findViewById(R.id.Boton_Llaves);
        P = (EditText) findViewById(R.id.Texto_KeyP);
        Q = (EditText) findViewById(R.id.Texto_KeyQ);
        E = (EditText) findViewById(R.id.Texto_KeyE);
        MostrarLlaves = (TextView) findViewById(R.id.MostrarLlavesRsa);
        CheckPermission();
        CheckPermission2();

        Cifrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Borrar();
                Intent intent = new Intent(RSALlavesVista.this, CifradoRSAVista.class);
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

        GenerarLlaves.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MostrarLlaves.setText("");
                String PA = P.getText().toString();
                String QA = Q.getText().toString();
                String EA = E.getText().toString();
                int p = Integer.parseInt(PA);
                int q = Integer.parseInt(QA);
                int N = p*q;
                int Delta =(p-1)*(q-1);
                int en =Integer.parseInt(EA);
                boolean Condicion= ePrimo(p);
                boolean Condicion2= ePrimo(q);
                int mcd = obtener_mcd(en,Delta);
                AlgortimoSDES s = new AlgortimoSDES(1);
                String ruta = s.Leer();
                if(mcd==1&&Condicion==true && Condicion2==true && q>p&&ruta.length()>0)
                {
                    BigInteger ef = new BigInteger(Integer.toString(en));
                    BigInteger R = new BigInteger(Integer.toString(Delta));
                    BigInteger d = calcularD(ef,R);
                    String rutaLlavePrivada = ruta+"private.key";
                    String llavePrivada ="("+N+","+d+")";
                    s.CrearArchivo(rutaLlavePrivada,llavePrivada,"");

                    String llavePublica = "("+N+","+en+")";
                    String rutaLLavePublica=ruta+"public.key";
                    s.CrearArchivo(rutaLLavePublica,llavePublica,"");
                    String mensaje ="\nLlave privada: " +llavePrivada+"\n"+"\nRuta llave privada: "+rutaLlavePrivada +"\n"+"\nLlave publica: "+llavePublica+"\n"+ "\nRuta llave publica: "+rutaLLavePublica;
                    MostrarLlaves.setText(mensaje);
                }
                if(mcd>1)
                {
                    MostrarLlaves.setText("E invalido, ingrese uno nuevamente");
                    E.setText("");
                }
                if(Condicion==false)
                {
                    MostrarLlaves.setText("p debe de ser prima ingrese nuevamente");
                    P.setText("");
                }
                if(Condicion2==false)
                {
                    MostrarLlaves.setText("q debe de ser prima ingrese nuevamente");
                    Q.setText("");
                }
                if(q<p)
                {
                    MostrarLlaves.setText("q debe de ser mayor a p");
                    Q.setText("");
                    P.setText("");
                }
                if(ruta.length()==0)
                {
                    MostrarLlaves.setText("Debe de ingresar una ruta de almacenamiento");
                }
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
                    MostrarLlaves.setText("Ruta de almacenamiento de archivos llave privada y publica: "+actual);
                }
                //endregion
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
        if(ContextCompat.checkSelfPermission(RSALlavesVista.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(RSALlavesVista.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(RSALlavesVista.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_WRITE_EXTERNAL);
            }
        }
    }
    private void CheckPermission2()
    {
        if(ContextCompat.checkSelfPermission(RSALlavesVista.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(RSALlavesVista.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(RSALlavesVista.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_READ_EXTERNAL);
            }
        }
    }
    public static boolean ePrimo(int n)

    {
        return !esPrimo(n,n-1);
    }
    private static boolean esPrimo(int n,int divisor)
    {
        boolean res=false;
        if(divisor==2)
        {
            res=(n%2==0);
        }else
        {
            res=(n%divisor==0)||esPrimo(n,divisor-1);
        }
        return res;
    }

    public static int obtener_mcd(int a, int b)
    {
        if(b==0)
            return a;
        else
            return obtener_mcd(b, a % b);
    }
    public static BigInteger calcularD(BigInteger valorE,BigInteger valorR)
    {
        BigInteger valorD = valorE.modInverse(valorR);
        return valorD;
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

package com.example.ivana.laboratorio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Cifrado
{
    public void CrearMatriz(String mensaje , int grado , char[][] caracteres)
    {
        int olas = (grado - 1) * 2;

        for (int  i = 0 ; i < grado ; i ++ )
        {
            for (int j = i ; j < mensaje.length(); j+=olas)
            {
                int pos2 = j + (olas - i*2);
                caracteres[i][j] = mensaje.charAt(j);
                if(pos2 < mensaje.length())
                {
                    caracteres[i][pos2] = mensaje.charAt(pos2);

                }

            }
        }
    }


    public String CifrarMensaje(char[][] caracteres, int x, int y){
        String mensaje = "";
        for (int  i = 0 ; i < x ; i ++)
        {
            for (int j = 0 ; j < y ; j ++)
            {
                if(caracteres[i][j]!='\0')
                {
                    mensaje = mensaje+caracteres[i][j];

                }

            }
        }
        return mensaje;

    }

    public void CrearArchivo(String ruta,String mensaje)
    {
        try
        {

            new File(ruta).createNewFile();

            FileWriter fw = new FileWriter(ruta);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mensaje);
            bw.close();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String Leer()
    {
        String mensaje="";
        String ruta ="/storage/emulated/0/Download/RutaArchivo.txt";
        try
        {
            if (new File(ruta).exists())
            {
                FileReader fr=new FileReader(ruta);
                BufferedReader br = new BufferedReader(fr);
                String s;
                while((s = br.readLine()) != null) {
                    mensaje=(s);
                }
                fr.close();
            }

        }
        catch (IOException e)
        {

        }

        return mensaje;
    }
}

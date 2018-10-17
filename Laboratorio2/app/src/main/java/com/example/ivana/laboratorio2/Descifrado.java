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

public class Descifrado
{
    public  void CrearMatriz(String mensaje , int grado , char[][] caracteres)
    {
        int olas = (grado - 1) * 2;
        int k =0;

        for (int  i = 0 ; i < grado ; i ++ )
        {
            for (int j = i; j < mensaje.length() && k<mensaje.length(); j+=olas)
            {
                int pos2 = j + (olas - i*2);
                caracteres[i][j] = mensaje.charAt(k);
                k++;
                if(i!=0 && i!= grado-1 && pos2 < mensaje.length())
                {
                    caracteres[i][pos2] = mensaje.charAt(k);
                    k++;
                }
            }
        }
    }
    public String MensajeDecifrado(char[][] caracteres, int x, int y)
    {
        String MensajeDecifrado = "";
        for (int  i = 0 ; i < y ; i ++)
        {
            for (int j = 0 ; j < x ; j ++)
            {
                if(caracteres[j][i]!='\0')
                {
                    MensajeDecifrado = MensajeDecifrado+caracteres[j][i];
                }
            }
        }
        return MensajeDecifrado;

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

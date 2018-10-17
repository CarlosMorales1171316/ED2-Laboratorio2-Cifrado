package com.example.ivana.laboratorio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AlgortimoSDES
{
    public int K1, K2;
    public static final int P10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    public static final int P10max = 10;
    public static final int P8[] = {6, 3, 7, 4, 8, 5, 10, 9};
    public static final int P8max = 10;
    public static final int P4[] = { 2, 4, 3, 1};
    public static final int P4max = 4;
    public static final int IP[] = { 2, 6, 3, 1, 4, 8, 5, 7};
    public static final int IPmax = 8;
    public static final int IPI[] = { 4, 1, 3, 5, 7, 2, 8, 6};
    public static final int IPImax = 8;
    public static final int EP[] = { 4, 1, 2, 3, 2, 3, 4, 1};
    public static final int EPmax = 4;
    public static final int S0[][] = {{ 1, 0, 3, 2},{ 3, 2, 1, 0},{ 0, 2, 1,
            3},{ 3, 1, 3, 2}};
    public static final int S1[][] = {{ 0, 1, 2, 3},{ 2, 0, 1, 3},{ 3, 0, 1,
            2},{ 2, 1, 0, 3}};
    public String pasos ="";
    public String pasos0 ="";


    public static int permutar( int x, int p[], int pmax)
    {
        int y = 0;
        for( int i = 0; i < p.length; ++i)
        {
            y <<= 1;
            y |= (x >> (pmax - p[i])) & 1;
        }
        return y;
    }

    public static int F( int R, int K)
    {
        int t = permutar( R, EP, EPmax) ^ K;
        int t0 = (t >> 4) & 0xF;
        int t1 = t & 0xF;
        t0 = S0[ ((t0 & 0x8) >> 2) | (t0 & 1) ][ (t0 >> 1) & 0x3 ];
        t1 = S1[ ((t1 & 0x8) >> 2) | (t1 & 1) ][ (t1 >> 1) & 0x3 ];
        t = permutar( (t0 << 2) | t1, P4, P4max);
        return t;

    }

    public static int fK( int m, int K)
    {
        int L = (m >> 4) & 0xF;
        int R = m & 0xF;
        return ((L ^ F(R,K)) << 4) | R;
    }

    public static int SW( int x)
    {
        return ((x & 0xF) << 4) | ((x >> 4) & 0xF);
    }


    public byte cifrar( int m)

    {

        m = permutar( m, IP, IPmax);

        ImprimirInformacion( m, 8);
        m = fK( m, K1);

        ImprimirInformacion( m, 8);
        m = SW( m);

        ImprimirInformacion( m, 8);
        m = fK( m, K2);

        ImprimirInformacion( m, 8);
        m = permutar( m, IPI, IPImax);
        return (byte) m;

    }

    public byte Descifrar( int m)

    {
        pasos=("\nProceso descifrado: "+ImprimirInformacion( m, 8));
        m = permutar( m, IP, IPmax);
        pasos =pasos+("\nPermutar : "+ImprimirInformacion( m, 8));
        m = fK( m, K2);
        pasos =pasos+("\nAntes de intercambiar valores: "+ImprimirInformacion( m, 8));
        m = SW( m);
        pasos =pasos+("\nDespues de intercambiar valores: "+ImprimirInformacion( m, 8));
        m = fK( m, K1);
        pasos =pasos+("\nAntes de la permutación de extracción: "+ ImprimirInformacion( m, 4));
        m = permutar( m, IPI, IPImax);
        pasos =pasos+("\nDespués de la permutación de extracción: "+ImprimirInformacion( m, 8));
        return (byte) m;
    }


    public String ImprimirInformacion( int x, int n)
    {
        String m="";
        int mask = 1 << (n-1);
        while( mask > 0)
        {
            m+=( ((x & mask) == 0) ? '0' : '1')+"";
            mask >>= 1;
        }
        return m;
    }


    public AlgortimoSDES( int K)
    {
        K = permutar( K, P10, P10max);
        int t1 = (K >> 5) & 0x1F;
        int t2 = K & 0x1F;
        t1 = ((t1 & 0xF) << 1) | ((t1 & 0x10) >> 4);
        t2 = ((t2 & 0xF) << 1) | ((t2 & 0x10) >> 4);
        K1 = permutar( (t1 << 5)| t2, P8, P8max);
        t1 = ((t1 & 0x7) << 2) | ((t1 & 0x18) >> 3);
        t2 = ((t2 & 0x7) << 2) | ((t2 & 0x18) >> 3);
        K2 = permutar( (t1 << 5)| t2, P8, P8max);

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

package com.example.ivana.laboratorio2;

import java.io.*;
import java.lang.*;
import java.util.ArrayList;

public class AlgortimoSDES
{

    public int Key1, Key2;
    public static final int P10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    public static final int P10max = 10;
    public static final int P8[] = { 6, 3, 7, 4, 8, 5, 10, 9};
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

    public static int permutar(int x,int p[],int pmax)
    {
        int y = 0;
        for( int i = 0; i < p.length; ++i)
        {
            y <<= 1;
            y |= (x >> (pmax - p[i])) & 1;
        }
        return y;
    }

    public static int F(int R,int K)
    {
        int t = permutar( R, EP, EPmax) ^ K;
        int t0 = (t >> 4) & 0xF;
        int t1 = t & 0xF;
        t0 = S0[ ((t0 & 0x8) >> 2) | (t0 & 1) ][ (t0 >> 1) & 0x3 ];
        t1 = S1[ ((t1 & 0x8) >> 2) | (t1 & 1) ][ (t1 >> 1) & 0x3 ];
        t = permutar( (t0 << 2) | t1, P4, P4max);
        return t;

    }

    public static int fK(int m,int K)
    {
        int L = (m >> 4) & 0xF;
        int R = m & 0xF;
        return ((L ^ F(R,K)) << 4) | R;
    }

    public static int SW(int x)
    {
        return ((x & 0xF) << 4) | ((x >> 4) & 0xF);

    }

    public byte cifrar(int m)
    {
        m = permutar( m, IP, IPmax);
        m = fK( m, Key1);
        m = SW( m);
        m = fK( m, Key2);
        m = permutar( m, IPI, IPImax);
        return (byte) m;

    }

    public byte Descifrar(int m)
    {
        pasos=("\nProceso descifrado: "+ImprimirInformacion( m, 8));
        m = permutar( m, IP, IPmax);
        pasos =pasos+("\nPermutar : "+ImprimirInformacion( m, 8));
        m = fK( m, Key2);
        pasos =pasos+("\nAntes de intercambiar valores: "+ImprimirInformacion( m, 8));
        m = SW( m);
        pasos =pasos+("\nDespues de intercambiar valores: "+ImprimirInformacion( m, 8));
        m = fK( m, Key1);
        pasos =pasos+("\nAntes de la permutación de extracción: "+ ImprimirInformacion( m, 4));
        m = permutar( m, IPI, IPImax);
        pasos =pasos+("\nDespués de la permutación de extracción: "+ImprimirInformacion( m, 8));
        return (byte) m;
    }

    public String ImprimirInformacion(int x, int n)
    {
        String m="";
        int mask = 1 << (n-1);
        while( mask > 0)
        {
            m+=( ((x & mask) == 0) ? '0' : '1');
            mask >>= 1;
        }
        return m;
    }

    public AlgortimoSDES(int Key)
    {
        Key = permutar( Key, P10, P10max);
        int t1 = (Key >> 5) & 0x1F;
        int t2 = Key & 0x1F;
        t1 = ((t1 & 0xF) << 1) | ((t1 & 0x10) >> 4);
        t2 = ((t2 & 0xF) << 1) | ((t2 & 0x10) >> 4);
        Key1 = permutar( (t1 << 5)| t2, P8, P8max);
        t1 = ((t1 & 0x7) << 2) | ((t1 & 0x18) >> 3);
        t2 = ((t2 & 0x7) << 2) | ((t2 & 0x18) >> 3);
        Key2 = permutar( (t1 << 5)| t2, P8, P8max);

    }



    public String[] BytesMensaje(String texto) throws UnsupportedEncodingException
    {
        byte[] bytes = texto.getBytes("US-ASCII");
        String[] NumeroDeBytes = new String[bytes.length];

        for (int i = 0; i < bytes.length; i++)
        {
            NumeroDeBytes[i]=(bytes[i]+"");
        }

        return NumeroDeBytes;
    }

    public String[] DescifrarMensaje(int llave,String mensaje)
    {
        AlgortimoSDES A =new AlgortimoSDES(llave);
        String MensajeDescifrado[]= new String[3];
        String descifrado ="";
        String[] Descifrado = mensaje.split(",");
        for (int i = 0; i < Descifrado.length; i++)
        {
            int m =A.Descifrar(Integer.parseInt(Descifrado[i]));
            String tempo1=A.ImprimirInformacion(m, 8);
            String tempo2 =A.convertirDecimal(tempo1);
            String tempo3 = A.ConvertirMensaje(tempo2);
            descifrado+=tempo3;
        }
        MensajeDescifrado[0] =A.pasos;
        MensajeDescifrado[1] = descifrado;
        return MensajeDescifrado;
    }

    public String convertirDecimal(String binario)
    {
        int decimal=0;
        int power=0;
        while(binario.length()>0)
        {
            int temp = Integer.parseInt(binario.charAt((binario.length())-1)+"");
            decimal+=temp*Math.pow(2, power++);
            binario=binario.substring(0,binario.length()-1);
        }
        return (decimal)+"";
    }

    public String ConvertirMensaje(String Numeros)
    {
        String mensaje ="";
        ArrayList<String> Key= new ArrayList<String>();
        ArrayList<String> Value= new ArrayList<String>();
        for(int i=0;i<256;i++)
        {
            Key.add(i+"");
            Value.add(""+(char)i);
        }

        for (int i = 0; i < 256; i++)
        {
            if(Key.get(i).equals(Numeros))
            {
                mensaje+= Value.get(i);
            }
        }
        return mensaje;
    }

    public String[] CifrarMensaje(int K, String texto) throws UnsupportedEncodingException
    {
        String[] mensaje = new String[3];
        AlgortimoSDES A = new AlgortimoSDES(K);
        String[] bytes = new String[100];
        bytes= BytesMensaje(texto);
        String mensaje0 ="",mensaje1="";
        A.pasos0 = "Key (1): "+ A.ImprimirInformacion(Key1, 8)+"\nKey (2): "+ A.ImprimirInformacion(Key2, 8);
        mensaje[2]= A.pasos0;
        for(int i=0;i<bytes.length;i++)
        {
            int m = Integer.parseInt(bytes[i]);
            m = A.cifrar(m);
            if(i+1!= bytes.length)
            {
                mensaje0 +=m+",";
            }
            if(i+1==bytes.length)
            {
                mensaje0+=m;
            }
            mensaje1 += A.ImprimirInformacion(m, 8);
        }
        mensaje[0]=mensaje1;
        mensaje[1]=mensaje0;
        return mensaje;
    }
}

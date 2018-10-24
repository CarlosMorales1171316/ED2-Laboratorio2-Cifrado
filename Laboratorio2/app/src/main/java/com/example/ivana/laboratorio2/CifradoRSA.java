package com.example.ivana.laboratorio2;

import java.math.BigInteger;
import java.util.ArrayList;

public class CifradoRSA
{
    public String CifrarMensaje(String llave,String mensaje)
    {
        String cifrado ="";

        String[] LlavePublica =LlavePublica(llave);

        ArrayList<String> caracteres = Caracteres(mensaje);
        ArrayList<String> ascii = ConvertirAscii(caracteres);

        for(int i =0;i<ascii.size();i++)
        {
            BigInteger n, e, N,potencia;

            n = new BigInteger(LlavePublica[0]);
            e = new BigInteger(LlavePublica[1]);
            N = new BigInteger(ascii.get(i));
            potencia = N.modPow(e, n);

            if(i+1!=ascii.size())
            {
                cifrado +=potencia+",";
            }
            if(i+1==ascii.size())
            {
                cifrado +=potencia;
            }
        }


        return cifrado;
    }

    public ArrayList<String> Caracteres(String mensaje)
    {
        ArrayList<String> nuevo = new ArrayList<String>();
        for (int i = 0; i < mensaje.length(); i++)
        {
            nuevo.add(mensaje.charAt(i)+"");
        }
        return nuevo;
    }

    public ArrayList<String> ConvertirAscii(ArrayList<String> caracteres)
    {
        ArrayList<String> nuevo = new ArrayList<String>();

        for(String s: caracteres)
        {
            String tempo = ConvertirMensaje(s);
            nuevo.add(tempo);

        }
        return nuevo;
    }

    public String ConvertirMensaje(String letra)
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
            if(Value.get(i).equals(letra))
            {
                mensaje+= Key.get(i);
            }
        }
        return mensaje;
    }

    public String[] LlavePublica(String llave)
    {
        String n = llave.substring(1, llave.length()-1);
        String[] tempo = n.split(",");
        return tempo;
    }

    public String GenerarLlavePublica(String p,String q,String e)
    {
        String llavePublica="";
        int N = Integer.parseInt(p)*Integer.parseInt(q);
        llavePublica ="("+N+","+e+")";
        return llavePublica;
    }

    public String GenerarLlavePrivada(String p,String q,String e)
    {
        String llavePrivada="";
        int N = Integer.parseInt(p)*Integer.parseInt(q);
        int Delta = (Integer.parseInt(p)-1)*(Integer.parseInt(q)-1);
        BigInteger E = new BigInteger(e);
        BigInteger R = new BigInteger(Integer.toString(Delta));
        BigInteger d = calcularD(E,R);
        llavePrivada ="("+N+","+d+")";
        return llavePrivada;
    }

    public BigInteger calcularD(BigInteger valorE, BigInteger valorR)
    {
        BigInteger valorD;
        valorD = valorE.modInverse(valorR);
        return valorD;
    }

}

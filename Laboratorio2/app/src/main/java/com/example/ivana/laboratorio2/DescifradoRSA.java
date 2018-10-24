package com.example.ivana.laboratorio2;

import java.math.BigInteger;

public class DescifradoRSA
{
    public String DescifrarMensaje(String llave,String mensaje)
    {
        String descifrado = "";
        String[] Nuevo = mensaje.split(",");
        String[]  Llave = LlavePrivada(llave);

        //int n = Integer.parseInt(Llave[0]);
        for(int i =0;i<Nuevo.length;i++)
        {

            //int C = Integer.parseInt((Nuevo[i]));
            BigInteger d, n, C,potencia,mod;

            d = new BigInteger(Llave[1]);
            n = new BigInteger(Llave[0]);
            C = new BigInteger(Nuevo[i]);

            potencia = C.modPow(d, n);
            String tempo = potencia+"";
            String convertir = convertirASCII(Integer.parseInt(tempo));
            descifrado +=convertir;
        }
        return descifrado;
    }

    private String convertirASCII(int ascii) {
        return Character.toString((char) ascii);
    }

    public String[] LlavePrivada(String llave)
    {
        String n = llave.substring(1, llave.length()-1);
        String[] tempo = n.split(",");
        return tempo;
    }

}

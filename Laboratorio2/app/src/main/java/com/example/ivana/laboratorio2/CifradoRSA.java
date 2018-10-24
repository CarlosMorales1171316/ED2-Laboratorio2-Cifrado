package com.example.ivana.laboratorio2;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CifradoRSA {
    private BigInteger valorN;
    private BigInteger valorE;
    private List<Integer> mensaje;
    private BigInteger valorP;
    private BigInteger valorQ;
    private BigInteger valorR;
    private BigInteger valorD;
    private String MensajeCifrado;
    private RSA rsa = new RSA();


    public CifradoRSA(BigInteger valorN, BigInteger valorE, String mensaje) {
        this.valorN = valorN;
        this.valorE = valorE;
        this.mensaje = convertirASCII(mensaje);
    }

    private List<Integer> convertirASCII(String mensaje) {
        List<Integer> ascii = new ArrayList<>();
        for (char ch : mensaje.toCharArray()) {
            ascii.add((int) ch);
        }
        return ascii;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    public String[] cifrar()
    {
        calcularQ();
        calcularR();
        calcularD();
        ConstruirMensajeCifrado();
        String[] imprimir = Imprimir();
        return imprimir;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ConstruirMensajeCifrado()
    {
        StringJoiner joiner = new StringJoiner(",");
        for (int ch : mensaje) {
            BigInteger chh = BigInteger.valueOf(ch);
            String cifrado = chh.pow(valorE.intValue()).mod(valorN).toString();
            joiner.add(cifrado);
        }
        MensajeCifrado = joiner.toString();
    }

    private void calcularR() {
        valorR = (valorP.subtract(BigInteger.ONE)).multiply(valorQ.subtract(BigInteger.ONE));
    }

    private String[] Imprimir() {
        String[]Imprimir=new String[3];
        Imprimir[0] = ("" +
                "\nn es: " + valorN +
                "\ne es: " + valorE +
                "\np es: " + valorP +
                "\nq es: " + valorQ +
                "\nd es: " + valorD +
                "\nMensaje cifrado: " + MensajeCifrado);
        Imprimir[1] = MensajeCifrado;
        return Imprimir;

    }

    private void calcularD() {
        valorD = valorE.modInverse(valorR);
    }

    private void calcularQ() {
        List<BigInteger> pqList = rsa.calcularQ(valorN);
        valorP = pqList.get(0);
        valorQ = pqList.get(1);
    }


}



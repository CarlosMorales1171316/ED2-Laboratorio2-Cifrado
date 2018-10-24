package com.example.ivana.laboratorio2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DescifradoRSA {
    private BigInteger valorP;
    private BigInteger valorQ;
    private String MensajeDescifrado;
    private BigInteger valorR;
    private BigInteger valorD;
    private List<Integer> DescifrarValores;
    private BigInteger valorN;
    private BigInteger valorE;
    private RSA rsa = new RSA();

    public DescifradoRSA(BigInteger valorN, BigInteger valorE, String descifrar) {
        this.DescifrarValores = convertirLista(descifrar);
        this.valorN = valorN;
        this.valorE = valorE;
    }

    public String[] Descifrar() {
        calcularQ();
        calcularR();
        calcularD();
        ConstruirMensajeDescifrado();
        String[] imprimir = Imprimir();
        return imprimir;
    }

    private String[] Imprimir() {
        String d = MensajeDescifrado;
        String[] mensajes = new String[3];

        mensajes[0]= ("" +
                "\nn es: " + valorN +
                "\ne es: " + valorE +
                "\np es: " + valorP +
                "\nq es: " + valorQ +
                "\nd es: " + valorD +
                "\nMensaje descifrado: " + MensajeDescifrado);
        mensajes[1] = d;
        return mensajes;
    }

    private void ConstruirMensajeDescifrado() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer value : DescifrarValores) {
            BigInteger c = BigInteger.valueOf(value);
            int d = valorD.intValue();
            int ascii = c.pow(d).mod(valorN).intValue();
            String ch = convertirASCII(ascii);
            stringBuilder.append(ch);
        }
        MensajeDescifrado = stringBuilder.toString();
    }

    private String convertirASCII(int ascii) {
        return Character.toString((char) ascii);
    }

    private void calcularD() {
        valorD = valorE.modInverse(valorR);
    }


    private void calcularR() {
        valorR = (valorP.subtract(BigInteger.ONE)).multiply(valorQ.subtract(BigInteger.ONE));
    }

    private void calcularQ() {
        List<BigInteger> pq = rsa.calcularQ(valorN);
        valorP = pq.get(0);
        valorQ = pq.get(1);
    }

    private static List<Integer> convertirLista(String Descifrar) {
        List<Integer> values = new ArrayList<>();
        for (String value : Descifrar.split(","))
            values.add(Integer.valueOf(value));
        return values;
    }


}

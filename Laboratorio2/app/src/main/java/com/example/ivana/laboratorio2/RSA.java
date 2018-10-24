package com.example.ivana.laboratorio2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger r;

    public List<BigInteger> calcularQ(BigInteger modulo) {
        int n = modulo.intValue();
        List<BigInteger> factor = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factor.add(BigInteger.valueOf(i));
                n /= i;
            }
        }
        return factor;
    }

}

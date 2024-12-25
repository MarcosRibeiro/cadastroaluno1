package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeslocamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Deslocamento getDeslocamentoSample1() {
        return new Deslocamento().id(1L).nome("nome1").grau("grau1");
    }

    public static Deslocamento getDeslocamentoSample2() {
        return new Deslocamento().id(2L).nome("nome2").grau("grau2");
    }

    public static Deslocamento getDeslocamentoRandomSampleGenerator() {
        return new Deslocamento().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).grau(UUID.randomUUID().toString());
    }
}

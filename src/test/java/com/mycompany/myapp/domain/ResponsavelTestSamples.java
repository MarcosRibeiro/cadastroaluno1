package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResponsavelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Responsavel getResponsavelSample1() {
        return new Responsavel().id(1L).nome("nome1").parentesco("parentesco1");
    }

    public static Responsavel getResponsavelSample2() {
        return new Responsavel().id(2L).nome("nome2").parentesco("parentesco2");
    }

    public static Responsavel getResponsavelRandomSampleGenerator() {
        return new Responsavel()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .parentesco(UUID.randomUUID().toString());
    }
}

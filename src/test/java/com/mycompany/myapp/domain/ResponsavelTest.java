package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CadastroAlunoTestSamples.*;
import static com.mycompany.myapp.domain.ResponsavelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResponsavelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Responsavel.class);
        Responsavel responsavel1 = getResponsavelSample1();
        Responsavel responsavel2 = new Responsavel();
        assertThat(responsavel1).isNotEqualTo(responsavel2);

        responsavel2.setId(responsavel1.getId());
        assertThat(responsavel1).isEqualTo(responsavel2);

        responsavel2 = getResponsavelSample2();
        assertThat(responsavel1).isNotEqualTo(responsavel2);
    }

    @Test
    void cadastroAlunoTest() {
        Responsavel responsavel = getResponsavelRandomSampleGenerator();
        CadastroAluno cadastroAlunoBack = getCadastroAlunoRandomSampleGenerator();

        responsavel.setCadastroAluno(cadastroAlunoBack);
        assertThat(responsavel.getCadastroAluno()).isEqualTo(cadastroAlunoBack);

        responsavel.cadastroAluno(null);
        assertThat(responsavel.getCadastroAluno()).isNull();
    }
}

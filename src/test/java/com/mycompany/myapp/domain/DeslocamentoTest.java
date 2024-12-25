package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CadastroAlunoTestSamples.*;
import static com.mycompany.myapp.domain.DeslocamentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeslocamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deslocamento.class);
        Deslocamento deslocamento1 = getDeslocamentoSample1();
        Deslocamento deslocamento2 = new Deslocamento();
        assertThat(deslocamento1).isNotEqualTo(deslocamento2);

        deslocamento2.setId(deslocamento1.getId());
        assertThat(deslocamento1).isEqualTo(deslocamento2);

        deslocamento2 = getDeslocamentoSample2();
        assertThat(deslocamento1).isNotEqualTo(deslocamento2);
    }

    @Test
    void cadastroAlunoTest() {
        Deslocamento deslocamento = getDeslocamentoRandomSampleGenerator();
        CadastroAluno cadastroAlunoBack = getCadastroAlunoRandomSampleGenerator();

        deslocamento.setCadastroAluno(cadastroAlunoBack);
        assertThat(deslocamento.getCadastroAluno()).isEqualTo(cadastroAlunoBack);

        deslocamento.cadastroAluno(null);
        assertThat(deslocamento.getCadastroAluno()).isNull();
    }
}

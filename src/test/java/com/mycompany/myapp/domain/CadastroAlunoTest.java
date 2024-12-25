package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CadastroAlunoTestSamples.*;
import static com.mycompany.myapp.domain.DeslocamentoTestSamples.*;
import static com.mycompany.myapp.domain.ResponsavelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CadastroAlunoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CadastroAluno.class);
        CadastroAluno cadastroAluno1 = getCadastroAlunoSample1();
        CadastroAluno cadastroAluno2 = new CadastroAluno();
        assertThat(cadastroAluno1).isNotEqualTo(cadastroAluno2);

        cadastroAluno2.setId(cadastroAluno1.getId());
        assertThat(cadastroAluno1).isEqualTo(cadastroAluno2);

        cadastroAluno2 = getCadastroAlunoSample2();
        assertThat(cadastroAluno1).isNotEqualTo(cadastroAluno2);
    }

    @Test
    void responsaveisTest() {
        CadastroAluno cadastroAluno = getCadastroAlunoRandomSampleGenerator();
        Responsavel responsavelBack = getResponsavelRandomSampleGenerator();

        cadastroAluno.addResponsaveis(responsavelBack);
        assertThat(cadastroAluno.getResponsaveis()).containsOnly(responsavelBack);
        assertThat(responsavelBack.getCadastroAluno()).isEqualTo(cadastroAluno);

        cadastroAluno.removeResponsaveis(responsavelBack);
        assertThat(cadastroAluno.getResponsaveis()).doesNotContain(responsavelBack);
        assertThat(responsavelBack.getCadastroAluno()).isNull();

        cadastroAluno.responsaveis(new HashSet<>(Set.of(responsavelBack)));
        assertThat(cadastroAluno.getResponsaveis()).containsOnly(responsavelBack);
        assertThat(responsavelBack.getCadastroAluno()).isEqualTo(cadastroAluno);

        cadastroAluno.setResponsaveis(new HashSet<>());
        assertThat(cadastroAluno.getResponsaveis()).doesNotContain(responsavelBack);
        assertThat(responsavelBack.getCadastroAluno()).isNull();
    }

    @Test
    void deslocamentosTest() {
        CadastroAluno cadastroAluno = getCadastroAlunoRandomSampleGenerator();
        Deslocamento deslocamentoBack = getDeslocamentoRandomSampleGenerator();

        cadastroAluno.addDeslocamentos(deslocamentoBack);
        assertThat(cadastroAluno.getDeslocamentos()).containsOnly(deslocamentoBack);
        assertThat(deslocamentoBack.getCadastroAluno()).isEqualTo(cadastroAluno);

        cadastroAluno.removeDeslocamentos(deslocamentoBack);
        assertThat(cadastroAluno.getDeslocamentos()).doesNotContain(deslocamentoBack);
        assertThat(deslocamentoBack.getCadastroAluno()).isNull();

        cadastroAluno.deslocamentos(new HashSet<>(Set.of(deslocamentoBack)));
        assertThat(cadastroAluno.getDeslocamentos()).containsOnly(deslocamentoBack);
        assertThat(deslocamentoBack.getCadastroAluno()).isEqualTo(cadastroAluno);

        cadastroAluno.setDeslocamentos(new HashSet<>());
        assertThat(cadastroAluno.getDeslocamentos()).doesNotContain(deslocamentoBack);
        assertThat(deslocamentoBack.getCadastroAluno()).isNull();
    }
}

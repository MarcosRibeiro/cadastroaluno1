package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CadastroAluno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CadastroAluno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CadastroAlunoRepository extends JpaRepository<CadastroAluno, Long> {}

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Deslocamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Deslocamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeslocamentoRepository extends JpaRepository<Deslocamento, Long> {}

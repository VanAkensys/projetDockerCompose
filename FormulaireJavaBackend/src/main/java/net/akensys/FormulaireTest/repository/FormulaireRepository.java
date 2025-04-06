package net.akensys.FormulaireTest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.akensys.FormulaireTest.entity.Formulaire;

@Repository
public interface FormulaireRepository extends JpaRepository<Formulaire, Long> {
    List<Formulaire> findByClientId(Long clientId);
}

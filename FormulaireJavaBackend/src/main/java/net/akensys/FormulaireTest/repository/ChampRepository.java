package net.akensys.FormulaireTest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.akensys.FormulaireTest.entity.Champ;

@Repository
public interface ChampRepository extends JpaRepository<Champ, Long> {
    List<Champ> findByFormulaireIdOrderByOrdre(Long formulaireId);

    Optional<Champ> findByOrdreAndFormulaireId(Integer ordre, Long formulaireId);

    void deleteByFormulaireId(Long formulaireId);
}
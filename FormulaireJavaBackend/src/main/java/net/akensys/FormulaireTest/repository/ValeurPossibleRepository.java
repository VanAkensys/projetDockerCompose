package net.akensys.FormulaireTest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.akensys.FormulaireTest.entity.ValeurPossible;

@Repository
public interface ValeurPossibleRepository extends JpaRepository<ValeurPossible, Long> {
    List<ValeurPossible> findByChampIdOrderByOrdre(Long champId);

    void deleteByChampId(Long champId);
}
package net.akensys.FormulaireTest.repository;

import net.akensys.FormulaireTest.entity.ReferenceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceEntryRepository extends JpaRepository<ReferenceEntry, Long> {
    
    Optional<ReferenceEntry> findByReferenceId(String referenceId);

    Optional<ReferenceEntry> findByFormulaireId(Long formulaireId);

    List<ReferenceEntry> findByClientId(Long clientId);
}

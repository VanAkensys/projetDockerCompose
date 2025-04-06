package net.akensys.FormulaireTest.repository;

import net.akensys.FormulaireTest.entity.DataEntry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataEntryRepository extends JpaRepository<DataEntry, Long> {

     // 🔹 Récupérer les entrées d'un formulaire spécifique (triées par ordre)
     List<DataEntry> findByFormulaireIdOrderByOrdre(Long formulaireId);

     // 🔹 Récupérer toutes les entrées associées à une référence spécifique (triées par ordre)
     List<DataEntry> findByReferenceIdOrderByOrdre(String referenceId);
 
     // 🔹 Récupérer toutes les références distinctes pour un client donné
     List<DataEntry> findByClientId(Long clientId);

}


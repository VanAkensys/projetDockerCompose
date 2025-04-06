package net.akensys.FormulaireTest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "data_entry")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "formulaire_id", nullable = false)
    private Long formulaireId;

    @Column(name = "reference_id", nullable = false)
    private String referenceId; 

    @Column(name = "champ_id", nullable = false)
    private Long champId;

    @Column(name = "libelle", nullable = false)
    private String libelle;
    
    @Column(name = "ordre", nullable = false)
    private Integer ordre; 

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "client_id", nullable = false)
    private Long clientId; // Associe les entrées à un client spécifique
    
    @ElementCollection
    @CollectionTable(name = "data_entry_values", joinColumns = @JoinColumn(name = "data_entry_id"))
    @Column(name = "valeur")
    private List<String> valeur; // Stocke une ou plusieurs valeurs pour un champ (ex: checkbox, multi-liste)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}

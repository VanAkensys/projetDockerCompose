package net.akensys.FormulaireTest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reference_entry")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_id", unique = true, nullable = false)
    private String referenceId;

    @Column(name = "formulaire_id", nullable = false)
    private Long formulaireId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "author", nullable = false) 
    private String author;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

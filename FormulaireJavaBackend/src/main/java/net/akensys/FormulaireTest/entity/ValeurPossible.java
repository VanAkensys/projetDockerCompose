package net.akensys.FormulaireTest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "valeurs_possibles")
@Builder
@Data
public class ValeurPossible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "champ_id", nullable = false)
    private Champ champ;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String valeur;

    @Column(nullable = false)
    private Integer ordre;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

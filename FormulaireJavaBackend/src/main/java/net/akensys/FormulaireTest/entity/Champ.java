package net.akensys.FormulaireTest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import net.akensys.FormulaireTest.model.ChampType;

@Entity
@Table(name = "champs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Champ {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formulaire_id", nullable = false)
    private Formulaire formulaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChampType type; 

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private Integer ordre;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

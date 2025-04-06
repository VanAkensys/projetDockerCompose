package net.akensys.FormulaireTest.model;

import java.util.List;
import lombok.*;
import net.akensys.FormulaireTest.entity.Champ;
import net.akensys.FormulaireTest.entity.ValeurPossible;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFormulaireRequest {
    private String titre;
    private Long clientId;
    private List<Champ> champs;
    private Integer ordre;
    private List<ValeurPossible> valeursPossibles;
}

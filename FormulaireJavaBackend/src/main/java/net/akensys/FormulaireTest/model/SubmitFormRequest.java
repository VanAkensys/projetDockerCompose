package net.akensys.FormulaireTest.model;

import lombok.Data;
import java.util.List;

@Data
public class SubmitFormRequest {
    private Long formulaireId;
    private Long clientId;
    private String author; 
    private List<ChampReponse> champs;

    @Data
    public static class ChampReponse {
        private Long champId;
        private List<String> valeurs;  
    }
}

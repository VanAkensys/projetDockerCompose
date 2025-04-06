package net.akensys.FormulaireTest.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import net.akensys.FormulaireTest.entity.Client;
import net.akensys.FormulaireTest.entity.Formulaire;
import net.akensys.FormulaireTest.model.ChampType;
import net.akensys.FormulaireTest.model.CreateFormulaireRequest;
import net.akensys.FormulaireTest.repository.ClientRepository;
import net.akensys.FormulaireTest.service.FormulaireService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/formulaires")
public class FormulaireController {

    private final ClientRepository clientRepository; 
    private final FormulaireService formulaireService; 


    @GetMapping("/clients/{id}/formulaires")
    public ResponseEntity<List<Formulaire>> getFormulairesByClientId(@PathVariable Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));
        List<Formulaire> formulaires = formulaireService.getFormulairesByClientId(client.getId());
        return ResponseEntity.ok(formulaires);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getFormulaireDetails(@PathVariable Long id) {
        Map<String, Object> formulaireDetails = formulaireService.getChampsWithValeursByFormulaireId(id);
        return ResponseEntity.ok(formulaireDetails);
    }

    @PostMapping("/create")
    public ResponseEntity<Formulaire> createFormulaire(@RequestBody CreateFormulaireRequest request) {
        Formulaire savedForm = formulaireService.createFormulaire(
                request.getTitre(),
                request.getClientId(),
                request.getChamps(),
                request.getValeursPossibles()
        );
        return ResponseEntity.ok(savedForm);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Formulaire> updateFormulaire(
            @PathVariable Long id,
            @RequestBody CreateFormulaireRequest request) {
        
        Formulaire updatedFormulaire = formulaireService.updateFormulaire(
                id,
                request.getTitre(),
                request.getChamps(),
                request.getValeursPossibles()
        );

        return ResponseEntity.ok(updatedFormulaire);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String, String>> deleteFormulaire(@PathVariable Long id) {
        formulaireService.deleteFormulaire(id);

        // Renvoyer un JSON au lieu d'un String brut
        Map<String, String> response = Map.of("message", "Formulaire supprimé avec succès !");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getChampTypes")
    public ResponseEntity<List<String>> getChampTypes() {
        List<String> types = Arrays.stream(ChampType.values())
                                   .map(Enum::name)
                                   .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }


}
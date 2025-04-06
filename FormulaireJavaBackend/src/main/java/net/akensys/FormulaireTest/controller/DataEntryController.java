package net.akensys.FormulaireTest.controller;

import lombok.RequiredArgsConstructor;
import net.akensys.FormulaireTest.entity.DataEntry;
import net.akensys.FormulaireTest.model.SubmitFormRequest;
import net.akensys.FormulaireTest.service.DataEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data")
public class DataEntryController {

    private final DataEntryService dataEntryService;

    // API pour enregistrer les données
    @PostMapping("/submit")
    public ResponseEntity<?> submitForm(@RequestBody SubmitFormRequest request) {
        dataEntryService.saveFormData(request);
        return ResponseEntity.ok("{\"message\": \"Données enregistrées avec succès !\"}");
    }

    @GetMapping("/{formulaireId}")
    public ResponseEntity<List<Map<String, Object>>> getReferencesByFormulaireId(@PathVariable Long formulaireId) {
        List<Map<String, Object>> references = dataEntryService.getReferencesByFormulaireId(formulaireId);
        return ResponseEntity.ok(references);
    }

    // API pour récupérer les données par référence
    @GetMapping("/reference/{referenceId}")
    public ResponseEntity<Map<String, Object>> getStructuredDataByReferenceId(@PathVariable String referenceId) {
        Map<String, Object> structuredData = dataEntryService.getStructuredDataByReferenceId(referenceId);
        return ResponseEntity.ok(structuredData);
    }
    @GetMapping("/all-references/{clientId}")
    public ResponseEntity<List<Map<String, Object>>> getReferencesByClientId(@PathVariable Long clientId) {
        List<Map<String, Object>> references = dataEntryService.findReferencesByClientId(clientId);
        return ResponseEntity.ok(references);
    }

    @GetMapping("/all-references")
    public ResponseEntity<List<Map<String, Object>>> getAllReferences() {
        List<Map<String, Object>> references = dataEntryService.getAllReferences();
        return ResponseEntity.ok(references);
    }

    
}

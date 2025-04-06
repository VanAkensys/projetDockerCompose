package net.akensys.FormulaireTest.service;

import lombok.RequiredArgsConstructor;
import net.akensys.FormulaireTest.entity.Champ;
import net.akensys.FormulaireTest.entity.Client;
import net.akensys.FormulaireTest.entity.DataEntry;
import net.akensys.FormulaireTest.entity.Formulaire;
import net.akensys.FormulaireTest.entity.ReferenceEntry;
import net.akensys.FormulaireTest.model.SubmitFormRequest;
import net.akensys.FormulaireTest.repository.ChampRepository;
import net.akensys.FormulaireTest.repository.ClientRepository;
import net.akensys.FormulaireTest.repository.DataEntryRepository;
import net.akensys.FormulaireTest.repository.FormulaireRepository;
import net.akensys.FormulaireTest.repository.ReferenceEntryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class DataEntryService {

    private final DataEntryRepository dataEntryRepository;
    private final ClientRepository clientRepository;
    private final FormulaireRepository formulaireRepository;
    private final ChampRepository champRepository;
    private final ReferenceEntryRepository referenceEntryRepository;


    @Transactional
    public void saveFormData(SubmitFormRequest request) {

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Formulaire formulaire = formulaireRepository.findById(request.getFormulaireId())
                .orElseThrow(() -> new RuntimeException("Formulaire non trouvé"));
        String referenceId = UUID.randomUUID().toString();

        // 3️⃣ Créer une nouvelle entrée de référence avec `author`
        ReferenceEntry referenceEntry = new ReferenceEntry();
        referenceEntry.setReferenceId(referenceId);
        referenceEntry.setFormulaireId(request.getFormulaireId());
        referenceEntry.setClientId(request.getClientId());
        referenceEntry.setTitre(generateReferenceId(request.getAuthor(), formulaire.getTitre()));
        referenceEntry.setAuthor(request.getAuthor()); // 🔹 On stocke l’auteur renseigné
        referenceEntry.setCreatedAt(LocalDateTime.now());
        referenceEntryRepository.save(referenceEntry);

        // 4️⃣ Enregistrer les données du formulaire
        List<DataEntry> entries = new ArrayList<>();
        for (SubmitFormRequest.ChampReponse champReponse : request.getChamps()) {
            Champ champ = champRepository.findById(champReponse.getChampId())
                    .orElseThrow(() -> new RuntimeException("Champ ID " + champReponse.getChampId() + " non trouvé"));

            if (champReponse.getValeurs() == null || champReponse.getValeurs().isEmpty()) {
                throw new RuntimeException("Le champ '" + champ.getLibelle() + "' doit contenir une ou plusieurs valeurs.");
            }

            DataEntry dataEntry = new DataEntry();
            dataEntry.setFormulaireId(request.getFormulaireId());
            dataEntry.setReferenceId(referenceId);
            dataEntry.setClientId(request.getClientId());
            dataEntry.setChampId(champ.getId());
            dataEntry.setLibelle(champ.getLibelle());
            dataEntry.setType(champ.getType().toString());
            dataEntry.setOrdre(champ.getOrdre());
            dataEntry.setValeur(champReponse.getValeurs());
            dataEntry.setCreatedAt(LocalDateTime.now());

            entries.add(dataEntry);
        }

        // 5️⃣ Sauvegarde des entrées de données
        dataEntryRepository.saveAll(entries);
    }

    private String generateReferenceId(String clientNom, String formulaireTitre) {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return clientNom.replaceAll("\\s+", "_") + "-" + formulaireTitre.replaceAll("\\s+", "_") + "-" + dateStr;
    }

    public Map<String, Object> getStructuredDataByReferenceId(String referenceId) {
        // 🔹 Récupérer toutes les entrées associées à la référence
        List<DataEntry> entries = dataEntryRepository.findByReferenceIdOrderByOrdre(referenceId);
    
        if (entries.isEmpty()) {
            throw new RuntimeException("Aucune donnée trouvée pour la référence : " + referenceId);
        }
    
        // 🔹 Extraire `formulaireId` et `clientId` depuis la première entrée
        Long formulaireId = entries.get(0).getFormulaireId();
        Long clientId = entries.get(0).getClientId();
    
        // 🔹 Structurer les champs sous forme de liste
        List<Map<String, Object>> champsList = entries.stream()
                .map(entry -> Map.of(
                        "champId", entry.getChampId(),
                        "libelle", entry.getLibelle(),
                        "type", entry.getType(),
                        "ordre", entry.getOrdre(),
                        "valeurs", entry.getValeur()
                ))
                .collect(Collectors.toList());
    
        // 🔹 Construire l'objet final
        return Map.of(
                "formulaireId", formulaireId,
                "clientId", clientId,
                "champs", champsList
        );
    }

    public List<Map<String, Object>> getReferencesByFormulaireId(Long formulaireId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
        return referenceEntryRepository.findByFormulaireId(formulaireId)
                .stream()
                .map(reference -> {
                    Map<String, Object> referenceData = new HashMap<>();
                    referenceData.put("referenceId", reference.getReferenceId());
                    referenceData.put("formulaireId", reference.getFormulaireId());
                    referenceData.put("titre", reference.getTitre());
                    referenceData.put("author", reference.getAuthor());  // 🔹 Ajout de l'auteur
                    referenceData.put("date", reference.getCreatedAt().format(formatter)); // 🔹 Formatage de la date
    
                    return referenceData;
                })
                .toList();
    }

    public List<Map<String, Object>> findReferencesByClientId(Long clientId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
        return referenceEntryRepository.findByClientId(clientId)
                .stream()
                .map(reference -> {
                    Map<String, Object> referenceData = new HashMap<>();
                    referenceData.put("referenceId", reference.getReferenceId());
                    referenceData.put("formulaireId", reference.getFormulaireId());
                    referenceData.put("titre", reference.getTitre());
                    referenceData.put("author", reference.getAuthor());
                    referenceData.put("date", reference.getCreatedAt().format(formatter)); // 🔹 Formatage de la date
    
                    return referenceData;
                })
                .toList();
    }

    public List<Map<String, Object>> getAllReferences() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
        return referenceEntryRepository.findAll()
                .stream()
                .map(reference -> {
                    Map<String, Object> referenceData = new HashMap<>();
                    referenceData.put("referenceId", reference.getReferenceId());
                    referenceData.put("formulaireId", reference.getFormulaireId());
                    referenceData.put("titre", reference.getTitre());
                    referenceData.put("author", reference.getAuthor());  // 🔹 Ajout de l'auteur
                    referenceData.put("date", reference.getCreatedAt().format(formatter)); // 🔹 Formatage de la date
    
                    return referenceData;
                })
                .toList();
    }


}

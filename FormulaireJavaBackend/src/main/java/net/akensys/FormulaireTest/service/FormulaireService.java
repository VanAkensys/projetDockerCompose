package net.akensys.FormulaireTest.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.akensys.FormulaireTest.entity.Champ;
import net.akensys.FormulaireTest.entity.Client;
import net.akensys.FormulaireTest.entity.Formulaire;
import net.akensys.FormulaireTest.entity.ValeurPossible;
import net.akensys.FormulaireTest.repository.ChampRepository;
import net.akensys.FormulaireTest.repository.ClientRepository;
import net.akensys.FormulaireTest.repository.FormulaireRepository;
import net.akensys.FormulaireTest.repository.ValeurPossibleRepository;

@Service
@RequiredArgsConstructor
public class FormulaireService {
    private final ChampRepository champRepository;
    private final ValeurPossibleRepository valeurPossibleRepository;
    private final FormulaireRepository formulaireRepository;
    private final ClientRepository clientRepository; 


    public List<Formulaire> getFormulairesByClientId(Long clientId) {
        return formulaireRepository.findByClientId(clientId);
    }

        public Map<String, Object> getChampsWithValeursByFormulaireId(Long formulaireId) {
        // Récupérer le formulaire pour son titre
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new IllegalArgumentException("Formulaire non trouvé avec l'ID : " + formulaireId));

        // Récupérer les champs du formulaire
        List<Champ> champs = champRepository.findByFormulaireIdOrderByOrdre(formulaireId);

        // Construire la liste des champs avec leurs valeurs possibles
        List<Map<String, Object>> champsAvecValeurs = new ArrayList<>();
        for (Champ champ : champs) {
            Map<String, Object> champData = new LinkedHashMap<>(); // Utiliser LinkedHashMap ici
            champData.put("id", champ.getId());
            champData.put("type", champ.getType());
            champData.put("libelle", champ.getLibelle());
            champData.put("ordre", champ.getOrdre());

            if (champ.getType().accepteValeursMultiples()) {
                List<Map<String, Object>> valeursPossibles = valeurPossibleRepository.findByChampIdOrderByOrdre(champ.getId())
                        .stream()
                        .map(valeur -> {
                            Map<String, Object> valeurData = new LinkedHashMap<>();
                            valeurData.put("id", valeur.getId());
                            valeurData.put("libelle", valeur.getLibelle());
                            valeurData.put("valeur", valeur.getValeur());
                            valeurData.put("ordre", valeur.getOrdre());
                            return valeurData;
                        })
                        .toList();
                champData.put("valeursPossibles", valeursPossibles);
            }

            champsAvecValeurs.add(champData);
        }

        // Construire la réponse avec l'ordre désiré
        Map<String, Object> response = new LinkedHashMap<>(); // Utiliser LinkedHashMap ici
        response.put("id", formulaire.getId());
        response.put("titre", formulaire.getTitre()); // Inclure le titre avant les champs
        response.put("champs", champsAvecValeurs);

        return response;
    }

    @Transactional
    public Formulaire createFormulaire(String titre, Long clientId, List<Champ> champs, List<ValeurPossible> valeursPossibles) {
        // Vérifier si le client existe
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
    
        // Créer et sauvegarder le formulaire
        Formulaire formulaire = Formulaire.builder()
                .titre(titre)
                .client(client)
                .createdAt(LocalDateTime.now())
                .build();
        formulaire = formulaireRepository.save(formulaire);
    
        // Étape 1 : Sauvegarder tous les champs
        List<Champ> champsSauvegardes = new ArrayList<>();
        for (Champ champ : champs) {
            champ.setFormulaire(formulaire);
            champ.setCreatedAt(LocalDateTime.now());
            champ.setId(null); // Laisser Hibernate générer l'ID
            champ = champRepository.save(champ);
            champsSauvegardes.add(champ); // On stocke les champs sauvegardés
        }
    
        // Étape 2 : Associer les valeurs possibles aux bons champs
        for (ValeurPossible valeur : valeursPossibles) {
            // Trouver le champ associé en fonction de son libellé
            Champ champAssocie = champsSauvegardes.stream()
                    .filter(ch -> ch.getLibelle().equals(valeur.getChamp().getLibelle()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Champ associé non trouvé pour la valeur : " + valeur.getLibelle()));
    
            // Associer la valeur au bon champ et sauvegarder
            valeur.setChamp(champAssocie);
            valeur.setCreatedAt(LocalDateTime.now());
            valeurPossibleRepository.save(valeur);
        }
    
        return formulaire;
    }

    
    @Transactional
    public Formulaire updateFormulaire(Long formulaireId, String titre, List<Champ> champs, List<ValeurPossible> valeursPossibles) {
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new RuntimeException("Formulaire non trouvé"));

        formulaire.setTitre(titre);
        formulaire.setUpdatedAt(LocalDateTime.now());
        formulaire = formulaireRepository.save(formulaire);

        // 1️⃣ Récupérer les anciens champs
        List<Champ> anciensChamps = champRepository.findByFormulaireIdOrderByOrdre(formulaireId);

        // 2️⃣ Supprimer toutes les anciennes valeurs possibles pour éviter la duplication
        for (Champ champ : anciensChamps) {
            valeurPossibleRepository.deleteByChampId(champ.getId());
        }

        // 3️⃣ Mettre à jour les champs existants ou les créer
        List<Champ> nouveauxChamps = new ArrayList<>();
        for (Champ champ : champs) {
            final Integer champOrdre = champ.getOrdre();

            Champ champExistant = anciensChamps.stream()
                    .filter(c -> c.getOrdre().equals(champOrdre))
                    .findFirst()
                    .orElse(null);

            if (champExistant == null) {
                // Nouveau champ
                champ.setFormulaire(formulaire);
                champ.setCreatedAt(LocalDateTime.now());
                champ.setId(null); // Laisser Hibernate générer un nouvel ID
                champ = champRepository.save(champ);
            } else {
                // Mise à jour d'un champ existant
                champExistant.setLibelle(champ.getLibelle());
                champExistant.setType(champ.getType());
                champExistant = champRepository.save(champExistant);
                champ = champExistant;
            }

            nouveauxChamps.add(champ);
        }

        // 4️⃣ Associer les nouvelles valeurs possibles aux bons champs
        for (ValeurPossible valeur : valeursPossibles) {
            final Integer valeurChampOrdre = valeur.getChamp().getOrdre();

            Champ champAssocie = champRepository.findByOrdreAndFormulaireId(valeurChampOrdre, formulaire.getId())
                    .orElseThrow(() -> new RuntimeException("Champ associé non trouvé pour la valeur : " + valeur.getLibelle()));

            valeur.setChamp(champAssocie);
            valeur.setCreatedAt(LocalDateTime.now());
            valeurPossibleRepository.save(valeur);
        }

        return formulaire;
    }


    @Transactional
    public void deleteFormulaire(Long formulaireId) {
        // Vérifie si le formulaire existe
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new RuntimeException("Formulaire non trouvé"));

        // Supprimer toutes les valeurs possibles associées aux champs du formulaire
        List<Champ> champs = champRepository.findByFormulaireIdOrderByOrdre(formulaireId);
        for (Champ champ : champs) {
            valeurPossibleRepository.deleteByChampId(champ.getId());
        }

        // Supprimer les champs associés au formulaire
        champRepository.deleteByFormulaireId(formulaireId);

        // Supprimer le formulaire
        formulaireRepository.delete(formulaire);
    }
}
package net.akensys.FormulaireTest.model;

import java.util.EnumSet;
import java.util.Set;

public enum ChampType {
    TEXTE,              // Champ texte simple
    MOT_DE_PASSE,       // Mot de passe
    EMAIL,              // Adresse email
    TELEPHONE,          // Numéro de téléphone
    URL,                // Lien web
    RECHERCHE,          // Barre de recherche
    NOMBRE,             // Nombre
    ZONE_DE_TEXTE,      // Zone de texte multi-lignes
    DATE,               // Date
    HEURE,              // Heure
    DATE_HEURE,         // Date + Heure
    MOIS,               // Mois
    SEMAINE,            // Semaine
    BOUTON_RADIO,       // Boutons radio (choix unique)
    CASE_A_COCHER,      // Cases à cocher (choix multiples)
    LISTE_DEROUlANTE,   // Liste déroulante
    LISTE_MULTIPLE,     // Liste déroulante avec sélection multiple
    FICHIER,            // Upload de fichiers
    IMAGE,              // Image cliquable
    COULEUR,            // Sélecteur de couleurs
    CURSEUR,            // Curseur (slider)
    BARRE_DE_PROGRESSION, // Barre de progression
    CACHE,              // Champ caché
    CAPTCHA,            // Vérification anti-bot
    CODE_TEMPORAIRE,    // Code à usage unique (OTP)
    SIGNATURE,          // Signature électronique
    GLISSER_DEPOSER,    // Glisser-déposer
    LOCALISATION,       // Sélecteur de localisation
    SCANNEUR_CODE_BARRES, // Scanner QR code / code-barres
    SELECTION_EMOJI,    // Sélecteur d’emoji
    SAISIE_VOCALE;       // Entrée vocale

    private static final Set<ChampType> TYPES_AVEC_VALEURS_POSSIBLES = EnumSet.of(
        CASE_A_COCHER, BOUTON_RADIO, LISTE_DEROUlANTE, LISTE_MULTIPLE
    );

    // Méthode pour vérifier si un type peut avoir plusieurs valeurs
    public boolean accepteValeursMultiples() {
        return TYPES_AVEC_VALEURS_POSSIBLES.contains(this);
    }
}
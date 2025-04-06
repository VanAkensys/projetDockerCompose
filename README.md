# Formulaire Web – Projet Docker Fullstack

##  Description de l'application

Cette application web permet de **générer**, **modifier**, **remplir** et **consulter** des formulaires dynamiques.  
Elle est composée de :
-  **formulaire-angular** : le front-end en Angular (compilé et servi par NGINX),
-  **FormulaireJavaBackend** : le back-end en Java Spring Boot (REST API),
-  **MariaDB** : base de données relationnelle pour stocker les données des formulaires et utilisateurs.

---

## Objectif du projet

Ce projet a pour but d’apprendre à **conteneuriser une application fullstack** avec **Docker** :
- Gérer plusieurs services avec **Docker Compose**
- Automatiser la création de la base de données avec un script SQL
- Déployer un front Angular avec NGINX
- Interconnecter une API Spring Boot à une base MariaDB

---

## Installer Docker et Docker Compose

Merci de suivre les instructions officielles sur le site de Docker pour éviter toute erreur liée à la version de votre système :

- [Installer Docker sur Ubuntu (documentation officielle)](https://docs.docker.com/engine/install/ubuntu/)
- [Installer Docker Compose (documentation officielle)](https://docs.docker.com/compose/install/)

---

## Structure du projet

```text
projetDocker/
├── docker-compose.yml               # Fichier de configuration Docker
├── README.md                        # Ce fichier !
│
├── formulaire-angular/              # Frontend Angular compilé
│   ├── browser/                     # Dossier généré avec `ng build`
│   ├── Dockerfile                   # Image NGINX personnalisée pour Angular
│   └── nginx.conf                   # Configuration NGINX pour servir Angular
│
├── FormulaireJavaBackend/          # Backend Spring Boot
│   ├── Dockerfile                   # Dockerfile pour builder le backend
│   ├── HELP.md                      # Aide générée par Spring Initializr
│   ├── mvnw                         # Wrapper Maven Linux
│   ├── mvnw.cmd                     # Wrapper Maven Windows
│   ├── pom.xml                      # Fichier Maven de configuration
│   ├── src/                         # Code source Java
│   └── target/                      # Dossier de build Maven
```

## Lancer l'application

### Cloner le dépôt

```text
git clone https://github.com/VanAkensys/projetDockerCompose.git
cd projetDocker
```
### Construire et lancer les conteneurs
```text
docker-compose up --build
```
Attendre que les 3 services soient bien lancés (Angular + Backend + MariaDb).

### Accéder à l'application
 
Interface utilisateur (Angular + NGINX) : http://localhost:4200

API backend Spring Boot : http://localhost:8080

### Paramètres pour se connecter : 

**Utilisateur** : demoDocker

**Mot de passe** : demoDocker

### Pour arrêter tous les conteneurs :

```text
docker-compose down
```

## Auteur
TRINH Ngo Van Quoc
LP DEVOPS – p2203378

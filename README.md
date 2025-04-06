# Formulaire Web – Projet Docker Fullstack

## 📄 Description de l'application

Cette application web permet de **générer**, **modifier**, **remplir** et **consulter** des formulaires dynamiques.  
Elle est composée de :
- 🎨 **formulaire-angular** : le front-end en Angular (compilé et servi par NGINX),
- ⚙️ **FormulaireJavaBackend** : le back-end en Java Spring Boot (REST API),
- 🗄️ **MySQL** : base de données relationnelle pour stocker les données des formulaires et utilisateurs.

---

## 🎯 Objectif du projet

Ce projet a pour but d’apprendre à **conteneuriser une application fullstack** avec **Docker** :
- Gérer plusieurs services avec **Docker Compose**
- Automatiser la création de la base de données avec un script SQL
- Déployer un front Angular avec NGINX
- Interconnecter une API Spring Boot à une base MySQL

---

## 🐳 Installer Docker et Docker Compose

Merci de suivre les instructions officielles sur le site de Docker pour éviter toute erreur liée à la version de votre système :

- [Installer Docker sur Ubuntu (documentation officielle)](https://docs.docker.com/engine/install/ubuntu/)
- [Installer Docker Compose (documentation officielle)](https://docs.docker.com/compose/install/)

---

## 📁 Structure du projet

projetDocker/ ├── docker-compose.yml ├── README.md ├── formulaire-angular/ # Projet Angular compilé (ng build) │ └── ... ├── FormulaireJavaBackend/ # Backend Spring Boot (avec Dockerfile) │ └── ...

yaml
Copier
Modifier

---

## 🚀 Lancer l'application

### 1️⃣ Cloner le dépôt

git clone https://github.com/TON-UTILISATEUR/TON-REPO.git
cd projetDocker

2️⃣ Construire et lancer les conteneurs
bash
Copier
Modifier
docker-compose up --build
Attendre que les 3 services soient bien lancés (Angular + Backend + MySQL).

🌐 Accéder à l'application
Interface utilisateur (Angular + NGINX) : http://localhost:4200

API backend Spring Boot : http://localhost:8080

🛠️ Paramètres de base MySQL
Utilisateur : demoDocker

Mot de passe : demoDocker

🧯 Arrêter l'application
Pour arrêter tous les conteneurs :

bash
Copier
Modifier
docker-compose down
👤 Auteur
TRINH Ngo Van Quoc
LP DEVOPS – p2203378

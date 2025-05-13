# B2BTravel
## Description du Projet
**B2BTravel** est une application desktop développée avec JavaFX, destinée à faciliter la gestion des voyages professionnels pour les entreprises. Elle centralise les besoins liés aux utilisateurs, hébergements, réclamations, événements et fidélité, tout en offrant une interface intuitive et sécurisée.
### Objectifs :
- Simplifier la gestion des voyages d'affaires.
- Offrir une solution tout-en-un pour les gestionnaires RH, employés et prestataires de services.
- Automatiser les processus de réservation, de réclamation et de suivi de fidélité.
## Table des Matières
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Modules du Projet](#modules-du-projet)
- [Contribution](#contribution)
- [Collaborateurs](#collaborateurs)
- [Licence](#licence)
- [🙏 Remerciements](#-remerciements)
## Installation
1. Clonez le repository :
```bash
git clone https://github.com/ckthiri1/B2BTravel.git
cd B2BTravel
```
2. Importez le projet dans votre IDE (Eclipse, IntelliJ IDEA ou NetBeans) :
- Assurez-vous d'avoir installé JavaFX SDK
- Configurez le JavaFX SDK dans votre IDE

3. Configurez votre base de données :
- Modifiez les paramètres de connexion dans le fichier `src/main/resources/config/database.properties`
- Exécutez le script SQL fourni dans `src/main/resources/database/init.sql`

4. Compilez et exécutez le projet :
```bash
mvn clean install
mvn javafx:run
```
## Utilisation
Le projet repose sur les technologies suivantes :
- Java 17+
- JavaFX 17+
- FXML (pour les interfaces utilisateur)
- CSS (pour le style)
- MySQL (base de données relationnelle)
- Maven (gestionnaire de dépendances)

L'application se lance avec l'écran de connexion. Utilisez les identifiants par défaut (admin/admin) pour vous connecter en tant qu'administrateur.
## Modules du Projet
👤 Gestion des Utilisateurs
- Connexion / Inscription
- Rôles (admin, client)
- Profil utilisateur
- Nombre de voyages réalisés
- Reconnaissance faciale

🏨 Gestion des Hébergements
- Ajout, modification, suppression d'hébergements
- Réservation par les utilisateurs
- Filtres par type, ville, prix

📬 Gestion des Réclamations
- Envoi de réclamations avec pièces jointes
- Suivi de statut (en attente, traité, rejeté)
- Interface d'administration pour modération

✈️ Gestion des Voyages
- Planification de voyages
- Affectation de participants
- Intégration avec hébergements et événements

🎉 Gestion des Événements
- Création d'événements liés à un voyage
- Inscription utilisateurs
- Calendrier des événements

🎁 Gestion de la Fidélité
- Attribution de points en fonction des voyages
- Échange de points contre des récompenses
- Historique de fidélité

##  Contribution
Les contributions sont les bienvenues pour améliorer ce projet. Pour contribuer :
1. Forkez le projet.
2. Créez une branche (`git checkout -b feature/ma-nouvelle-fonctionnalite`).
3. Commitez vos changements (`git commit -m "Ajout de nouvelle fonctionnalité"`).
4. Poussez votre branche (`git push origin feature/ma-nouvelle-fonctionnalite`).
5. Créez une Pull Request.

## Collaborateurs
- [Chedi Kthiri](https://github.com/ckthiri1)
- [Ghassen Hchaichi](https://github.com/Hchaichi8)
- [Aziz Loueti](https://github.com/azizx0)
- [Hamza Mathlouthi](https://github.com/xmizou07)
- [Emna Gaied](https://github.com/amnagaied)
- [Islem Bouchaala](https://github.com/slayeeeem)

## Licence
Ce projet est sous licence MIT. Vous pouvez l'utiliser, le modifier et le redistribuer librement.

🔗 Dépôt GitHub : https://github.com/ckthiri1/B2BTravel
## 🙏 Remerciements
Un grand merci à Esprit School of Engineering pour le soutien pédagogique et l'encadrement tout au long de la réalisation de ce projet.

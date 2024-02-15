# Projet de Fin d'Études (PFE) - Générateur de Builds pour Wakfu
Ce projet est une application web conçue pour faciliter la création de builds pour les joueurs du MMORPG Wakfu. Les builds, qui représentent des configurations d'équipement dans le jeu, sont souvent complexes à concevoir et à optimiser. Notre application vise à simplifier ce processus en permettant aux utilisateurs de générer automatiquement des builds personnalisés en fonction de leurs besoins et de leurs préférences.

# Objectifs du Projet
Les principaux objectifs de ce projet sont les suivants :

* Offrir aux joueurs de Wakfu un outil convivial et intuitif pour créer et personnaliser leurs builds.
* Utiliser les dernières technologies de développement pour garantir des performances optimales et une expérience utilisateur fluide.
* Implémenter un procédé de génération de builds basé sur les spécificités du jeu.

# Technologies Utilisées
Le projet est développé en utilisant les technologies suivantes :

* Spring Boot pour le développement du backend. Spring Boot offre une plateforme robuste et flexible pour la création d'applications Java basées sur des services.
* Angular pour le développement du frontend. Angular est un framework JavaScript moderne et puissant pour la création d'interfaces utilisateur dynamiques et réactives.
* MongoDB comme système de gestion de base de données NoSQL. MongoDB offre une grande flexibilité pour stocker et manipuler les données de manière efficace.

# Prérequis
Avant de lancer l'application, assurez-vous d'avoir installé Docker et Docker Compose sur votre système.

# Installation et utilisation

## Clonez ce dépôt Git sur votre machine locale en utilisant la commande suivante :
1. git clone https://github.com/Tarchaud/PFE/

## Naviguez vers le répertoire cloné :
2. cd path/to/PFE

## Lancer l'application :
3. docker-compose up

## Arrêter l'application :
4. Ctrl+C ou docker-compose down

# To do
back :
- get stats
connexion / inscription:
- retourner une erreur si déjà inscrit


front:
- affichage des stats
- pouvoir cliquer sur un item du build
- connexion / inscription

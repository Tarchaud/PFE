# PFE 
Projet d'application Web permettant aux joueurs du MMORPG Wakfu de générer automatiquement leurs builds, qui sont des pages d'équipements du jeu.

# Technos
Fait en Spring Boot pour le Back, Angular pour le front, Mongo DB pour la base de données.

# Lancer l'application
docker-compose up --build

# To do
- Faire la persistence de données avec mongo DB pour les opérations CRUD codées, cf https://www.youtube.com/watch?v=qVNOw9TWwxo
- Ajouter les items dans le model Build
- Faire des routes spéciales pour les Items en fonction des equipmentItemTypes (plusieurs en paramètres)
- Modifier l'accès aux données (aller les chercher sur mongoDB), en initialisant la db avec les json

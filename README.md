# PFE 
Projet d'application Web permettant aux joueurs du MMORPG Wakfu de générer automatiquement leurs builds, qui sont des pages d'équipements du jeu.

# Technos
Fait en Spring Boot pour le Back, Angular pour le front, Mongo DB pour la base de données.

# Lancer l'application
docker-compose up

# Arrêter l'application
docker-compose down

# To do
- générer les items en fonction d'effects croissants:
-> récupérer le premier param pour un action ID d'un item
-> trier les items par premier param croissant

- firebase pour avoir déjà des builds?
- les int en float
- parentId de equipmentItemType en string
- connexion / inscription
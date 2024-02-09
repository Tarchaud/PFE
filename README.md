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

- cas particulier des familiers :
Mettre un familier de base, mais aussi chercher s'il y en a pas un qui correspond à un des effets choisis. Pour ça, on va les prendre dans l'ordre les effets, et on demande à l'user de les rentrer (les effets) par ordre d'importance.
+ Gérer les cas où on pourrait avoir un familier quand même : on peut les avoir dans le filtre pour un cost low, et pour les lvls 0-15, si on prend divers effects

- firebase pour avoir déjà des builds?
- les int en float
- parentId de equipmentItemType en string
- connexion / inscription
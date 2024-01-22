# PFE 
Projet d'application Web permettant aux joueurs du MMORPG Wakfu de générer automatiquement leurs builds, qui sont des pages d'équipements du jeu.

# Technos
Fait en Spring Boot pour le Back, Angular pour le front, Mongo DB pour la base de données.

# Faire fonctionner le back
docker build -t springboot/wakfubuilderapp .

docker run -p 8080:8080 springboot/wakfubuilderapp

# To do
- Faire la persistence de données avec mongo DB pour les opérations CRUD codées, cf https://www.mongodb.com/compatibility/spring-boot

- Faire le front avec Angular

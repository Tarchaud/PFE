#!/bin/bash

echo "Test d'initialisation de db avec juste actions.json pour l'instant"

filename="/docker-entrypoint-initdb.d/actions.json"
collection=$(basename "$filename" .json)
mongoimport --db PFE_Wakfu --type json --collection "$collection" --file "$filename" --jsonArray
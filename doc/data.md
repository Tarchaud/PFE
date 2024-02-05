# Documentation Data

## Item

### Item Rarity

Voici à quoi correspond la valeur de la variable rarity :

```js
{
    0: "Commun",
    1: "Inhabituel",
    2: "Rare",
    3: "Mythique",
    4: "Légendaire",
    5: "Relique",
    6: "Souvenir",
    7: "Epique"
}
```

## Action

### Element

Voici à quoi correspond les morceaux de string dans les descriptions des actions  

```json
{
    "[el1]": "Feu",
    "[el2]": "Eau",
    "[el3]": "Terre",
    "[el4]": "Air",
}
```

### Parsing des descriptions des actions dans le front

On utilise le module NodeJs `Waktrinser` qui permet remplacer les arguments dans la description d'une action

```ts
//Exemple avec la description  'Renvoie |[#7.3]*100|% des dégâts'
import Waktrinser from '@methodwakfu-public/waktrinser';
console.log(Waktrinser.decodeString('Renvoie |[#7.3]*100|% des dégâts', [0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0.001], 100));

//Sortie de la fonction
'Renvoie 10% des dégâts'
```

package com.pfe.wakfubuilder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Build;
import com.pfe.wakfubuilder.model.Item;
import com.pfe.wakfubuilder.model.Item.DefinitionEffect;
import com.pfe.wakfubuilder.repository.BuildRepository;
import com.pfe.wakfubuilder.repository.ItemRepository;

@Service
public class BuildService {

    private final BuildRepository buildRepository;
    private final ItemRepository itemRepository;
    private final EquipmentItemTypeService equipmentItemTypeService;
    private final ItemService itemService;

    public BuildService(BuildRepository buildRepository, ItemRepository itemRepository, EquipmentItemTypeService equipmentItemTypeService, ItemService itemService) {
        this.buildRepository = buildRepository;
        this.itemRepository = itemRepository;
        this.equipmentItemTypeService = equipmentItemTypeService;
        this.itemService = itemService;
    }

    public List<Build> getBuilds() {
        return buildRepository.findAll();
    }

    public Build getBuild(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        ObjectId objectId = new ObjectId(id);
        return buildRepository.findById(objectId).orElse(null);
    }

    public void deleteBuild(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        ObjectId objectId = new ObjectId(id);
        buildRepository.deleteById(objectId);
    }    

    public void saveBuild(Build build) {
        if (build == null) {
            throw new IllegalArgumentException("Build cannot be null");
        }
        buildRepository.save(build);
    }

    public void updateBuild(String id, Build build) {
        ObjectId objectId = new ObjectId(id);
        build.setId(objectId);
        buildRepository.save(build);
    }

    public Build generateBuild(String name, int level, Build.Cost cost, List<Integer> effects) {

        // Récupérer les raretés en fonction du coût
        List<Integer> rarities;
        switch (cost) {
            case low:
                rarities = Arrays.asList(1, 2, 3);
                break;
            case medium:
                rarities = Collections.singletonList(3);
                break;
            case high:
                rarities = Arrays.asList(4, 5, 6, 7);
                break;
            default:
                throw new IllegalArgumentException("Invalid cost: " + cost);
        }

        // Filtrer les items en fonction des critères spécifiés
        List<Item> filteredItems;
        if (level <= 15) {
            filteredItems = itemRepository.findByCriteria(1, level, rarities, effects);
        } else {
            filteredItems = itemRepository.findByCriteria(level-15, level, rarities, effects);
        }

        // Sélectionne un item pour chaque equipmentPositions possible
        List<Item> selectedItems = selectItems(filteredItems, level, effects);

        Build build = new Build();
        build.setName(name);
        build.setLevel(level);
        build.setCost(cost);
        build.setEffects(effects);
        build.setItems(selectedItems.toArray(new Item[selectedItems.size()]));
        saveBuild(build);

        return build;
    }

    private List<Item> selectItems(List<Item> filteredItems, Integer level, List<Integer> effects) {

        // Hashmap avec les positions possibles des items en clés
        Map<String, Item> equipmentPositionsMap = new LinkedHashMap<>();

        equipmentPositionsMap.put("PET", null);
        equipmentPositionsMap.put("", null); // equipmentPosition des montures = []
        equipmentPositionsMap.put("FIRST_WEAPON", null);
        equipmentPositionsMap.put("SECOND_WEAPON", null);
        equipmentPositionsMap.put("LEFT_HAND", null);
        equipmentPositionsMap.put("RIGHT_HAND", null);
        
        equipmentPositionsMap.put("BACK", null);
        equipmentPositionsMap.put("LEGS", null);
        equipmentPositionsMap.put("NECK", null);
        equipmentPositionsMap.put("BELT", null);
        equipmentPositionsMap.put("HEAD", null);
        equipmentPositionsMap.put("CHEST", null);
        equipmentPositionsMap.put("SHOULDERS", null);
        equipmentPositionsMap.put("ACCESSORY", null);
    
        // On s'occupera de ces cas particuliers à part
        List<String> specialPositions = Arrays.asList(
            "PET",
            "",
            "FIRST_WEAPON",
            "SECOND_WEAPON",
            "LEFT_HAND",
            "RIGHT_HAND"
        );

        // On parcourt les equipmentPositions
        for (String equipmentPosition : equipmentPositionsMap.keySet()) {

            // Si l'equipmentPosition est un cas particulier
            if (specialPositions.contains(equipmentPosition)) {

                // Familier
                if (equipmentPosition.equals("PET")) {
                    setOptimalPet(filteredItems, level, effects, equipmentPositionsMap);
                } 
                // Monture
                else if (equipmentPosition.equals("")) {
                    setOptimalMount(filteredItems, level, equipmentPositionsMap);
                } 
                // Armes
                else if (equipmentPosition.equals("FIRST_WEAPON")) {
                    setOptimalWeapon(filteredItems, effects, equipmentPositionsMap);
                }
                else if (equipmentPosition.equals("SECOND_WEAPON")) {
                    continue;
                }
                // Anneaux
                else if (equipmentPosition.equals("LEFT_HAND")) {
                    setLeftRing(filteredItems, effects, equipmentPositionsMap);
                }
                else if (equipmentPosition.equals("RIGHT_HAND")) {
                    setRightRing(filteredItems, effects, equipmentPositionsMap);
                }

            } else {

                // On récupère tous les items qui ont l'equipmentPosition courante
                List<Item> itemsWithGivenEquipmentPosition = filteredItems.stream()
                    .filter(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).contains(equipmentPosition))
                    .collect(Collectors.toList());
            
                // On ajoute à la hashmap l'item optimal pour l'equipmentPosition courante
                equipmentPositionsMap.put(equipmentPosition, getOptimalItem(itemsWithGivenEquipmentPosition, effects));
                
                // On supprime de la liste des items filtrés tous ceux qui ont l'equipmentPosition courante
                filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).contains(equipmentPosition));
     
                }
        }
        
        return new ArrayList<>(equipmentPositionsMap.values());
    }

    private void setOptimalPet(List<Item> filteredItems, Integer level, List<Integer> effects, Map<String, Item> equipmentPositionsMap) {
        
        // On supprime tous les familiers de liste des items filtrés au cas où il y en aurait
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals("PET"));

        // Liste des familiers du jeu
        // On utilise cette liste car les familier sont éliminés quasi systématiquement par les filtres
        List<Item> pets = itemRepository.findByEquipmentItemTypeIds(Collections.singletonList(582));

        // S'il n'existe pas de familier avec au moins un effet demandé
        if (itemService.getItemsWithTheMostEffects(pets, effects).isEmpty()) {
            // Si le level n'est pas suffisant
            if (level < 36) {
                // Ajouter le gelutin comme familier de base à l'emplacement des familiers 
                Item gelutin = itemRepository.findById(12237);
                equipmentPositionsMap.put("PET", gelutin);

            }
            // Sinon on peut mettre un meilleur familier de base
            else {
                // Ajouter le peroucan comme familier de base à l'emplacement des familiers 
                Item peroucan = itemRepository.findById(14887);
                equipmentPositionsMap.put("PET", peroucan);
            }
        } else {
            // On enregistre le familier optimal pour l'emplacement des familiers
            equipmentPositionsMap.put("PET", getOptimalItem(pets, effects));
        }
    }

    private void setOptimalMount(List<Item> filteredItems, Integer level, Map<String, Item> equipmentPositionsMap) {

        // On peut directement supprimer toutes les montures car elles offrent toutes le même bonus
        // Pareil que les familiers, les montures sont éliminées quasi systématiquement par les filtres
        filteredItems.removeIf(item -> equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()).length == 0);

        // Si le level est suffisant
        if (level >= 35) {
            // Ajouter la dragodinde comme item de base à l'emplacement des montures
            Item dragodinde = itemRepository.findById(18682);
            equipmentPositionsMap.put("", dragodinde);
        }

    }

    private void setOptimalWeapon(List<Item> filteredItems, List<Integer> effects, Map<String, Item> equipmentPositionsMap) {

        // En bdd : les armes à deux mains sont appelées "FIRST_WEAPON", et elles ont l'equipmentPosition "SECOND_WEAPON" désactivée
        // Il y a besoin d'un traitement particulier pour savoir si une arme à une main + une dague / un bouclier est meilleure qu'une arme à deux mains

        // Les armes à une main
        List<Integer> itemTypeIdsOfOneHandedWeapons = Arrays.asList(108, 110, 113, 115, 254);
        List<Item> oneHandedWeapons = filteredItems.stream()
            .filter(item -> itemTypeIdsOfOneHandedWeapons.contains(item.getBaseParameters().getItemTypeId()))
            .collect(Collectors.toList());

        Item oneHandedWeaponSelected = getOptimalItem(oneHandedWeapons, effects);

        // On récupère la somme de l'arme à une main sélectionnée
        float sumForOneHandedWeapon = 0.0f;

        if (oneHandedWeaponSelected != null) {
            for (DefinitionEffect definitionEffect : oneHandedWeaponSelected.getDefinitionsEffect()) {
                if (effects.contains(definitionEffect.getActionId())) {
                    sumForOneHandedWeapon += definitionEffect.getParams()[0];
                }
            }
        }

        // Les dagues et les boucliers
        List<Item> secondWeapons = filteredItems.stream()
            .filter(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).contains("SECOND_WEAPON"))
            .collect(Collectors.toList());

        Item secondWeaponSelected = getOptimalItem(secondWeapons, effects);

        // On récupère la somme de la seconde arme sélectionnée
        float sumForSecondWeapon = 0.0f;

        if (secondWeaponSelected != null) {
            for (DefinitionEffect definitionEffect : secondWeaponSelected.getDefinitionsEffect()) {
                if (effects.contains(definitionEffect.getActionId())) {
                    sumForSecondWeapon += definitionEffect.getParams()[0];
                }
            }
        }

        float sumOfBothWeapons = sumForOneHandedWeapon + sumForSecondWeapon;

        // Les armes à deux mains
        List<Integer> itemTypeIdsOfTwoHandedWeapons = Arrays.asList(101, 253, 111, 114, 117, 223);
        List<Item> twoHandedWeapons = filteredItems.stream()
            .filter(item -> itemTypeIdsOfTwoHandedWeapons.contains(item.getBaseParameters().getItemTypeId()))
            .collect(Collectors.toList());

        Item twoHandedWeaponSelected = getOptimalItem(twoHandedWeapons, effects);

        // On récupère la somme de l'arme à deux mains sélectionnée

        float sumForTwoHandedWeapon = 0.0f;

        if (twoHandedWeaponSelected != null) {
            for (DefinitionEffect definitionEffect : twoHandedWeaponSelected.getDefinitionsEffect()) {
                if (effects.contains(definitionEffect.getActionId())) {
                    sumForTwoHandedWeapon += definitionEffect.getParams()[0];
                }
            }
        }

        // Si la somme des deux armes est supérieure à la somme de l'arme à deux mains
        if (sumOfBothWeapons > sumForTwoHandedWeapon) {
            // On ajoute à la hashmap l'arme à une main sélectionnée
            equipmentPositionsMap.put("FIRST_WEAPON", oneHandedWeaponSelected);
            // On ajoute à la hashmap la seconde arme sélectionnée
            equipmentPositionsMap.put("SECOND_WEAPON", secondWeaponSelected);
        } else {
            // On ajoute à la hashmap l'arme à deux mains sélectionnée
            equipmentPositionsMap.put("FIRST_WEAPON", twoHandedWeaponSelected);
            // L'item sélectionné pour l'emplacement "SECOND_WEAPON" est null
        }
        
        // On supprime les armes de la liste des items filtrés
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals("FIRST_WEAPON"));
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals("SECOND_WEAPON"));
    }

    private void setLeftRing (List<Item> filteredItems, List<Integer> effects, Map<String, Item> equipmentPositionsMap) {

        // On récupère les anneaux
        // Dans la db, les anneaux sont à l'equipmentPosition ["LEFT_HAND", "RIGHT_HAND"]
        List<Item> rings = filteredItems.stream()
            .filter(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Arrays.asList("LEFT_HAND", "RIGHT_HAND")))
            .collect(Collectors.toList());
            
        // On récupère le meilleur anneau
        Item leftRingSelected = getOptimalItem(rings, effects);

        // On ajoute à la hashmap l'anneau sélectionné
        equipmentPositionsMap.put("LEFT_HAND", leftRingSelected);

        // On le retire de la liste des items filtrés pour récupérer le deuxième meilleur anneau ensuite
        filteredItems.remove(leftRingSelected);
    }

    private void setRightRing(List<Item> filteredItems, List<Integer> effects, Map<String, Item> equipmentPositionsMap) {
        
        List<Item> rings = filteredItems.stream()
            .filter(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Arrays.asList("LEFT_HAND", "RIGHT_HAND")))
            .collect(Collectors.toList());

        // On est obligés de faire un traitement particulier aussi ici
        // Car RIGHT_HAND ne correspond pas à une position en db
        equipmentPositionsMap.put("RIGHT_HAND", getOptimalItem(rings, effects));
        
        // On retire tous les anneaux restants de la liste des items filtrés
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Arrays.asList("LEFT_HAND", "RIGHT_HAND")));

    }

    private Item getOptimalItem(List<Item> items, List<Integer> effects) {

        List<Item> itemsWithTheMostEffects = itemService.getItemsWithTheMostEffects(items, effects);

        // Somme pour le set d'items
        float sumForSet = 0.0f;

        // Somme pour un item en particulier
        float sumForItem = 0.0f;

        // L'item qu'on va sélectionner
        Item itemSelected = null;

        // On parcourt le set d'items
        for (Item item : itemsWithTheMostEffects) {

            // On réinitialise la somme pour l'item courant
            sumForItem = 0.0f;

            // On récupère les definitionsEffect de l'item courant, qui sont les effets qu'il offre
            List<DefinitionEffect> definitionsEffectOfItem = item.getDefinitionsEffect();

            for (DefinitionEffect definitionEffect : definitionsEffectOfItem) {

                // Si l'effet courant est dans la liste des effets demandés
                if (effects.contains(definitionEffect.getActionId())) {
                    // On ajoute la valeur de l'effet à la somme pour l'item courant
                    sumForItem += definitionEffect.getParams()[0];
                }
            }

            // Si la somme pour l'item courant est supérieure à la somme pour le set d'items
            if (sumForItem > sumForSet) {
                // On met à jour l'item sélectionné et la somme pour le set d'items
                itemSelected = item;
                sumForSet = sumForItem;
            }

        }

        return itemSelected;
    }
    
}

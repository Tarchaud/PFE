package com.pfe.wakfubuilder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
                rarities = Arrays.asList(4, 5, 6);
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
        HashMap<List<String>, Item> equipmentPositionsMap = new HashMap<>();

        equipmentPositionsMap.put(Collections.singletonList("PET"), null);
        equipmentPositionsMap.put(new ArrayList<>(), null); // equipmentPosition des montures = []
        equipmentPositionsMap.put(Collections.singletonList("BACK"), null);
        equipmentPositionsMap.put(Collections.singletonList("LEGS"), null);
        equipmentPositionsMap.put(Collections.singletonList("FIRST_WEAPON"), null);
        equipmentPositionsMap.put(Collections.singletonList("SECOND_WEAPON"), null);
        equipmentPositionsMap.put(Collections.singletonList("NECK"), null);
        equipmentPositionsMap.put(Collections.singletonList("BELT"), null);
        equipmentPositionsMap.put(Collections.singletonList("HEAD"), null);
        equipmentPositionsMap.put(Collections.singletonList("CHEST"), null);
        equipmentPositionsMap.put(Collections.singletonList("SHOULDERS"), null);
        equipmentPositionsMap.put(Collections.singletonList("ACCESSORY"), null);
        equipmentPositionsMap.put(Arrays.asList("LEFT_HAND", "RIGHT_HAND"), null);
    
        // On s'occupe des cas particuliers des familiers et des montures à part
        
        // Liste des familiers du jeu
        List<Item> pets = itemRepository.findByEquipmentItemTypeIds(Collections.singletonList(582));
        getOptimalPet(filteredItems, level, effects, equipmentPositionsMap, pets);

        getOptimalMount(filteredItems, level, equipmentPositionsMap);

        float totalValue = 0.0f;
        float totalValueOfTheItem = 0.0f;

        // On parcourt les equipmentPositions
        for (List<String> equipmentPosition : equipmentPositionsMap.keySet()) {

            // Si l'equipmentPosition est celle des familiers ou des montures, passer à l'itération suivante
            if (equipmentPosition.equals(Collections.singletonList("PET")) || equipmentPosition.isEmpty()) {
                continue;
            }

            totalValue = 0.0f;

            // On récupère tous les items qui ont l'equipmentPosition courante
            List<Item> itemsWithGivenEquipmentPosition = filteredItems.stream()
                .filter(item -> equipmentPosition.equals(Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()))))
                .collect(Collectors.toList());
        
            // On récupère les items qui ont le plus d'effets demandés
            List<Item> itemsWithTheMostEffects = itemService.getItemsWithTheMostEffects(itemsWithGivenEquipmentPosition, effects);
                  
            // On cherche à savoir quel item, parmi ceux qui ont le plus d'effets demandés,
            // a la plus grande somme de ses effets / celui qui donne le meilleur bonus
            for (Item item : itemsWithTheMostEffects) {

                totalValueOfTheItem = 0.0f;

                // On récupère les definitionsEffect de l'item, qui sont les effets de l'item
                List<DefinitionEffect> definitionsEffectOfTheItem = item.getDefinitionsEffect();

                for (DefinitionEffect definitionEffect : definitionsEffectOfTheItem) {

                    // Si l'id de l'effet de l'item est dans la liste des ids des effets demandés
                    if (effects.contains(definitionEffect.getActionId())) {

                        // On l'ajoute à la valeur totale de l'item
                        totalValueOfTheItem += definitionEffect.getParams()[0];

                    }
                }

                // Si la valeur total de l'item est supérieure à la valeur globale du set d'items
                if (totalValueOfTheItem > totalValue) {

                    // On définit l'item courant à la position courante
                    equipmentPositionsMap.put(equipmentPosition, item);

                    // La valeur totale du set d'item devient la valeur totale de l'item courant
                    totalValue = totalValueOfTheItem;
                }
            }
            
            // On supprime de la liste des items filtrés tous ceux qui ont l'equipmentPosition courante
            filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(equipmentPosition));
            
        }
        
        return new ArrayList<>(equipmentPositionsMap.values());
    }

    private void getOptimalPet(List<Item> filteredItems, Integer level, List<Integer> effects, HashMap<List<String>, Item> equipmentPositionsMap, List<Item> pets) {
        
        // Si le level n'est pas suffisant
        if (level < 36) {
            // Ajouter le gelutin comme familier de base à l'emplacement des familiers 
            Item gelutin = itemRepository.findById(12237);
            equipmentPositionsMap.put(Collections.singletonList("PET"), gelutin);

        }
        // Sinon on peut mettre un meilleur familier de base
        else {
            // Ajouter le peroucan comme familier de base à l'emplacement des familiers 
            Item peroucan = itemRepository.findById(14887);
            equipmentPositionsMap.put(Collections.singletonList("PET"), peroucan);
        }

        // On supprime tous les familiers de liste des items filtrés au cas où il y en aurait
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Collections.singletonList("PET")));

        // Même principe que pour les items, mais pas sur la même liste (ici pets au lieu de filteredItems)
        List<Item> petsWithTheMostEffects = itemService.getItemsWithTheMostEffects(pets, effects);

        float totalValue = 0.0f;
        float totalValueOfThePet = 0.0f;

        for (Item pet : petsWithTheMostEffects) {
            totalValueOfThePet = 0.0f;
            List<DefinitionEffect> definitionsEffectOfThePet = pet.getDefinitionsEffect();

            for (DefinitionEffect definitionEffect : definitionsEffectOfThePet) {
                if (effects.contains(definitionEffect.getActionId())) {
                    totalValueOfThePet += definitionEffect.getParams()[0];
                }
            }

            if (totalValueOfThePet > totalValue) {
                equipmentPositionsMap.put(Collections.singletonList("PET"), pet);
                totalValue = totalValueOfThePet;
            }
        }
    }

    private void getOptimalMount(List<Item> filteredItems, Integer level, HashMap<List<String>, Item> equipmentPositionsMap) {

        // Si le level est suffisant
        if (level >= 35) {
            // Ajouter la dragodinde comme item de base à l'emplacement des montures
            Item dragodinde = itemRepository.findById(18682);
            equipmentPositionsMap.put(new ArrayList<>(), dragodinde);
        }

        // On peut directement supprimer toutes les montures car elles offrent toutes le même bonus
        filteredItems.removeIf(item -> equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()).length == 0);

    }
    
}

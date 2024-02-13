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
        equipmentPositionsMap.put(Collections.singletonList("FIRST_WEAPON"), null);
        equipmentPositionsMap.put(Collections.singletonList("SECOND_WEAPON"), null);
        equipmentPositionsMap.put(Arrays.asList("LEFT_HAND", "RIGHT_HAND"), null);

        equipmentPositionsMap.put(Collections.singletonList("BACK"), null);
        equipmentPositionsMap.put(Collections.singletonList("LEGS"), null);
        equipmentPositionsMap.put(Collections.singletonList("NECK"), null);
        equipmentPositionsMap.put(Collections.singletonList("BELT"), null);
        equipmentPositionsMap.put(Collections.singletonList("HEAD"), null);
        equipmentPositionsMap.put(Collections.singletonList("CHEST"), null);
        equipmentPositionsMap.put(Collections.singletonList("SHOULDERS"), null);
        equipmentPositionsMap.put(Collections.singletonList("ACCESSORY"), null);
    
        // On s'occupera de ces cas particuliers à part

        List<List<String>> specialPositions = Arrays.asList(
            Collections.singletonList("PET"),
            Collections.emptyList(),
            Collections.singletonList("FIRST_WEAPON"),
            Collections.singletonList("SECOND_WEAPON"),
            Arrays.asList("LEFT_HAND", "RIGHT_HAND")
        );

        // On parcourt les equipmentPositions
        for (List<String> equipmentPosition : equipmentPositionsMap.keySet()) {

            // Si l'equipmentPosition est un cas particulier
            if (specialPositions.contains(equipmentPosition)) {

                // Familier
                if (equipmentPosition.equals(Collections.singletonList("PET"))) {
                    List<Item> pets = itemRepository.findByEquipmentItemTypeIds(Collections.singletonList(582));
                    setOptimalPet(filteredItems, level, effects, equipmentPositionsMap, pets);
                } 
                // Monture
                else if (equipmentPosition.isEmpty()) {
                    setOptimalMount(filteredItems, level, equipmentPositionsMap);
                } 
                // Armes
                else if (equipmentPosition.equals(Collections.singletonList("FIRST_WEAPON"))) {
                    setOptimalWeapon(filteredItems, effects, equipmentPositionsMap);
                }
                else if (equipmentPosition.equals(Collections.singletonList("SECOND_WEAPON"))) {
                    continue;
                }
                // Anneaux
                else if (equipmentPosition.equals(Arrays.asList("LEFT_HAND", "RIGHT_HAND"))) {
                    setOptimalRings(filteredItems, effects, equipmentPositionsMap);
                }

            } else {

                // On récupère tous les items qui ont l'equipmentPosition courante
                List<Item> itemsWithGivenEquipmentPosition = filteredItems.stream()
                    .filter(item -> equipmentPosition.equals(Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()))))
                    .collect(Collectors.toList());
            
                // On récupère les items qui ont le plus d'effets demandés
                List<Item> itemsWithTheMostEffects = itemService.getItemsWithTheMostEffects(itemsWithGivenEquipmentPosition, effects);
                    
                // On ajoute à la hashmap l'item optimal pour l'equipmentPosition courante
                equipmentPositionsMap.put(equipmentPosition, getOptimalItem(itemsWithTheMostEffects, effects));
                
                // On supprime de la liste des items filtrés tous ceux qui ont l'equipmentPosition courante
                filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(equipmentPosition));
     
                }
        }
        
        return new ArrayList<>(equipmentPositionsMap.values());
    }

    private void setOptimalPet(List<Item> filteredItems, Integer level, List<Integer> effects, HashMap<List<String>, Item> equipmentPositionsMap, List<Item> pets) {
        
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

        // On enregistre le familier optimal pour l'emplacement des familiers
        // Si petsWithTheMostEffects est vide, ce sera un familier de base
        equipmentPositionsMap.put(Collections.singletonList("PET"), getOptimalItem(petsWithTheMostEffects, effects));
    }

    private void setOptimalMount(List<Item> filteredItems, Integer level, HashMap<List<String>, Item> equipmentPositionsMap) {

        // Si le level est suffisant
        if (level >= 35) {
            // Ajouter la dragodinde comme item de base à l'emplacement des montures
            Item dragodinde = itemRepository.findById(18682);
            equipmentPositionsMap.put(new ArrayList<>(), dragodinde);
        }

        // On peut directement supprimer toutes les montures car elles offrent toutes le même bonus
        filteredItems.removeIf(item -> equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()).length == 0);

    }

    private void setOptimalWeapon(List<Item> filteredItems, List<Integer> effects, HashMap<List<String>, Item> equipmentPositionsMap) {

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

        for (DefinitionEffect definitionEffect : oneHandedWeaponSelected.getDefinitionsEffect()) {
            if (effects.contains(definitionEffect.getActionId())) {
                sumForOneHandedWeapon += definitionEffect.getParams()[0];
            }
        }

        // Les dagues et les boucliers
        List<Item> secondWeapons = filteredItems.stream()
            .filter(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Collections.singletonList("SECOND_WEAPON")))
            .collect(Collectors.toList());

        Item secondWeaponSelected = getOptimalItem(secondWeapons, effects);

        // On récupère la somme de la seconde arme sélectionnée
        float sumForSecondWeapon = 0.0f;

        for (DefinitionEffect definitionEffect : secondWeaponSelected.getDefinitionsEffect()) {
            if (effects.contains(definitionEffect.getActionId())) {
                sumForSecondWeapon += definitionEffect.getParams()[0];
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

        for (DefinitionEffect definitionEffect : twoHandedWeaponSelected.getDefinitionsEffect()) {
            if (effects.contains(definitionEffect.getActionId())) {
                sumForTwoHandedWeapon += definitionEffect.getParams()[0];
            }
        }

        // Si la somme des deux armes est supérieure à la somme de l'arme à deux mains
        if (sumOfBothWeapons > sumForTwoHandedWeapon) {
            // On ajoute à la hashmap l'arme à une main sélectionnée
            equipmentPositionsMap.put(Collections.singletonList("FIRST_WEAPON"), oneHandedWeaponSelected);
            // On ajoute à la hashmap la seconde arme sélectionnée
            equipmentPositionsMap.put(Collections.singletonList("SECOND_WEAPON"), secondWeaponSelected);
        } else {
            // On ajoute à la hashmap l'arme à deux mains sélectionnée
            equipmentPositionsMap.put(Collections.singletonList("FIRST_WEAPON"), twoHandedWeaponSelected);
            // L'item sélectionné pour l'emplacement "SECOND_WEAPON" est null
        }
        
        // On supprime les armes de la liste des items filtrés
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Collections.singletonList("FIRST_WEAPON")));
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Collections.singletonList("SECOND_WEAPON")));
    }

    private void setOptimalRings(List<Item> filteredItems, List<Integer> effects, HashMap<List<String>, Item> equipmentPositionsMap) {
        List<Item> rings = filteredItems.stream()
            .filter(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Arrays.asList("LEFT_HAND", "RIGHT_HAND")))
            .collect(Collectors.toList());
        List<Item> ringsWithTheMostEffects = itemService.getItemsWithTheMostEffects(rings, effects);
        equipmentPositionsMap.put(Arrays.asList("LEFT_HAND", "RIGHT_HAND"), getOptimalItem(ringsWithTheMostEffects, effects));
        filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(Arrays.asList("LEFT_HAND", "RIGHT_HAND")));
    }

    private Item getOptimalItem(List<Item> items, List<Integer> effects) {

        // Somme pour le set d'items
        float sumForSet = 0.0f;

        // Somme pour un item en particulier
        float sumForItem = 0.0f;

        // L'item qu'on va sélectionner
        Item itemSelected = null;

        // On parcourt le set d'items
        for (Item item : items) {

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

package com.pfe.wakfubuilder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public BuildService(BuildRepository buildRepository, ItemRepository itemRepository, EquipmentItemTypeService equipmentItemTypeService) {
        this.buildRepository = buildRepository;
        this.itemRepository = itemRepository;
        this.equipmentItemTypeService = equipmentItemTypeService;
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
        List<Item> selectedItems = selectItems(filteredItems, effects);

        Build build = new Build();
        build.setName(name);
        build.setLevel(level);
        build.setCost(cost);
        build.setEffects(effects);
        build.setItems(selectedItems.toArray(new Item[selectedItems.size()]));
        saveBuild(build);

        return build;
    }

    private List<Item> selectItems(List<Item> filteredItems, List<Integer> effects) {

        List<List<String>> equipmentPositions = new ArrayList<>();

        equipmentPositions.add(Collections.singletonList("BACK"));
        equipmentPositions.add(Collections.singletonList("LEGS"));
        equipmentPositions.add(Collections.singletonList("FIRST_WEAPON"));
        equipmentPositions.add(Collections.singletonList("SECOND_WEAPON"));
        equipmentPositions.add(Collections.singletonList("NECK"));
        equipmentPositions.add(Collections.singletonList("BELT"));
        equipmentPositions.add(Collections.singletonList("HEAD"));
        equipmentPositions.add(Collections.singletonList("CHEST"));
        equipmentPositions.add(Collections.singletonList("SHOULDERS"));
        equipmentPositions.add(Collections.singletonList("PET"));
        equipmentPositions.add(new ArrayList<>()); // equipmentPosition des montures
        equipmentPositions.add(Collections.singletonList("ACCESSORY"));
        equipmentPositions.add(Arrays.asList("LEFT_HAND", "RIGHT_HAND"));

        List<Item> selectedItems = new ArrayList<>();
        for (int i = 0; i < equipmentPositions.size(); i++) {
            selectedItems.add(null);
        }
    
        // Ajouter la dragodinde comme item de base à l'emplacement des montures car toutes les montures ont le même bonus
        Item dragodinde = itemRepository.findById(18682);
        selectedItems.set(equipmentPositions.indexOf(new ArrayList<>()), dragodinde);

        // On supprime toutes les montures de la liste des items filtrés
        filteredItems.removeIf(item -> equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()).length == 0);

        // On parcourt toutes les equipmentPositions
        for (List<String> equipmentPosition : equipmentPositions) {

            float totalValue = 0.0f;

            // On récupère tous les items qui ont l'equipmentPosition courante
            List<Item> itemsWithGivenEquipmentPosition = filteredItems.stream()
                .filter(item -> equipmentPosition.equals(Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()))))
                .collect(Collectors.toList());
            System.out.println("Les items qui ont l'equipmentPosition " + equipmentPosition + " : " + itemsWithGivenEquipmentPosition.size());

            for (Item item : itemsWithGivenEquipmentPosition) {

                float totalValueOfTheItem = 0.0f;

                // On récupère les definitionsEffect de l'item, qui sont les effets de l'item
                List<DefinitionEffect> definitionsEffectOfTheItem = item.getDefinitionsEffect();

                for (DefinitionEffect definitionEffect : definitionsEffectOfTheItem) {

                    // Si l'id de l'effet de l'item est dans la liste des ids des effets demandés
                    if (effects.contains(definitionEffect.getActionId())) {

                        // On l'ajoute à la valeur totale de l'item
                        totalValueOfTheItem += definitionEffect.getParams()[0];

                    }
                }

                if (totalValueOfTheItem > totalValue) {

                    // On définit l'item courant à la position courante
                    selectedItems.set(equipmentPositions.indexOf(equipmentPosition), item);

                    // La valeur totale du set d'item devient la valeur totale de l'item courant
                    totalValue = totalValueOfTheItem;
                }
            }
            
            // On supprime de la liste des items filtrés tous ceux qui ont l'equipmentPosition courante
            filteredItems.removeIf(item -> Arrays.asList(equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId())).equals(equipmentPosition));
            
        }
        
        return selectedItems;
    }
    
}

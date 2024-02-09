package com.pfe.wakfubuilder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Build;
import com.pfe.wakfubuilder.model.Item;
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
        List<Item> filteredItems = itemRepository.findByCriteria(level-15, level, rarities, effects);

        // Sélectionne un item pour chaque equipmentPositions possible
        List<Item> selectedItems = selectItems(filteredItems);

        Build build = new Build();
        build.setName(name);
        build.setLevel(level);
        build.setCost(cost);
        build.setEffects(effects);
        build.setItems(selectedItems.toArray(new Item[selectedItems.size()]));
        saveBuild(build);

        return build;
    }


    // Pour l'instant, on sélectionne au hasard parmis les items filtrés, ensuite on fera par valeur d'effects croissants
    private List<Item> selectItems(List<Item> filteredItems) {
        List<Item> selectedItems = new ArrayList<>();
        Random random = new Random();
        Set<String> selectedPositions = new HashSet<>();
    
        // traiter le cas de la position ['LEFT_HAND', 'RIGHT_HAND']
        // qui comporte deux positions en une seule
        while (selectedItems.size() < 15 && !filteredItems.isEmpty()) {
            // Sélectionner un item aléatoire parmi les items filtrés
            Item selectedItem = filteredItems.get(random.nextInt(filteredItems.size()));
            String[] equipmentPositions = equipmentItemTypeService.getEquipmentPositionByItemTypeId(selectedItem.getBaseParameters().getItemTypeId());
    
            // Vérifier chaque position d'équipement
            boolean allPositionsSelected = true;
            for (String equipmentPosition : equipmentPositions) {
                if (!selectedPositions.contains(equipmentPosition)) {
                    allPositionsSelected = false;
                    break;
                }
            }
    
            // Si toutes les positions d'équipement ne sont pas déjà sélectionnées, ajouter l'item à la liste des items pour le build
            if (!allPositionsSelected) {
                selectedItems.add(selectedItem);
                // Ajouter toutes les positions d'équipement de l'item à l'ensemble des positions sélectionnées
                selectedPositions.addAll(Arrays.asList(equipmentPositions));
            }
    
            // Supprimer l'item sélectionné de la liste filtrée
            filteredItems.remove(selectedItem);
        }
    
        return selectedItems;
    }
    
}

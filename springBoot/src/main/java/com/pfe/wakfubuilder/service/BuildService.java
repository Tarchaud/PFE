package com.pfe.wakfubuilder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Build;
import com.pfe.wakfubuilder.model.Item;
import com.pfe.wakfubuilder.repository.ItemRepository;

@Service
public class BuildService {

    private final ItemRepository itemRepository;
    private final EquipmentItemTypeService equipmentItemTypeService;

    public BuildService(ItemRepository itemRepository, EquipmentItemTypeService equipmentItemTypeService) {
        this.itemRepository = itemRepository;
        this.equipmentItemTypeService = equipmentItemTypeService;
    }

    static private ArrayList<Build> builds = new ArrayList<>(Arrays.asList(
            new Build(1, "build 1", 1, Build.Cost.low),
            new Build(2, "build 2", 2, Build.Cost.medium),
            new Build(3, "build 3", 3, Build.Cost.high),
            new Build(4, "build 4", 4, Build.Cost.low)
    ));

    public List<Build> getBuilds() {
        return builds;
    }

    public Build getBuild(long id) {
        return builds.stream().filter(build -> build.getId() == id).findFirst().orElse(null);
    }

    public void deleteBuild(long id) {
        builds.removeIf(build -> build.getId() == id);
    }

    public static void addBuild(Build build) {
        builds.add(build);
    }

    public void updateBuild(long id, Build build) {
        builds.forEach(buildlambda -> {
            if (buildlambda.getId() == id) {
                builds.set(builds.indexOf(buildlambda), build);
            }
        });
    }

    public Build generateBuild(String name, int level, Build.Cost cost, List<Integer> effects) {

        // Récupérer les raretés en fonction du coût
        List<Integer> rarities;
        switch (cost) {
            case low:
                rarities = Arrays.asList(1, 2, 3);
                break;
            case medium:
                rarities = Collections.singletonList(4);
                break;
            case high:
                rarities = Arrays.asList(5, 6);
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
        builds.add(build);

        return build;
    }


    // Pour l'instant, on sélectionne au hasard parmis les items filtrés, ensuite on fera par valeur d'effects croissants
    private List<Item> selectItems(List<Item> filteredItems) {
        List<Item> selectedItems = new ArrayList<>();
        Random random = new Random();

        // on test avec 14, car il risque d'y avoir une boucle infinie à cause de la position ['LEFT_HAND', 'RIGHT_HAND']
        // qui comporte deux positions en une seule
        while (selectedItems.size() < 14) {
            if (filteredItems.isEmpty()) {
                break;
            }
            // Sélectionner un item aléatoire parmi les items filtrés
            Item selectedItem = filteredItems.get(random.nextInt(filteredItems.size()));
            String[] equipmentPosition = equipmentItemTypeService.getEquipmentPositionByItemTypeId(selectedItem.getBaseParameters().getItemTypeId());

            // Ajouter l'item sélectionné à la liste des items pour le build
            selectedItems.add(selectedItem);

            // Supprimer tous les items ayant la même equipmentPosition de la liste filtrée
            filteredItems.removeIf(item -> equipmentItemTypeService.getEquipmentPositionByItemTypeId(item.getBaseParameters().getItemTypeId()).equals(equipmentPosition));
        }

        return selectedItems;
    }
}

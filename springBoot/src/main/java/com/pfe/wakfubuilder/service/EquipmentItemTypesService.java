package com.pfe.wakfubuilder.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.EquipmentItemTypes;

@Service
public class EquipmentItemTypesService {
    
    private List<EquipmentItemTypes> equipmentItemTypes;

    public EquipmentItemTypesService() {
        this.equipmentItemTypes = new ArrayList<>();
        loadEquipmentItemTypesFromJson();
    }

    private void loadEquipmentItemTypesFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("db/equipmentItemTypes.json");
            EquipmentItemTypes[] equipmentItemTypesArray = objectMapper.readValue(resource.getInputStream(), EquipmentItemTypes[].class);
            equipmentItemTypes.addAll(Arrays.asList(equipmentItemTypesArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<EquipmentItemTypes> getEquipmentItemTypes() {
        return equipmentItemTypes;
    }

    public EquipmentItemTypes getEquipmentItemType(long id) {
        return equipmentItemTypes.stream().filter(equipmentItemType -> equipmentItemType.getId() == id).findFirst().orElse(null);
    }
}

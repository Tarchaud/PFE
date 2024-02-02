package com.pfe.wakfubuilder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pfe.wakfubuilder.model.EquipmentItemTypes;
import com.pfe.wakfubuilder.service.EquipmentItemTypesService;

@RestController
public class EquipmentItemTypesController {
    
    @Autowired
    private EquipmentItemTypesService equipmentItemTypesService;

    @RequestMapping("/equipmentItemTypes")
    public List<EquipmentItemTypes> getEquipmentItemTypes() {
        return equipmentItemTypesService.getEquipmentItemTypes();
    }

    @RequestMapping("/equipmentItemType/{id}")
    public EquipmentItemTypes getEquipmentItemType(@PathVariable long id) {
        return equipmentItemTypesService.getEquipmentItemType(id);
    }
}

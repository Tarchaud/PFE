package com.pfe.wakfubuilder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pfe.wakfubuilder.model.EquipmentItemType;
import com.pfe.wakfubuilder.service.EquipmentItemTypeService;

@RestController
public class EquipmentItemTypeController {
    
    @Autowired
    private EquipmentItemTypeService equipmentItemTypeService;

    @RequestMapping("/equipmentItemTypes")
    public List<EquipmentItemType> getEquipmentItemTypes() {
        return equipmentItemTypeService.getEquipmentItemTypes();
    }

    @RequestMapping("/equipmentItemType/{id}")
    public EquipmentItemType getEquipmentItemType(@PathVariable long id) {
        return equipmentItemTypeService.getEquipmentItemType(id);
    }
}

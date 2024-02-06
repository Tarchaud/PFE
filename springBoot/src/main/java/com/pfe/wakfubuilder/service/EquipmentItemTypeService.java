package com.pfe.wakfubuilder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.EquipmentItemType;
import com.pfe.wakfubuilder.repository.EquipmentItemTypeRepository;


@Service
public class EquipmentItemTypeService {

    private final EquipmentItemTypeRepository equipmentItemTypeRepository;

    public EquipmentItemTypeService(EquipmentItemTypeRepository equipmentItemTypeRepository) {
        this.equipmentItemTypeRepository = equipmentItemTypeRepository;
    }

    public List<EquipmentItemType> getEquipmentItemTypes() {
        return equipmentItemTypeRepository.findAll();
    }

    public EquipmentItemType getEquipmentItemType(String id) {
        if (id == null) {
            return null;
        }
        Optional<EquipmentItemType> optionalEquipmentItemType = equipmentItemTypeRepository.findById(id);
        return optionalEquipmentItemType.orElse(null);
    }
}

package com.pfe.wakfubuilder.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pfe.wakfubuilder.model.EquipmentItemType;

public interface EquipmentItemTypeRepository extends MongoRepository<EquipmentItemType, Long> {
}

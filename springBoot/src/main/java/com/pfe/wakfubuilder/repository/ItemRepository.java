package com.pfe.wakfubuilder.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pfe.wakfubuilder.model.Item;

public interface ItemRepository extends MongoRepository<Item, String> {
    @Query("{ 'definition.item.baseParameters.itemTypeId': { $in: ?0 } }")
    List<Item> findByEquipmentItemTypeIds(List<Integer> ids);
}

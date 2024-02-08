package com.pfe.wakfubuilder.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pfe.wakfubuilder.model.Item;

public interface ItemRepository extends MongoRepository<Item, String> {
    @Query("{ 'definition.item.baseParameters.itemTypeId': { $in: ?0 } }")
    List<Item> findByEquipmentItemTypeIds(List<Integer> ids);

    @Query("{ 'definition.item.baseParameters.level': { $gte: ?0, $lte: ?1 }, 'definition.item.baseParameters.rarity': { $in: ?0 }, 'definition.equipEffects.effect.definition.actionId': { $elemMatch: { $in: ?0 } } }")
    List<Item> findByCriteria(int minLevel, int maxLevel, List<Integer> rarities, List<Integer> effects);
}

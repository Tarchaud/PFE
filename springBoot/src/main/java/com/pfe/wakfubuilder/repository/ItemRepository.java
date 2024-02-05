package com.pfe.wakfubuilder.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pfe.wakfubuilder.model.Item;

public interface ItemRepository extends MongoRepository<Item, Long> {
}

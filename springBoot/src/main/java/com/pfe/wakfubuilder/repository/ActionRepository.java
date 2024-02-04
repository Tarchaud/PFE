package com.pfe.wakfubuilder.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pfe.wakfubuilder.model.Action;

public interface ActionRepository extends MongoRepository<Action, Long> {
}
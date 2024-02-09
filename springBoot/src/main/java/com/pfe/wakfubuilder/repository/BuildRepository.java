package com.pfe.wakfubuilder.repository;

import com.pfe.wakfubuilder.model.Build;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildRepository extends MongoRepository<Build, ObjectId> {
}
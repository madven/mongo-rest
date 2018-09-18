package com.selcukc.mongo_rest.repositories;

import com.selcukc.mongo_rest.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<UserEntity, String> {
//	UserEntity findBy_id(String _id);
}

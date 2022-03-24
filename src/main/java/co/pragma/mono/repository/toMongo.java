package co.pragma.mono.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.pragma.mono.model.ImagenMongo;

public interface toMongo extends MongoRepository<ImagenMongo, String> {
    
}

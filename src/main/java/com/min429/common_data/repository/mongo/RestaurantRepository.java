package com.min429.common_data.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.min429.common_data.domain.Restaurant;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}

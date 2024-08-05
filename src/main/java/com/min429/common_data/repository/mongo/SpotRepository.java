package com.min429.common_data.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.min429.common_data.domain.enums.Area;
import com.min429.common_data.domain.Spot;

@Repository
public interface SpotRepository extends MongoRepository<Spot, String> {
	Page<Spot> findAllByArea(Area area, Pageable pageable);
}

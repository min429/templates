package com.min429.common_data.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.min429.common_data.domain.BoardImage;

@Repository
public interface BoardImageRepository extends MongoRepository<BoardImage, String> {
    Optional<BoardImage> findByContentId(Long contentId);
}

package com.min429.common_data.util;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RemoveDuplicatesUtil {

	private final MongoTemplate mongoTemplate;

	@PostConstruct
	public void removeDuplicates() {
		removeDuplicatesFromCollection("restaurant");
		removeDuplicatesFromCollection("spot");
		removeDuplicatesFromCollection("accommodation");
	}

	private void removeDuplicatesFromCollection(String collectionName) {
		// 중복 문서 식별
		Aggregation aggregation = newAggregation(
			group("contentid")
				.addToSet("_id").as("uniqueIds")
				.count().as("count"),
			match(Criteria.where("count").gt(1))
		);

		AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
		List<Map> duplicates = results.getMappedResults();

		// 중복 문서 삭제
		for (Map doc : duplicates) {
			List<String> uniqueIds = (List<String>) doc.get("uniqueIds");
			if (uniqueIds != null && uniqueIds.size() > 1) {
				Query query = new Query(Criteria.where("_id").in(uniqueIds.subList(1, uniqueIds.size())));
				mongoTemplate.remove(query, collectionName);
			}
		}
	}
}
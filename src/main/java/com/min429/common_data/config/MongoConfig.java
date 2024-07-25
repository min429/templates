package com.min429.common_data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(mongoUri);
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), "tour-platform");
	}

	/**
	 * mongoTemplate 빈이 등록된 이후 실행되는 index 등록 메소드
	 **/
	@Bean
	public ApplicationListener<ContextRefreshedEvent> initIndexes(MongoTemplate mongoTemplate) {
		return event -> {
			mongoTemplate.indexOps("restaurant")
				.ensureIndex(new Index().on("contentid", Sort.Direction.ASC).unique()); // unique -> 고유한 필드
			mongoTemplate.indexOps("spot")
				.ensureIndex(new Index().on("contentid", Sort.Direction.ASC).unique());
			mongoTemplate.indexOps("accommodation")
				.ensureIndex(new Index().on("contentid", Sort.Direction.ASC).unique());
		};
	}
}

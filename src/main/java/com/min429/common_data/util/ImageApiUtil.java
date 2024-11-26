package com.min429.common_data.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min429.common_data.domain.Accommodation;
import com.min429.common_data.domain.BoardImage;
import com.min429.common_data.domain.Restaurant;
import com.min429.common_data.domain.Spot;
import com.min429.common_data.exception.ApiException;
import com.min429.common_data.repository.mongo.AccommodationRepository;
import com.min429.common_data.repository.mongo.BoardImageRepository;
import com.min429.common_data.repository.mongo.RestaurantRepository;
import com.min429.common_data.repository.mongo.SpotRepository;
import com.min429.common_data.util.dto.RequestBoardImageResult;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ImageApiUtil {

	private static final String baseUrl = "http://apis.data.go.kr/B551011/KorService1/";
	private final BoardImageRepository boardImageRepository;
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate = new RestTemplate();
	private final Long numOfRows = 10L;
	private final AccommodationRepository accommodationRepository;
	private final RestaurantRepository restaurantRepository;
	private final SpotRepository spotRepository;
	@Value("${api_key}")
	private String apiKey;
	private boolean hasNext;
	private Long pageNo = 1L;
	private Map<Long, BoardImage> boardImageMap = new HashMap<>();

	// @PostConstruct
	public void schedule() {
		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<Accommodation> accommodations = accommodationRepository.findAll();
		List<Spot> spots = spotRepository.findAll();

		// for (Restaurant restaurant : restaurants) {
		// 	process(restaurant.getContentid());
		// }
		// for (Accommodation accommodation : accommodations) {
		// 	process(accommodation.getContentid());
		// }
		for(Spot spot : spots){
			process(spot.getContentid());
		}

		System.out.println("size: "+boardImageMap.size());

		boardImageRepository.saveAll(boardImageMap.values());
	}

	public void process(Long contentId) {
		hasNext = true;
		pageNo = 1L;

		while (hasNext) {
			searchDetailImage(contentId, pageNo++);
		}
	}

	public void searchDetailImage(Long contentId, Long pageNo) {
		String uri = getCommonUri("detailImage1", pageNo)
			.queryParam("contentId", contentId)
			.queryParam("imageYN", "Y")
			.queryParam("subImageYN", "Y")
			.build().toUri().toString();

		requestDetailImageApi(uri);
	}

	public void requestDetailImageApi(String uri) {
		Iterator<JsonNode> items = requestApi(uri);
		setBoardImageFrom(items);
	}

	public UriComponentsBuilder getCommonUri(String endpoint, Long pageNo) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl + endpoint)
			.queryParam("serviceKey", apiKey)
			.queryParam("numOfRows", numOfRows)
			.queryParam("pageNo", pageNo)
			.queryParam("MobileOS", "ETC")
			.queryParam("MobileApp", "AppTest")
			.queryParam("_type", "json");
	}

	public Iterator<JsonNode> requestApi(String uri) throws ApiException {
		try {
			String response = restTemplate.getForObject(uri, String.class);
			JsonNode root = objectMapper.readTree(response);
			String resultCode = root
				.path("response")
				.path("header")
				.path("resultCode").asText();

			if (!"0000".equals(resultCode))
				throw new ApiException("API 요청 실패");

			isEndOfPage(root);

			Iterator<JsonNode> items = root
				.path("response")
				.path("body")
				.path("items")
				.path("item").elements();
			return items;
		} catch (JsonProcessingException e) {
			throw new ApiException("파싱 실패", e);
		} catch (Exception e) {
			throw new ApiException("기타 예외", e);
		}
	}

	public void isEndOfPage(JsonNode root) {
		Long totalCount = root
			.path("response")
			.path("body")
			.path("totalCount").asLong();

		hasNext = (pageNo * numOfRows) < totalCount;
	}

	public void setBoardImageFrom(Iterator<JsonNode> items) {
		while (items.hasNext()) {
			JsonNode item = items.next();

			RequestBoardImageResult result = RequestBoardImageResult.builder()
				.contentId(item.path("contentid").asLong())
				.imageName(item.path("imgname").asText())
				.originalImageUrl(item.path("originimgurl").asText())
				.serialNum(item.path("serialnum").asText())
				.smallImageUrl(item.path("smallimageurl").asText())
				.cpyrhtDivCd(item.path("cpyrhtDivCd").asText())
				.build();

			BoardImage.from(boardImageMap, result);
		}
	}
}

package com.min429.common_data.util;

import static com.min429.common_data.domain.enums.Area.*;
import static com.min429.common_data.domain.enums.ContentType.*;
import static com.min429.common_data.domain.enums.Type.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min429.common_data.domain.Accommodation;
import com.min429.common_data.domain.Info;
import com.min429.common_data.domain.Restaurant;
import com.min429.common_data.domain.Spot;
import com.min429.common_data.domain.enums.Area;
import com.min429.common_data.domain.enums.ContentType;
import com.min429.common_data.domain.enums.Sigungu;
import com.min429.common_data.domain.enums.Type;
import com.min429.common_data.exception.ApiException;
import com.min429.common_data.repository.mongo.AccommodationRepository;
import com.min429.common_data.repository.mongo.RestaurantRepository;
import com.min429.common_data.repository.mongo.SpotRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Transactional
@EnableScheduling
public class TourApiUtil {

	private static final String baseUrl = "http://apis.data.go.kr/B551011/KorService1/";
	private final SpotRepository spotRepository;
	private final RestaurantRepository restaurantRepository;
	private final AccommodationRepository accommodationRepository;
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate = new RestTemplate();
	private final Long numOfRows = 10L;
	@Value("${api_key}")
	private String apiKey;

	@PostConstruct
	@Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 0시 0분에 실행
	public void schedule() throws ApiException {
		process(제주도, 음식점, RESTAURANT);
		process(제주도, 숙소, ACCOMMODATION);
		process(제주도, 관광지, SPOT);
	}

	public void process(Area area, ContentType contentType, Type type) {
		Long pageNum = getAreaBaseList1PageNum(area, contentType);
		for (long page = 1; page <= pageNum; page++)
			searchAreaBasedList1(area, contentType, type, page);
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

	public void requestAreaBasedList1Api(String uri, Type type) {
		Iterator<JsonNode> items = requestApi(uri);
		List<Info> infos = getInfos(items);
		switch (type) {
			case SPOT: {
				for (Info info : infos)
					searchDetailCommon(info.getContentid(), SPOT, 1L);
				break;
			}
			case RESTAURANT: {
				for (Info info : infos)
					searchDetailCommon(info.getContentid(), RESTAURANT, 1L);
				break;
			}
			case ACCOMMODATION: {
				for (Info info : infos)
					searchDetailCommon(info.getContentid(), ACCOMMODATION, 1L);
				break;
			}
			default:
				throw new ApiException("타입 예외");
		}
	}

	public void requestDetailCommonApi(String uri, Type type) {
		JsonNode item = requestApi(uri).next();
		switch (type) {
			case SPOT: {
				Spot spot = new Spot(getInfo(item));
				spotRepository.save(spot);
				break;
			}
			case RESTAURANT: {
				Restaurant restaurant = new Restaurant(getInfo(item));
				restaurantRepository.save(restaurant);
				break;
			}
			case ACCOMMODATION: {
				Accommodation accommodation = new Accommodation(getInfo(item));
				accommodationRepository.save(accommodation);
				break;
			}
			default:
				throw new ApiException("타입 예외");
		}
	}

	public void searchAreaBasedList1(Area area, ContentType contentType, Type type, Long pageNo) {
		String uri = getCommonUri("areaBasedList1", pageNo).queryParam("listYN", "Y") // 목록구분
			.queryParam("arrange", "A") // 정렬구분
			.queryParam("contentTypeId", contentType.id()) // 콘텐츠 타입 ID
			.queryParam("areaCode", area.code()) // 지역코드
			.build().toUri().toString();
		requestAreaBasedList1Api(uri, type);
	}

	public void searchDetailCommon(Long contentId, Type type, Long pageNo) {
		String uri = getCommonUri("detailCommon1", pageNo).queryParam("contentId", contentId) // 콘텐츠 ID
			.queryParam("defaultYN", "Y") // 기본정보 조회여부
			.queryParam("firstImageYN", "Y") // 대표이미지 조회여부
			.queryParam("areacodeYN", "Y") // 지역코드 조회여부
			.queryParam("catcodeYN", "Y") // 서비스분류코드 조회여부
			.queryParam("addrinfoYN", "Y") // 주소 조회여부
			.queryParam("mapinfoYN", "Y") // 좌표 조회여부
			.queryParam("overviewYN", "Y") // 콘텐츠 개요 조회여부
			.build().toUri().toString();
		requestDetailCommonApi(uri, type);
	}

	private Info getInfo(JsonNode item) {
		return Info.builder()
			.contentid(item.path("contentid").asLong())
			.contentType(ContentType.fromId(item.path("contenttypeid").asLong()))
			.title(item.path("title").asText())
			.firstimage(item.path("firstimage").asText())
			.area(Area.fromCode(item.path("areacode").asLong()))
			.sigungu(Sigungu.fromCode(item.path("sigungucode").asLong()))
			.cat1(item.path("cat1").asText())
			.cat2(item.path("cat2").asText())
			.cat3(item.path("cat3").asText())
			.addr1(item.path("addr1").asText())
			.mapx(item.path("mapx").asDouble())
			.mapy(item.path("mapy").asDouble())
			.tel(item.path("tel").asText())
			.overview(item.path("overview").asText())
			.build();
	}

	public List<Info> getInfos(Iterator<JsonNode> items) {
		List<Info> infos = new LinkedList<>();
		while (items.hasNext()) {
			JsonNode item = items.next();
			infos.add(Info.builder().contentid(item.path("contentid").asLong()).build());
		}
		return infos;
	}

	public Long getAreaBaseList1PageNum(Area area, ContentType contentType) {
		String uri = getCommonUri("areaBasedList1", 1L).queryParam("listYN", "N") // 목록구분
			.queryParam("contentTypeId", contentType.id()) // 콘텐츠 타입 ID
			.queryParam("areaCode", area.code()) // 지역코드
			.build().toUri().toString();
		JsonNode item = requestApi(uri).next();
		Long totalCnt = item.path("totalCnt").asLong();
		if (totalCnt % numOfRows == 0)
			return totalCnt / numOfRows;
		return totalCnt / numOfRows + 1;
	}
}

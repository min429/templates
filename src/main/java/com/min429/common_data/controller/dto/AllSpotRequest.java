package com.min429.common_data.controller.dto;

import org.antlr.v4.runtime.misc.NotNull;

public record AllSpotRequest(

	@NotNull String area, // 지역
	String sigungu, // 시군구
	Long numOfRows, // 한 페이지 결과 수
	Long pageNo, // 페이지 번호
	String MobileOS, // 모바일 OS
	String MobileApp, // 모바일 앱 이름
	String ServiceKey, // 서비스 키
	String listYN, // 목록 출력 여부
	String arrange // 정렬 구분
) {
}

package com.min429.common_data.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info {

	private String addr1; // 주소
	private Long areacode; // 지역코드
	private String cat1; // 대분류 코드
	private String cat2; // 중분류 코드
	private String cat3; // 소분류 코드
	private Long contentid; // 콘텐츠 ID
	private Long contenttypeid; // 콘텐츠 타입 ID
	private String firstimage; // 대표이미지
	private Double mapx; // GPS X좌표
	private Double mapy; // GPS Y좌표
	private Long sigungucode; // 시군구코드
	private String title; // 콘텐츠제목
	private String tel; // 전화번호
	private String overview; // 소개

	public Info(Info info) {
		this.addr1 = info.addr1;
		this.areacode = info.areacode;
		this.cat1 = info.cat1;
		this.cat2 = info.cat2;
		this.cat3 = info.cat3;
		this.contentid = info.contentid;
		this.contenttypeid = info.contenttypeid;
		this.firstimage = info.firstimage;
		this.mapx = info.mapx;
		this.mapy = info.mapy;
		this.sigungucode = info.sigungucode;
		this.title = info.title;
		this.tel = info.tel;
		this.overview = info.overview;
	}
}

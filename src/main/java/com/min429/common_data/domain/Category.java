package com.min429.common_data.domain;

public enum Category {
	관광지(12L),
	문화시설(14L),
	행사(15L),
	여행코스(25L),
	레포츠(28L),
	숙소(32L),
	쇼핑(38L),
	음식점(39L);

	private final Long code;

	Category(Long code) {
		this.code = code;
	}

	public Long code() {
		return code;
	}
}

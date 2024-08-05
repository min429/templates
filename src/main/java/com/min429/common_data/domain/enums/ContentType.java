package com.min429.common_data.domain.enums;

public enum ContentType {
	관광지(12L),
	문화시설(14L),
	행사(15L),
	여행코스(25L),
	레포츠(28L),
	숙소(32L),
	쇼핑(38L),
	음식점(39L);

	private final Long id;

	ContentType(Long id) {
		this.id = id;
	}

	public Long id() {
		return id;
	}

	public static ContentType fromId(Long id) {
		for (ContentType contentType : ContentType.values()) {
			if (contentType.id().equals(id)) {
				return contentType;
			}
		}
		throw new IllegalArgumentException("Unknown id: " + id);
	}
}

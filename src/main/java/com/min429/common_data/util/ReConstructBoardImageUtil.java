package com.min429.common_data.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.min429.common_data.domain.Accommodation;
import com.min429.common_data.domain.BoardImage;
import com.min429.common_data.domain.Info;
import com.min429.common_data.domain.Restaurant;
import com.min429.common_data.domain.Spot;
import com.min429.common_data.repository.mongo.AccommodationRepository;
import com.min429.common_data.repository.mongo.BoardImageRepository;
import com.min429.common_data.repository.mongo.RestaurantRepository;
import com.min429.common_data.repository.mongo.SpotRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ReConstructBoardImageUtil {

    private final AccommodationRepository accommodationRepository;
    private final RestaurantRepository restaurantRepository;
    private final SpotRepository spotRepository;
    private final BoardImageRepository boardImageRepository;

    @PostConstruct
    public void process() {
        List<Info> infos = new ArrayList<>();
        List<Accommodation> accommodations = accommodationRepository.findAll();
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<Spot> spots = spotRepository.findAll();

        infos.addAll(accommodations);
        infos.addAll(restaurants);
        infos.addAll(spots);

        List<BoardImage> boardImages = new ArrayList<>();

        infos.stream().forEach(
            info -> {
                boardImageRepository.findByContentId(info.getContentid()).ifPresent(
                    boardImage -> {
                        boardImage.setBoardId(info.getId());
                        boardImages.add(boardImage);
                    }
                );
            }
        );

        boardImageRepository.saveAll(boardImages);
    }
}

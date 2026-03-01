package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.repository.TourRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourService {
    private final TourRepository tourRepository;

    //TODO user check
    public Optional<ArrayList<TourDto>> getAllToursOfUser(String username) {
        return tourRepository.getAllToursByCreator(username)
                .map(tours -> (ArrayList<TourDto>) tours.stream()
                        .map(TourDto::fromEntity)
                        .collect(Collectors.toList()));
    }

    //TODO user check
    public Optional<TourDto> getTourByUuid(UUID uuid) {
        return tourRepository.getTourByUuid(uuid)
                .map(TourDto::fromEntity);
    }

    public Optional<TourDto> addTour(TourDto tourDto) {
        return Optional.ofNullable(tourDto)
                .map(_ -> TourDto.toEntity(tourDto))
                .map(tourRepository::save)
                .map(TourDto::fromEntity);
    }

    //TODO user check
    public Optional<TourDto> updateTour(TourDto tourDto) {
        return Optional.ofNullable(tourDto)
                .map(_ -> tourRepository.getTourByUuid(UUID.fromString(tourDto.uuid())))
                .filter(Optional::isPresent)
                .map(_ -> TourDto.toEntity(tourDto))
                .map(tourRepository::save)
                .map(TourDto::fromEntity);
    }

    //TODO user check
    public Long deleteTour(UUID uuid) {
        return tourRepository.deleteTourByUuid(uuid);
    }
}

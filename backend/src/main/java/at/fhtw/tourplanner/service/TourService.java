package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.LoginDto;
import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.repository.TourRepository;
import at.fhtw.tourplanner.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourService {
    private final TourRepository tourRepository;

    public Optional<ArrayList<Tour>> getAllToursOfUser(String username) {
        return tourRepository.getAllToursOfUser(username);
    }

    public Optional<Tour> getTourByUuid(UUID uuid) {
        return tourRepository.getTourByUuid(uuid);
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
                .map(dto -> tourRepository.getTourByUuid(UUID.fromString(tourDto.uuid())))
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

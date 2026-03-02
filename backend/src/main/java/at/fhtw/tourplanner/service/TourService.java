package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.dto.TourUpdateDto;
import at.fhtw.tourplanner.model.Account;
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

    public Optional<ArrayList<TourDto>> getAllToursOfUser(Account account) {
        return tourRepository.getAllToursByCreator(account.getUsername())
                .map(tours -> (ArrayList<TourDto>) tours.stream()
                        .map(TourDto::fromEntity)
                        .collect(Collectors.toList()));
    }

    public Optional<TourDto> getTourByUuid(UUID uuid, Account account) {
        return tourRepository.getTourByUuid(uuid)
                .filter(tour -> tour.getCreator().getUuid().equals(account.getUuid()))
                .map(TourDto::fromEntity);
    }

    public Optional<TourDto> addTour(TourDto tourDto, Account account) {
        return Optional.ofNullable(tourDto)
                .map(_ -> TourDto.toEntity(tourDto, account))
                .map(tourRepository::save)
                .map(TourDto::fromEntity);
    }

    //TODO user check
    public Optional<TourDto> updateTour(TourUpdateDto tourDto, Account account) {
        return Optional.ofNullable(tourDto)
                .map(_ -> tourRepository.getTourByUuid(UUID.fromString(tourDto.uuid())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(tour -> TourUpdateDto.merge(tour, tourDto))
                .map(tourRepository::save)
                .map(TourDto::fromEntity);
    }

    //TODO user check
    public Long deleteTour(UUID uuid) {
        return tourRepository.deleteTourByUuid(uuid);
    }
}

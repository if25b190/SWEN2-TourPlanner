package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.TourLogDto;
import at.fhtw.tourplanner.dto.TourLogUpdateDto;
import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.repository.TourLogRepository;
import at.fhtw.tourplanner.repository.TourRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourLogService {
    private final TourLogRepository tourLogRepository;
    private final TourRepository tourRepository;

    public List<TourLogDto> getAllTourLogsOfTour(UUID uuid, Account account) {
        return tourRepository.getTourByUuid(uuid)
                .map(Tour::getLogs)
                .map(logs -> logs.stream().filter(x -> x.getCreator().getUuid().equals(account.getUuid())).toList())
                .map(logs -> logs.stream().map(TourLogDto::fromEntity).toList())
                .orElse(Collections.emptyList());
    }

    public Optional<TourLogDto> getTourLogByUuid(UUID uuid, Account account) {
        return tourLogRepository.getTourLogByUuid(uuid)
                .filter(log -> log.getCreator().getUuid().equals(account.getUuid()))
                .map(TourLogDto::fromEntity);
    }

    public Optional<TourLogDto> addTourLog(TourLogDto tourLogDto, Account account) {
        return tourRepository.getTourByUuid(UUID.fromString(tourLogDto.tour()))
                .filter(x -> x.getCreator().getUuid().equals(account.getUuid()))
                .map(x -> x.addLog(TourLogDto.toEntity(tourLogDto)))
                .map(tourLogRepository::save)
                .map(TourLogDto::fromEntity);
    }

    public Optional<TourLogDto> updateTourLog(TourLogUpdateDto tourLogUpdateDto, Account account) {
        return Optional.ofNullable(tourLogUpdateDto)
                .map(_ -> tourLogRepository.getTourLogByUuid(UUID.fromString(tourLogUpdateDto.uuid())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(log -> log.getCreator().getUuid().equals(account.getUuid()))
                .map(log -> TourLogUpdateDto.merge(log, tourLogUpdateDto))
                .map(tourLogRepository::save)
                .map(TourLogDto::fromEntity);
    }

    public Optional<Integer> deleteTour(UUID uuid, Account account) {
        return tourLogRepository.getTourLogByUuid(uuid)
                .filter(log -> log.getCreator().getUuid().equals(account.getUuid()))
                .map(log -> {
                    this.tourLogRepository.delete(log);
                    return 1;
                });
    }
}

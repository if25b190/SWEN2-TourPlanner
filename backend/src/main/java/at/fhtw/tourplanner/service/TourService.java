package at.fhtw.tourplanner.service;

import at.fhtw.tourplanner.dto.TourDto;
import at.fhtw.tourplanner.dto.TourExportDto;
import at.fhtw.tourplanner.dto.TourLogExportDto;
import at.fhtw.tourplanner.dto.TourUpdateDto;
import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.TourLog;
import at.fhtw.tourplanner.repository.TourLogRepository;
import at.fhtw.tourplanner.repository.TourRepository;
import at.fhtw.tourplanner.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourService {
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final TourLogRepository tourLogRepository;
    private final OpenRouteService openRouteService;

    public List<TourDto> getAllToursOfUser(Account account) {
        return Optional.ofNullable(account)
                .map(Account::getUuid)
                .flatMap(userRepository::findByUuid)
                .map(Account::getTours)
                .map(tours -> tours.stream()
                        .map(tour -> {
                            openRouteService.setGeoInformationOfTour(tour);
                            return TourDto.fromEntity(tour);
                }).toList())
                .orElse(Collections.emptyList());
    }

    public Optional<TourDto> getTourByUuid(UUID uuid, Account account) {
        return tourRepository.getTourByUuid(uuid)
                .filter(tour -> tour.getCreator().getUuid().equals(account.getUuid()))
                .map(tour -> {
                    openRouteService.setGeoInformationOfTour(tour);
                    return TourDto.fromEntity(tour);
                });
    }

    public Page<TourDto> searchTour(String searchTerm, Pageable pageable, Account account) {
        return new PageImpl<>(
                tourRepository.findAllWithSearch(searchTerm, pageable)
                    .filter(tour -> tour.getCreator().getUuid().equals(account.getUuid()))
                    .map(tour -> {
                        openRouteService.setGeoInformationOfTour(tour);
                        return TourDto.fromEntity(tour);
                    })
                    .toList()
        );
    }

    public Optional<TourDto> addTour(TourDto tourDto, Account account) {
        return Optional.ofNullable(account)
                .map(Account::getUuid)
                .flatMap(userRepository::findByUuid)
                .map(user -> user.addTour(TourDto.toEntity(tourDto)))
                .map(tourRepository::save)
                .map(tour -> {
                    openRouteService.setGeoInformationOfTour(tour);
                    return TourDto.fromEntity(tour);
                });
    }

    public Optional<TourDto> updateTour(TourUpdateDto tourDto, Account account) {
        return Optional.ofNullable(tourDto)
                .map(_ -> tourRepository.getTourByUuid(UUID.fromString(tourDto.uuid())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(tour -> tour.getCreator().getUuid().equals(account.getUuid()))
                .map(tour -> TourUpdateDto.merge(tour, tourDto))
                .map(tourRepository::save)
                .map(tour -> {
                    openRouteService.setGeoInformationOfTour(tour);
                    return TourDto.fromEntity(tour);
                });
    }

    public Optional<Integer> deleteTour(UUID uuid, Account account) {
        return tourRepository.getTourByUuid(uuid)
                .filter(tour -> tour.getCreator().getUuid().equals(account.getUuid()))
                .map(tour -> {
                    this.tourRepository.delete(tour);
                    return 1;
                });
    }

    public List<TourExportDto> exportAllTourData(Account account) {
        return Optional.ofNullable(account)
                .map(Account::getUuid)
                .flatMap(userRepository::findByUuid)
                .map(Account::getTours)
                .map(tours -> tours.stream()
                .map(TourExportDto::fromEntity).toList())
                .orElse(Collections.emptyList());
    }

    public void importTourData(MultipartFile file, Account account) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new FileNotFoundException("No file provided");
            }

            ObjectMapper mapper = new ObjectMapper();
            List<TourExportDto> dtos = mapper.readValue(file.getInputStream(), new TypeReference<>() {});

            for (TourExportDto dto: dtos) {
                Tour tour = TourExportDto.toEntity(dto);
                tour.setCreator(account);
                tourRepository.save(tour);

                if(dto.logs() != null) {
                    List<TourLog> logs = dto.logs().stream()
                            .map(TourLogExportDto::toEntity)
                            .peek(log -> {
                                log.setTour(tour);
                                log.setCreator(account);
                            })
                            .toList();
                    tour.setLogs(logs);
                }

                tourLogRepository.saveAll(tour.getLogs());
            }
        }
        catch (FileNotFoundException e) {
            log.error("Failed to import tours due missing file", e);
            throw new FileNotFoundException("Failed to import tours due missing file");
        } catch (IOException e) {
            log.error("Failed to process json file", e);
            throw new IOException("Failed to process json file");
        }
    }
}

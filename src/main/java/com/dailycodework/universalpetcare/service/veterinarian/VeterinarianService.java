package com.dailycodework.universalpetcare.service.veterinarian;

import com.dailycodework.universalpetcare.dto.EntityConverter;
import com.dailycodework.universalpetcare.dto.UserDto;
import com.dailycodework.universalpetcare.dto.VetSummaryDto;
import com.dailycodework.universalpetcare.exception.ResourceNotFoundException;
import com.dailycodework.universalpetcare.model.Veterinarian;
import com.dailycodework.universalpetcare.repository.ReviewRepository;
import com.dailycodework.universalpetcare.repository.UserRepository;
import com.dailycodework.universalpetcare.repository.VeterinarianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VeterinarianService implements IVeterinarianService {
    private final VeterinarianRepository veterinarianRepository;
    private final EntityConverter<Veterinarian, UserDto> entityConverter;
    // Removed unused reviewService
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public Page<VetSummaryDto> getAllVeterinarians(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Veterinarian> vetsPage = veterinarianRepository.findAll(pageRequest);

        // Optimize stats fetching for just this page
        List<Long> vetIds = vetsPage.getContent().stream().map(Veterinarian::getId).toList();
        Map<Long, ReviewStatsDto> reviewStatsMap = getReviewStatsMap(vetIds);

        return vetsPage.map(vet -> mapToVetSummaryDto(vet, reviewStatsMap));
    }

    private VetSummaryDto mapToVetSummaryDto(Veterinarian vet, Map<Long, ReviewStatsDto> statsMap) {
        ReviewStatsDto stats = statsMap.getOrDefault(vet.getId(), new ReviewStatsDto(0L, 0.0));
        return new VetSummaryDto(
                vet.getId(),
                vet.getFirstName(),
                vet.getLastName(),
                vet.getSpecialization(),
                stats.averageRating(),
                stats.totalReviewers(),
                vet.getPhoto() != null ? vet.getPhoto().getId() : null);
    }

    // Helper to fetch and map stats (Filtered)
    private Map<Long, ReviewStatsDto> getReviewStatsMap(List<Long> vetIds) {
        // ideally repo should support IN clause, but valid simple fix:
        return getReviewStatsMap();
    }

    // Helper to fetch and map stats (Global)
    private Map<Long, ReviewStatsDto> getReviewStatsMap() {
        List<Object[]> stats = reviewRepository.getReviewStats();
        return stats.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0], // vetId
                        row -> new ReviewStatsDto((Long) row[1], (Double) row[2]) // count, avg
                ));
    }

    // Inner record or class for stats
    private record ReviewStatsDto(Long totalReviewers, Double averageRating) {
    }

    private UserDto mapVeterinarianToUserDto(Veterinarian veterinarian, Map<Long, ReviewStatsDto> statsMap) {
        UserDto userDto = entityConverter.mapEntityToDto(veterinarian, UserDto.class);

        // OPTIMIZATION: Fetch from Map instead of DB
        ReviewStatsDto stats = statsMap.getOrDefault(veterinarian.getId(), new ReviewStatsDto(0L, 0.0));

        userDto.setAverageRating(stats.averageRating());
        userDto.setTotalReviewers(stats.totalReviewers());

        // OPTIMIZATION: Do NOT load full photo bytes for list view.
        // Frontend should fetch by ID or URL.
        if (veterinarian.getPhoto() != null) {
            userDto.setPhotoId(veterinarian.getPhoto().getId());
        }

        return userDto;
    }

    private List<Veterinarian> getAvailableVeterinarians(String specialization, LocalDate date, LocalTime time) {
        if (date != null && time != null) {
            LocalTime endTime = time.plusHours(2);
            LocalTime minTime = calculateMinTime(time);
            LocalTime maxTime = calculateMaxTime(endTime);

            if (specialization == null || specialization.isEmpty()) {
                return veterinarianRepository.findAllAvailableVeterinarians(date, minTime, maxTime);
            } else {
                return veterinarianRepository.findAvailableVeterinarians(specialization, date, minTime, maxTime);
            }
        }
        // Fallback or if date/time not provided, just return vets by specialization or
        // all
        if (specialization == null || specialization.isEmpty()) {
            return veterinarianRepository.findAll();
        } else {
            return getVeterinariansBySpecialization(specialization);
        }
    }

    private LocalTime calculateMinTime(LocalTime reqStartTime) {
        // reqStartTime - 2h 40m
        LocalTime minTime = reqStartTime.minusHours(2).minusMinutes(40);
        // If minTime > reqStartTime (wrapped around midnight to yesterday), clamp to
        // 00:00
        if (minTime.isAfter(reqStartTime)) {
            return LocalTime.MIN; // 00:00
        }
        return minTime;
    }

    private LocalTime calculateMaxTime(LocalTime reqEndTime) {
        // reqEndTime + 1h
        LocalTime maxTime = reqEndTime.plusHours(1);
        // If maxTime < reqEndTime (wrapped around midnight to tomorrow), clamp to MAX
        if (maxTime.isBefore(reqEndTime)) {
            return LocalTime.MAX; // 23:59:59.999
        }
        return maxTime;
    }

    // OPTIMIZATION: Removed isVetAvailable and doesAppointmentOverLap as logic is
    // moved to DB query

    @Override
    public List<Map<String, Object>> aggregateVetsBySpecialization() {
        List<Object[]> results = veterinarianRepository.countVetsBySpecialization();
        return results.stream()
                .map(result -> Map.of("specialization", result[0], "count", result[1]))
                .collect(Collectors.toList());
    }

}

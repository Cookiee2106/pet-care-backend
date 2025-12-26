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

    @Override
    public List<UserDto> getAllVeterinariansWithDetails() {
        List<Veterinarian> veterinarians = userRepository.findAllByUserType("VET");
        Map<Long, ReviewStatsDto> reviewStatsMap = getReviewStatsMap();

        return veterinarians.stream()
                .map(vet -> mapVeterinarianToUserDto(vet, reviewStatsMap))
                .toList();
    }

    @Override
    public List<String> getSpecializations() {
        return veterinarianRepository.getSpecializations();
    }

    @Override
    public List<UserDto> findAvailableVetsForAppointment(String specialization, LocalDate date, LocalTime time) {
        List<Veterinarian> filteredVets = getAvailableVeterinarians(specialization, date, time);
        Map<Long, ReviewStatsDto> reviewStatsMap = getReviewStatsMap();

        return filteredVets.stream()
                .map(vet -> mapVeterinarianToUserDto(vet, reviewStatsMap))
                .toList();
    }

    @Override
    public List<Veterinarian> getVeterinariansBySpecialization(String specialization) {
        if (!veterinarianRepository.existsBySpecialization(specialization)) {
            throw new ResourceNotFoundException("Không tìm thấy bác sĩ thú y nào với chuyên khoa " + specialization);
        }
        return veterinarianRepository.findBySpecialization(specialization);
    }

    @Override
    public List<Map<String, Object>> aggregateVetsBySpecialization() {
        List<Object[]> results = veterinarianRepository.countVetsBySpecialization();
        return results.stream()
                .map(result -> Map.of("specialization", result[0], "count", result[1]))
                .collect(Collectors.toList());
    }

    // ==========================================
    // PRIVATE HELPERS
    // ==========================================

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

    private UserDto mapVeterinarianToUserDto(Veterinarian veterinarian, Map<Long, ReviewStatsDto> statsMap) {
        UserDto userDto = entityConverter.mapEntityToDto(veterinarian, UserDto.class);
        ReviewStatsDto stats = statsMap.getOrDefault(veterinarian.getId(), new ReviewStatsDto(0L, 0.0));
        userDto.setAverageRating(stats.averageRating());
        userDto.setTotalReviewers(stats.totalReviewers());

        if (veterinarian.getPhoto() != null) {
            userDto.setPhotoId(veterinarian.getPhoto().getId());
        }
        return userDto;
    }

    private Map<Long, ReviewStatsDto> getReviewStatsMap(List<Long> vetIds) {
        // Simple implementation reusing global fetch for now
        return getReviewStatsMap();
    }

    private Map<Long, ReviewStatsDto> getReviewStatsMap() {
        List<Object[]> stats = reviewRepository.getReviewStats();
        return stats.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> new ReviewStatsDto((Long) row[1], (Double) row[2])));
    }

    private record ReviewStatsDto(Long totalReviewers, Double averageRating) {
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
        if (specialization == null || specialization.isEmpty()) {
            return veterinarianRepository.findAll();
        } else {
            // Calling the method defined in this class
            return getVeterinariansBySpecialization(specialization);
        }
    }

    private LocalTime calculateMinTime(LocalTime reqStartTime) {
        LocalTime minTime = reqStartTime.minusHours(2).minusMinutes(40);
        if (minTime.isAfter(reqStartTime))
            return LocalTime.MIN;
        return minTime;
    }

    private LocalTime calculateMaxTime(LocalTime reqEndTime) {
        LocalTime maxTime = reqEndTime.plusHours(1);
        if (maxTime.isBefore(reqEndTime))
            return LocalTime.MAX;
        return maxTime;
    }
}

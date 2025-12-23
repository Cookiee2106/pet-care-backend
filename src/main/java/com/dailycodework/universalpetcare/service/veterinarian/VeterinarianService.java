package com.dailycodework.universalpetcare.service.veterinarian;

import com.dailycodework.universalpetcare.dto.EntityConverter;
import com.dailycodework.universalpetcare.dto.UserDto;
import com.dailycodework.universalpetcare.exception.ResourceNotFoundException;
import com.dailycodework.universalpetcare.model.Veterinarian;
import com.dailycodework.universalpetcare.repository.ReviewRepository;
import com.dailycodework.universalpetcare.repository.UserRepository;
import com.dailycodework.universalpetcare.repository.VeterinarianRepository;
import com.dailycodework.universalpetcare.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
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
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllVeterinariansWithDetails() {
        List<Veterinarian> veterinarians = userRepository.findAllByUserType("VET");
        return veterinarians.stream()
                .map(this::mapVeterinarianToUserDto)
                .toList();
    }

    @Override
    public List<String> getSpecializations() {
        return veterinarianRepository.getSpecializations();
    }

    @Override
    public List<UserDto> findAvailableVetsForAppointment(String specialization, LocalDate date, LocalTime time) {
        List<Veterinarian> filteredVets = getAvailableVeterinarians(specialization, date, time);
        return filteredVets.stream()
                .map(this::mapVeterinarianToUserDto)
                .toList();
    }

    @Override
    public List<Veterinarian> getVeterinariansBySpecialization(String specialization) {
        if (!veterinarianRepository.existsBySpecialization(specialization)) {
            throw new ResourceNotFoundException("Không tìm thấy bác sĩ thú y nào với chuyên khoa " + specialization);
        }
        return veterinarianRepository.findBySpecialization(specialization);

    }

    private UserDto mapVeterinarianToUserDto(Veterinarian veterinarian) {
        UserDto userDto = entityConverter.mapEntityToDto(veterinarian, UserDto.class);
        double averageRating = reviewService.getAverageRatingForVet(veterinarian.getId());
        Long totalReviewer = reviewRepository.countByVeterinarianId(veterinarian.getId());
        userDto.setAverageRating(averageRating);
        userDto.setTotalReviewers(totalReviewer);

        // OPTIMIZATION: Do NOT load full photo bytes for list view.
        // Frontend should fetch by ID or URL.
        if (veterinarian.getPhoto() != null) {
            userDto.setPhotoId(veterinarian.getPhoto().getId());
            // try {
            // byte[] photoBytes =
            // photoService.getImageData(veterinarian.getPhoto().getId());
            // userDto.setPhoto(photoBytes);
            // } catch (SQLException e) {
            // throw new RuntimeException(e.getMessage());
            // }
        }

        return userDto;
    }

    private List<Veterinarian> getAvailableVeterinarians(String specialization, LocalDate date, LocalTime time) {
        if (date != null && time != null) {
            LocalTime endTime = time.plusHours(2);
            if (specialization == null || specialization.isEmpty()) {
                return veterinarianRepository.findAllAvailableVeterinarians(date, time, endTime);
            } else {
                return veterinarianRepository.findAvailableVeterinarians(specialization, date, time, endTime);
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

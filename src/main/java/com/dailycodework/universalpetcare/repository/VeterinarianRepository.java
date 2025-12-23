package com.dailycodework.universalpetcare.repository;

import com.dailycodework.universalpetcare.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
        List<Veterinarian> findBySpecialization(String specialization);

        boolean existsBySpecialization(String specialization);

        @Query("SELECT DISTINCT v.specialization FROM Veterinarian v")
        List<String> getSpecializations();

        @Query("SELECT v.specialization as specialization, COUNT(v) as count FROM Veterinarian v GROUP BY v.specialization")
        List<Object[]> countVetsBySpecialization();

        @Query("SELECT v FROM Veterinarian v WHERE v.specialization = :specialization " +
                        "AND v.id NOT IN (" +
                        "  SELECT a.veterinarian.id FROM Appointment a " +
                        "  WHERE a.appointmentDate = :date " +
                        "  AND a.appointmentTime < :maxTime " +
                        "  AND a.appointmentTime > :minTime" +
                        ")")
        List<Veterinarian> findAvailableVeterinarians(@Param("specialization") String specialization,
                        @Param("date") LocalDate date,
                        @Param("minTime") LocalTime minTime,
                        @Param("maxTime") LocalTime maxTime);

        @Query("SELECT v FROM Veterinarian v WHERE v.id NOT IN (" +
                        "  SELECT a.veterinarian.id FROM Appointment a " +
                        "  WHERE a.appointmentDate = :date " +
                        "  AND a.appointmentTime < :maxTime " +
                        "  AND a.appointmentTime > :minTime" +
                        ")")
        List<Veterinarian> findAllAvailableVeterinarians(@Param("date") LocalDate date,
                        @Param("minTime") LocalTime minTime,
                        @Param("maxTime") LocalTime maxTime);
}

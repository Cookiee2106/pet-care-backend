package com.dailycodework.universalpetcare.repository;

import com.dailycodework.universalpetcare.model.User;
import com.dailycodework.universalpetcare.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.gender = :gender, u.phoneNumber = :phoneNumber WHERE u.id = :userId")
    User updateUser(@Param("userId") Long userId,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("gender") String gender,
            @Param("phoneNumber") String phoneNumber);

    List<Veterinarian> findAllByUserType(String vet);

    long countByUserType(String type);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isEnabled = :enabled WHERE u.id = :userId")
    void updateUserEnabledStatus(@Param("userId") Long userId, @Param("enabled") boolean enabled);

    @Query("SELECT u FROM User u WHERE " +
            "(:role IS NULL OR :role = '' OR u.userType = :role) AND " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<User> searchUsers(@Param("keyword") String keyword, @Param("role") String role);
}

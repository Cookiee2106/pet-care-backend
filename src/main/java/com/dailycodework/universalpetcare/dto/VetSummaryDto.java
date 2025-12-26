package com.dailycodework.universalpetcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetSummaryDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private Double averageRating;
    private Long totalReviewers;
    private Long photoId;

    // Constructor matching JPQL projection if needed,
    // but the service aggregation approach might be cleaner for the rating/photoId.
    // However, for pure DB projection we need a constructor matching the SELECT
    // fields.
    // Let's stick to the user's request: fields matching the projection.

    // Note: Rating and ReviewCount are calculated, so they might not be directly in
    // the User table projection
    // unless we join Review table.
    // The previous optimization calculated these in memory via batch.
    // To keep it simple and performant:
    // 1. Fetch Page<User> (or projection of User fields)
    // 2. Map to DTO and fill rating/photoId from the batch maps we already built.

    // BUT the user explicitly asked for "Select ONLY the fields... Projection".
    // "Fields: id, firstName, lastName, specialization, photoUrl..."
    // Since we don't store photoUrl (we use photoId from blob), and rating is
    // calculated...
    // I will adapt the DTO to what we actually have.
}

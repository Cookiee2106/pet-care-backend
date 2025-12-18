package com.dailycodework.universalpetcare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "vet_id")
public class Veterinarian extends User {
    // Inherits id from User - no need to redefine
    private String specialization;

    public Veterinarian(String specialization) {
        this.specialization = specialization;
    }
}

package com.dailycodework.universalpetcare.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Veterinarian extends User {
    // Inherits id from User - no need to redefine
    // PrimaryKeyJoinColumn defaults to "id" which matches our database schema
    private String specialization;

    public Veterinarian(String specialization) {
        this.specialization = specialization;
    }
}

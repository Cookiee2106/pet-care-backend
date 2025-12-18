package com.dailycodework.universalpetcare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "adm_id")
public class Admin extends User{
    // Inherits id from User - no need to redefine
}

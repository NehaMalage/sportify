package com.sportsvenue.venuemanagement.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private int totalCourts;
    private int capacity = 0;
    private String facilities;
    private String description;
    private String imageUrl;
    private String sportType;
    private double pricePerHour;
    private String openingTime;
    private String closingTime;
    private LocalDateTime createdAt;
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private User manager;
}

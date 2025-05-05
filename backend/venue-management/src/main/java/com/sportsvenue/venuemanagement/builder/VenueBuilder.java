package com.sportsvenue.venuemanagement.builder;

import com.sportsvenue.venuemanagement.model.Venue;
import com.sportsvenue.venuemanagement.model.User;

public class VenueBuilder {
    private String name;
    private String location;
    private String description;
    private String imageUrl;
    private String sportType;
    private Integer totalCourts;
    private Double pricePerHour;
    private String openingTime;
    private String closingTime;
    private String facilities;
    private User manager;
    private Boolean active;

    public VenueBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public VenueBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public VenueBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public VenueBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public VenueBuilder withSportType(String sportType) {
        this.sportType = sportType;
        return this;
    }

    public VenueBuilder withTotalCourts(Integer totalCourts) {
        this.totalCourts = totalCourts;
        return this;
    }

    public VenueBuilder withPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
        return this;
    }

    public VenueBuilder withOpeningTime(String openingTime) {
        this.openingTime = openingTime;
        return this;
    }

    public VenueBuilder withClosingTime(String closingTime) {
        this.closingTime = closingTime;
        return this;
    }

    public VenueBuilder withFacilities(String facilities) {
        this.facilities = facilities;
        return this;
    }

    public VenueBuilder withManager(User manager) {
        this.manager = manager;
        return this;
    }

    public VenueBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Venue build() {
        Venue venue = new Venue();
        venue.setName(name);
        venue.setLocation(location);
        venue.setDescription(description);
        venue.setImageUrl(imageUrl);
        venue.setSportType(sportType);
        venue.setTotalCourts(totalCourts);
        venue.setPricePerHour(pricePerHour);
        venue.setOpeningTime(openingTime);
        venue.setClosingTime(closingTime);
        venue.setFacilities(facilities);
        venue.setManager(manager);
        venue.setActive(active);
        return venue;
    }
} 
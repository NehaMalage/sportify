package com.sportsvenue.venuemanagement.service.impl;

import com.sportsvenue.venuemanagement.model.Venue;
import com.sportsvenue.venuemanagement.repository.VenueRepository;
import com.sportsvenue.venuemanagement.repository.UserRepository;
import com.sportsvenue.venuemanagement.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sportsvenue.venuemanagement.model.User;
import com.sportsvenue.venuemanagement.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    private static final Logger logger = LoggerFactory.getLogger(VenueServiceImpl.class);

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    @Override
    public Venue getVenueById(Long id) {
        return venueRepository.findById(id).orElseThrow(() -> new RuntimeException("Venue not found"));
    }

    @Override
    public Venue createVenue(Venue venue) {
        // Validate venue data
        if (venue.getName() == null || venue.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Venue name is required");
        }
        if (venue.getLocation() == null || venue.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Venue location is required");
        }
        if (venue.getTotalCourts() <= 0) {
            throw new IllegalArgumentException("Total courts must be greater than 0");
        }

        // Check if venue with same name already exists
        if (venueRepository.existsByName(venue.getName())) {
            throw new IllegalArgumentException("A venue with this name already exists");
        }

        // Set creation timestamp
        venue.setCreatedAt(LocalDateTime.now());
        
        // Save the venue
        return venueRepository.save(venue);
    }

    @Override
    public Venue updateVenue(Long id, Venue updatedVenue) {
        return venueRepository.findById(id).map(venue -> {
            venue.setName(updatedVenue.getName());
            venue.setLocation(updatedVenue.getLocation());
            venue.setFacilities(updatedVenue.getFacilities());
            venue.setTotalCourts(updatedVenue.getTotalCourts());
            venue.setDescription(updatedVenue.getDescription());
            venue.setImageUrl(updatedVenue.getImageUrl());
            venue.setSportType(updatedVenue.getSportType());
            venue.setPricePerHour(updatedVenue.getPricePerHour());
            venue.setOpeningTime(updatedVenue.getOpeningTime());
            venue.setClosingTime(updatedVenue.getClosingTime());
            return venueRepository.save(venue);
        }).orElseThrow(() -> new RuntimeException("Venue not found"));
    }

    @Override
    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }

    @Override
    public List<Venue> searchVenues(String name, String location, String sportType) {
        List<Venue> venues = venueRepository.findAll();

        return venues.stream()
                .filter(venue -> name == null || venue.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(venue -> location == null || venue.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(venue -> sportType == null
                        || venue.getSportType().toLowerCase().contains(sportType.toLowerCase()))
                .collect(Collectors.toList());
    }
}
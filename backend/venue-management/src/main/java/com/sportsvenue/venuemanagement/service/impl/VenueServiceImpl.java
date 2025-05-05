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
import com.sportsvenue.venuemanagement.builder.VenueBuilder;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return venueRepository.findAllActiveVenues();
        }

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return venueRepository.findAll();
        }

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_VENUE_MANAGER"))) {
            User manager = userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return venueRepository.findByManagerId(manager.getId());
        }

        return venueRepository.findAllActiveVenues();
    }

    @Override
    public List<Venue> getVenuesByManagerId(Long managerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("No authenticated user found");
        }

        // Only allow access if the requesting user is an admin or the manager themselves
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            User currentUser = userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!currentUser.getId().equals(managerId)) {
                throw new AccessDeniedException("You don't have permission to view these venues");
            }
        }

        return venueRepository.findByManagerId(managerId);
    }

    @Override
    public Venue getVenueById(Long id) {
        return venueRepository.findById(id).orElseThrow(() -> new RuntimeException("Venue not found"));
    }

    @Override
    public Venue createVenue(Venue venueRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User manager = null;
        if (auth != null) {
            String username = auth.getName();
            manager = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        // Check if venue with same name already exists
        if (venueRepository.existsByName(venueRequest.getName())) {
            throw new IllegalArgumentException("A venue with this name already exists");
        }

        // Use the builder to create the venue
        Venue venue = new VenueBuilder()
                .withName(venueRequest.getName())
                .withLocation(venueRequest.getLocation())
                .withDescription(venueRequest.getDescription())
                .withImageUrl(venueRequest.getImageUrl())
                .withSportType(venueRequest.getSportType())
                .withTotalCourts(venueRequest.getTotalCourts())
                .withPricePerHour(venueRequest.getPricePerHour())
                .withOpeningTime(venueRequest.getOpeningTime())
                .withClosingTime(venueRequest.getClosingTime())
                .withFacilities(venueRequest.getFacilities())
                .withManager(manager)
                .withActive(true)
                .build();

        return venueRepository.save(venue);
    }

    @Override
    public Venue updateVenue(Long id, Venue updatedVenue) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Venue existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        
        // Check if user is admin or the venue manager
        if (auth != null && !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            String username = auth.getName();
            User manager = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!existingVenue.getManager().getId().equals(manager.getId())) {
                throw new AccessDeniedException("You don't have permission to update this venue");
            }
        }

        // Use the builder to update the venue
        Venue venue = new VenueBuilder()
                .withName(updatedVenue.getName())
                .withLocation(updatedVenue.getLocation())
                .withDescription(updatedVenue.getDescription())
                .withImageUrl(updatedVenue.getImageUrl())
                .withSportType(updatedVenue.getSportType())
                .withTotalCourts(updatedVenue.getTotalCourts())
                .withPricePerHour(updatedVenue.getPricePerHour())
                .withOpeningTime(updatedVenue.getOpeningTime())
                .withClosingTime(updatedVenue.getClosingTime())
                .withFacilities(updatedVenue.getFacilities())
                .withManager(existingVenue.getManager())
                .withActive(existingVenue.isActive())
                .build();

        venue.setId(id); // Preserve the ID for update
        return venueRepository.save(venue);
    }

    @Override
    public void deleteVenue(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        
        // Check if user is admin or the venue manager
        if (auth != null && !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            String username = auth.getName();
            User manager = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!venue.getManager().getId().equals(manager.getId())) {
                throw new AccessDeniedException("You don't have permission to delete this venue");
            }
        }
        
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
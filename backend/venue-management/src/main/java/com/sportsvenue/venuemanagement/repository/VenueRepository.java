package com.sportsvenue.venuemanagement.repository;

import com.sportsvenue.venuemanagement.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    boolean existsByName(String name);
    
    List<Venue> findByManagerId(Long managerId);
    
    @Query("SELECT v FROM Venue v WHERE v.manager.id = :managerId")
    List<Venue> findVenuesByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT v FROM Venue v WHERE v.active = true")
    List<Venue> findAllActiveVenues();
}

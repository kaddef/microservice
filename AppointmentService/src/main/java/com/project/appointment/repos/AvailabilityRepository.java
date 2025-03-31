package com.project.appointment.repos;

import com.project.appointment.models.Availability;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AvailabilityRepository extends CrudRepository<Availability, Long> {
    Optional<Availability> findByUserId(Long userId);
}

package com.example.meddesk.repository;

import com.example.meddesk.model.doctor.Doctor;

import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository {
    Optional<Doctor> findById(Long id);
    void update (Doctor doctor);
    void create(Doctor doctor);
    //boolean hasTickets
}

package com.example.meddesk.repository;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.model.patient.Patient;

import java.util.Optional;

public interface PatientRepository {
    Optional<Patient> findById(Long id);
    void update (Patient patient);
    void create(Patient patient);
}

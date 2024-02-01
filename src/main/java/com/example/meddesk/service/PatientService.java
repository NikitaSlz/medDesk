package com.example.meddesk.service;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.model.patient.Patient;

public interface PatientService {
    Patient getById(Long id);

    Patient update(Patient patient);
    Patient create (Patient patient);
}

package com.example.meddesk.service.impl;

import com.example.meddesk.model.exception.ResourceNotFoundException;
import com.example.meddesk.model.patient.Patient;
import com.example.meddesk.repository.PatientRepository;
import com.example.meddesk.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    @Override
    @Transactional(readOnly = true)
    public Patient getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Patient not found"));
    }

    @Override
    @Transactional
    public Patient update(Patient patient) {
        patientRepository.update(patient);
        return patient;
    }

    @Override
    @Transactional
    public Patient create(Patient patient) {
        if(patient.getBirthday()==null) {
            throw new IllegalArgumentException("Birthday is not null");
        }
        patientRepository.create(patient);
        return patient;
    }
}

package com.example.meddesk.service.impl;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.model.exception.ResourceNotFoundException;
import com.example.meddesk.repository.DoctorRepository;
import com.example.meddesk.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional(readOnly = true)
    public Doctor getById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    @Override
    @Transactional
    public Doctor update(Doctor doctor) {
        doctorRepository.update(doctor);
        return doctor;
    }

    @Override
    @Transactional
    public Doctor create(Doctor doctor) {
        doctorRepository.create(doctor);
        return doctor;
    }


}

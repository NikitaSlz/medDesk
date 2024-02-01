package com.example.meddesk.service;

import com.example.meddesk.model.doctor.Doctor;

import java.util.UUID;

public interface DoctorService {

    Doctor getById(Long id);

    Doctor update( Doctor doctor);
    Doctor create (Doctor doctor);
}

package com.example.meddesk.service;

import com.example.meddesk.model.ticket.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {
    Ticket getById(Long id);

    List<Ticket> getAllByDoctorId(Long id);

    List<Ticket> getAllFreeTicketsByDoctorIdAndDate(Long id, LocalDateTime date);

    List<Ticket> getAllByPatientId(Long id);
    Ticket create(Ticket ticket, Long doctor_id);

    Ticket create(Ticket ticket, Long doctor_id, Long patient_id);

    Ticket update(Ticket ticket, Long patient_id, Long doctor_id);

    void delete(Long id);
}

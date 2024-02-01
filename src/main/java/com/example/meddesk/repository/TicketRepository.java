package com.example.meddesk.repository;

import com.example.meddesk.model.ticket.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> findById(Long id);

    List<Ticket> findAllByDoctorId(Long doctorId);

    List<Ticket> findAllByPatientId(Long patientId);

    List<Ticket> findAllFreeByDoctorIdAndDate(Long doctorId, LocalDateTime date);

    void assignToDoctorById(Long ticketId, Long doctorId);

    void assignToPatientById(Long ticketId, Long patientId, Long doctorId);

    void update(Ticket ticket);

    void create(Ticket ticket);

    void delete(Long id);


}

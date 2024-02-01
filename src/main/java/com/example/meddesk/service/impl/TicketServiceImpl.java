package com.example.meddesk.service.impl;

import com.example.meddesk.model.exception.ResourceNotFoundException;
import com.example.meddesk.model.ticket.Status;
import com.example.meddesk.model.ticket.Ticket;
import com.example.meddesk.repository.TicketRepository;
import com.example.meddesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public Ticket getById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllByDoctorId(Long id) {
        return ticketRepository.findAllByDoctorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllFreeTicketsByDoctorIdAndDate(Long id, LocalDateTime dateTime) {
        return ticketRepository.findAllFreeByDoctorIdAndDate(id, dateTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllByPatientId(Long id) {
        return ticketRepository.findAllByPatientId(id);
    }

    @Override
    @Transactional
    public Ticket create(Ticket ticket, Long doctor_id) {
        ticket.setStatus(Status.FREE);
        ticketRepository.create(ticket);
        ticketRepository.assignToDoctorById(ticket.getId(), doctor_id);
        return ticket;
    }

    @Override
    @Transactional
    public Ticket create(Ticket ticket, Long doctor_id, Long patient_id) {
        ticket.setStatus(Status.RESERVED);
        ticketRepository.create(ticket);
        ticketRepository.assignToDoctorById(ticket.getId(), doctor_id);
        ticketRepository.assignToPatientById(ticket.getId(), patient_id, doctor_id);
        return ticket;
    }


    @Override
    @Transactional
    public Ticket update(Ticket ticket, Long patient_id, Long doctor_id) {
        if (ticket.getStatus() == Status.FREE) {
            /*if (ticket.getStartTime().isAfter(LocalDateTime.now()))
                ticket.setStatus(Status.RESERVED);*/
            ticket.setStatus(Status.RESERVED);
        }
        ticketRepository.assignToPatientById(ticket.getId(), patient_id, doctor_id);
        ticketRepository.update(ticket);
        return ticket;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ticketRepository.delete(id);
    }

    boolean isExists(Ticket ticket, Long doctor_id) {
        List<Ticket> tickets = ticketRepository.findAllByDoctorId(doctor_id);
        long count = tickets.stream().filter(x -> ticket.getStartTime().isAfter(x.getStartTime())
                && ticket.getStartTime().isBefore(x.getEndTime())
                && ticket.getEndTime().isAfter(x.getStartTime())).count();
        return count>0?true:false;
    }
}

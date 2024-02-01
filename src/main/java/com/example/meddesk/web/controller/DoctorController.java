package com.example.meddesk.web.controller;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.model.ticket.Ticket;
import com.example.meddesk.service.DoctorService;
import com.example.meddesk.service.TicketService;
import com.example.meddesk.web.dto.doctor.DoctorDto;
import com.example.meddesk.web.dto.ticket.TicketDto;
import com.example.meddesk.web.dto.validation.OnCreate;
import com.example.meddesk.web.dto.validation.OnUpdate;
import com.example.meddesk.web.mappers.mappers.DoctorMapper;
import com.example.meddesk.web.mappers.mappers.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController {
    private final DoctorService doctorService;
    private final TicketService ticketService;
    private final DoctorMapper doctorMapper;
    private final TicketMapper ticketMapper;

    @PutMapping
    public DoctorDto update(@Validated(OnUpdate.class) @RequestBody DoctorDto dto) {
        Doctor doctor = doctorMapper.toEntity(dto);
        Doctor updatedDoctor = doctorService.update(doctor);
        return doctorMapper.toDto(updatedDoctor);
    }

    @GetMapping("/{id}")
    public DoctorDto getById(@PathVariable Long id) {
        Doctor doctor = doctorService.getById(id);
        return doctorMapper.toDto(doctor);
    }

    @GetMapping("/{id}/tickets")
    public List<TicketDto> getTicketsById(@PathVariable Long id) {
        List<Ticket> tickets = ticketService.getAllByDoctorId(id);
        return ticketMapper.toDto(tickets);
    }

    @PostMapping("/{id}/tickets")
    public TicketDto createTicket(@PathVariable Long id,
                                  @Validated(OnCreate.class) @RequestBody TicketDto dto) {
        Ticket ticket = ticketMapper.toEntity(dto);
        Ticket createdTicket = ticketService.create(ticket, id);
        return ticketMapper.toDto(createdTicket);
    }
}

package com.example.meddesk.web.controller;

import com.example.meddesk.model.ticket.Ticket;
import com.example.meddesk.service.TicketService;
import com.example.meddesk.web.dto.ticket.TicketDto;
import com.example.meddesk.web.dto.validation.OnUpdate;
import com.example.meddesk.web.mappers.mappers.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Validated
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    /**
     * Заполнение слотов врачей клиентами
     *
     * @param ticket_id
     * @param patient_id
     * @return обновленный слот
     */

    @PutMapping
    public TicketDto update(@Validated(OnUpdate.class) @RequestParam Long ticket_id, @RequestParam Long patient_id) {
        Ticket ticket = ticketService.getById(ticket_id);
        if (ticket != null) {
            ticketService.update(ticket, patient_id, ticket.getDoctor_id());
        }
        return ticketMapper.toDto(ticketService.getById(ticket_id));
    }

    /***
     * Получение свободных слотов времени к указанному врачу по его id и дате date
     * @param doctor_id
     * @param date
     * @return возвращает список талонов к врачу на указанную дату
     */
    @GetMapping("/getFree")
    public List<TicketDto> getAllFreeByDoctorIdAndDate(@RequestParam Long doctor_id, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime dateTime = localDate.atStartOfDay();
        List<Ticket> tickets = ticketService.getAllFreeTicketsByDoctorIdAndDate(doctor_id, dateTime);
        return ticketMapper.toDto(tickets);
    }

    /***
     * Получение всех талонов по id пациента
     * @param id
     * @return
     */
    @GetMapping("/patient/{id}")
    public List<TicketDto> getAllByPatientId(@PathVariable Long id) {
        List<Ticket> tickets = ticketService.getAllByPatientId(id);
        return ticketMapper.toDto(tickets);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        ticketService.delete(id);
    }

    @GetMapping("/{id}")
    public TicketDto getById(@PathVariable Long id) {
        Ticket ticket = ticketService.getById(id);
        return ticketMapper.toDto(ticket);
    }
}

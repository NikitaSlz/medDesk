package com.example.meddesk.model.patient;

import com.example.meddesk.model.ticket.Ticket;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class Patient {
    private Long id;
    private String name;

    private LocalDateTime birthday;
    private List<Ticket> tickets;
}

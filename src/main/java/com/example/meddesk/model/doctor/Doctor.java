package com.example.meddesk.model.doctor;

import com.example.meddesk.model.ticket.Ticket;
import lombok.Data;

import java.util.List;
@Data
public class Doctor {
    private Long id;
    private String name; //ФИО
    private String roomNumber;
    private List<Ticket> tickets;
}

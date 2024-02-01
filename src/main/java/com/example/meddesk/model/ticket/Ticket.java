package com.example.meddesk.model.ticket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Ticket {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long doctor_id;
    private Long patient_id;

    private Status status;
}

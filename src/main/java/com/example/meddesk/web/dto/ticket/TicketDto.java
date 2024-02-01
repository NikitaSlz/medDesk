package com.example.meddesk.web.dto.ticket;

import com.example.meddesk.model.ticket.Status;
import com.example.meddesk.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
public class TicketDto {
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @NotNull(message = "Doctor_id must be not null", groups = OnUpdate.class)
    private Long doctor_id;
    private Long patient_id;

    private Status status;
}

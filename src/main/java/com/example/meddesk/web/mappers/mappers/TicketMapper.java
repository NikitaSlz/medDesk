package com.example.meddesk.web.mappers.mappers;

import com.example.meddesk.model.ticket.Ticket;
import com.example.meddesk.web.dto.ticket.TicketDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketDto toDto(Ticket ticket);
    List<TicketDto> toDto(List<Ticket> tickets);
    Ticket toEntity(TicketDto dto);
}

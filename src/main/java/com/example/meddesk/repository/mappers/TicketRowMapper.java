package com.example.meddesk.repository.mappers;

import com.example.meddesk.model.ticket.Status;
import com.example.meddesk.model.ticket.Ticket;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TicketRowMapper {
    @SneakyThrows
    public static Ticket mapRow(ResultSet resultSet) {
        if(resultSet.next()) {
            Ticket ticket = new Ticket();
            ticket.setId(resultSet.getLong("ticket_id"));
            ticket.setStartTime((resultSet.getTimestamp("ticket_start_time").toLocalDateTime()));
            ticket.setEndTime((resultSet.getTimestamp("ticket_end_time").toLocalDateTime()));
            ticket.setStatus(Status.valueOf(resultSet.getString("ticket_status")));
            ticket.setDoctor_id(resultSet.getLong("doctor_id"));
            ticket.setPatient_id(resultSet.getLong("patient_id"));
            return ticket;
        }
        return null;
    }

    @SneakyThrows
    public static List<Ticket> mapRows(ResultSet resultSet) {
        List<Ticket> tickets = new ArrayList<>();
        while(resultSet.next()) {
            Ticket ticket = new Ticket();
            ticket.setId(resultSet.getLong("ticket_id"));
            if(!resultSet.wasNull()) {
                ticket.setStartTime((resultSet.getTimestamp("ticket_start_time").toLocalDateTime()));
                ticket.setEndTime((resultSet.getTimestamp("ticket_end_time").toLocalDateTime()));
                ticket.setStatus(Status.valueOf(resultSet.getString("ticket_status")));
                ticket.setDoctor_id(resultSet.getLong("doctor_id"));
            }
            tickets.add(ticket);
        }
        return tickets;
    }

    @SneakyThrows
    public static List<Ticket> mapRowsPatient(ResultSet resultSet) {
        List<Ticket> tickets = new ArrayList<>();
        while(resultSet.next()) {
            Ticket ticket = new Ticket();
            ticket.setId(resultSet.getLong("ticket_id"));
            if(!resultSet.wasNull()) {
                ticket.setStartTime((resultSet.getTimestamp("ticket_start_time").toLocalDateTime()));
                ticket.setEndTime((resultSet.getTimestamp("ticket_end_time").toLocalDateTime()));
                ticket.setStatus(Status.valueOf(resultSet.getString("ticket_status")));
                ticket.setDoctor_id(resultSet.getLong("doctor_id"));
                ticket.setPatient_id(resultSet.getLong("patient_id"));
            }
            tickets.add(ticket);
        }
        return tickets;
    }
}

package com.example.meddesk.repository.mappers;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.model.ticket.Ticket;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;

public class DoctorRowMapper {
    @SneakyThrows
    public static Doctor mapRow(ResultSet resultSet) {
        List<Ticket> tickets = TicketRowMapper.mapRows(resultSet);
        resultSet.beforeFirst();
        if(resultSet.next()) {
            Doctor doctor = new Doctor();
            doctor.setId(resultSet.getLong("doctor_id"));
            doctor.setName(resultSet.getString("doctor_name"));
            doctor.setRoomNumber(resultSet.getString("doctor_room"));
            doctor.setTickets(tickets);
            return doctor;
        }
        return null;
    }
}

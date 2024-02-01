package com.example.meddesk.repository.mappers;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.model.patient.Patient;
import com.example.meddesk.model.ticket.Ticket;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;

public class PatientRowMapper {
    @SneakyThrows
    public static Patient mapRow(ResultSet resultSet) {
        List<Ticket> tickets = TicketRowMapper.mapRows(resultSet);
        resultSet.beforeFirst();
        if(resultSet.next()) {
            Patient patient = new Patient();
            patient.setId(resultSet.getLong("patient_id"));
            patient.setName(resultSet.getString("patient_name"));
            patient.setBirthday(resultSet.getTimestamp("patient_birthday").toLocalDateTime());
            patient.setTickets(tickets);
            return patient;
        }
        return null;
    }
}

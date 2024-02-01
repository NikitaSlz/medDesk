package com.example.meddesk.repository.impl;

import com.example.meddesk.model.exception.ResourceMappingException;
import com.example.meddesk.model.ticket.Ticket;
import com.example.meddesk.repository.DataSourceConfig;
import com.example.meddesk.repository.TicketRepository;
import com.example.meddesk.repository.mappers.TicketRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {
    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT t.id as ticket_id,
                   t.start_time as ticket_start_time,
                   t.end_time as ticket_end_time,
                   t.status as ticket_status,
                   dt.doctor_id as doctor_id,
                   dt.patient_id as patient_id
            FROM tickets t
                JOIN doctors_tickets dt on t.id = dt.ticket_id
            WHERE id = ?
            """;
    private final String FIND_ALL_BY_DOCTOR_ID = """
            SELECT t.id as ticket_id,
                   t.start_time as ticket_start_time,
                   t.end_time as ticket_end_time,
                   t.status as ticket_status
            FROM tickets t
                JOIN doctors_tickets dt on t.id = dt.ticket_id
            WHERE dt.doctor_id = ?
            """;
    private final String FIND_ALL_BY_PATIENT_ID = """
            SELECT t.id as ticket_id,
                   t.start_time as ticket_start_time,
                   t.end_time as ticket_end_time,
                   t.status as ticket_status,
                   dt.doctor_id as doctor_id,
                   dt.patient_id as patient_id
            FROM tickets t
                     JOIN doctors_tickets dt on t.id = dt.ticket_id
            WHERE dt.patient_id = ?""";
    private final String FIND_ALL_BY_PATIENT_ID_FULL_INFORMATION = """
            SELECT t.id as ticket_id,
                   t.start_time as ticket_start_time,
                   t.end_time as ticket_end_time,
                   t.status as ticket_status,
                   d.name as doctor_name,
                   d.roomnumber as doctor_room,
                   p.name as patients_name
            FROM tickets t
                JOIN doctors_tickets dt on t.id = dt.ticket_id
                JOIN doctors d on d.id = dt.doctor_id
                JOIN patients p on p.id = dt.patient_id
            WHERE dt.patient_id = ?""";

    private final String FIND_ALL_FREE_BY_DOCTOR_ID_AND_DATE = """
            SELECT t.id as ticket_id,
                   t.start_time as ticket_start_time,
                   t.end_time as ticket_end_time,
                   t.status as ticket_status,
                   d.name as doctor_name,
                   d.roomnumber as doctor_room,
                   d.id as doctor_id
            FROM tickets t
                     JOIN doctors_tickets dt on t.id = dt.ticket_id
                     JOIN doctors d on d.id = dt.doctor_id
            WHERE dt.doctor_id = ? AND t.status = 'FREE' AND  DATE(t.start_time) = ?""";
    private final String ASSIGN_DOCTOR = """
            INSERT INTO doctors_tickets (ticket_id, doctor_id)
            VALUES (?, ?)""";

    private final String ASSIGN_PATIENT = """
            UPDATE doctors_tickets
                SET patient_id = ?
            WHERE ticket_id = ? and doctor_id = ?""";
    private final String UPDATE = """
            UPDATE tickets
            SET start_time = ?,
                end_time = ?,
                status = ?
            WHERE id = ?""";

    private final String CREATE = """
            INSERT INTO tickets (start_time, end_time, status)
            VALUES(?, ?, ?)""";

    private final String DELETE = """
            DELETE FROM tickets
            WHERE id = ?""";

    @Override
    public Optional<Ticket> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(TicketRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding ticket by id", e);
        }
    }

    @Override
    public List<Ticket> findAllByDoctorId(Long doctorId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_DOCTOR_ID);
            statement.setLong(1, doctorId);
            try (ResultSet rs = statement.executeQuery()) {
                return TicketRowMapper.mapRows(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding tickets by doctor_id", e);
        }
    }

    @Override
    public List<Ticket> findAllByPatientId(Long patientId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_PATIENT_ID);
            statement.setLong(1, patientId);
            try (ResultSet rs = statement.executeQuery()) {
                return TicketRowMapper.mapRowsPatient(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding tickets by patient_id", e);
        }
    }

    @Override
    public List<Ticket> findAllFreeByDoctorIdAndDate(Long doctorId, LocalDateTime date) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_FREE_BY_DOCTOR_ID_AND_DATE);
            statement.setLong(1, doctorId);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            statement.setTimestamp(2, Timestamp.valueOf(date));
            try (ResultSet rs = statement.executeQuery()) {
                return TicketRowMapper.mapRows(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding free tickets by doctor_id", e);
        }
    }

    @Override
    public void assignToDoctorById(Long ticketId, Long doctorId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN_DOCTOR);
            statement.setLong(1, ticketId);
            statement.setLong(2, doctorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while assigning to doctor", e);
        }
    }

    @Override
    public void assignToPatientById(Long ticketId, Long patientId, Long doctor_id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN_PATIENT);
            statement.setLong(2, ticketId);
            statement.setLong(1, patientId);
            statement.setLong(3, doctor_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while assigning to patient", e);
        }
    }

    @Override
    public void update(Ticket ticket) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            if (ticket.getStartTime() == null) {
                statement.setNull(1, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(1, Timestamp.valueOf(ticket.getStartTime()));
            }
            if (ticket.getEndTime() == null) {
                statement.setNull(2, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(2, Timestamp.valueOf(ticket.getEndTime()));
            }
            statement.setString(3, ticket.getStatus().name());
            statement.setLong(4, ticket.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating ticket", e);
        }
    }

    @Override
    public void create(Ticket ticket) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            if (ticket.getStartTime() == null) {
                statement.setNull(1, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(1, Timestamp.valueOf(ticket.getStartTime()));
            }
            if (ticket.getEndTime() == null) {
                statement.setNull(2, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(2, Timestamp.valueOf(ticket.getEndTime()));
            }
            statement.setString(3, ticket.getStatus().name());
            statement.executeUpdate();
            try(ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                ticket.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating ticket", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while deleting ticket");
        }
    }
}

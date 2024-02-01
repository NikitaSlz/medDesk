package com.example.meddesk.repository.impl;

import com.example.meddesk.model.exception.ResourceMappingException;
import com.example.meddesk.model.patient.Patient;
import com.example.meddesk.repository.DataSourceConfig;
import com.example.meddesk.repository.PatientRepository;
import com.example.meddesk.repository.mappers.PatientRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepository {
    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT p.id         as patient_id,
                   p.name       as patient_name,
                   p.birthday   as patient_birthday,
                   d.name       as doctor_name,
                   d.roomnumber as doctor_room,
                   t.start_time as start_time_date,
                   t.end_time   as end_time_date,
                   t.status     as ticket_status
            FROM patients p
                     LEFT JOIN doctors_tickets dt on p.id = dt.patient_id
                     LEFT JOIN tickets t on dt.ticket_id = t.id
                     LEFT JOIN doctors d on dt.doctor_id = d.id
            WHERE p.id = ?
            ORDER BY t.start_time""";
    private final String UPDATE = """
            UPDATE patients
            SET name = ?,
            birthday = ?
            WHERE id = ?""";

    private final String CREATE = """
            INSERT INTO patients (name, birthday)
            VALUES(?, ?)""";
    @Override
    public Optional<Patient> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(PatientRowMapper.mapRow(rs));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while finding patient by id");
        }
    }

    @Override
    public void update(Patient patient) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, patient.getName());
            statement.setTimestamp(2, Timestamp.valueOf(patient.getBirthday()));
            statement.setLong(3, patient.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while updating patient");
        }
    }

    @Override
    public void create(Patient patient) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, patient.getName());
            statement.setTimestamp(2, Timestamp.valueOf(patient.getBirthday()));
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                patient.setId(rs.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while creating patient");
        }
    }
}

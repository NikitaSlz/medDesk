package com.example.meddesk.repository.impl;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.model.exception.ResourceMappingException;
import com.example.meddesk.repository.DataSourceConfig;
import com.example.meddesk.repository.DoctorRepository;
import com.example.meddesk.repository.mappers.DoctorRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DoctorRepositoryImpl implements DoctorRepository {
    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT d.id as doctor_id,
                   d.name as doctor_name,
                   d.roomnumber as doctor_room,
                   p.name as patient_name,
                   t.status as ticket_status,
                   t.id as ticket_id,
                   t.start_time as ticket_start_time,
                   t.end_time as ticket_end_time
            FROM doctors d
                LEFT JOIN doctors_tickets dt on d.id = dt.doctor_id
                LEFT JOIN tickets t on dt.ticket_id = t.id
                LEFT JOIN patients p on dt.patient_id = p.id
            WHERE d.id = ?""";

    private final String UPDATE = """
            UPDATE doctors
            SET name = ?,
            roomnumber = ?
            WHERE id = ?""";

    private final String CREATE = """
            INSERT INTO doctors (name, roomnumber)
            VALUES(?, ?)""";


    @Override
    public Optional<Doctor> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(DoctorRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding by id", e);
        }
    }

    @Override
    public void update(Doctor doctor) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getRoomNumber());
            statement.setLong(3, doctor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating doctor", e);
        }
    }

    @Override
    public void create(Doctor doctor) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getRoomNumber());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                doctor.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating doctor", e);
        }
    }
}

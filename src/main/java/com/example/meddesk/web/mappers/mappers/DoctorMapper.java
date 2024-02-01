package com.example.meddesk.web.mappers.mappers;

import com.example.meddesk.model.doctor.Doctor;
import com.example.meddesk.web.dto.doctor.DoctorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor doctor);
    Doctor toEntity(DoctorDto dto);
}

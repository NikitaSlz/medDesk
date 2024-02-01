package com.example.meddesk.web.mappers.mappers;

import com.example.meddesk.model.patient.Patient;
import com.example.meddesk.web.dto.patient.PatientDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDto toDto(Patient patient);
    Patient toEntity(PatientDto dto);
}

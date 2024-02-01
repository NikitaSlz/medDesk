package com.example.meddesk.web.dto.doctor;

import com.example.meddesk.web.dto.validation.OnCreate;
import com.example.meddesk.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DoctorDto {
    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;
    @NotNull(message = "Name must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String name; //ФИО
    @NotNull(message = "Room must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 4, message = "Room length must be smaller than 4 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String roomNumber;

}

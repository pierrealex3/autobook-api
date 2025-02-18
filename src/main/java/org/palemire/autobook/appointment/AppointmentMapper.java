package org.palemire.autobook.appointment;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentEntity fromDtoToEntity(AppointmentDto dto);

    @InheritInverseConfiguration
    AppointmentDto fromEntityToDto(AppointmentEntity entity);
}

package org.palemire.autobook.appointment;

import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentEntity fromDtoToEntity(AppointmentDto dto);

    @AfterMapping
    default void linkNotes(@MappingTarget AppointmentEntity appointmentEntity) {
        appointmentEntity.getAppointmentNotes().forEach(an -> an.setAppointment(appointmentEntity));
    }

    @InheritInverseConfiguration
    AppointmentDto fromEntityToDto(AppointmentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    void fromDetachedToManaged(AppointmentEntity detached, @MappingTarget AppointmentEntity managed);
}

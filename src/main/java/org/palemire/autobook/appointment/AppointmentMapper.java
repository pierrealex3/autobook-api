package org.palemire.autobook.appointment;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = AppointmentWorkItemMapper.class)
public interface AppointmentMapper {

    @Mapping(target = "appointmentWorkItems", source = "appointmentWorkItems", qualifiedByName = "appointmentWorkItemMapperDtoToEntity")
    AppointmentEntity fromDtoToEntity(AppointmentDto dto);

    @AfterMapping
    default void linkBackreferences(@MappingTarget AppointmentEntity appointmentEntity) {
        appointmentEntity.getAppointmentNotes().forEach(an -> an.setAppointment(appointmentEntity));
        appointmentEntity.getAppointmentWorkItems().forEach(awi -> awi.setAppointment(appointmentEntity));
    }

    AppointmentDto fromEntityToDto(AppointmentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    void fromDetachedToManaged(AppointmentEntity detached, @MappingTarget AppointmentEntity managed);
}

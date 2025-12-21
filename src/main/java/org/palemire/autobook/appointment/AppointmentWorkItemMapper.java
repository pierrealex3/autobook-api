package org.palemire.autobook.appointment;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AppointmentWorkItemMapper {

    @Named("appointmentWorkItemMapperDtoToEntity")
    Set<AppointmentWorkItemEntity> fromDtoToEntity(List<AppointmentWorkItemDto> dto);

    AppointmentWorkItemEntity fromDtoToEntity(AppointmentWorkItemDto dto);

    AppointmentWorkItemLaborEntity fromDtoToEntity(AppointmentWorkItemLaborDto dto);

    @AfterMapping
    default void linkBackreferences(@MappingTarget AppointmentWorkItemEntity appointmentWorkItem) {
        appointmentWorkItem.getAppointmentWorkItemsLabor().forEach(awil -> awil.setAppointmentWorkItem(appointmentWorkItem));
    }

}

package org.palemire.autobook.appointment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentWorkItemMapper {

    AppointmentWorkItemEntity fromDtoToEntity(AppointmentWorkItemDto dto);

}

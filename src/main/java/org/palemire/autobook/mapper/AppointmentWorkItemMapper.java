package org.palemire.autobook.mapper;

import org.mapstruct.*;

import org.palemire.autobook.dto.*;
import org.palemire.autobook.entity.AppointmentWorkItemEntity;
import org.palemire.autobook.entity.AppointmentWorkItemLaborEntity;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {PieceOperationMapper.class, PieceMapper.class})
public abstract class AppointmentWorkItemMapper {

    @Named("appointmentWorkItemMapperDtoToEntity")
    abstract Set<AppointmentWorkItemEntity> fromDtoToEntity(List<AppointmentWorkItemDtoReq> dto);

    abstract AppointmentWorkItemLaborEntity fromDtoToEntity(AppointmentWorkItemLaborDto dto);

    @AfterMapping
    void linkBackreferences(@MappingTarget AppointmentWorkItemEntity appointmentWorkItem) {
        appointmentWorkItem.getAppointmentWorkItemsLabor()
                .forEach(labor -> labor.setAppointmentWorkItem(appointmentWorkItem));
    }

    @Named("appointmentWorkItemMapperEntityToDto")
    abstract List<AppointmentWorkItemDtoRes> fromEntityToDto(Set<AppointmentWorkItemEntity> entity);

}

package org.palemire.autobook.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.palemire.autobook.dto.AppointmentDto;
import org.palemire.autobook.entity.AppointmentEntity;

import java.time.temporal.ChronoField;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Mapper(componentModel = "spring", uses = AppointmentWorkItemMapper.class)
public interface AppointmentMapper {

    DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("HH:mm:ss")
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .toFormatter();

    @Mapping(target = "appointmentWorkItems", source = "appointmentWorkItems", qualifiedByName = "appointmentWorkItemMapperDtoToEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "xid", ignore = true)
    AppointmentEntity fromDtoToEntity(AppointmentDto dto);

    @AfterMapping
    default void linkBackreferences(@MappingTarget AppointmentEntity appointmentEntity) {
        appointmentEntity.getAppointmentNotes().forEach(an -> an.setAppointment(appointmentEntity));
        appointmentEntity.getAppointmentWorkItems().forEach(awi -> awi.setAppointment(appointmentEntity));
    }

    @Mapping(target = "appointmentWorkItems", source = "appointmentWorkItems", qualifiedByName = "appointmentWorkItemMapperEntityToDto")
    AppointmentDto fromEntityToDto(AppointmentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "xid", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    void fromDetachedToManaged(AppointmentEntity detached, @MappingTarget AppointmentEntity managed);

    default LocalTime map(String time) {
        return time == null ? null : LocalTime.parse(time);
    }

    default String map(LocalTime time) {
        return time == null ? null : time.format(TIME_FORMATTER);
    }
}

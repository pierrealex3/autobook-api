package org.palemire.autobook.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.palemire.autobook.vehicle.cud.VehicleEntity;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "appointment.appointmentId", target = "eventTargetId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(qualifiedByName = "vehicleTag", source = "vehicle", target = "vehicleTag")
    @Mapping(qualifiedByName = "dateTitleTag", source = "appointment", target = "date")
    @Mapping(source = "appointment.appointmentTitle", target = "title")
    @Mapping(constant = "APPOINTMENT", target = "eventType")
    EventDto fromAppointmentToEvent(EventAppointmentDomain appointment, VehicleEntity vehicle);

    @Named("vehicleTag")
    default String vehicleTag(VehicleEntity vehicle) {
        return Optional.ofNullable(vehicle.getTag()).orElseGet( () -> "%s %s %d".formatted(vehicle.getBrand(), vehicle.getModel(), vehicle.getYear()) );
    }

    @Named("dateTitleTag")
    default String dateTitleTag(EventAppointmentDomain appointment) {
        var dateTitleTag = appointment.getAppointmentDate().toString();
        if (appointment.getAppointmentTime() != null) {
            dateTitleTag += " @ " + appointment.getAppointmentTime();
        }
        return dateTitleTag;
    }
}

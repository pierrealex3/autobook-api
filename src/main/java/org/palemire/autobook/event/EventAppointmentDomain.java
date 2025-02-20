package org.palemire.autobook.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Getter
@Setter
public class EventAppointmentDomain {
    private Integer appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String appointmentTitle;

    public EventAppointmentDomain(Integer appointmentId, java.sql.Date appointmentDate, java.sql.Time appointmentTime, String appointmentTitle) {
        this.appointmentId = appointmentId;
        Optional.ofNullable(appointmentDate).map(java.sql.Date::toLocalDate).ifPresent(v -> this.appointmentDate = v);
        Optional.ofNullable(appointmentTime).map(java.sql.Time::toLocalTime).ifPresent(v -> this.appointmentTime = v);
        this.appointmentTitle = appointmentTitle;
    }
}

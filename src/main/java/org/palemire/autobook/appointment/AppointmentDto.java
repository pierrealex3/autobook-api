package org.palemire.autobook.appointment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AppointmentDto {

    private String title;

    private String note;

    private LocalDate date;

    private LocalTime time;

    private List<AppointmentNoteDto> appointmentNotes;
}

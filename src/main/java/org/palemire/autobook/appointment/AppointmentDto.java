package org.palemire.autobook.appointment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Jacksonized
@Getter
@Setter
public class AppointmentDto {

    private String title;

    private String note;

    private LocalDate date;

    private LocalTime time;

    private List<AppointmentNoteDto> appointmentNotes;
}

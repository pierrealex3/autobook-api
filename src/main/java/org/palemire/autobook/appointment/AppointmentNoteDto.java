package org.palemire.autobook.appointment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@Setter
public class AppointmentNoteDto {

    private String note;
}

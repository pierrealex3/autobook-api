package org.palemire.autobook.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@Setter
public class AppointmentCreatedDto {
    private String xid;
}

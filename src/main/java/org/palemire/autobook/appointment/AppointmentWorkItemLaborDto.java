package org.palemire.autobook.appointment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder
@Jacksonized
@Getter
@Setter
public class AppointmentWorkItemLaborDto {

    private String title;
    private BigDecimal hoursWorked;
    private BigDecimal cost;
}

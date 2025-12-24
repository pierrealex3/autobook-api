package org.palemire.autobook.appointment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@SuperBuilder
@Jacksonized
@Getter
@Setter
public class AppointmentWorkItemPieceBuyDto extends AppointmentWorkItemPieceDto {
    private BigDecimal cost;
}

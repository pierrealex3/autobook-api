package org.palemire.autobook.appointment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@Getter
@Setter
public class AppointmentWorkItemPieceRemovalDto extends AppointmentWorkItemPieceDto {
}

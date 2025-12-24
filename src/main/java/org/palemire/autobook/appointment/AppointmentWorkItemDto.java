package org.palemire.autobook.appointment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
@Getter
@Setter
public class AppointmentWorkItemDto {

    private String title;

    private List<AppointmentWorkItemLaborDto> appointmentWorkItemsLabor;

    private List<AppointmentWorkItemPieceDto> appointmentWorkItemsPiece;


}

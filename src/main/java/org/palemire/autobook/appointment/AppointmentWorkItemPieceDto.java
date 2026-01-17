package org.palemire.autobook.appointment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "category"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AppointmentWorkItemPieceBuyDto.class, name = "buy"),
        @JsonSubTypes.Type(value = AppointmentWorkItemPieceInstallDto.class, name = "install"),
        @JsonSubTypes.Type(value = AppointmentWorkItemPieceRemovalDto.class, name = "removal"),
        @JsonSubTypes.Type(value = AppointmentWorkItemPieceRepairDto.class, name = "repair")
})
public abstract class AppointmentWorkItemPieceDto {
    private String title;
    private PieceSummaryDto piece;
}

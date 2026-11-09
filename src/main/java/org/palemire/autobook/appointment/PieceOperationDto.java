package org.palemire.autobook.appointment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PieceOperationBuyDto.class, name = "buy"),
        @JsonSubTypes.Type(value = PieceOperationInstallDto.class, name = "install"),
        @JsonSubTypes.Type(value = PieceOperationRepairDto.class, name = "repair"),
        @JsonSubTypes.Type(value = PieceOperationSellDto.class, name = "sell"),
        @JsonSubTypes.Type(value = PieceOperationThrashDto.class, name = "thrash")
})
public abstract class PieceOperationDto {
    private BigDecimal cost;
    private LocalDateTime performedAt;
    private PieceSummaryDto piece;
}

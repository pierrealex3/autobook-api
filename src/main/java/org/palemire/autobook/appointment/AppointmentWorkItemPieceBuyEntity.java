package org.palemire.autobook.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "appointment_work_item_piece_buy")
@Entity
@DiscriminatorValue("BUY")
@Getter
@Setter
public class AppointmentWorkItemPieceBuyEntity extends AppointmentWorkItemPieceEntity {

    @Column(name = "cost")
    private BigDecimal cost;
}

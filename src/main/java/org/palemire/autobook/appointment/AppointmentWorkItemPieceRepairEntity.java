package org.palemire.autobook.appointment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "appointment_work_item_piece_repair")
@Entity
@DiscriminatorValue("REPAIR")
@Getter
@Setter
public class AppointmentWorkItemPieceRepairEntity extends AppointmentWorkItemPieceEntity {
}

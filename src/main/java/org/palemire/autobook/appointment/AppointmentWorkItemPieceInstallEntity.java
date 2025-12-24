package org.palemire.autobook.appointment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "appointment_work_item_piece_install")
@Entity
@DiscriminatorValue("INSTALL")
@Getter
@Setter
public class AppointmentWorkItemPieceInstallEntity extends AppointmentWorkItemPieceEntity {
}

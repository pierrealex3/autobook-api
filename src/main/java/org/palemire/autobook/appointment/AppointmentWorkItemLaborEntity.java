package org.palemire.autobook.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "appointment_work_item_labor")
@Entity
@Getter
@Setter
public class AppointmentWorkItemLaborEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "hours_worked")
    private BigDecimal hoursWorked;

    @Column(name = "cost")
    private BigDecimal cost;

    @JoinColumn(name = "appointment_work_item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppointmentWorkItemEntity appointmentWorkItem;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentWorkItemLaborEntity other)) return false;

        // actual check
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}

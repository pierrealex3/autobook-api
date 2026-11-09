package org.palemire.autobook.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Table(name = "appointment_work_item")
@Entity
@Getter
@Setter
public class AppointmentWorkItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private AppointmentEntity appointment;

    @OneToMany(mappedBy = "appointmentWorkItem", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<AppointmentWorkItemLaborEntity> appointmentWorkItemsLabor = new HashSet<>();

    @ManyToMany(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JoinTable(
            name = "appointment_work_item_piece_operation_asso",
            joinColumns = @JoinColumn(name = "appointment_work_item_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_operation_id")
    )
    @Setter(AccessLevel.NONE)
    private Set<PieceOperationEntity> pieceOperations = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentWorkItemEntity other)) return false;

        // actual check
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}

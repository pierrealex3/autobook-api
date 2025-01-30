package org.palemire.autobook.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.palemire.autobook.vehicle.cud.VehicleEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "api_user")
@Setter
@Getter
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String id;

    @Cascade(CascadeType.ALL)
    @ManyToMany
    @JoinTable(
            name = "user_vehicle_asso",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Setter(AccessLevel.NONE)
    private Set<VehicleEntity> vehicleSet = new HashSet<>();;
}

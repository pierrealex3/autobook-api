package org.palemire.autobook.user;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.palemire.autobook.vehicle.cud.VehicleEntity;

import java.io.Serializable;
import java.sql.Timestamp;

@Embeddable
@Getter
@Setter
public class UserVehiclePossessionId implements Serializable {

    @Column(name = "possession_date")
    private Timestamp possessionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicleEntity;

}

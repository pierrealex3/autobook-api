package org.palemire.autobook.user;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_vehicle_asso")
@Getter
@Setter
public class UserVehicleAssociationEntity {

    @EmbeddedId
    private  UserVehiclePossessionId id;

}

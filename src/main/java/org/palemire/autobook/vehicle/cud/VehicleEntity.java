package org.palemire.autobook.vehicle.cud;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.palemire.autobook.appointment.AppointmentEntity;
import org.palemire.autobook.user.UserEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;
    @Column(name = "modelyear")
    private Short year;

    @Column(name = "tag")
    private String tag;

    @ManyToMany(mappedBy = "vehicleSet")
    @Setter(AccessLevel.NONE)
    private Set<UserEntity> userSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<AppointmentEntity> appointments = new ArrayList<>();

}

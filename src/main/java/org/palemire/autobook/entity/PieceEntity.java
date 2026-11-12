package org.palemire.autobook.entity;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Table(name = "piece")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "category")
@Getter
@Setter
public abstract class PieceEntity {

    private static final ULID ULID_GENERATOR = new ULID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Column(name = "xid")
    private String xid;

    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "tag")
    private String tag;

    @Column(name = "km_life")
    private Integer kmLife;

    @Column(name = "days_life")
    private Integer daysLife;

    @PrePersist
    public void prePersist() {
        if (xid == null)
            xid = ULID_GENERATOR.nextULID();
    }

}

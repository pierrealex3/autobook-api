package org.palemire.autobook.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "piece_tire")
@Entity
@DiscriminatorValue("TIRE")
@Getter
@Setter
public class PieceTireEntity extends PieceEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tire_season_id")
    private StaticTireSeasonEntity tireSeason;

}

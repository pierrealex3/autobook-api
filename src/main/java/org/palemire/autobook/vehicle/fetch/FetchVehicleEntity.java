package org.palemire.autobook.vehicle.fetch;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
        name = "FetchVehicleListMapping",
        classes = @ConstructorResult(
                targetClass = VehicleDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "brand"),
                        @ColumnResult(name = "model"),
                        @ColumnResult(name = "modelyear"),
                        @ColumnResult(name = "tag")
                }
        )
)
@Entity
public class FetchVehicleEntity {

    @Id
    private Long id;
}

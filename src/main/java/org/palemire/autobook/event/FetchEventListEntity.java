package org.palemire.autobook.event;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import org.palemire.autobook.vehicle.cud.VehicleEntity;

@SqlResultSetMapping(
        name = "FetchEventListMapping",
        classes = @ConstructorResult(
                targetClass = EventAppointmentDomain.class,
                columns = {
                        @ColumnResult(name = "appointmentId"),
                        @ColumnResult(name = "appointmentDate"),
                        @ColumnResult(name = "appointmentTime"),
                        @ColumnResult(name = "appointmentTitle")
                }
        ),
        entities = @EntityResult(
                entityClass = VehicleEntity.class,
                fields = {
                        @FieldResult(name = "id", column = "vehicleId"),
                        @FieldResult(name = "brand", column = "brand"),
                        @FieldResult(name = "model", column = "model"),
                        @FieldResult(name = "year", column = "modelyear"),
                        @FieldResult(name = "tag", column = "tag")
                }
        )
)
@Entity
public class FetchEventListEntity {
    @Id
    private Long id;
}

package org.palemire.autobook.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FetchEventsDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public List fetchAllEvents(String userId) {

        var query = entityManager.createNativeQuery(
                "SELECT a.id as appointmentId, a.date as appointmentDate, a.time as appointmentTime, a.title as appointmentTitle, v.id as vehicleId, v.brand, v.model, v.modelyear, v.tag FROM appointment a INNER JOIN vehicle v ON a.vehicle_id = v.id WHERE v.id in (SELECT vv.vehicle_id FROM user_vehicle_asso vv WHERE vv.user_id = :userid)",
                "FetchEventListMapping"
        );
        query.setParameter("userid", userId);
        return query.getResultList();
    }
}

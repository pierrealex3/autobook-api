package org.palemire.autobook.vehicle.fetch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FetchVehicleDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public List<VehicleDto> fetchAllVehicles(String userId) {

        var query = entityManager.createNativeQuery(
                "SELECT v.id, v.brand, v.model, v.modelyear, v.tag FROM vehicle v WHERE v.id in (SELECT vv.vehicle_id FROM user_vehicle_asso vv WHERE user_id = :userid)",
                "FetchVehicleListMapping"
        );
        query.setParameter("userid", userId);
        return query.getResultList();
    }
}

package org.palemire.autobook.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.palemire.autobook.dto.VehicleDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FetchVehicleDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public List<VehicleDto> fetchAllVehicles(String userId) {

        var query = entityManager.createNativeQuery(
                "SELECT v.id, v.brand, v.model, v.modelyear, v.tag FROM vehicle v WHERE v.id in (SELECT vv.vehicle_id FROM user_vehicle_asso vv WHERE user_id = :userid)"
        );
        query.setParameter("userid", userId);
        List<?> rows = query.getResultList();
        List<VehicleDto> vehicles = new ArrayList<>(rows.size());
        for (Object result : rows) {
            Object[] row = (Object[]) result;
            vehicles.add(new VehicleDto()
                    .id(((Number) row[0]).intValue())
                    .brand((String) row[1])
                    .model((String) row[2])
                    .year(((Number) row[3]).intValue())
                    .tag((String) row[4]));
        }
        return vehicles;
    }
}

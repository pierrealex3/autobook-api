package org.palemire.autobook.vehicle.fetch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchVehicleService {
    private final FetchVehicleDao fetchVehicleDao;

    public List<VehicleDto> getAllVehicles(String userid) {
        return fetchVehicleDao.fetchAllVehicles(userid);
    }

}

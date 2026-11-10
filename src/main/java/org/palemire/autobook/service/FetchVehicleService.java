package org.palemire.autobook.service;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.dao.FetchVehicleDao;
import org.palemire.autobook.dto.VehicleDto;
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

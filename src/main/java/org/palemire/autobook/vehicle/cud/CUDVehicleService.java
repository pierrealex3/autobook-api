package org.palemire.autobook.vehicle.cud;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.user.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CUDVehicleService {

    private final VehicleRepository vehicleRepository;

    @Transactional
    public void createVehicle(CUDVehicleDto vehicleDto, UserEntity userEntity) {

        var vehicleEntity = new VehicleEntity();
        mapEntity(vehicleDto, vehicleEntity);
        vehicleEntity.getUserSet().add(userEntity);

        vehicleRepository.save(vehicleEntity);
    }

    @Transactional
    public void modifyVehicle(CUDVehicleDto vehicleDto) {

        var vehicleEntity = vehicleRepository.findById(vehicleDto.getId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        mapEntity(vehicleDto, vehicleEntity);

        vehicleRepository.save(vehicleEntity);
    }

    void mapEntity(CUDVehicleDto vehicleDto, VehicleEntity vehicleEntity) {
        vehicleEntity.setBrand(vehicleDto.getBrand());
        vehicleEntity.setModel(vehicleDto.getModel());
        vehicleEntity.setYear(vehicleDto.getYear());
        vehicleEntity.setTag(vehicleDto.getTag());
    }
}

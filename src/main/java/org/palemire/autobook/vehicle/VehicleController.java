package org.palemire.autobook.vehicle;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.Constants;
import org.palemire.autobook.vehicle.fetch.FetchVehicleService;
import org.palemire.autobook.vehicle.fetch.VehicleDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constants.AUTOBOOK_API_ROOT + "/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final FetchVehicleService fetchVehicleService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<VehicleDto>> getAllVehicles(@RequestHeader("X-MockUser") String userid) {
        List<VehicleDto> vehicleDtoList = fetchVehicleService.getAllVehicles(userid);
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4242");
        return new ResponseEntity<List<VehicleDto>>(vehicleDtoList, headers, HttpStatus.OK);
    }

}

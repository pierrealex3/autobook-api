package org.palemire.autobook.vehicle.fetch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VehicleDto {

    private Integer id;
    private String brand;
    private String model;
    private Short year;
    private String tag;

}

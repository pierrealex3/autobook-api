package org.palemire.autobook.vehicle.cud;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CUDVehicleDto {

    private Integer id;
    private String brand;
    private String model;
    private Short year;
    private String tag;
}

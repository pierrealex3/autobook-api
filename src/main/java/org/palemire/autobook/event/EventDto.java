package org.palemire.autobook.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDto {
    private Integer eventTargetId;
    private Integer vehicleId;
    private String vehicleTag;
    private String date;
    private String title;
    private String eventType;
}

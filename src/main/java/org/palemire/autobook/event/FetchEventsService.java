package org.palemire.autobook.event;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.vehicle.cud.VehicleEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchEventsService {

    private final FetchEventsDao fetchEventsDao;
    private final EventMapper eventMapper;

    public List<EventDto> getAllEvents(String userId) {
        List<EventDto> eventDtos = new ArrayList<>();

        List<Object[]> domains = (List<Object[]>)fetchEventsDao.fetchAllEvents(userId);
        for (Object[] domain : domains) {
            var vehicleDomain = (VehicleEntity)domain[0];
            var appointmentDomain = (EventAppointmentDomain)domain[1];
            eventDtos.add(eventMapper.fromAppointmentToEvent(appointmentDomain, vehicleDomain));
        }
        return eventDtos;
    }


}

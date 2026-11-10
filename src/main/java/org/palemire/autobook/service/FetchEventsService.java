package org.palemire.autobook.service;

import lombok.RequiredArgsConstructor;
import org.palemire.autobook.dao.FetchEventsDao;
import org.palemire.autobook.domain.EventAppointmentDomain;
import org.palemire.autobook.dto.EventDto;
import org.palemire.autobook.entity.VehicleEntity;
import org.palemire.autobook.mapper.EventMapper;
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

        List<Object[]> domains = fetchEventsDao.fetchAllEvents(userId);
        for (Object[] domain : domains) {
            var vehicleDomain = (VehicleEntity)domain[0];
            var appointmentDomain = (EventAppointmentDomain)domain[1];
            eventDtos.add(eventMapper.fromAppointmentToEvent(appointmentDomain, vehicleDomain));
        }
        return eventDtos;
    }


}

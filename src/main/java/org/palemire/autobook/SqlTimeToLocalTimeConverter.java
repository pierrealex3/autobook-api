package org.palemire.autobook;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Time;
import java.time.LocalTime;

@Converter(autoApply = true)
public class SqlTimeToLocalTimeConverter implements AttributeConverter<Time, LocalTime> {

    @Override
    public LocalTime convertToDatabaseColumn(Time attribute) {
        return (attribute != null) ? attribute.toLocalTime() : null;
    }

    @Override
    public Time convertToEntityAttribute(LocalTime dbData) {
        return (dbData != null) ? Time.valueOf(dbData) : null;
    }
}


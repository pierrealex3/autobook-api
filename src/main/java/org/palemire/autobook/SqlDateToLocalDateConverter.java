package org.palemire.autobook;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class SqlDateToLocalDateConverter implements AttributeConverter<Date, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(Date attribute) {
        return (attribute != null) ? attribute.toLocalDate() : null;
    }

    @Override
    public Date convertToEntityAttribute(LocalDate dbData) {
        return (dbData != null) ? Date.valueOf(dbData) : null;
    }
}


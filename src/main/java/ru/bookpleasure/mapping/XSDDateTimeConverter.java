package ru.bookpleasure.mapping;

import org.dozer.DozerConverter;
import org.dozer.converters.DateConverter;

import javax.xml.bind.DatatypeConverter;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Kirill on 06/11/16.
 */
public class XSDDateTimeConverter extends DozerConverter<String, Timestamp> {

    public XSDDateTimeConverter() {
        super(String.class, Timestamp.class);
    }

    @Override
    public Timestamp convertTo(String source, Timestamp destination) {
        return new Timestamp(
                DatatypeConverter.parseDateTime(source)
                        .getTime()
                        .getTime());
    }

    @Override
    public String convertFrom(Timestamp source, String destination) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source.getTime());
        return DatatypeConverter.printDateTime(calendar);
    }
}

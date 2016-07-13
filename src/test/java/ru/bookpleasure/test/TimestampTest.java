package ru.bookpleasure.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Kirill on 10/07/16.
 */
public class TimestampTest {

    @Test(dataProvider = "provider")
    public void testStringToTimestampConversion(String date) {
        new Timestamp(DatatypeConverter.parseDateTime(date).getTime().getTime());
    }

    @DataProvider
    Object[][] provider() {
        return new Object[][]{
                {"2013-12-26T08:28:34Z"},
                {"2011-07-01T09:00:00.000+04:00"}
        };
    }

}

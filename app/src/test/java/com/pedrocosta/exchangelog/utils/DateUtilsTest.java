package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.base.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class DateUtilsTest {

    @Test
    public void testDateConvertFromStringToDateDefaultFormat() throws ParseException {
        String dateStr = "2021-04-05";
        Date date1 = DateUtils.stringToDate(dateStr);
        Date date2 = new SimpleDateFormat(DateUtils.DEFAULT_FORMAT).parse(dateStr);
        assert date2.equals(date1);
    }

    @Test
    public void testDateConvertFromStringToDateWithFormat() throws ParseException {
        Date date1 = DateUtils.stringToDate("05/04/2021", "dd/MM/yyyy");
        Date date2 = new SimpleDateFormat(DateUtils.DEFAULT_FORMAT).parse("2021-04-05");
        assert date2.equals(date1);
    }

    @Test
    public void testDateConvertFromDateToStringDefaultFormat() throws ParseException {
        String dateStr = "2021-04-05";
        Date date = new SimpleDateFormat(DateUtils.DEFAULT_FORMAT).parse(dateStr);
        String str = DateUtils.dateToString(date);
        assert dateStr.equals(str);
    }

    @Test
    public void testDateConvertFromDateToStringWithFormat() throws ParseException {
        Date date = new SimpleDateFormat(DateUtils.DEFAULT_FORMAT).parse("2021-04-05");
        String str = DateUtils.dateToString(date, "dd/MM/yyyy");
        assert "05/04/2021".equals(str);
    }
}

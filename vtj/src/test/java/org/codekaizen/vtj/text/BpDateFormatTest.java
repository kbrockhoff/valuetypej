/*
 * Copyright (c) 2008 Kevin Brockhoff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codekaizen.vtj.text;

import static org.testng.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang.SerializationUtils;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link BpDateFormat}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
@Test
public class BpDateFormatTest {

    /**
     * Creates a new BpDateFormatTest object.
     */
    public BpDateFormatTest() {
    }

    /**
     * DOCUMENT ME!
     */
    public void testConstruction() {
        BpDateFormat fmt1 = null;
        BpDateFormat fmt2 = null;
        fmt1 = new BpDateFormat();
        fmt2 = new BpDateFormat(BpDateFormat.JVM_DATE_TIME, Locale.getDefault());
        assertEquals(fmt1, fmt2);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void testSerialization() throws Exception {
        BpDateFormat fmt1 = null;
        BpDateFormat fmt2 = null;
        fmt1 = new BpDateFormat(BpDateFormat.ISO_DATE_TIME, null);
        fmt2 = (BpDateFormat) SerializationUtils.deserialize(SerializationUtils.serialize(fmt1));
        assertEquals(fmt1, fmt2);
    }

    /**
     * DOCUMENT ME!
     */
    public void testFormatting() {
        DateFormat fmt1 = null;
        DateFormat fmt2 = null;
        Date date = null;
        String s1 = null;
        String s2 = null;

        date = new Date();

        fmt1 = new BpDateFormat(BpDateFormat.JVM_DATE_TIME, null);
        fmt2 = DateFormat.getDateTimeInstance();
        s1 = fmt1.format(date);
        s2 = fmt2.format(date);
        assertEquals(s2, s1);

        fmt1 = new BpDateFormat(BpDateFormat.JVM_DATE_ONLY, null);
        fmt2 = DateFormat.getDateInstance(DateFormat.SHORT);
        s1 = fmt1.format(date);
        s2 = fmt2.format(date);
        assertEquals(s2, s1);

        fmt1 = new BpDateFormat(BpDateFormat.JVM_TIME_ONLY, null);
        fmt2 = DateFormat.getTimeInstance(DateFormat.SHORT);
        s1 = fmt1.format(date);
        s2 = fmt2.format(date);
        assertEquals(s2, s1);

        fmt1 = new BpDateFormat(BpDateFormat.JVM_FULL_DATE, null);
        fmt2 = DateFormat.getDateInstance(DateFormat.FULL);
        s1 = fmt1.format(date);
        s2 = fmt2.format(date);
        assertEquals(s2, s1);

        fmt1 = new BpDateFormat(BpDateFormat.JVM_DATE_TIME, Locale.CANADA_FRENCH);
        fmt2 = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CANADA_FRENCH);
        s1 = fmt1.format(date);
        s2 = fmt2.format(date);
        assertEquals(s2, s1);

    }

    /**
     * DOCUMENT ME!
     *
     * @throws  ParseException  DOCUMENT ME!
     */
    public void testParsing() throws ParseException {
        DateFormat fmt1 = null;
        DateFormat fmt2 = null;
        Date dt1 = null;
        Date dt2 = null;
        String s = null;

        fmt1 = new BpDateFormat();

        fmt2 = new SimpleDateFormat("EEE MMM dd, yyyy");
        s = "Mon Jul 29, 2002";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEE, MMM dd, yyyy");
        s = "Mon, Jul 29, 2002";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEE,MMM dd, yyyy");
        s = "Mon,Jul 29, 2002";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("MM/dd/yyyy");
        s = "11/17/2001";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEEEEE MM/dd/yyyy");
        s = "Sunday 03/17/2002";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEE MM/dd");
        s = "THU 03/14";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("MM/dd");
        s = "03/14";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEEEEE: M/dd/yyyy");
        s = "Sunday: 7/29/2002";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEEEEEE M/dd/yy");
        s = "Tuesday 7/30/02";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("M/d/yy");
        s = "7/3/02";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("M-d-yy");
        s = "7-3-02";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("MM-dd-yy");
        s = "07-03-02";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("yyyy-MM-dd");
        s = "2002-07-03";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("MM/d/yy");
        s = "12/6/01";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("MMddyy");
        s = "072202";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEE, MMM dd, yyyy");
        s = "FRI, AUG 02, 2002";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
        s = "2002-01-21 12:00:00.000-0600";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse("2002-01-21 12:00:00.000 CST");
        assertEquals(dt2, dt1);

        s = "2002-01-21T12:00:00.000-0600";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse("2002-01-21 12:00:00.000 CST");
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy zzz");
        s = "Thu Jan 04 10:42:10 2001 CST";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        s = "Thu Jan 04 10:42:10 2001";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        s = "Sun Jul 18 22:51:47 PDT 1999";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("HH:mm:ss");
        s = "10:42:10";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("hh:mm:ss aa");
        s = "10:42:10 PM";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("yyyy-'W'ww");
        s = "2002-W28";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("yyyy\'W\'ww");
        s = "2002W28";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("HH:mm");
        s = "14:33";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS\'Z\'");
        fmt2.setTimeZone(TimeZone.getTimeZone("UTC"));
        s = "2001-01-02 14:33:18.426Z";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
        s = "2001-01-02T14:33:18";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("MMM dd, yyyy");
        s = "Jan 12, 1952";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("MM.dd.yy");
        s = "12.13.52";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("h:mmaa");
        s = "3:30pm";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("dd-MMM-yy");
        s = "12-Jan-52";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("d-MMMMM-yyyy");
        s = "8-August-2002";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("\'T\'HH:mm:ss");
        s = "T14:33:18";
        dt1 = fmt1.parse(s);
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

        fmt2 = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ssZ");
        s = "2002-01-21T12:00:00-06:00";
        dt1 = fmt1.parse(s);
        s = "2002-01-21T12:00:00-0600";
        dt2 = fmt2.parse(s);
        assertEquals(dt2, dt1);

    }

    /**
     * DOCUMENT ME!
     */
    public void testIsoFormatting() {
        BpDateFormat fmt1 = null;
        String str1 = null;
        Calendar cal = Calendar.getInstance();

        if (!cal.getTimeZone().getID().equals("America/Chicago")) {
            cal.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
            cal.clear();
        }

        cal.set(2002, Calendar.JANUARY, 2, 8, 16, 44);
        cal.set(Calendar.MILLISECOND, 18);

        fmt1 = new BpDateFormat(BpDateFormat.ISO_DATE_ONLY, Locale.US);
        str1 = fmt1.format(cal.getTime());
        assertEquals("2002-01-02", str1);

        fmt1 = new BpDateFormat(BpDateFormat.ISO_YEAR_WEEK, Locale.US);
        str1 = fmt1.format(cal.getTime());
        assertEquals("2002-W01", str1);

        fmt1 = new BpDateFormat(BpDateFormat.ISO_TIME_ONLY, Locale.US);
        str1 = fmt1.format(cal.getTime());
        assertEquals("T08:16:44", str1);

        fmt1 = new BpDateFormat(BpDateFormat.ISO_DATE_TIME, Locale.US);
        str1 = fmt1.format(cal.getTime());
        assertEquals("2002-01-02T08:16:44.018-06:00", str1);

        cal.set(Calendar.MONTH, Calendar.JUNE);
        str1 = fmt1.format(cal.getTime());
        assertEquals("2002-06-02T08:16:44.018-05:00", str1);

        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2002, Calendar.JANUARY, 2, 8, 16, 44);
        cal.set(Calendar.MILLISECOND, 18);
        fmt1.setTimeZone(TimeZone.getTimeZone("UTC"));
        str1 = fmt1.format(cal.getTime());
        assertEquals("2002-01-02T08:16:44.018Z", str1);

        cal.set(Calendar.MONTH, Calendar.JUNE);
        str1 = fmt1.format(cal.getTime());
        assertEquals("2002-06-02T08:16:44.018Z", str1);

        cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
        cal.set(2002, Calendar.JANUARY, 2, 8, 16, 44);
        cal.set(Calendar.MILLISECOND, 18);
        fmt1.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        str1 = fmt1.format(cal.getTime());
        assertEquals("2002-01-02T08:16:44.018+03:30", str1);

    }

    /**
     * DOCUMENT ME!
     */
    public void testCloning() {
        BpDateFormat fmt1 = null;
        BpDateFormat fmt2 = null;

        fmt1 = new BpDateFormat(BpDateFormat.ISO_DATE_TIME, null);
        fmt2 = (BpDateFormat) fmt1.clone();
        assertEquals(fmt1, fmt2);

    }

}

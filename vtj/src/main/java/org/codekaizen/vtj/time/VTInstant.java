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
package org.codekaizen.vtj.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.codekaizen.vtj.VT;
import org.codekaizen.vtj.math.VTNumber;


/**
 * <p>Represents an instantaneous point on the time-line. It uses the standard C/C++/Java epoch of <code>
 * 1970-01-01T00:00:00Z</code>.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTInstant extends VT<VTInstant> {

    /** The <code>1970-01-01T00:00:00Z</code> epoch instant. */
    public static final VTInstant EPOCH = new VTInstant(0L, 0);

    /** The number of nanoseconds in a second. */
    static final int NANOS_PER_SECOND = 1000000000;

    /** The number of seconds in a day. */
    static final long SECONDS_PER_DAY = 24 * 60 * 60;
    private static final long serialVersionUID = 7289361661861648572L;

    private final long epochSeconds;
    private final int nanoOfSecond;

    /**
     * Constructs an instant on the time-line.
     *
     * @param  epochSeconds  the seconds +/- 1970-01-01T00:00:00Z
     * @param  nanoOfSecond  the fraction of a second
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public VTInstant(final long epochSeconds, final int nanoOfSecond) {

        if (nanoOfSecond < -NANOS_PER_SECOND || nanoOfSecond >= NANOS_PER_SECOND) {
            throw new IllegalArgumentException("Nanosecond fraction is out of range");
        }

        if (nanoOfSecond < 0) {
            this.epochSeconds = epochSeconds - 1L;
            this.nanoOfSecond = nanoOfSecond + NANOS_PER_SECOND;
        } else {
            this.epochSeconds = epochSeconds;
            this.nanoOfSecond = nanoOfSecond;
        }
    }

    /**
     * Constructs an instant on the time-line from a standard Java milliseconds value.
     *
     * @param  milliSeconds  the milliseconds +/- 1970-01-01T00:00:00Z
     */
    public VTInstant(final long milliSeconds) {
        this(milliSeconds / 1000L, 1000000 * (int) (milliSeconds % 1000L));
    }

    /**
     * Constructs an instant on the time-line from a standard MS Excel date/time value.
     *
     * @param  serialDate  the days and fraction from 1899-12-30T00:00:00 local time
     */
    public VTInstant(final float serialDate) {
        this(calcSeconds(serialDate), calcNanos(serialDate));
    }

    /**
     * Returns the number of seconds from the epoch of 1970-01-01T00:00:00Z (identical to C, C++, etc.). Instants on the
     * time-line after the epoch are positive, earlier are negative.
     *
     * @return  the seconds from the epoch
     */
    public long getEpochSeconds() {
        return this.epochSeconds;
    }

    /**
     * Returns the number of nanoseconds, later along the time-line, from the start of the second returned by {@link
     * #getEpochSeconds()}.
     *
     * @return  the nanoseconds within the second, always positive, never exceeds 999,999,999
     */
    public int getNanoOfSecond() {
        return this.nanoOfSecond;
    }

    /**
     * Returns the serial float value using the 1900 date system with days as whole numbers and time as fractional
     * values of 24 hours. This is the same date/time value used by Lotus 1-2-3 and Microsoft Excel.
     *
     * <p>The well-known bug introduced when Lotus 1-2-3 was originally created under the illusion 1900 is a leap year
     * when it actually is not.</p>
     *
     * @return  the number of days since December 30, 1899
     */
    public float getSerialValue() {
        return calcSerialDate(this.epochSeconds, this.nanoOfSecond);
    }

    /**
     * Returns a date/time whose value is <code>(this + val)</code>.
     *
     * @param  val  number value to be added to this value
     *
     * @return  the new date/time object
     */
    public VTInstant plus(final VTNumber<?> val) {
        long sec = 0L;
        int nano = 0;

        if (val instanceof VTDuration) {
            final VTDuration ts = (VTDuration) val;
            sec = this.getEpochSeconds() + ts.getSeconds();
            nano = this.getNanoOfSecond() + ts.getNanoOfSecond();
        } else {
            // assume value is in days
            sec = this.getEpochSeconds() + calcSeconds(val.floatValue());
            nano = this.getNanoOfSecond() + calcNanos(val.floatValue());
        }

        if (nano > NANOS_PER_SECOND) {
            sec += 1L;
            nano -= NANOS_PER_SECOND;
        }

        return new VTInstant(sec, nano);
    }

    /**
     * Returns a number whose value is <code>(this - val)</code>.
     *
     * @param  val  number value to be subtracted from this value
     *
     * @return  the new date/time object
     */
    public VTInstant minus(final VTNumber<?> val) {
        long sec = 0L;
        int nano = 0;

        if (val instanceof VTDuration) {
            final VTDuration ts = (VTDuration) val;
            sec = this.getEpochSeconds() - ts.getSeconds();
            nano = this.getNanoOfSecond() - ts.getNanoOfSecond();
        } else {
            // assume value is in days
            sec = this.getEpochSeconds() - calcSeconds(val.floatValue());
            nano = this.getNanoOfSecond() - calcNanos(val.floatValue());
        }

        if (nano < 0) {
            sec -= 1L;
            nano += NANOS_PER_SECOND;
        }

        return new VTInstant(sec, nano);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInstant copy() {
        return new VTInstant(this.epochSeconds, this.nanoOfSecond);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTInstant o) {
        int result = 0;

        if (this.getEpochSeconds() < o.getEpochSeconds()) {
            result = -1;
        } else if (this.getEpochSeconds() > o.getEpochSeconds()) {
            result = 1;
        }

        if (result == 0) {

            if (this.getNanoOfSecond() < o.getNanoOfSecond()) {
                result = -1;
            } else if (this.getNanoOfSecond() > o.getNanoOfSecond()) {
                result = 1;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.epochSeconds ^ (this.epochSeconds >>> 32));
        result = prime * result + this.nanoOfSecond;

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  obj  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof VTInstant)) {
            return false;
        }

        final VTInstant other = (VTInstant) obj;

        if (this.epochSeconds != other.epochSeconds) {
            return false;
        }

        return this.nanoOfSecond == other.nanoOfSecond;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return "TODO";
    }

    /**
     * Validates whether the supplied value is valid by MS Excel rules.
     *
     * @param  serialDate  the days and fraction from 1899-12-30T00:00:00 local time
     *
     * @throws  IllegalArgumentException  if negative or the 1900 non-existent leap day
     */
    static void validateExcelDate(final float serialDate) {

        if (serialDate < 1.0f) {
            throw new IllegalArgumentException("dates before 1900-01-01 cannot be represented");
        }

        if (serialDate >= 60.0f && serialDate < 61.0) {
            throw new IllegalArgumentException("1900-02-29 does not exist");
        }

        if (serialDate > Float.MAX_VALUE - 1.0) {
            throw new IllegalArgumentException("");
        }
    }

    /**
     * Calculates the number of seconds in the supplied serial date.
     *
     * @param  serialDate  the days and fraction from 1899-12-30T00:00:00 local time
     *
     * @return  the seconds +/- 1970-01-01T00:00:00Z
     */
    static long calcSeconds(final float serialDate) {
        validateExcelDate(serialDate);

        final Date dt = getJavaDate(serialDate);

        return dt.getTime() / 1000L;
    }

    /**
     * Calculates the fraction of a second in the supplied serial date.
     *
     * @param  serialDate  the days and fraction from 1899-12-30T00:00:00 local time
     *
     * @return  the fraction of a second in nanoseconds
     */
    static int calcNanos(final float serialDate) {
        validateExcelDate(serialDate);

        final Date dt = getJavaDate(serialDate);
        final int ms = (int) (dt.getTime() % 1000L);

        return ms * 1000000;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  date  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Date getJavaDate(final double date) {
        final int startYear = 1900;
        int dayAdjust = -1;  // Excel thinks 2/29/1900 is a valid date, which it
                             // isn't
        final int wholeDays = (int) Math.floor(date);

        if (wholeDays < 61) {
            // Date is prior to 3/1/1900, so adjust because Excel thinks
            // 2/29/1900 exists
            // If Excel date == 2/29/1900, will become 3/1/1900 in Java
            // representation
            dayAdjust = 0;
        }

        final GregorianCalendar calendar = new GregorianCalendar(startYear, 0, wholeDays + dayAdjust);
        final int millisecondsInDay = (int) ((date - Math.floor(date)) * (SECONDS_PER_DAY * 1000L) + 0.5);
        calendar.set(GregorianCalendar.MILLISECOND, millisecondsInDay);

        return calendar.getTime();
    }

    /**
     * Calculates a MS Excel serial date from the supplied values.
     *
     * @param  secs  the seconds +/- 1970-01-01T00:00:00Z
     * @param  nanos  the fraction of a second
     *
     * @return  the days and fraction from 1899-12-30T00:00:00 local time
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    static float calcSerialDate(final long secs, final int nanos) {
        final GregorianCalendar cal = new GregorianCalendar();
        long ms = secs * 1000L;
        ms += (long) (((double) nanos) / ((double) NANOS_PER_SECOND) * 1000.0);
        cal.setTimeInMillis(ms);

        if (cal.get(Calendar.YEAR) < 1900) {
            throw new IllegalArgumentException("cannot handle dates before 1900");
        }

        // Because of daylight time saving we cannot use
        //     date.getTime() - calStart.getTimeInMillis()
        // as the difference in milliseconds between 00:00 and 04:00
        // can be 3, 4 or 5 hours but Excel expects it to always
        // be 4 hours.
        // E.g. 2004-03-28 04:00 CEST - 2004-03-28 00:00 CET is 3 hours
        // and 2004-10-31 04:00 CET - 2004-10-31 00:00 CEST is 5 hours
        final double fraction = (((cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)) * 60 +
                        cal.get(Calendar.SECOND)) * 1000 + cal.get(Calendar.MILLISECOND)) /
                (double) (SECONDS_PER_DAY * 1000L);
        final Calendar calStart = dayStart(cal);

        double value = fraction + absoluteDay(calStart);

        if (value >= 60) {
            value++;
        }

        return (float) value;
    }

    static Calendar dayStart(final Calendar cal) {
        // force recalculation of internal fields
        cal.get(Calendar.HOUR_OF_DAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.get(Calendar.HOUR_OF_DAY);

        return cal;
    }

    static int absoluteDay(final Calendar cal) {
        return cal.get(Calendar.DAY_OF_YEAR) + daysInPriorYears(cal.get(Calendar.YEAR));
    }

    private static int daysInPriorYears(final int yr) {

        if (yr < 1900) {
            throw new IllegalArgumentException("'year' must be 1900 or greater");
        }

        final int yr1 = yr - 1;
        final int leapDays = yr1 / 4  // plus julian leap days in prior years
                - yr1 / 100  // minus prior century years
                + yr1 / 400  // plus years divisible by 400
                - 460;  // leap days in previous 1900 years

        return 365 * (yr - 1900) + leapDays;
    }

}

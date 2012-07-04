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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * <p>A bullet-proof date/time parser that formats <code>Date</code>'s in one of the predefined styles. The available
 * styles include the localized styles available by calling the static methods of <code>DateFormat</code>, the most
 * commonly used styles from ISO 8601 and the style used by the standard C runtime library.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class BpDateFormat extends DateFormat implements Serializable, Cloneable {

    /** Formats date/times in the default for the locale. */
    public static final int JVM_DATE_TIME = 0;

    /** Formats dates in the default for the locale. */
    public static final int JVM_DATE_ONLY = 1;

    /** Formats times in the default for the locale. */
    public static final int JVM_TIME_ONLY = 2;

    /** DOCUMENT ME! */
    public static final int JVM_FULL_DATE = 3;

    /** DOCUMENT ME! */
    public static final int C_STYLE = 4;

    /** Formats date/times in ISO format: 2002-01-29T14:32:18-0600 */
    public static final int ISO_DATE_TIME = 5;

    /** Formats dates in format: 2002-01-29 */
    public static final int ISO_DATE_ONLY = 6;

    /** Formats times in format: T14:32:18 */
    public static final int ISO_TIME_ONLY = 7;

    /** Formats dates in format: 2002-W04 */
    public static final int ISO_YEAR_WEEK = 8;

    private static final long serialVersionUID = -2386872158335555263L;
    private static final String[] STYLES = {
            "JVM_DATE_TIME", "JVM_DATE_ONLY", "JVM_TIME_ONLY", "JVM_FULL_DATE", "C_STYLE", "ISO_DATE_TIME",
            "ISO_DATE_ONLY", "ISO_TIME_ONLY", "ISO_YEAR_WEEK",
        };
    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DATE = 2;
    private static final int DAY_OF_WEEK = 3;
    private static final int WEEK_OF_YEAR = 4;
    private static final int HOUR_OF_DAY = 5;
    private static final int MINUTE = 6;
    private static final int SECOND = 7;
    private static final int MILLISECOND = 8;
    private static final int ZONE_OFFSET = 9;
    private static final int FLD_COUNT = 10;
    private static final int NO_VALUE = -99999;

    private DateFormat dtFmt;
    private int style;
    private Locale locale;
    private transient DateFormatSymbols symbols;
    private transient int epoch;

    /**
     * Constructs a parser/formatter that formats date/times the same as a call to <code>
     * DateFormat.getDateTimeInstance()</code>. It will parse any kind of date/time string in the default locale's
     * language.
     */
    public BpDateFormat() {
        this(JVM_DATE_TIME, null);
    }

    /**
     * Constructs a parser/formatter that formats date/times in the specified style. It will parse any kind of date/time
     * string in the supplied locale's language.
     *
     * @param  style  one of the int constants of this class, defaults to <code>JVM_DATE_TIME</code>
     * @param  locale  the locale to format for, uses the default if supplied parameter is <code>null</code>
     */
    public BpDateFormat(final int style, final Locale locale) {
        boolean useDefault;
        this.style = style;

        if (locale == null) {
            this.locale = Locale.getDefault();
            useDefault = true;
        } else {
            this.locale = (Locale) locale.clone();
            useDefault = this.locale.equals(Locale.getDefault());
        }

        switch (style) {
        case JVM_DATE_TIME:

            if (useDefault) {
                this.dtFmt = DateFormat.getDateTimeInstance();
            } else {
                this.dtFmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, this.locale);
            }

            break;
        case JVM_DATE_ONLY:

            if (useDefault) {
                this.dtFmt = DateFormat.getDateInstance(DateFormat.SHORT);
            } else {
                this.dtFmt = DateFormat.getDateInstance(DateFormat.SHORT, this.locale);
            }

            break;
        case JVM_TIME_ONLY:

            if (useDefault) {
                this.dtFmt = DateFormat.getTimeInstance(DateFormat.SHORT);
            } else {
                this.dtFmt = DateFormat.getTimeInstance(DateFormat.SHORT, this.locale);
            }

            break;
        case JVM_FULL_DATE:
            this.dtFmt = DateFormat.getDateInstance(DateFormat.FULL, this.locale);

            break;
        case C_STYLE:
            this.dtFmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

            break;
        case ISO_DATE_TIME:
            this.dtFmt = null;

            break;
        case ISO_DATE_ONLY:
            this.dtFmt = null;

            break;
        case ISO_TIME_ONLY:
            this.dtFmt = null;

            break;
        case ISO_YEAR_WEEK:
            this.dtFmt = null;

            break;
        default:
            this.style = JVM_DATE_TIME;

            if (useDefault) {
                this.dtFmt = DateFormat.getDateTimeInstance();
            } else {
                this.dtFmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, this.locale);
            }

            break;
        }

        this.initialize();
    }

    private void initialize() {
        super.setCalendar(Calendar.getInstance(this.locale));
        this.epoch = super.calendar.get(Calendar.YEAR) - 80;
        super.setLenient(true);

        switch (this.style) {
        case JVM_DATE_ONLY:
        case JVM_DATE_TIME:
        case JVM_TIME_ONLY:
        case JVM_FULL_DATE:
        case C_STYLE:
            super.setNumberFormat(NumberFormat.getNumberInstance(this.locale));

            break;
        case ISO_DATE_ONLY:
        case ISO_DATE_TIME:
        case ISO_TIME_ONLY:
        case ISO_YEAR_WEEK:
            super.getCalendar().setFirstDayOfWeek(Calendar.MONDAY);
            super.getCalendar().setMinimalDaysInFirstWeek(4);
            super.setNumberFormat(new DecimalFormat("00"));

            break;
        default:
            throw new IllegalArgumentException("invalid style");
        }

        if (this.dtFmt instanceof SimpleDateFormat) {
            this.symbols = ((SimpleDateFormat) this.dtFmt).getDateFormatSymbols();
        } else {
            this.symbols = new DateFormatSymbols(this.locale);
        }
    }

    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.initialize();
    }

    /**
     * Returns the style this object uses for formatting date/times.
     *
     * @return  one of the int constants of this class
     */
    public int getStyle() {
        return this.style;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  text  DOCUMENT ME!
     * @param  pos  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Date parse(final String text, final ParsePosition pos) {
        Date dt = null;
        final TimeZone tz = super.calendar.getTimeZone();
        StringBuffer buf = null;
        final int start = pos.getIndex();
        boolean numValue = false;
        char prev = '\0';
        char last = '\0';
        int result = -1;
        int spaceCnt = 0;

        super.calendar.clear();

        final int[] parts = new int[FLD_COUNT];

        for (int i = 0; i < FLD_COUNT; i++) {
            parts[i] = NO_VALUE;
        }

        String raw = text.substring(start);

        if (raw.length() > 48) {

            if (raw.indexOf(' ', 48) != -1) {
                raw = raw.substring(0, raw.indexOf(' ', 48));
            }
        }

        try {
            final int len = raw.length();

            for (int i = 0; i < len; i++) {

                switch (Character.getType(raw.charAt(i))) {
                case Character.DECIMAL_DIGIT_NUMBER:
                    spaceCnt = 0;

                    if (buf == null) {
                        numValue = true;
                        buf = new StringBuffer();
                        buf.append(raw.charAt(i));
                    } else if (numValue) {
                        buf.append(raw.charAt(i));
                    } else {
                        result = this.evalToken(buf.toString(), parts, prev, raw.charAt(i), last);

                        if (result == 0) {
                            break;
                        }

                        pos.setIndex(pos.getIndex() + result);

                        if ("W".equals(buf.toString())) {
                            last = 'W';
                        } else if ("T".equals(buf.toString())) {
                            last = 'T';
                        } else {
                            last = '\0';
                        }

                        numValue = true;
                        buf = new StringBuffer();
                        buf.append(raw.charAt(i));
                    }

                    break;
                case Character.UPPERCASE_LETTER:
                case Character.LOWERCASE_LETTER:
                    spaceCnt = 0;

                    if (buf == null) {
                        numValue = false;
                        buf = new StringBuffer();
                        buf.append(raw.charAt(i));
                    } else if (!numValue) {
                        buf.append(raw.charAt(i));
                    } else {
                        result = this.evalToken(buf.toString(), parts, prev, raw.charAt(i), last);

                        if (result == 0) {
                            break;
                        }

                        pos.setIndex(pos.getIndex() + result);
                        last = '\0';
                        numValue = false;
                        buf = new StringBuffer();
                        buf.append(raw.charAt(i));
                    }

                    break;
                case Character.SPACE_SEPARATOR:
                    spaceCnt++;

                    if (spaceCnt > 2) {
                        result = 0;

                        break;
                    }

                    if (buf != null) {
                        result = this.evalToken(buf.toString(), parts, prev, raw.charAt(i), last);

                        if (result == 0) {
                            break;
                        }

                        pos.setIndex(pos.getIndex() + result);
                        last = '\0';
                    }

                    buf = null;
                    prev = raw.charAt(i);
                    pos.setIndex(pos.getIndex() + 1);

                    break;
                default:
                    spaceCnt = 0;

                    if (buf != null) {
                        result = this.evalToken(buf.toString(), parts, prev, raw.charAt(i), last);

                        if (result == 0) {
                            break;
                        }

                        pos.setIndex(pos.getIndex() + result);
                        last = '\0';
                    } else if (pos.getIndex() == start) {
                        return dt;
                    }

                    buf = null;
                    prev = raw.charAt(i);
                    pos.setIndex(pos.getIndex() + 1);

                    break;
                }

                if (result == 0) {
                    break;
                }
            }

            if (buf != null && result != 0) {
                result = this.evalToken(buf.toString(), parts, prev, '\0', last);
                pos.setIndex(pos.getIndex() + result);
                buf = null;
            }

        } catch (Exception ex) {
            pos.setErrorIndex(pos.getIndex());
            pos.setIndex(start);

            return dt;
        }

        int valCnt = 0;

        for (int i = 0; i < FLD_COUNT; i++) {

            if (parts[i] != NO_VALUE) {
                valCnt++;

                switch (i) {
                case YEAR:

                    if (parts[i] < 100) {

                        if (parts[i] < (this.epoch % 100)) {
                            parts[i] += (this.epoch / 100 + 1) * 100;
                        } else {
                            parts[i] += (this.epoch / 100) * 100;
                        }
                    }

                    super.calendar.set(Calendar.YEAR, parts[i]);

                    break;
                case MONTH:

                    if (parts[i] < Calendar.JANUARY || parts[i] > Calendar.DECEMBER) {
                        pos.setErrorIndex(start);
                        pos.setIndex(start);

                        return dt;
                    }

                    super.calendar.set(Calendar.MONTH, parts[i]);

                    break;
                case DATE:

                    if (parts[i] < 1 || parts[i] > 31) {
                        pos.setErrorIndex(start);
                        pos.setIndex(start);

                        return dt;
                    }

                    super.calendar.set(Calendar.DATE, parts[i]);

                    break;
                case DAY_OF_WEEK:

                    if (parts[i] < 0 || parts[i] > 7) {
                        pos.setErrorIndex(start);
                        pos.setIndex(start);

                        return dt;
                    }

                    super.calendar.set(Calendar.DAY_OF_WEEK, parts[i]);

                    break;
                case WEEK_OF_YEAR:

                    if (parts[i] < 1 || parts[i] > 53) {
                        pos.setErrorIndex(start);
                        pos.setIndex(start);

                        return dt;
                    }

                    super.calendar.set(Calendar.WEEK_OF_YEAR, parts[i]);

                    break;
                case HOUR_OF_DAY:

                    if (parts[i] < 0 || parts[i] > 24) {
                        pos.setErrorIndex(start);
                        pos.setIndex(start);

                        return dt;
                    }

                    super.calendar.set(Calendar.HOUR_OF_DAY, parts[i]);

                    break;
                case MINUTE:

                    if (parts[i] < 0 || parts[i] > 60) {
                        pos.setErrorIndex(start);
                        pos.setIndex(start);

                        return dt;
                    }

                    super.calendar.set(Calendar.MINUTE, parts[i]);

                    break;
                case SECOND:

                    if (parts[i] < 0 || parts[i] > 60) {
                        pos.setErrorIndex(start);
                        pos.setIndex(start);

                        return dt;
                    }

                    super.calendar.set(Calendar.SECOND, parts[i]);

                    break;
                case MILLISECOND:
                    super.calendar.set(Calendar.MILLISECOND, parts[i]);

                    break;
                case ZONE_OFFSET:

                    String tzs = "GMT";

                    if (parts[i] != 0) {
                        tzs += Integer.toString(parts[i]);

                        if (tzs.length() == 7) {
                            tzs = tzs.substring(0, 4) + "0" + tzs.substring(4, 5) + ":" + tzs.substring(5);
                        } else if (tzs.length() == 8) {
                            tzs = tzs.substring(0, 6) + ":" + tzs.substring(6);
                        }
                    }

                    super.calendar.setTimeZone(TimeZone.getTimeZone(tzs));

                    break;
                }
            }
        }

        if (valCnt == 0 || valCnt == 1) {
            pos.setErrorIndex(pos.getIndex());
            pos.setIndex(start);

            return dt;
        }

        dt = super.calendar.getTime();
        super.calendar.setTimeZone(tz);

        return dt;
    }

    private int evalToken(final String token, final int[] parts, final char prev, final char cur, final char last) {
        int len = token.length();

        if (len == 0) {
            return len;
        }

        switch (Character.getType(token.charAt(0))) {
        case Character.DECIMAL_DIGIT_NUMBER:

            int val = 0;
            val = Integer.parseInt(token);

            switch (prev) {
            case '\0':

                if (cur == ':' && len < 3) {
                    parts[HOUR_OF_DAY] = val;
                } else if (len > 4) {
                    parts[MONTH] = Integer.parseInt(token.substring(0, 2)) - 1;
                    parts[DATE] = Integer.parseInt(token.substring(2, 4));
                    parts[YEAR] = Integer.parseInt(token.substring(4));
                } else if (len == 4) {
                    parts[YEAR] = val;
                } else if (last == 'W' && len == 2) {
                    parts[WEEK_OF_YEAR] = val;
                } else if (this.locale.getCountry().equals("US")) {
                    parts[MONTH] = val - 1;
                } else if (Locale.UK.equals(this.locale) || Locale.FRANCE.equals(this.locale) ||
                        Locale.GERMANY.equals(this.locale) || Locale.ITALY.equals(this.locale) ||
                        Locale.UK.equals(this.locale)) {
                    parts[DATE] = val;
                } else {
                    parts[MONTH] = val - 1;
                }

                break;
            case '/':
            case '-':
            case '.':

                if (len > 4) {
                    len = 0;
                } else if (prev == '-' && last == 'W' && len == 2) {
                    parts[WEEK_OF_YEAR] = val;
                } else if (len == 4) {

                    if (parts[YEAR] == NO_VALUE) {
                        parts[YEAR] = val;
                    } else if (parts[ZONE_OFFSET] == NO_VALUE && prev == '-') {
                        parts[ZONE_OFFSET] = val * -1;
                    } else {
                        len = 0;
                    }
                } else if (len == 3 && prev == '.') {
                    parts[MILLISECOND] = val;
                } else if (len == 2 && prev == '-' && last == 'T') {
                    parts[HOUR_OF_DAY] = val;
                } else if (this.locale.getCountry().equals("US")) {

                    if (parts[MONTH] == NO_VALUE) {
                        parts[MONTH] = val - 1;
                    } else if (parts[DATE] == NO_VALUE) {
                        parts[DATE] = val;
                    } else if (parts[YEAR] == NO_VALUE) {
                        parts[YEAR] = val;
                    } else if (parts[MILLISECOND] == NO_VALUE && prev == '.') {
                        parts[MILLISECOND] = val;
                    } else {
                        len = 0;
                    }
                } else if (Locale.UK.equals(this.locale) || Locale.FRANCE.equals(this.locale) ||
                        Locale.GERMANY.equals(this.locale) || Locale.ITALY.equals(this.locale) ||
                        Locale.UK.equals(this.locale)) {

                    if (parts[DATE] == NO_VALUE) {
                        parts[DATE] = val - 1;
                    } else if (parts[MONTH] == NO_VALUE) {
                        parts[MONTH] = val;
                    } else if (parts[YEAR] == NO_VALUE) {
                        parts[YEAR] = val;
                    } else if (parts[MILLISECOND] == NO_VALUE && prev == '.') {
                        parts[MILLISECOND] = val;
                    } else {
                        len = 0;
                    }
                } else {

                    if (parts[MONTH] == NO_VALUE) {
                        parts[MONTH] = val - 1;
                    } else if (parts[DATE] == NO_VALUE) {
                        parts[DATE] = val;
                    } else if (parts[YEAR] == NO_VALUE) {
                        parts[YEAR] = val;
                    } else if (parts[MILLISECOND] == NO_VALUE && prev == '.') {
                        parts[MILLISECOND] = val;
                    } else {
                        len = 0;
                    }
                }

                break;
            case ':':

                if (parts[HOUR_OF_DAY] == NO_VALUE) {
                    len = 0;
                }

                if (parts[MINUTE] == NO_VALUE) {
                    parts[MINUTE] = val;
                } else if (parts[SECOND] == NO_VALUE) {
                    parts[SECOND] = val;
                } else {
                    len = 0;
                }

                break;
            case ' ':
            case ',':

                if (cur == ':' && len < 3) {
                    parts[HOUR_OF_DAY] = val;
                } else if (len > 4) {
                    len = 0;
                } else if (len == 4) {
                    parts[YEAR] = val;
                } else if (len == 3 && prev == '.') {
                    parts[MILLISECOND] = val;
                } else if (this.locale.getCountry().equals("US")) {

                    if (parts[MONTH] == NO_VALUE) {
                        parts[MONTH] = val - 1;
                    } else if (parts[DATE] == NO_VALUE) {
                        parts[DATE] = val;
                    } else if (parts[YEAR] == NO_VALUE) {
                        parts[YEAR] = val;
                    } else {
                        len = 0;
                    }
                } else if (Locale.UK.equals(this.locale) || Locale.FRANCE.equals(this.locale) ||
                        Locale.GERMANY.equals(this.locale) || Locale.ITALY.equals(this.locale) ||
                        Locale.UK.equals(this.locale)) {

                    if (parts[DATE] == NO_VALUE) {
                        parts[DATE] = val - 1;
                    } else if (parts[MONTH] == NO_VALUE) {
                        parts[MONTH] = val;
                    } else if (parts[YEAR] == NO_VALUE) {
                        parts[YEAR] = val;
                    } else {
                        len = 0;
                    }
                } else {

                    if (parts[MONTH] == NO_VALUE) {
                        parts[MONTH] = val - 1;
                    } else if (parts[DATE] == NO_VALUE) {
                        parts[DATE] = val;
                    } else if (parts[YEAR] == NO_VALUE) {
                        parts[YEAR] = val;
                    } else {
                        len = 0;
                    }
                }

                break;
            case '+':

                if (parts[ZONE_OFFSET] == NO_VALUE && len == 4) {
                    parts[ZONE_OFFSET] = val;
                } else {
                    len = 0;
                }

                break;
            default:
                len = 0;
            }

            break;
        case Character.UPPERCASE_LETTER:
        case Character.LOWERCASE_LETTER:

            if ("W".equals(token) && (prev == '-' || prev == '\0')) {
                break;
            } else if ("T".equals(token) && (prev == '-' || prev == '\0')) {
                break;
            } else if ("Z".equals(token)) {
                parts[ZONE_OFFSET] = 0;

                break;
            }

            String[] tokens = null;
            boolean found = false;
            tokens = this.symbols.getShortMonths();

            for (int i = 0; i < tokens.length; i++) {

                if (token.equalsIgnoreCase(tokens[i])) {

                    if (parts[MONTH] != NO_VALUE) {

                        if (parts[DATE] == NO_VALUE) {
                            parts[DATE] = parts[MONTH] + 1;
                        } else if (parts[YEAR] == NO_VALUE) {
                            parts[YEAR] = parts[MONTH] + 1;
                        } else {
                            return 0;
                        }
                    }

                    parts[MONTH] = i;
                    found = true;

                    break;
                }
            }

            if ("en".equalsIgnoreCase(this.locale.getLanguage())) {

                if (token.equalsIgnoreCase("Jly")) {

                    if (parts[MONTH] != NO_VALUE) {

                        if (parts[DATE] == NO_VALUE) {
                            parts[DATE] = parts[MONTH] + 1;
                        } else if (parts[YEAR] == NO_VALUE) {
                            parts[YEAR] = parts[MONTH] + 1;
                        } else {
                            return 0;
                        }
                    }

                    parts[MONTH] = Calendar.JULY;
                    found = true;
                }
            }

            if (found) {
                break;
            }

            tokens = this.symbols.getMonths();

            for (int i = 0; i < tokens.length; i++) {

                if (token.equalsIgnoreCase(tokens[i])) {

                    if (parts[MONTH] != NO_VALUE) {

                        if (parts[DATE] == NO_VALUE) {
                            parts[DATE] = parts[MONTH] + 1;
                        } else if (parts[YEAR] == NO_VALUE) {
                            parts[YEAR] = parts[MONTH] + 1;
                        } else {
                            return 0;
                        }
                    }

                    parts[MONTH] = i;
                    found = true;

                    break;
                }
            }

            if (found) {
                break;
            }

            if (parts[DAY_OF_WEEK] == NO_VALUE) {
                tokens = this.symbols.getShortWeekdays();

                for (int i = 0; i < tokens.length; i++) {

                    if (token.equalsIgnoreCase(tokens[i])) {
                        parts[DAY_OF_WEEK] = i;
                        found = true;

                        break;
                    }
                }

                if (found) {
                    break;
                }

                tokens = this.symbols.getWeekdays();

                for (int i = 0; i < tokens.length; i++) {

                    if (token.equalsIgnoreCase(tokens[i])) {
                        parts[DAY_OF_WEEK] = i;
                        found = true;

                        break;
                    }
                }

                if (found) {
                    break;
                }
            }

            tokens = this.symbols.getAmPmStrings();

            for (int i = 0; i < tokens.length; i++) {

                if (token.equalsIgnoreCase(tokens[i])) {

                    if (i == Calendar.AM) {

                        // hour OK if not 12 AM
                        if (parts[HOUR_OF_DAY] == 12) {
                            parts[HOUR_OF_DAY] = 0;
                        }
                    } else if (i == Calendar.PM) {

                        // hour OK if 12 PM
                        if (parts[HOUR_OF_DAY] != 12) {
                            parts[HOUR_OF_DAY] += 12;
                        }
                    }

                    found = true;

                    break;
                }
            }

            if (found) {
                break;
            }

            if (parts[ZONE_OFFSET] == NO_VALUE) {

                if ("GMT".equals(token)) {
                    parts[ZONE_OFFSET] = 0;

                    break;
                }

                final String[][] zones = this.symbols.getZoneStrings();

                for (final String[] zone : zones) {

                    if (found) {
                        break;
                    }

                    for (int j = 0; j < zone.length; j++) {

                        if (token.equalsIgnoreCase(zone[j])) {
                            super.calendar.setTimeZone(TimeZone.getTimeZone(zone[0]));
                            found = true;

                            break;
                        }
                    }
                }
            }

            if (found) {
                break;
            }

            len = 0;

            break;
        }

        return len;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  date  DOCUMENT ME!
     * @param  toAppendTo  DOCUMENT ME!
     * @param  fieldPosition  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public StringBuffer format(final Date date, final StringBuffer toAppendTo, final FieldPosition fieldPosition) {

        super.calendar.setTime(date);

        switch (this.style) {

        case JVM_DATE_ONLY:
        case JVM_DATE_TIME:
        case JVM_TIME_ONLY:
        case JVM_FULL_DATE:
        case C_STYLE:
            return this.dtFmt.format(date, toAppendTo, fieldPosition);

        case ISO_DATE_TIME:
            toAppendTo.append(super.calendar.get(Calendar.YEAR));
            toAppendTo.append('-');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.MONTH) + 1));
            toAppendTo.append('-');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.DATE)));
            toAppendTo.append('T');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.HOUR_OF_DAY)));
            toAppendTo.append(':');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.MINUTE)));
            toAppendTo.append(':');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.SECOND)));
            toAppendTo.append('.');

            String s = super.numberFormat.format(super.calendar.get(Calendar.MILLISECOND));

            while (s.length() < 3) {
                s = "0" + s;
            }

            toAppendTo.append(s);

            int offset = super.calendar.get(Calendar.ZONE_OFFSET);
            offset += super.calendar.get(Calendar.DST_OFFSET);

            if (offset == 0) {
                toAppendTo.append('Z');
            } else {
                final long m = offset / 60000L;
                final long h = m / 60L;

                if (h > 0) {
                    toAppendTo.append('+');
                }

                toAppendTo.append(super.numberFormat.format(h));
                toAppendTo.append(':');
                toAppendTo.append(super.numberFormat.format(m - h * 60));
            }

            if (fieldPosition != null) {

                switch (fieldPosition.getField()) {
                case DateFormat.YEAR_FIELD:
                    fieldPosition.setBeginIndex(0);
                    fieldPosition.setEndIndex(4);

                    break;
                case DateFormat.MONTH_FIELD:
                    fieldPosition.setBeginIndex(5);
                    fieldPosition.setEndIndex(7);

                    break;
                case DateFormat.DATE_FIELD:
                    fieldPosition.setBeginIndex(8);
                    fieldPosition.setEndIndex(10);

                    break;
                case DateFormat.HOUR_OF_DAY0_FIELD:
                    fieldPosition.setBeginIndex(11);
                    fieldPosition.setEndIndex(13);

                    break;
                case DateFormat.MINUTE_FIELD:
                    fieldPosition.setBeginIndex(14);
                    fieldPosition.setEndIndex(16);

                    break;
                case DateFormat.SECOND_FIELD:
                    fieldPosition.setBeginIndex(17);
                    fieldPosition.setEndIndex(19);

                    break;
                case DateFormat.MILLISECOND_FIELD:
                    fieldPosition.setBeginIndex(20);
                    fieldPosition.setEndIndex(23);

                    break;
                case DateFormat.TIMEZONE_FIELD:
                    fieldPosition.setBeginIndex(23);
                    fieldPosition.setEndIndex(toAppendTo.length());

                    break;
                }
            }

            break;

        case ISO_DATE_ONLY:
            toAppendTo.append(super.calendar.get(Calendar.YEAR));
            toAppendTo.append('-');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.MONTH) + 1));
            toAppendTo.append('-');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.DATE)));

            if (fieldPosition != null) {

                switch (fieldPosition.getField()) {
                case DateFormat.YEAR_FIELD:
                    fieldPosition.setBeginIndex(0);
                    fieldPosition.setEndIndex(4);

                    break;
                case DateFormat.MONTH_FIELD:
                    fieldPosition.setBeginIndex(5);
                    fieldPosition.setEndIndex(7);

                    break;
                case DateFormat.DATE_FIELD:
                    fieldPosition.setBeginIndex(8);
                    fieldPosition.setEndIndex(10);

                    break;
                }
            }

            break;

        case ISO_TIME_ONLY:
            toAppendTo.append('T');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.HOUR_OF_DAY)));
            toAppendTo.append(':');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.MINUTE)));
            toAppendTo.append(':');
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.SECOND)));

            if (fieldPosition != null) {

                switch (fieldPosition.getField()) {
                case DateFormat.HOUR_OF_DAY0_FIELD:
                    fieldPosition.setBeginIndex(0);
                    fieldPosition.setEndIndex(2);

                    break;
                case DateFormat.MINUTE_FIELD:
                    fieldPosition.setBeginIndex(3);
                    fieldPosition.setEndIndex(5);

                    break;
                case DateFormat.SECOND_FIELD:
                    fieldPosition.setBeginIndex(6);
                    fieldPosition.setEndIndex(8);

                    break;
                }
            }

            break;

        case ISO_YEAR_WEEK:

            if (super.calendar.get(Calendar.WEEK_OF_YEAR) == 1 && super.calendar.get(Calendar.DATE) > 27) {
                toAppendTo.append(super.calendar.get(Calendar.YEAR) + 1);
            } else {
                toAppendTo.append(super.calendar.get(Calendar.YEAR));
            }

            toAppendTo.append("-W");
            toAppendTo.append(super.numberFormat.format(super.calendar.get(Calendar.WEEK_OF_YEAR)));

            if (fieldPosition != null) {

                switch (fieldPosition.getField()) {
                case DateFormat.YEAR_FIELD:
                    fieldPosition.setBeginIndex(0);
                    fieldPosition.setEndIndex(4);

                    break;
                case DateFormat.WEEK_OF_YEAR_FIELD:
                    fieldPosition.setBeginIndex(6);
                    fieldPosition.setEndIndex(8);

                    break;
                }
            }

            break;

        }

        return toAppendTo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object clone() {
        return new BpDateFormat(this.style, this.locale);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int hashCode() {
        int result = 2251;
        result = result * 223 + this.style * 89;
        result = result * 223 + this.locale.hashCode();

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  obj  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof BpDateFormat)) {
            return false;
        }

        final BpDateFormat df = (BpDateFormat) obj;

        if (this.style != df.style) {
            return false;
        }

        return this.locale.equals(df.locale);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String toString() {
        return "BpDateFormat: " + STYLES[this.style] + " " + this.locale;
    }

}

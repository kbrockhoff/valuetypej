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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;


/**
 * <p>A bullet-proof number parser that formats <code>Number</code>'s in one of the predefined styles. The available
 * styles include the localized styles available by calling the static methods of <code>DateFormat</code>, the most
 * commonly used styles from ISO 8601 and the style used by the standard C runtime library.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class BpNumberFormat extends NumberFormat implements Serializable, Cloneable {

    /** Formats like <code>getNumberInstance</code>. */
    public static final int JVM_NUMBER = 0;

    /** Formats like <code>getIntegerInstance</code>. */
    public static final int JVM_INTEGER = 1;

    /** Formats like <code>getCurrencyInstance</code>. */
    public static final int JVM_CURRENCY = 2;

    /** Formats like <code>getPercentInstance</code>. */
    public static final int JVM_PERCENT = 3;

    private static final long serialVersionUID = -3406519011044526145L;
    private static final String[] STYLES = { "JVM_NUMBER", "JVM_INTEGER", "JVM_CURRENCY", "JVM_PERCENT", };

    private NumberFormat numFmt;
    private int style;
    private Locale locale;
    private transient DecimalFormatSymbols symbols;

    /**
     * Constructs a parser/formatter that formats numbers the same as a call to <code>
     * NumberFormat.getNumberInstance()</code>. It will parse any kind of number string in the default locale's
     * language.
     */
    public BpNumberFormat() {
        this(JVM_NUMBER, null);
    }

    /**
     * Constructs a parser/formatter that formats numbers in the specified style. It will parse any kind of number
     * string in the supplied locale's language.
     *
     * @param  style  one of the int constants of this class, defaults to <code>JVM_NUMBER</code>
     * @param  locale  the locale to format for, uses the default if supplied parameter is <code>null</code>
     */
    public BpNumberFormat(final int style, final Locale locale) {
        boolean useDefault = false;
        this.style = style;

        if (locale == null) {
            this.locale = Locale.getDefault();
            useDefault = true;
        } else {
            this.locale = (Locale) locale.clone();
            useDefault = this.locale.equals(Locale.getDefault());
        }

        switch (style) {
        case JVM_NUMBER:

            if (useDefault) {
                this.numFmt = NumberFormat.getNumberInstance();
            } else {
                this.numFmt = NumberFormat.getNumberInstance(this.locale);
            }

            break;
        case JVM_INTEGER:

            if (useDefault) {
                this.numFmt = NumberFormat.getNumberInstance();
            } else {
                this.numFmt = NumberFormat.getNumberInstance(this.locale);
            }

            this.numFmt.setMinimumFractionDigits(0);
            this.numFmt.setMaximumFractionDigits(0);
            this.numFmt.setParseIntegerOnly(true);

            break;
        case JVM_CURRENCY:

            if (useDefault) {
                this.numFmt = NumberFormat.getCurrencyInstance();
            } else {
                this.numFmt = NumberFormat.getCurrencyInstance(this.locale);
            }

            break;
        case JVM_PERCENT:

            if (useDefault) {
                this.numFmt = NumberFormat.getPercentInstance();
            } else {
                this.numFmt = NumberFormat.getPercentInstance(this.locale);
            }

            break;
        default:
            this.style = JVM_NUMBER;

            if (useDefault) {
                this.numFmt = NumberFormat.getNumberInstance();
            } else {
                this.numFmt = NumberFormat.getNumberInstance(this.locale);
            }

            break;
        }

        this.initialize();
    }

    private void initialize() {

        switch (this.style) {
        case JVM_NUMBER:
        case JVM_INTEGER:
        case JVM_CURRENCY:
        case JVM_PERCENT:
            break;
        default:
            throw new IllegalArgumentException("invalid style");
        }

        if (this.numFmt instanceof DecimalFormat) {
            this.symbols = ((DecimalFormat) this.numFmt).getDecimalFormatSymbols();
        } else {
            this.symbols = new DecimalFormatSymbols(this.locale);
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
     * Returns the style this object uses for formatting numbers.
     *
     * @return  one of the int constants of this class
     */
    public int getStyle() {
        return this.style;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  number  DOCUMENT ME!
     * @param  toAppendTo  DOCUMENT ME!
     * @param  pos  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public StringBuffer format(final long number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.numFmt.format(number, toAppendTo, pos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  text  DOCUMENT ME!
     * @param  parsePosition  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Number parse(final String text, final ParsePosition parsePosition) {
        Number num = null;
        StringBuffer buf = new StringBuffer(32);
        final int start = parsePosition.getIndex();
        String left = null;
        String right = null;
        boolean currency = false;
        boolean negative = false;
        boolean percent = false;
        boolean fraction = false;
        boolean done = false;
        boolean invalid = false;

        String raw = text.substring(start);

        if (raw.length() > 48) {

            if (raw.indexOf(' ', 48) != -1) {
                raw = raw.substring(0, raw.indexOf(' ', 48));
            }
        }

        try {
            final int len = raw.length();
            char ch = '\0';

            for (int i = 0; i < len; i++) {
                ch = raw.charAt(i);

                switch (Character.getType(ch)) {
                case Character.DECIMAL_DIGIT_NUMBER:
                    buf.append(ch);
                    parsePosition.setIndex(parsePosition.getIndex() + 1);

                    break;
                case Character.CURRENCY_SYMBOL:
                    currency = true;
                    parsePosition.setIndex(parsePosition.getIndex() + 1);

                    break;
                case Character.START_PUNCTUATION:
                    negative = true;
                    parsePosition.setIndex(parsePosition.getIndex() + 1);

                    break;
                case Character.END_PUNCTUATION:

                    if (negative) {
                        parsePosition.setIndex(parsePosition.getIndex() + 1);
                    }

                    done = true;

                    break;
                case Character.SPACE_SEPARATOR:
                    done = true;

                    break;
                default:

                    if (ch == this.symbols.getGroupingSeparator()) {
                        parsePosition.setIndex(parsePosition.getIndex() + 1);
                    } else if (ch == this.symbols.getDecimalSeparator()) {
                        parsePosition.setIndex(parsePosition.getIndex() + 1);

                        if (left == null) {
                            left = buf.toString();
                            buf = new StringBuffer(32);
                        } else {
                            done = true;
                        }
                    } else if (ch == this.symbols.getMinusSign()) {

                        if (negative || buf.length() > 0) {
                            invalid = true;
                            done = true;
                        } else {
                            negative = true;
                            parsePosition.setIndex(parsePosition.getIndex() + 1);
                        }
                    } else if (ch == this.symbols.getPercent()) {
                        percent = true;
                        parsePosition.setIndex(parsePosition.getIndex() + 1);
                    } else if (ch == this.symbols.getMonetaryDecimalSeparator()) {
                        parsePosition.setIndex(parsePosition.getIndex() + 1);

                        if (left == null) {
                            currency = true;
                            left = buf.toString();
                            buf = new StringBuffer(32);
                        } else {
                            done = true;
                        }
                    } else if (ch == '/') {

                        if (left == null && !fraction) {
                            parsePosition.setIndex(parsePosition.getIndex() + 1);
                            fraction = true;
                            left = buf.toString();
                            buf = new StringBuffer(32);
                        } else {
                            invalid = true;
                            done = true;
                        }

                    } else {
                        invalid = true;
                        done = true;
                    }

                    break;
                }

                if (done) {
                    break;
                }
            }

            if (invalid) {
                parsePosition.setErrorIndex(parsePosition.getIndex());
                parsePosition.setIndex(start);

                return num;
            }

            if (buf.length() > 0) {

                if (left == null) {
                    left = buf.toString();
                } else {
                    right = buf.toString();
                }
            }

            if (left == null) {
                num = null;
            } else if (right == null) {

                if (negative) {
                    num = Long.valueOf(this.symbols.getMinusSign() + left);
                } else {
                    num = Long.valueOf(left);
                }

                if (percent) {
                    num = new Double(num.doubleValue() / 100.0);
                } else if (currency) {
                    buf = new StringBuffer(32);

                    if (negative) {
                        buf.append(this.symbols.getMinusSign());
                    }

                    buf.append(left);
                    buf.append(this.symbols.getDecimalSeparator());
                    buf.append(this.symbols.getZeroDigit());
                    buf.append(this.symbols.getZeroDigit());
                    num = new BigDecimal(buf.toString());
                } else {

                    if (num.longValue() < ((long) Integer.MAX_VALUE) && num.longValue() > ((long) Integer.MIN_VALUE)) {
                        num = new Integer(num.intValue());
                    }
                }
            } else if (fraction) {
                final Double n = Double.valueOf(left);
                final Double d = Double.valueOf(right);
                num = new Double(n.doubleValue() / d.doubleValue());
            } else {
                buf = new StringBuffer(32);

                if (negative) {
                    buf.append(this.symbols.getMinusSign());
                }

                buf.append(left);
                buf.append(this.symbols.getDecimalSeparator());
                buf.append(right);

                if (percent) {
                    num = Double.valueOf(buf.toString());
                    num = new Double(num.doubleValue() / 100.0);
                } else if (currency) {
                    num = new BigDecimal(buf.toString());
                } else if (left.length() == 0) {
                    num = Double.valueOf(buf.toString());
                } else {

                    if (right.length() > 4 || right.length() < 2) {
                        num = Double.valueOf(buf.toString());
                    } else {
                        num = new BigDecimal(buf.toString());
                    }
                }
            }

        } catch (Exception ex) {
            parsePosition.setErrorIndex(parsePosition.getIndex());
            parsePosition.setIndex(start);

            return num;
        }

        return num;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  number  DOCUMENT ME!
     * @param  toAppendTo  DOCUMENT ME!
     * @param  pos  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public StringBuffer format(final double number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.numFmt.format(number, toAppendTo, pos);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object clone() {
        return new BpNumberFormat(this.style, this.locale);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int hashCode() {
        int result = 1999;
        result = result * 211 + this.style * 113;
        result = result * 211 + this.locale.hashCode();

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

        if (!(obj instanceof BpNumberFormat)) {
            return false;
        }

        final BpNumberFormat nf = (BpNumberFormat) obj;

        if (this.style != nf.style) {
            return false;
        }

        return this.locale.equals(nf.locale);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String toString() {
        return "BpNumberFormat: " + STYLES[this.style] + " " + this.locale;
    }

}

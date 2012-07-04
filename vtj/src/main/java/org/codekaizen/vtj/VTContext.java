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
package org.codekaizen.vtj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import org.codekaizen.vtj.text.BpDateFormat;
import org.codekaizen.vtj.text.BpNumberFormat;


/**
 * <p>Encapsulates the context settings for rules on performing numeric calculations and formating values.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class VTContext implements Serializable {

    private static final long serialVersionUID = -6324717210704100627L;

    /** Decimal scale large enough to be accurate for common situations used as the default. */
    public static final int DEFAULT_DECIMAL_SCALE = 6;

    private final ApplicationEnvironment applicationEnvironment;
    private final Locale locale;
    private final Currency currency;
    private final MathContext mathContext;
    private final boolean fixedScale;
    private final int moneyScale;
    private final int decimalScale;

    private VTContext() {
        this(ApplicationEnvironment.valueOf(
                System.getProperty("org.codekaizen.vtj.applicationMathEnvironment", "INTERNATIONAL_BUSINESS")),
            Locale.getDefault(), MathContext.DECIMAL64);
    }

    /**
     * Creates a new VTContext object.
     *
     * @param  applicationEnvironment  enum value for the application category
     * @param  locale  the locale to format for
     * @param  mathContext  the mathematical context
     *
     * @throws  IllegalArgumentException  if supplied invalid parameter
     */
    public VTContext(final ApplicationEnvironment applicationEnvironment, final Locale locale,
            final MathContext mathContext) throws IllegalArgumentException {
        super();
        AssertPrecondition.notNull("applicationEnvironment", applicationEnvironment);
        AssertPrecondition.notNull("locale", locale);
        AssertPrecondition.notNull("mathContext", mathContext);

        this.applicationEnvironment = applicationEnvironment;
        this.locale = locale;
        this.mathContext = mathContext;
        currency = Currency.getInstance(locale);

        switch (applicationEnvironment) {

        case INTERNATIONAL_BUSINESS:
            fixedScale = true;

            break;

        default:
            fixedScale = false;

            break;
        }

        moneyScale = this.currency.getDefaultFractionDigits();
        decimalScale = DEFAULT_DECIMAL_SCALE;
        validateFields();
    }

    /**
     * Creates a new VTContext object.
     *
     * @param  applicationEnvironment  enum value for the application category
     * @param  locale  the locale to format for
     * @param  mathContext  the mathematical context
     * @param  currency  the default currency
     * @param  fixedScale  use fixed scale or IEEE floating point
     * @param  moneyScale  the default decimal places for monetary values
     * @param  decimalScale  the default decimal places for non-monetary values
     *
     * @throws  IllegalArgumentException  if supplied invalid parameter
     */
    public VTContext(final ApplicationEnvironment applicationEnvironment, final Locale locale,
            final MathContext mathContext, final Currency currency, final boolean fixedScale, final int moneyScale,
            final int decimalScale) throws IllegalArgumentException {
        super();
        this.applicationEnvironment = applicationEnvironment;
        this.locale = locale;
        this.mathContext = mathContext;
        this.currency = currency;
        this.fixedScale = fixedScale;
        this.moneyScale = moneyScale;
        this.decimalScale = decimalScale;
        validateFields();
    }

    /**
     * Returns the default context which uses {@link ApplicationEnvironment#INTERNATIONAL_BUSINESS International
     * Business} formatting and the {@link java.util.Locale#getDefault() default locale}.
     *
     * @return  the default context
     */
    public static VTContext getDefault() {
        return VTContextHolder.DEFAULT_INSTANCE;
    }

    private void validateFields() {
        AssertPrecondition.notNull("applicationEnvironment", applicationEnvironment);
        AssertPrecondition.notNull("locale", locale);
        AssertPrecondition.notNull("mathContext", mathContext);
        AssertPrecondition.notNull("currency", currency);
        AssertPrecondition.withinRange("moneyScale", moneyScale, 0, 10);
        AssertPrecondition.withinRange("decimalScale", decimalScale, 0, 12);
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        validateFields();
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * Returns the application type to format for.
     *
     * @return  the environment enum value
     */
    public ApplicationEnvironment getApplicationEnvironment() {
        return applicationEnvironment;
    }

    /**
     * Returns the locale to format for.
     *
     * @return  the default locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns the currency to use as a default if not specified.
     *
     * @return  the default currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Returns the numeric precision to use for mathematical results as a default if not specified.
     *
     * @return  the default math precision
     */
    public int getPrecision() {
        return mathContext.getPrecision();
    }

    /**
     * Returns the rounding mode to use as default if not specified.
     *
     * @return  the rounding mode enum value
     */
    public RoundingMode getRoundingMode() {
        return mathContext.getRoundingMode();
    }

    /**
     * Returns whether result values should be rounded to a fixed scale or not.
     *
     * @return  round value or use IEEE floating point
     */
    public boolean isFixedScale() {
        return fixedScale;
    }

    /**
     * Returns the scale to round monetary values to if not specified.
     *
     * @return  the default monetary value decimal places
     */
    public int getMoneyScale() {
        return moneyScale;
    }

    /**
     * Returns the scale to round non-monetary values to if not specified.
     *
     * @return  the default non-monetary value decimal places
     */
    public int getDecimalScale() {
        return decimalScale;
    }

    /**
     * Returns a date-only formatter to use for this application environment.
     *
     * @return  the formatter
     *
     * @throws  AssertionError  DOCUMENT ME!
     */
    public DateFormat getDateFormat() {

        switch (applicationEnvironment) {

        case LOCAL_CUSTOM:
            return new BpDateFormat(BpDateFormat.JVM_DATE_ONLY, locale);

        case INTERNATIONAL_BUSINESS:
        case SCIENTIFIC:
            return new BpDateFormat(BpDateFormat.ISO_DATE_ONLY, locale);

        default:
            throw new AssertionError("undefined applicationEnvironment");
        }
    }

    /**
     * Returns the date-time formatter to use for this application environment.
     *
     * @return  the formatter
     *
     * @throws  AssertionError  DOCUMENT ME!
     */
    public DateFormat getTimestampFormat() {

        switch (applicationEnvironment) {

        case LOCAL_CUSTOM:
            return new BpDateFormat(BpDateFormat.JVM_DATE_TIME, locale);

        case INTERNATIONAL_BUSINESS:
        case SCIENTIFIC:
            return new BpDateFormat(BpDateFormat.ISO_DATE_TIME, locale);

        default:
            throw new AssertionError("undefined applicationEnvironment");
        }
    }

    /**
     * Returns the currency formatter to use for this application environment.
     *
     * @return  the formatter
     *
     * @throws  AssertionError  DOCUMENT ME!
     */
    public NumberFormat getCurrencyFormat() {

        switch (applicationEnvironment) {

        case LOCAL_CUSTOM:
        case INTERNATIONAL_BUSINESS:
        case SCIENTIFIC:
            return new BpNumberFormat(BpNumberFormat.JVM_CURRENCY, locale);

        default:
            throw new AssertionError("undefined applicationEnvironment");
        }
    }

    /**
     * Returns the floating point number formatter to use for this application environment.
     *
     * @return  the formatter
     *
     * @throws  AssertionError  DOCUMENT ME!
     */
    public NumberFormat getFloatingPointFormat() {

        switch (applicationEnvironment) {

        case LOCAL_CUSTOM:
            return new BpNumberFormat(BpNumberFormat.JVM_NUMBER, locale);

        case INTERNATIONAL_BUSINESS:
        case SCIENTIFIC:
            return new BpNumberFormat(BpNumberFormat.JVM_NUMBER, locale);

        default:
            throw new AssertionError("undefined applicationEnvironment");
        }
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
        result = prime * result + applicationEnvironment.hashCode();
        result = prime * result + locale.hashCode();
        result = prime * result + currency.hashCode();
        result = prime * result + mathContext.hashCode();
        result = prime * result + (fixedScale ? 1231 : 1237);
        result = prime * result + moneyScale;
        result = prime * result + decimalScale;

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

        if (!(obj instanceof VTContext)) {
            return false;
        }

        final VTContext other = (VTContext) obj;

        return applicationEnvironment.equals(other.applicationEnvironment) && locale.equals(other.locale) &&
                currency.equals(other.currency) && mathContext.equals(other.mathContext) &&
                fixedScale == other.fixedScale && moneyScale == other.moneyScale && decimalScale == other.decimalScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VTContext(");
        sb.append("applicationEnvironment:").append(applicationEnvironment);
        sb.append(", locale:").append(locale);
        sb.append(", currency:").append(currency);
        sb.append(", mathContext:").append(mathContext);
        sb.append(", fixedScale:").append(fixedScale);
        sb.append(", moneyScale:").append(moneyScale);
        sb.append(", decimalScale:").append(decimalScale);
        sb.append(")");

        return sb.toString();
    }

    private static class VTContextHolder {
        static final VTContext DEFAULT_INSTANCE = new VTContext();
    }

}

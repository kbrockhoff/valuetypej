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

import static org.testng.Assert.*;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import org.codekaizen.vtj.text.BpDateFormat;
import org.codekaizen.vtj.text.BpNumberFormat;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTContext}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTContextTest {

    /**
     * Creates a new VTContextTest object.
     */
    public VTContextTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldDefaultToInternationalBusiness() {
        final ApplicationEnvironment env = ApplicationEnvironment.INTERNATIONAL_BUSINESS;
        final Locale locale = Locale.getDefault();
        final Currency curr = Currency.getInstance(locale);
        final MathContext mc = MathContext.DECIMAL64;

        final VTContext vtc = VTContext.getDefault();
        assertEquals(vtc.getApplicationEnvironment(), env);
        assertEquals(vtc.getLocale(), locale);
        assertEquals(vtc.getCurrency(), curr);
        assertEquals(vtc.getPrecision(), mc.getPrecision());
        assertEquals(vtc.getRoundingMode(), mc.getRoundingMode());
        assertEquals(vtc.isFixedScale(), true);
        assertEquals(vtc.getMoneyScale(), curr.getDefaultFractionDigits());
        assertEquals(vtc.getDecimalScale(), VTContext.DEFAULT_DECIMAL_SCALE);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseContextValuesSuppliedInConstructor() {
        final ApplicationEnvironment env = ApplicationEnvironment.SCIENTIFIC;
        final Locale locale = Locale.GERMANY;
        final Currency curr = Currency.getInstance(locale);
        final MathContext mc = MathContext.DECIMAL128;

        final VTContext vtc = new VTContext(env, locale, mc);
        assertEquals(vtc.getApplicationEnvironment(), env);
        assertEquals(vtc.getLocale(), locale);
        assertEquals(vtc.getCurrency(), curr);
        assertEquals(vtc.getPrecision(), mc.getPrecision());
        assertEquals(vtc.getRoundingMode(), mc.getRoundingMode());
        assertEquals(vtc.isFixedScale(), false);
        assertEquals(vtc.getMoneyScale(), curr.getDefaultFractionDigits());
        assertEquals(vtc.getDecimalScale(), VTContext.DEFAULT_DECIMAL_SCALE);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfApplicationEnvironmentNull() {
        final VTContext vtc = new VTContext(null, Locale.UK, MathContext.DECIMAL32);
        assertEquals(vtc.getApplicationEnvironment(), ApplicationEnvironment.INTERNATIONAL_BUSINESS);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLocaleNull() {
        final VTContext vtc = new VTContext(ApplicationEnvironment.INTERNATIONAL_BUSINESS, null, MathContext.DECIMAL32);
        assertEquals(vtc.getApplicationEnvironment(), ApplicationEnvironment.INTERNATIONAL_BUSINESS);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfMathContextNull() {
        final VTContext vtc = new VTContext(ApplicationEnvironment.INTERNATIONAL_BUSINESS, Locale.UK, null);
        assertEquals(vtc.getApplicationEnvironment(), ApplicationEnvironment.INTERNATIONAL_BUSINESS);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseAllValuesSuppliedInConstructor() {
        final ApplicationEnvironment env = ApplicationEnvironment.INTERNATIONAL_BUSINESS;
        final Locale locale = Locale.CANADA;
        final Currency curr = Currency.getInstance("USD");
        final MathContext mc = new MathContext(18, RoundingMode.DOWN);

        final VTContext vtc = new VTContext(env, locale, mc, curr, true, 6, 10);
        assertEquals(vtc.getApplicationEnvironment(), env);
        assertEquals(vtc.getLocale(), locale);
        assertEquals(vtc.getCurrency(), curr);
        assertEquals(vtc.getPrecision(), mc.getPrecision());
        assertEquals(vtc.getRoundingMode(), mc.getRoundingMode());
        assertEquals(vtc.isFixedScale(), true);
        assertEquals(vtc.getMoneyScale(), 6);
        assertEquals(vtc.getDecimalScale(), 10);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseBpDateFormatForDateFormat() {
        final VTContext vtc = VTContext.getDefault();
        final DateFormat fmt = vtc.getDateFormat();
        assertEquals(fmt.getClass(), BpDateFormat.class);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseBpDateFormatForTimestampFormat() {
        final VTContext vtc = VTContext.getDefault();
        final DateFormat fmt = vtc.getTimestampFormat();
        assertEquals(fmt.getClass(), BpDateFormat.class);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseBpNumberFormatForCurrencyFormat() {
        final VTContext vtc = VTContext.getDefault();
        final NumberFormat fmt = vtc.getCurrencyFormat();
        assertEquals(fmt.getClass(), BpNumberFormat.class);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseBpNumberFormatForFloatingPointFormat() {
        final VTContext vtc = VTContext.getDefault();
        final NumberFormat fmt = vtc.getFloatingPointFormat();
        assertEquals(fmt.getClass(), BpNumberFormat.class);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldBeEquivalentForSameContextValues() {
        final ApplicationEnvironment env = ApplicationEnvironment.SCIENTIFIC;
        final Locale locale = Locale.GERMANY;
        final MathContext mc = MathContext.DECIMAL128;

        final VTContext vtc1 = new VTContext(env, locale, mc);
        assertTrue(vtc1.hashCode() == vtc1.hashCode());
        assertTrue(vtc1.equals(vtc1));

        VTContext vtc2 = new VTContext(env, locale, mc);
        assertTrue(vtc1.hashCode() == vtc2.hashCode());
        assertTrue(vtc1.equals(vtc2));

        vtc2 = new VTContext(env, Locale.FRANCE, mc);
        assertFalse(vtc1.hashCode() == vtc2.hashCode());
        assertFalse(vtc1.equals(vtc2));

        assertFalse(vtc1.equals(null));
        assertFalse(vtc1.equals(mc));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldPrintMainValuesInToString() {
        final VTContext vtc = VTContext.getDefault();
        final String s = vtc.toString();
        assertTrue(s.contains("applicationEnvironment:"));
        assertTrue(s.contains("locale:"));
        assertTrue(s.contains("mathContext:"));
    }

}

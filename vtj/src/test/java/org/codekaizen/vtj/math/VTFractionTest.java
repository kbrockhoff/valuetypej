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
package org.codekaizen.vtj.math;

import static org.testng.Assert.*;

import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTFraction}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTFractionTest extends AbstractVTNumberTest {

    /**
     * Creates a new VTFractionTest object.
     */
    public VTFractionTest() {
        super(VTFraction.class, new Class<?>[] { Long.TYPE, Long.TYPE, }, new Object[] { 3L, 5L, });
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldAcceptAnyValuesExceptAZeroDenominatorInConstructor() {
        VTFraction f = new VTFraction(10000000L, 22046226L);
        assertEquals(f.getNumerator(), 5000000L);
        assertEquals(f.getDenominator(), 11023113L);
        f = new VTFraction(9L, 3L);
        assertEquals(f.getNumerator(), 3L);
        assertEquals(f.getDenominator(), 1L);
        f = new VTFraction(25L, 100L);
        assertEquals(f.getNumerator(), 1L);
        assertEquals(f.getDenominator(), 4L);
        f = new VTFraction(0L, 100L);
        assertEquals(f.getNumerator(), 0L);
        assertEquals(f.getDenominator(), 1L);
        f = new VTFraction(-9L, 3L);
        assertEquals(f.getNumerator(), -3L);
        assertEquals(f.getDenominator(), 1L);
        f = new VTFraction(9L, -3L);
        assertEquals(f.getNumerator(), -3L);
        assertEquals(f.getDenominator(), 1L);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldThrowExceptionIfDenominatorIsZero() {
        final VTFraction f = new VTFraction(0L, 0L);
        assertEquals(f.intValue(), 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldConstructWithSmallestPossibleValuesFromDouble() {
        final VTFraction f = new VTFraction(0.25, 0.000001);
        assertEquals(f.getNumerator(), 1L);
        assertEquals(f.getDenominator(), 4L);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldCalculateReciprocalAsExpected() {
        VTFraction f = new VTFraction(100000000L, 22046226L);
        assertEquals(f.getNumerator(), 50000000L);
        assertEquals(f.getDenominator(), 11023113L);
        f = f.reciprocal();
        assertEquals(f.getNumerator(), 11023113L);
        assertEquals(f.getDenominator(), 50000000L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doAbsTesting() {
        VTFraction f = new VTFraction(4L, 7L);
        assertEquals(f.getNumerator(), 4L);
        assertEquals(f.getDenominator(), 7L);
        f = f.abs();
        assertEquals(f.getNumerator(), 4L);
        assertEquals(f.getDenominator(), 7L);
        f = new VTFraction(-4L, 7L);
        assertEquals(f.getNumerator(), -4L);
        assertEquals(f.getDenominator(), 7L);
        f = f.abs();
        assertEquals(f.getNumerator(), 4L);
        assertEquals(f.getDenominator(), 7L);
        f = new VTFraction(4L, -7L);
        assertEquals(f.getNumerator(), -4L);
        assertEquals(f.getDenominator(), 7L);
        f = f.abs();
        assertEquals(f.getNumerator(), 4L);
        assertEquals(f.getDenominator(), 7L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNegateTesting() {
        VTFraction f = new VTFraction(4L, 7L);
        assertEquals(f.getNumerator(), 4L);
        assertEquals(f.getDenominator(), 7L);
        f = f.negate();
        assertEquals(f.getNumerator(), -4L);
        assertEquals(f.getDenominator(), 7L);
        f = new VTFraction(-4L, 7L);
        assertEquals(f.getNumerator(), -4L);
        assertEquals(f.getDenominator(), 7L);
        f = f.negate();
        assertEquals(f.getNumerator(), 4L);
        assertEquals(f.getDenominator(), 7L);
        f = new VTFraction(4L, -7L);
        assertEquals(f.getNumerator(), -4L);
        assertEquals(f.getDenominator(), 7L);
        f = f.negate();
        assertEquals(f.getNumerator(), 4L);
        assertEquals(f.getDenominator(), 7L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPlusTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        VTFraction f2 = f1.plus(new VTFraction(4L, 7L));
        assertEquals(f2.getNumerator(), 8L);
        assertEquals(f2.getDenominator(), 7L);
        f2 = f1.plus(new VTInteger(1));
        assertEquals(f2.getNumerator(), 11L);
        assertEquals(f2.getDenominator(), 7L);
        f2 = f1.plus(new VTDouble(2.0));
        assertEquals(f2.getNumerator(), 18L);
        assertEquals(f2.getDenominator(), 7L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMinusTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        VTFraction f2 = f1.minus(new VTFraction(4L, 7L));
        assertEquals(f2.getNumerator(), 0L);
        assertEquals(f2.getDenominator(), 1L);
        f2 = f1.minus(new VTInteger(1));
        assertEquals(f2.getNumerator(), -3L);
        assertEquals(f2.getDenominator(), 7L);
        f2 = f1.minus(new VTDouble(2.0));
        assertEquals(f2.getNumerator(), -10L);
        assertEquals(f2.getDenominator(), 7L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMultiplyTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        final VTFraction f2 = f1.multiply(new VTFraction(4L, 7L));
        assertEquals(f2.getNumerator(), 16L);
        assertEquals(f2.getDenominator(), 49L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doDivTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        final VTFraction f2 = f1.div(new VTFraction(4L, 7L));
        assertEquals(f2.getNumerator(), 1L);
        assertEquals(f2.getDenominator(), 1L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPowTesting() {
        VTFraction f1 = new VTFraction(4L, 7L);
        VTFraction f2 = f1.pow(new VTFraction(2L, 1L));
        assertEquals(f2.getNumerator(), 16L);
        assertEquals(f2.getDenominator(), 49L);
        f1 = new VTFraction(14L, 7L);
        assertEquals(f1.doubleValue(), 2.0, 0.0001);
        f2 = f1.pow(new VTFraction(2L, 1L));
        assertEquals(f2.doubleValue(), 4.0, 0.0001);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doRoundTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        final VTFraction f2 = f1.round(1);
        assertEquals(f2.getNumerator(), 3L);
        assertEquals(f2.getDenominator(), 5L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doTruncateTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        final VTFraction f2 = f1.truncate();
        assertEquals(f2.getNumerator(), 0L);
        assertEquals(f2.getDenominator(), 1L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doCompareToTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        VTFraction f2 = new VTFraction(4L, 7L);
        assertTrue(f1.compareTo(f2) == 0);
        f2 = new VTFraction(5L, 7L);
        assertTrue(f1.compareTo(f2) < 0);
        f2 = new VTFraction(3L, 7L);
        assertTrue(f1.compareTo(f2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        final VTFraction f2 = new VTFraction(3L, 7L);
        assertFalse(f1.equals(f2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doToStringTesting() {
        final VTFraction f1 = new VTFraction(4L, 7L);
        assertEquals(f1.toString(), "4/7");
    }

}

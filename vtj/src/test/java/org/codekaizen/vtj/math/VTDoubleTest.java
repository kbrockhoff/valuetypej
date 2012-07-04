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

import java.text.DecimalFormat;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTDouble}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTDoubleTest extends AbstractVTNumberTest {

    /**
     * Creates a new VTDoubleTest object.
     */
    public VTDoubleTest() {
        super(VTDouble.class, new Class<?>[] { Double.TYPE, }, new Object[] { 1278518.2, });
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldAcceptAnyDoubleValueInConstructor() {
        final VTDouble d = new VTDouble(1.23);
        assertEquals(d.doubleValue(), 1.23, 0.001);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldAcceptNanInConstructor() {
        final VTDouble d = new VTDouble(Double.NaN);
        assertTrue(Double.isNaN(d.doubleValue()));
        assertEquals(d.intValue(), 0);
        assertEquals(d.toString(), "NaN");
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldAcceptPositiveInfinityInConstructor() {
        final VTDouble d = new VTDouble(Double.POSITIVE_INFINITY);
        assertEquals(d.doubleValue(), Double.POSITIVE_INFINITY);
        assertEquals(d.intValue(), Integer.MAX_VALUE);
        assertEquals(d.toString(), "\u221E");
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldAcceptNegativeInfinityInConstructor() {
        final VTDouble d = new VTDouble(Double.NEGATIVE_INFINITY);
        assertEquals(d.doubleValue(), Double.NEGATIVE_INFINITY);
        assertEquals(d.intValue(), Integer.MIN_VALUE);
        assertEquals(d.toString(), "-\u221E");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doAbsTesting() {
        VTDouble d1 = new VTDouble(12.18);
        VTDouble d2 = d1.abs();
        assertEquals(d2.doubleValue(), d1.doubleValue(), 0.001);
        d1 = new VTDouble(-12.18);
        d2 = d1.abs();
        assertEquals(d2.doubleValue() * -1.0, d1.doubleValue(), 0.001);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNegateTesting() {
        VTDouble d1 = new VTDouble(1458.0);
        VTDouble d2 = d1.negate();
        assertEquals(d2.intValue() * -1, d1.intValue());
        d1 = new VTDouble(-1458.0);
        d2 = d1.negate();
        assertEquals(d2.intValue() * -1, d1.intValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPlusTesting() {
        VTDouble d1 = new VTDouble(1458.0);
        VTDouble d2 = new VTDouble(1458.0);
        VTDouble d3 = d1.plus(d2);
        assertEquals(d3.intValue(), 2916);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDouble(1458.0);
        d2 = new VTDouble(0.0);
        d3 = d1.plus(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());

        final DecimalFormat fmt = new DecimalFormat("0.00");
        d1 = new VTDouble(12456.78);
        d2 = new VTDouble(1817.52);
        d3 = d1.plus(d2);
        assertEquals(fmt.format(d3.doubleValue()), "14274.30");
        d2 = new VTDouble(1817.523);
        d3 = d1.plus(d2);
        assertEquals(fmt.format(d3.doubleValue()), "14274.30");
        d2 = new VTDouble(-1817.52);
        d3 = d1.plus(d2);
        assertEquals(fmt.format(d3.doubleValue()), "10639.26");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMinusTesting() {
        VTDouble d1 = new VTDouble(1458.0);
        VTDouble d2 = new VTDouble(1458.0);
        VTDouble d3 = d1.minus(d2);
        assertEquals(d3.intValue(), 0);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDouble(1458.0);
        d2 = new VTDouble(0.0);
        d3 = d1.minus(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());

        final DecimalFormat fmt = new DecimalFormat("0.00");
        d1 = new VTDouble(12456.78);
        d2 = new VTDouble(1817.52);
        d3 = d1.minus(d2);
        assertEquals(fmt.format(d3.doubleValue()), "10639.26");
        d2 = new VTDouble(1817.523);
        d3 = d1.minus(d2);
        assertEquals(fmt.format(d3.doubleValue()), "10639.26");
        d2 = new VTDouble(-1817.52);
        d3 = d1.minus(d2);
        assertEquals(fmt.format(d3.doubleValue()), "14274.30");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMultiplyTesting() {
        VTDouble d1 = new VTDouble(1458.0);
        VTDouble d2 = new VTDouble(1458.0);
        VTDouble d3 = d1.multiply(d2);
        assertEquals(d3.intValue(), 2125764);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDouble(1458.0);
        d2 = new VTDouble(0.0);
        d3 = d1.multiply(d2);
        assertEquals(d3.intValue(), 0);
        assertFalse(d1.intValue() == d3.intValue());
        assertTrue(d2.intValue() == d3.intValue());

        final DecimalFormat fmt = new DecimalFormat("0.00");
        d1 = new VTDouble(12456.78);
        d2 = new VTDouble(0.181752);
        d3 = d1.multiply(d2);
        assertEquals(fmt.format(d3.doubleValue()), "2264.04");
        d2 = new VTDouble(-0.181752);
        d3 = d1.multiply(d2);
        assertEquals(fmt.format(d3.doubleValue()), "-2264.04");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doDivTesting() {
        VTDouble d1 = new VTDouble(1458.0);
        VTDouble d2 = new VTDouble(1458.0);
        VTDouble d3 = d1.div(d2);
        assertEquals(d3.intValue(), 1);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDouble(1458.0);
        d2 = new VTDouble(1.0);
        d3 = d1.div(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());

        final DecimalFormat fmt = new DecimalFormat("0.00");
        d1 = new VTDouble(12456.78);
        d2 = new VTDouble(0.181752);
        d3 = d1.div(d2);
        assertEquals(fmt.format(d3.doubleValue()), "68537.24");
        d2 = new VTDouble(-0.181752);
        d3 = d1.div(d2);
        assertEquals(fmt.format(d3.doubleValue()), "-68537.24");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPowTesting() {
        final VTDouble d1 = new VTDouble(1458.0);
        final VTDouble d2 = new VTDouble(0.5);
        final VTDouble d3 = d1.pow(d2);
        assertEquals(d3.doubleValue(), 38.183766, 0.000001);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doRoundTesting() {
        VTDouble d1 = new VTDouble(1458.0);
        VTDouble d2 = d1.round(0);
        assertEquals(d2.intValue(), 1458);
        d2 = d1.round(5);
        assertEquals(d2.intValue(), 1458);
        d1 = new VTDouble(1458.34);
        d2 = d1.round(0);
        assertEquals(d2.intValue(), 1458);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doTruncateTesting() {
        VTDouble d1 = new VTDouble(1458.0);
        VTDouble d2 = d1.truncate();
        assertEquals(d2.intValue(), 1458);
        d1 = new VTDouble(1458.34);
        d2 = d1.truncate();
        assertEquals(d2.intValue(), 1458);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doCompareToTesting() {
        final VTDouble d1 = new VTDouble(145.8);
        VTDouble d2 = new VTDouble(145.8);
        assertTrue(d1.compareTo(d2) == 0);
        d2 = new VTDouble(1458.0);
        assertTrue(d1.compareTo(d2) < 0);
        d2 = new VTDouble(14.58);
        assertTrue(d1.compareTo(d2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTDouble d1 = new VTDouble(145.8);
        final VTDouble d2 = new VTDouble(1458.0);
        assertFalse(d1.equals(d2));
        assertFalse(d1.equals(new Integer(1458)));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doToStringTesting() {
        VTDouble d = null;
        String s = null;
        d = new VTDouble(1234567890L);
        s = d.toString();
        assertEquals(s, "1.23456789E9");
        d = new VTDouble(-1234567890L);
        s = d.toString();
        assertEquals(s, "-1.23456789E9");
        d = new VTDouble(1234.56789);
        s = d.toString();
        assertEquals(s, "1.23456789E3");
        d = new VTDouble(-12.3456789);
        s = d.toString();
        assertEquals(s, "-1.23456789E1");
        d = new VTDouble(0.0012);
        s = d.toString();
        assertEquals(s, "1.2E-3");
        d = new VTDouble(-0.0123456789);
        s = d.toString();
        assertEquals(s, "-1.23456789E-2");
    }

}

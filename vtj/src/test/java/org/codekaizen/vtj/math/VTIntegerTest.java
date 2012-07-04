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
 * <p>Unit tests for {@link VTInteger}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTIntegerTest extends AbstractVTNumberTest {

    /**
     * Creates a new VTIntegerTest object.
     */
    public VTIntegerTest() {
        super(VTInteger.class, new Class<?>[] { Integer.TYPE, }, new Object[] { 1458, });
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldAcceptAnyIntegerInConstructor() {
        VTInteger i = new VTInteger(1);
        assertEquals(i.intValue(), 1);
        i = new VTInteger(0);
        assertEquals(i.intValue(), 0);
        i = new VTInteger(-1);
        assertEquals(i.intValue(), -1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldCalcModulusAsExceptedUsingFluentAPI() {
        final VTInteger d1 = new VTInteger(1278518);
        final VTInteger d2 = d1.mod(new VTInteger(100));
        assertEquals(d2.intValue(), 18);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldDeincrementUsingPrevious() {
        VTInteger d1 = new VTInteger(10);

        for (int i = 10; i > 0; i--) {
            assertEquals(d1.intValue(), i);
            d1 = d1.previous();
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldIncrementUsingNext() {
        VTInteger d1 = new VTInteger(0);

        for (int i = 0; i < 10; i++) {
            assertEquals(d1.intValue(), i);
            d1 = d1.next();
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doAbsTesting() {
        VTInteger d1 = new VTInteger(1218);
        VTInteger d2 = d1.abs();
        assertEquals(d2.doubleValue(), d1.doubleValue(), 0.001);
        d1 = new VTInteger(-1218);
        d2 = d1.abs();
        assertEquals(d2.doubleValue() * -1.0, d1.doubleValue(), 0.001);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNegateTesting() {
        VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = d1.negate();
        assertEquals(d2.intValue() * -1, d1.intValue());
        d1 = new VTInteger(-1458);
        d2 = d1.negate();
        assertEquals(d2.intValue() * -1, d1.intValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPlusTesting() {
        VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = new VTInteger(1458);
        VTInteger d3 = d1.plus(d2);
        assertEquals(d3.intValue(), 2916);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTInteger(1458);
        d2 = new VTInteger(0);
        d3 = d1.plus(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMinusTesting() {
        VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = new VTInteger(1458);
        VTInteger d3 = d1.minus(d2);
        assertEquals(d3.intValue(), 0);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTInteger(1458);
        d2 = new VTInteger(0);
        d3 = d1.minus(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMultiplyTesting() {
        VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = new VTInteger(1458);
        VTInteger d3 = d1.multiply(d2);
        assertEquals(d3.intValue(), 2125764);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTInteger(1458);
        d2 = new VTInteger(0);
        d3 = d1.multiply(d2);
        assertEquals(d3.intValue(), 0);
        assertFalse(d1.intValue() == d3.intValue());
        assertTrue(d2.intValue() == d3.intValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doDivTesting() {
        VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = new VTInteger(1458);
        VTInteger d3 = d1.div(d2);
        assertEquals(d3.intValue(), 1);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTInteger(1458);
        d2 = new VTInteger(100);
        d3 = d1.div(d2);
        assertEquals(d3.intValue(), 14);
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPowTesting() {
        final VTInteger d1 = new VTInteger(16);
        VTInteger d2 = d1.pow(new VTInteger(3));
        assertEquals(d2.intValue(), 4096);
        d2 = d1.pow(new VTInteger(-3));
        assertEquals(d2.intValue(), 0);
        d2 = d1.pow(new VTInteger(0));
        assertEquals(d2.intValue(), 1);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doRoundTesting() {
        VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = d1.round(0);
        assertEquals(d2.intValue(), 1458);
        d2 = d1.round(5);
        assertEquals(d2.intValue(), 1458);
        d1 = new VTInteger(145834);
        d2 = d1.round(0);
        assertEquals(d2.intValue(), 145834);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doTruncateTesting() {
        VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = d1.truncate();
        assertEquals(d2.intValue(), 1458);
        d1 = new VTInteger(145834);
        d2 = d1.truncate();
        assertEquals(d2.intValue(), 145834);

    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doCompareToTesting() {
        final VTInteger d1 = new VTInteger(1458);
        VTInteger d2 = new VTInteger(1458);
        assertTrue(d1.compareTo(d2) == 0);
        d2 = new VTInteger(14580);
        assertTrue(d1.compareTo(d2) < 0);
        d2 = new VTInteger(145);
        assertTrue(d1.compareTo(d2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTInteger d1 = new VTInteger(-1458);
        final VTInteger d2 = new VTInteger(-1457);
        assertFalse(d1.equals(d2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doToStringTesting() {
        VTInteger d = null;
        String s = null;
        d = new VTInteger(Integer.MAX_VALUE);
        s = d.toString();
        assertEquals(-1, s.indexOf('.'));
        d = new VTInteger(Integer.MIN_VALUE);
        s = d.toString();
        assertEquals(s.indexOf('.'), -1);
        assertEquals(s.indexOf('-'), 0);
    }

}

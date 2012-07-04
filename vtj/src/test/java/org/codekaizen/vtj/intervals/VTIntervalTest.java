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
package org.codekaizen.vtj.intervals;

import static org.testng.Assert.*;

import org.codekaizen.vtj.AbstractValueTypeTest;
import org.codekaizen.vtj.ValueType;
import org.codekaizen.vtj.math.VTDecimal;
import org.codekaizen.vtj.math.VTInteger;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTInterval}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTIntervalTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTIntervalTest object.
     */
    public VTIntervalTest() {
        super(VTInterval.class, new Class<?>[] { ValueType.class, ValueType.class, Boolean.TYPE, Boolean.TYPE, },
            new Object[] { new VTInteger(0), new VTInteger(9), Boolean.TRUE, Boolean.TRUE, });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTInterval() {
        final VTString lower = new VTString("A");
        final VTString higher = new VTString("Z");
        VTInterval<VTString> vti1 = new VTInterval<VTString>(lower, higher, true, true);
        assertEquals(vti1.getLowerBound(), lower);
        assertEquals(vti1.getHigherBound(), higher);
        assertEquals(vti1.isLowerBoundIncluded(), true);
        assertEquals(vti1.isHigherBoundIncluded(), true);

        try {
            vti1 = new VTInterval<VTString>(higher, lower, true, true);
            fail("Should thrown an exception where higher bound is " + "less than lower bound");
        } catch (IllegalArgumentException iae) {
            // do nothing
        }

        VTInterval<VTInteger> vti2 = new VTInterval<VTInteger>(new VTInteger(0), null, true, false);
        assertNull(vti2.getHigherBound());
        vti2 = new VTInterval<VTInteger>(null, new VTInteger(0), false, false);
        assertNull(vti2.getLowerBound());
        vti2 = new VTInterval<VTInteger>(null, null, false, false);
        assertNull(vti2.getLowerBound());
        assertNull(vti2.getHigherBound());
        vti2 = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(0), true, true);
        assertEquals(vti2.isLowerBoundIncluded(), true);
        assertEquals(vti2.isHigherBoundIncluded(), true);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetLowerBound() {
        final VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);
        assertEquals(vti.getLowerBound(), new VTDecimal(100000L, 2));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetHigherBound() {
        final VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);
        assertEquals(vti.getHigherBound(), new VTDecimal(200000L, 2));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIsLowerBoundIncluded() {
        final VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);
        assertFalse(vti.isLowerBoundIncluded());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIsHigherBoundIncluded() {
        final VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);
        assertTrue(vti.isHigherBoundIncluded());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIsEmpty() {
        final VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);
        assertFalse(vti.isEmpty());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testContains() {
        VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2), new VTDecimal(200000L, 2),
                false, true);
        assertFalse(vti.contains(new VTDecimal(100000L, 2)));
        assertTrue(vti.contains(new VTDecimal(200000L, 2)));
        assertTrue(vti.contains(new VTDecimal(105000L, 2)));
        assertFalse(vti.contains(new VTDecimal(5000L, 2)));
        assertFalse(vti.contains(new VTDecimal(205000L, 2)));
        assertFalse(vti.contains(null));
        vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2), new VTDecimal(200000L, 2), true, false);
        assertTrue(vti.contains(new VTDecimal(100000L, 2)));
        assertFalse(vti.contains(new VTDecimal(200000L, 2)));
        assertTrue(vti.contains(new VTDecimal(105000L, 2)));
        assertFalse(vti.contains(new VTDecimal(5000L, 2)));
        assertFalse(vti.contains(new VTDecimal(205000L, 2)));
        assertFalse(vti.contains(null));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetPosition() {
        final VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);
        assertEquals(vti.getPosition(new VTDecimal(100000L, 2)), IntervalElementPosition.LOWER_BOUND);
        assertEquals(vti.getPosition(new VTDecimal(200000L, 2)), IntervalElementPosition.HIGHER_BOUND);
        assertEquals(vti.getPosition(new VTDecimal(105000L, 2)), IntervalElementPosition.IN_INTERVAL);
        assertEquals(vti.getPosition(new VTDecimal(5000L, 2)), IntervalElementPosition.LOWER);
        assertEquals(vti.getPosition(new VTDecimal(205000L, 2)), IntervalElementPosition.HIGHER);

        try {
            vti.getPosition(null);
            fail("should have thrown NullPointerException");
        } catch (IllegalArgumentException npe) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testCompareIntervalTo() {
        final VTInterval<VTInteger> vtiX = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false);
        VTInterval<VTInteger> vtiY = null;

        try {
            vtiX.compareIntervalTo(vtiY);
            fail("should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // do nothing
        }

        vtiY = new VTInterval<VTInteger>(new VTInteger(-20), new VTInteger(-10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDED_BY);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-10), new VTInteger(0), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.MET_BY);
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.EQUALS);
        vtiY = new VTInterval<VTInteger>(new VTInteger(10), new VTInteger(20), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.MEETS);
        vtiY = new VTInterval<VTInteger>(new VTInteger(20), new VTInteger(30), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(5), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.OVERLAPED_BY);
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(15), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.OVERLAPS);
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(5), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.STARTED_BY);
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(15), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.STARTS);
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.FINISHED_BY);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.FINISHES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-10), new VTInteger(20), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.DURING);
        vtiY = new VTInterval<VTInteger>(new VTInteger(4), new VTInteger(6), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.CONTAINS);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIntersection() {
        final VTInterval<VTInteger> vtiX = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false);
        VTInterval<VTInteger> vtiY = null;
        IntervalValueType<VTInteger> vtiZ = null;

        try {
            vtiZ = vtiX.intersection(vtiY);
            fail("should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // do nothing
        }

        vtiY = new VTInterval<VTInteger>(new VTInteger(-20), new VTInteger(-10), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(-10), new VTInteger(0), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(10), new VTInteger(20), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(20), new VTInteger(30), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(5), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(5), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(15), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(10), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(5), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(5), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(15), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(10), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(10), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(10), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(-10), new VTInteger(20), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false));
        vtiY = new VTInterval<VTInteger>(new VTInteger(4), new VTInteger(6), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTInterval<VTInteger>(new VTInteger(4), new VTInteger(6), true, false));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTInterval<VTDecimal> vti = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);

        try {
            vti.compareTo(null);
            fail("should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // do nothing
        }

        assertTrue(vti.compareTo(
                new VTInterval<VTDecimal>(new VTDecimal(100000L, 2), new VTDecimal(200000L, 2), false, true)) == 0);
        assertTrue(vti.compareTo(
                new VTInterval<VTDecimal>(new VTDecimal(200000L, 2), new VTDecimal(500000L, 2), false, true)) < 0);
        assertTrue(vti.compareTo(
                new VTInterval<VTDecimal>(new VTDecimal(0L, 2), new VTDecimal(100000L, 2), false, true)) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTInterval<VTDecimal> vti1 = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2),
                new VTDecimal(200000L, 2), false, true);
        final VTInterval<VTDecimal> vti2 = new VTInterval<VTDecimal>(new VTDecimal(200000L, 2),
                new VTDecimal(500000L, 2), false, true);
        assertFalse(vti1.equals(vti2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        VTInterval<VTDecimal> vti1 = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2), new VTDecimal(200000L, 2),
                false, true);
        assertEquals(vti1.toString(), "(1000.00,2000.00]");
        vti1 = new VTInterval<VTDecimal>(new VTDecimal(100000L, 2), new VTDecimal(200000L, 2), true, false);
        assertEquals(vti1.toString(), "[1000.00,2000.00)");

        VTInterval<VTInteger> vti2 = new VTInterval<VTInteger>(new VTInteger(0), null, true, false);
        assertEquals(vti2.toString(), "[0,)");
        vti2 = new VTInterval<VTInteger>(null, new VTInteger(0), false, false);
        assertEquals(vti2.toString(), "(,0)");
        vti2 = new VTInterval<VTInteger>(null, null, false, false);
        assertEquals(vti2.toString(), "(,)");
    }

}

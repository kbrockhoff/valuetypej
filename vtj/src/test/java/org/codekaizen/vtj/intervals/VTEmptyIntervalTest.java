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
import org.codekaizen.vtj.math.VTDecimal;
import org.codekaizen.vtj.math.VTInteger;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTEmptyInterval}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTEmptyIntervalTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTEmptyIntervalTest object.
     */
    public VTEmptyIntervalTest() {
        super(VTEmptyInterval.class, new Class<?>[] {}, new Object[] {});
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTInterval() {
        final VTEmptyInterval<VTString> vti1 = new VTEmptyInterval<VTString>();
        assertTrue(vti1.isEmpty());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetLowerBound() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertNull(vti.getLowerBound());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetHigherBound() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertNull(vti.getHigherBound());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIsLowerBoundIncluded() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertFalse(vti.isLowerBoundIncluded());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIsHigherBoundIncluded() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertFalse(vti.isHigherBoundIncluded());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIsEmpty() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertTrue(vti.isEmpty());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testContains() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertFalse(vti.contains(new VTDecimal(100000L, 2)));
        assertFalse(vti.contains(new VTDecimal(200000L, 2)));
        assertFalse(vti.contains(new VTDecimal(105000L, 2)));
        assertFalse(vti.contains(new VTDecimal(5000L, 2)));
        assertFalse(vti.contains(new VTDecimal(205000L, 2)));
        assertFalse(vti.contains(null));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetPosition() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertEquals(vti.getPosition(new VTDecimal(100000L, 2)), IntervalElementPosition.HIGHER);
        assertEquals(vti.getPosition(new VTDecimal(200000L, 2)), IntervalElementPosition.HIGHER);
        assertEquals(vti.getPosition(new VTDecimal(105000L, 2)), IntervalElementPosition.HIGHER);
        assertEquals(vti.getPosition(new VTDecimal(5000L, 2)), IntervalElementPosition.HIGHER);
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
        final VTEmptyInterval<VTInteger> vtiX = new VTEmptyInterval<VTInteger>();
        VTInterval<VTInteger> vtiY = null;

        try {
            vtiX.compareIntervalTo(vtiY);
            fail("should have thrown NullPointerException");
        } catch (IllegalArgumentException npe) {
            // do nothing
        }

        vtiY = new VTInterval<VTInteger>(new VTInteger(-20), new VTInteger(-10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-10), new VTInteger(0), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(10), new VTInteger(20), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(20), new VTInteger(30), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(5), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(15), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(5), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(15), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(10), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(-10), new VTInteger(20), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
        vtiY = new VTInterval<VTInteger>(new VTInteger(4), new VTInteger(6), true, false);
        assertEquals(vtiX.compareIntervalTo(vtiY), IntervalRelation.PRECEDES);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIntersection() {
        final VTEmptyInterval<VTInteger> vtiX = new VTEmptyInterval<VTInteger>();
        VTInterval<VTInteger> vtiY = null;
        IntervalValueType<VTInteger> vtiZ = null;

        try {
            vtiZ = vtiX.intersection(vtiY);
            fail("should have thrown NullPointerException");
        } catch (IllegalArgumentException npe) {
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
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(10), new VTInteger(20), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(20), new VTInteger(30), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(5), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(15), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(5), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(0), new VTInteger(15), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(5), new VTInteger(10), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(-5), new VTInteger(10), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(-10), new VTInteger(20), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
        vtiY = new VTInterval<VTInteger>(new VTInteger(4), new VTInteger(6), true, false);
        vtiZ = vtiX.intersection(vtiY);
        assertEquals(vtiZ, new VTEmptyInterval<VTInteger>());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();

        try {
            vti.compareTo(null);
            fail("should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // do nothing
        }

        assertTrue(vti.compareTo(new VTEmptyInterval<VTDecimal>()) < 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTEmptyInterval<VTDecimal> vti1 = new VTEmptyInterval<VTDecimal>();
        final VTInterval<VTDecimal> vti2 = new VTInterval<VTDecimal>(new VTDecimal(200000L, 2),
                new VTDecimal(500000L, 2), false, true);
        assertFalse(vti1.equals(vti2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTEmptyInterval<VTDecimal> vti = new VTEmptyInterval<VTDecimal>();
        assertEquals(vti.toString(), "(0,0)");
    }

}

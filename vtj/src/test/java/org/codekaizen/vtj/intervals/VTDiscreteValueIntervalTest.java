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
import org.codekaizen.vtj.enums.VTBoolean;
import org.codekaizen.vtj.math.VTInteger;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTDiscreteValueInterval}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTDiscreteValueIntervalTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTDiscreteValueIntervalTest object.
     */
    public VTDiscreteValueIntervalTest() {
        super(VTDiscreteValueInterval.class, new Class<?>[] { ValueType.class, ValueType.class, },
            new Object[] { new VTInteger(0), new VTInteger(9), });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTDiscreteValueInterval() {
        VTDiscreteValueInterval<VTInteger> dvi = new VTDiscreteValueInterval<VTInteger>(new VTInteger(1),
                new VTInteger(52));
        assertEquals(dvi.getLowerBound(), new VTInteger(1));
        assertEquals(dvi.getHigherBound(), new VTInteger(52));
        assertTrue(dvi.isLowerBoundIncluded());
        assertTrue(dvi.isHigherBoundIncluded());

        try {
            dvi = new VTDiscreteValueInterval<VTInteger>(new VTInteger(0), null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // do nothing
        }

        try {
            final VTDiscreteValueInterval<VTBoolean> dvib = new VTDiscreteValueInterval<VTBoolean>(VTBoolean.FALSE,
                    VTBoolean.TRUE);
            dvib.toString();
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIterator() {
        final VTDiscreteValueInterval<VTInteger> dvi = new VTDiscreteValueInterval<VTInteger>(new VTInteger(1),
                new VTInteger(52));
        int i = dvi.getLowerBound().intValue();

        for (final VTInteger integer : dvi) {
            assertEquals(integer.intValue(), i);
            i++;
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTDiscreteValueInterval<VTInteger> dvi1 = new VTDiscreteValueInterval<VTInteger>(new VTInteger(0),
                new VTInteger(9));
        VTDiscreteValueInterval<VTInteger> dvi2 = new VTDiscreteValueInterval<VTInteger>(new VTInteger(0),
                new VTInteger(9));
        assertTrue(dvi1.compareTo(dvi2) == 0);
        dvi2 = new VTDiscreteValueInterval<VTInteger>(new VTInteger(10), new VTInteger(19));
        assertTrue(dvi1.compareTo(dvi2) < 0);
        dvi2 = new VTDiscreteValueInterval<VTInteger>(new VTInteger(-10), new VTInteger(-1));
        assertTrue(dvi1.compareTo(dvi2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTDiscreteValueInterval<VTInteger> dvi1 = new VTDiscreteValueInterval<VTInteger>(new VTInteger(0),
                new VTInteger(9));
        final VTDiscreteValueInterval<VTInteger> dvi2 = new VTDiscreteValueInterval<VTInteger>(new VTInteger(0),
                new VTInteger(99));
        assertFalse(dvi1.equals(dvi2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTDiscreteValueInterval<VTInteger> dvi = new VTDiscreteValueInterval<VTInteger>(new VTInteger(1),
                new VTInteger(52));
        assertEquals(dvi.toString(), "[1,52]");
    }

}

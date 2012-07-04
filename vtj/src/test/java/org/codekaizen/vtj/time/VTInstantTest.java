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
package org.codekaizen.vtj.time;

import static org.testng.Assert.*;

import org.codekaizen.vtj.AbstractValueTypeTest;
import org.testng.annotations.Test;


/**
 * <p>Units tests for {@link VTInstant}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTInstantTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTInstantTest object.
     */
    public VTInstantTest() {
        super(VTInstant.class, new Class<?>[] { Long.TYPE, Integer.TYPE, },
            new Object[] { new Long(25483940596L), new Integer(500000000) });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTInstantLongInt() {
        VTInstant vti = new VTInstant(-364503600L, 0);
        assertEquals(vti.getEpochSeconds(), -364503600L);
        assertEquals(vti.getNanoOfSecond(), 0);
        vti = new VTInstant(364503600L, 0);
        assertEquals(vti.getEpochSeconds(), 364503600L);
        assertEquals(vti.getNanoOfSecond(), 0);
        vti = new VTInstant(1213738265L, 122000000);
        assertEquals(vti.getEpochSeconds(), 1213738265L);
        assertEquals(vti.getNanoOfSecond(), 122000000);
        vti = new VTInstant(-364484715L, -750000000);
        assertEquals(vti.getEpochSeconds(), -364484716L);
        assertEquals(vti.getNanoOfSecond(), 250000000);

        try {
            vti = new VTInstant(1213738265L, 1220000000);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTInstantLong() {
        // birth date -364503600000
        VTInstant vti = new VTInstant(-364503600000L);
        assertEquals(vti.getEpochSeconds(), -364503600L);
        assertEquals(vti.getNanoOfSecond(), 0);
        // birth second -364484716000
        vti = new VTInstant(-364484716000L);
        assertEquals(vti.getEpochSeconds(), -364484716L);
        assertEquals(vti.getNanoOfSecond(), 0);
        // birth ms -364484715750
        vti = new VTInstant(-364484715750L);
        assertEquals(vti.getEpochSeconds(), -364484716L);
        assertEquals(vti.getNanoOfSecond(), 250000000);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTInstantFloat() {
        VTInstant vti = new VTInstant(39616.0f);
        assertEquals(vti.getEpochSeconds(), 1213678800L);
        assertEquals(vti.getNanoOfSecond(), 0);

        try {
            vti = new VTInstant(-39616.0f);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetEpochSeconds() {
        final VTInstant vti = new VTInstant(1213738265L, 122000000);
        assertEquals(vti.getEpochSeconds(), 1213738265L);
        assertEquals(vti.getNanoOfSecond(), 122000000);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetNanoOfSecond() {
        final VTInstant vti = new VTInstant(-364484715L, -750000000);
        assertEquals(vti.getEpochSeconds(), -364484716L);
        assertEquals(vti.getNanoOfSecond(), 250000000);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetSerialValue() {
        final VTInstant vti = new VTInstant(-2203869600L, 0);
        assertEquals(vti.getSerialValue(), 61.0f);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testPlus() {
        // TODO Auto-generated method stub

    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testMinus() {
        // TODO Auto-generated method stub

    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTInstant vti1 = new VTInstant(1213738265L, 122000000);
        VTInstant vti2 = new VTInstant(1213738265L, 125000000);
        assertTrue(vti1.compareTo(vti2) < 0);
        vti2 = new VTInstant(1213738265L, 122000000);
        assertTrue(vti1.compareTo(vti2) == 0);
        vti2 = new VTInstant(1213738265L, 0);
        assertTrue(vti1.compareTo(vti2) > 0);
        vti2 = new VTInstant(1213738264L, 122000000);
        assertTrue(vti1.compareTo(vti2) > 0);
        vti2 = new VTInstant(1213738266L, 122000000);
        assertTrue(vti1.compareTo(vti2) < 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTInstant vti1 = new VTInstant(1213738265L, 122000000);
        final VTInstant vti2 = new VTInstant(1213738265L, 125000000);
        assertFalse(vti1.equals(vti2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        // TODO Auto-generated method stub

    }

}

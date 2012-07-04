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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;


/**
 * <p>Units tests for package-level static methods in {@link VTInstant}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTInstantStaticMethodsTest {

    /**
     * Creates a new VTInstantStaticMethodsTest object.
     */
    public VTInstantStaticMethodsTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testEpoch() {
        assertEquals(VTInstant.EPOCH.getEpochSeconds(), 0L);
        assertEquals(VTInstant.EPOCH.getNanoOfSecond(), 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testCalcSeconds() {
        assertEquals(VTInstant.calcSeconds(1.0f), -2208967200L);
        assertEquals(VTInstant.calcSeconds(39616.0f), 1213678800L);
        assertEquals(VTInstant.calcSeconds(39616.6888888888f), 1213738200L);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCalcInvalidSeconds() {
        VTInstant.calcSeconds(-1.0f);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCalcFeb29Seconds() {
        VTInstant.calcSeconds(60.25f);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testCalcNanos() {
        assertEquals(VTInstant.calcNanos(1.0f), 0);
        assertEquals(VTInstant.calcNanos(39616.0f), 0);
        assertEquals(VTInstant.calcNanos(39616.6888888888f), 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testCalcSerialDate() {
        assertEquals(VTInstant.calcSerialDate(0L, 0), 25568.75f);
        assertEquals(VTInstant.calcSerialDate(1213678800L, 0), 39616.0f);
        assertEquals(VTInstant.calcSerialDate(1213738265L, 122000000), 39616.688888888f);
        assertEquals(VTInstant.calcSerialDate(-2208967200L, 0), 1.0f);
        assertEquals(VTInstant.calcSerialDate(-2203956000L, 0), 59.0f);
        assertEquals(VTInstant.calcSerialDate(-2203883100L, 0), 59.84375f);
        assertEquals(VTInstant.calcSerialDate(-2203869600L, 0), 61.0f);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCalcInvalidSerialDate() {
        VTInstant.calcSerialDate(-2408967200L, 0);
    }

}

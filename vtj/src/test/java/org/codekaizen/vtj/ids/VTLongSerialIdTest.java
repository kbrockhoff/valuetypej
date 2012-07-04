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
package org.codekaizen.vtj.ids;

import static org.testng.Assert.*;

import org.codekaizen.vtj.AbstractValueTypeTest;
import org.testng.annotations.Test;


/**
 * <p>Units tests for {@link VTLongSerialId}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTLongSerialIdTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTLongSerialIdTest object.
     */
    public VTLongSerialIdTest() {
        super(VTLongSerialId.class, new Class<?>[] { Long.TYPE, }, new Object[] { 2345L, });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTLongSerialId() {
        final VTLongSerialId sid = new VTLongSerialId(28795847321L);
        assertEquals(sid.longValue(), 28795847321L);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testVTLongNegativeSerialId() {
        new VTLongSerialId(-143L);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testLongValue() {
        final VTLongSerialId sid = new VTLongSerialId(28795L);
        assertEquals(sid.longValue(), 28795L);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTLongSerialId sid1 = new VTLongSerialId(28794L);
        VTLongSerialId sid2 = new VTLongSerialId(28794L);
        assertTrue(sid1.compareTo(sid2) == 0);
        sid2 = new VTLongSerialId(28795L);
        assertTrue(sid1.compareTo(sid2) < 0);
        sid2 = new VTLongSerialId(28793L);
        assertTrue(sid1.compareTo(sid2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTLongSerialId sid1 = new VTLongSerialId(28794L);
        final VTLongSerialId sid2 = new VTLongSerialId(28795L);
        assertFalse(sid1.equals(sid2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTLongSerialId sid1 = new VTLongSerialId(28794L);
        assertEquals(sid1.toString(), "sid:28794");
    }

}

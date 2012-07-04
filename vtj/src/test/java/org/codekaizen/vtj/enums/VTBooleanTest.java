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
package org.codekaizen.vtj.enums;

import static org.testng.Assert.*;

import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTBoolean}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTBooleanTest extends AbstractEnumValueTypeTest {

    /**
     * Creates a new VTBooleanTest object.
     */
    public VTBooleanTest() {
        super(VTBoolean.class, new Class<?>[] {}, new Object[] {});
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldBeConsistentBetweenNameAndBooleanValue() {

        for (int i = 0; i < VTBoolean.values().length; i++) {

            if (VTBoolean.TRUE.equals(VTBoolean.values()[i])) {
                assertTrue(VTBoolean.values()[i].booleanValue());
            } else {
                assertFalse(VTBoolean.values()[i].booleanValue());
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doCompareToTesting() {
        assertFalse(VTBoolean.FALSE.compareTo(VTBoolean.TRUE) == 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        assertFalse(VTBoolean.FALSE.equals(VTBoolean.TRUE));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doToStringTesting() {
        assertEquals(VTBoolean.FALSE.toString(), "FALSE");
        assertEquals(VTBoolean.TRUE.toString(), "TRUE");
        assertEquals(VTBoolean.UNKNOWN.toString(), "UNKNOWN");
    }

}

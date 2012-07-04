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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTIsoGender}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTIsoGenderTest extends AbstractEnumValueTypeTest {

    /**
     * Creates a new VTIsoGenderTest object.
     */
    public VTIsoGenderTest() {
        super(VTIsoGender.class, new Class<?>[] {}, new Object[] {});
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldReturnStandardISODigitCodes() {
        VTIsoGender e1 = null;
        VTIsoGender e2 = null;

        for (int i = 0; i < VTIsoGender.values().length; i++) {
            e1 = VTIsoGender.values()[i];
            e2 = VTIsoGender.getEnumValue(e1.getDigitCode());
            assertEquals(e2, e1);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doCompareToTesting() {
        assertFalse(VTIsoGender.MALE.compareTo(VTIsoGender.FEMALE) == 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        assertFalse(VTIsoGender.MALE.equals(VTIsoGender.FEMALE));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doToStringTesting() {
        assertEquals(VTIsoGender.MALE.toString(), "MALE");
        assertEquals(VTIsoGender.FEMALE.toString(), "FEMALE");
    }

}

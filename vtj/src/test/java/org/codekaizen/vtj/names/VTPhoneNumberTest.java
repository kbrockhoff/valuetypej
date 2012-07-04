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
package org.codekaizen.vtj.names;

import static org.testng.Assert.*;

import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTPhoneNumber}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTPhoneNumberTest extends AbstractNameTypeTest {

    /**
     * Creates a new VTPhoneNumberTest object.
     */
    public VTPhoneNumberTest() {
        super(VTPhoneNumber.class, new Class<?>[] { String.class, }, new Object[] { "+1 214-999-9999", });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getValidPatterns() {
        return new String[] {
                // North America
                "214-999-9999", "1-403-999-9999",
                // Europe
                "00 44 20 7 999-9999",
                // Japan
                "001~0041 81 468 9999",
                // Mexico
                "00 52 998-999-9999",
                // North America to Mexico
                "011-52-998-999-9999",
                // North America to Europe
                "011-33-562-999-9999",
                // North America to Japan
                "011-81-468-999-9999",
                // Japan to US
                "001~0041 1* 214-999-9999",
                // Japan to Europe
                "001~0041 44 20 7 999-9999",
                // Europe to US
                "00 1* 214-999-9999",
            };
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getInvalidPatterns() {
        return new String[] { "", "abc-def-hijk", };
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldRecognizeNorthAmericanPartsInAllCommonFormats() {
        VTPhoneNumber pn = new VTPhoneNumber("1-214-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("(214) 999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("(214) 999-9999 #184");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertEquals(pn.getExtension(), new VTString("184"));
        pn = new VTPhoneNumber("1-214-999-9999#184");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertEquals(pn.getExtension(), new VTString("184"));
        pn = new VTPhoneNumber("1-403-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("403"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("001~0041 1* 214-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("00 1* 214-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldRecognizeInternationalPartsInAllCommonFormats() {
        VTPhoneNumber pn = new VTPhoneNumber("011 81 805-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+81"));
        assertEquals(pn.getAreaCode(), new VTString("805"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("00 44 20 7 999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+44"));
        assertEquals(pn.getAreaCode(), new VTString("20"));
        assertEquals(pn.getLocalNumber(), new VTString("7-999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("001~0041 81 468 9999");
        assertEquals(pn.getCountryCode(), new VTString("+81"));
        assertEquals(pn.getAreaCode(), new VTString("468"));
        assertEquals(pn.getLocalNumber(), new VTString("9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("00 52 998-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+52"));
        assertEquals(pn.getAreaCode(), new VTString("998"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("011-52-998-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+52"));
        assertEquals(pn.getAreaCode(), new VTString("998"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("011-33-562-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+33"));
        assertEquals(pn.getAreaCode(), new VTString("562"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("011-81-468-999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+81"));
        assertEquals(pn.getAreaCode(), new VTString("468"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
        pn = new VTPhoneNumber("001~0041 44 20 7 999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+44"));
        assertEquals(pn.getAreaCode(), new VTString("20"));
        assertEquals(pn.getLocalNumber(), new VTString("7-999-9999"));
        assertNull(pn.getExtension());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldAssignNamePartsConstructorArgumentsToCorrectFields() {
        final VTPhoneNumber pn = new VTPhoneNumber("1", "214", "999-9999");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertNull(pn.getExtension());
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidLocalNumber() {
        final VTPhoneNumber pn = new VTPhoneNumber("1-214-999-999");
        assertNull(pn.getExtension());
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidAreaCode() {
        final VTPhoneNumber pn = new VTPhoneNumber("1-114-999-9999");
        assertNull(pn.getExtension());
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullConstructorArgument() {
        final VTPhoneNumber pn = new VTPhoneNumber(null);
        assertNull(pn.getExtension());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldAddNamePartsAsExpectedUsingFluentAPI() {
        VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        pn = pn.plus("184");
        assertEquals(pn.getCountryCode(), new VTString("+1"));
        assertEquals(pn.getAreaCode(), new VTString("214"));
        assertEquals(pn.getLocalNumber(), new VTString("999-9999"));
        assertEquals(pn.getExtension(), new VTString("184"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementStartsWithAsExpectedUsingFluentAPI() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        assertTrue(pn.startsWith("+1"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementEndsWithAsExpectedUsingFluentAPI() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        assertTrue(pn.endsWith("999-9999"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldReturnNumberOfNamePartsAsSize() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        assertEquals(pn.size(), 3);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementCharAtPerCharSequenceAPI() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        // added country code
        assertEquals(pn.charAt(0), '+');
        assertEquals(pn.charAt(4), '1');
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementLengthPerCharSequenceAPI() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        // +1- added
        assertEquals(pn.length(), 15);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementSubSequencePerCharSequenceAPI() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        // +1- added
        assertEquals(pn.subSequence(7, 10).toString(), "999");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldIterateOverNameParts() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        int idx = 0;

        for (final VTString string : pn) {

            switch (idx) {
            case 0:
                assertEquals(string, new VTString("214"));

                break;
            case 1:
                assertEquals(string, new VTString("999"));

                break;
            case 2:
                assertEquals(string, new VTString("9999"));

                break;
            }

            idx++;
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTPhoneNumber pn1 = new VTPhoneNumber("214-999-9998");
        VTPhoneNumber pn2 = new VTPhoneNumber("214-999-9998");
        assertTrue(pn1.compareTo(pn2) == 0);
        pn2 = new VTPhoneNumber("214-999-9999");
        assertTrue(pn1.compareTo(pn2) < 0);
        pn2 = new VTPhoneNumber("214-999-7999");
        assertTrue(pn1.compareTo(pn2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTPhoneNumber pn1 = new VTPhoneNumber("214-999-9998");
        final VTPhoneNumber pn2 = new VTPhoneNumber("214-999-9999");
        assertFalse(pn1.equals(pn2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTPhoneNumber pn = new VTPhoneNumber("214-999-9999");
        assertEquals(pn.toString(), "+1-214-999-9999");
    }

}

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
package org.codekaizen.vtj.text;

import static org.testng.Assert.*;

import java.io.IOException;
import org.codekaizen.vtj.AbstractValueTypeTest;
import org.testng.annotations.Test;


/**
 * <p>Units tests for {@link VTString}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTStringTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTStringTest object.
     */
    public VTStringTest() {
        super(VTString.class, new Class<?>[] { CharSequence.class, }, new Object[] { "Test String", });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTString() {
        VTString s = new VTString("Hello world!");
        assertEquals(s.toString(), "Hello world!");

        final StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append("world!");
        s = new VTString(sb);
        assertEquals(s.toString(), "Hello world!");

        final StringBuffer buf = new StringBuffer();
        s = new VTString(buf);
        assertEquals(s.toString(), "");

        try {
            s = new VTString(null);
            fail("should not have allowed a null parameter");
        } catch (IllegalArgumentException iae) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testPlus() {
        VTString s = new VTString("Hello world!");
        s = s.plus(new VTString(" Life")).plus(new VTString(" is")).plus(new VTString(" great!"));
        assertEquals(s.toString(), "Hello world! Life is great!");
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testCharAt() {
        final VTString s = new VTString("Hello world!");
        assertEquals(s.charAt(0), 'H');
        assertEquals(s.charAt(6), 'w');
        assertEquals(s.charAt(11), '!');

        try {
            s.charAt(12);
            fail("should have thrown index out of bounds");
        } catch (IndexOutOfBoundsException e) {
            // so nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testLength() {
        VTString s = new VTString("Hello world!");
        assertEquals(s.length(), 12);
        s = new VTString("");
        assertEquals(s.length(), 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testSubSequence() {
        final VTString s1 = new VTString("Hello world!");
        final VTString s2 = (VTString) s1.subSequence(6, 11);
        assertEquals(s2.toString(), "world");
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test
    public void testAppendChar() throws IOException {
        final VTString s = (VTString) new VTString("Hello").append(' ').append('w').append('o').append('r').append('l')
                .append('d').append('!');
        assertEquals(s.toString(), "Hello world!");
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test
    public void testAppendSequence() throws IOException {
        final VTString s = (VTString) new VTString("Hello").append(" world!").append(new VTString(" Life")).append(
                new StringBuffer(" is")).append(new StringBuilder(" great!"));
        assertEquals(s.toString(), "Hello world! Life is great!");
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test
    public void testAppendSubSequence() throws IOException {
        VTString s = new VTString("Hello");
        s = (VTString) s.append("Hello world!", 5, 12);
        assertEquals(s.toString(), "Hello world!");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        VTString s1 = new VTString("Hello world!");
        VTString s2 = new VTString("Hello world!");
        assertTrue(s1.compareTo(s2) == 0);
        s1 = new VTString("Hello world");
        assertTrue(s1.compareTo(s2) < 0);
        s2 = new VTString("Hello");
        assertTrue(s1.compareTo(s2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTString s1 = new VTString("Hello world");
        final VTString s2 = new VTString("Hello world!");
        assertFalse(s1.equals(s2));
        assertFalse(s1.equals("Hello world"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTString s = new VTString("Hello world!");
        assertEquals(s.toString(), "Hello world!");
    }

}

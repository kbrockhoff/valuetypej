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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTJavaName}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTJavaNameTest extends AbstractNameTypeTest {

    /**
     * Creates a new VTJavaNameTest object.
     */
    public VTJavaNameTest() {
        super(VTJavaName.class, new Class<?>[] { String.class, },
            new Object[] { "org.codekaizen.vtj.names.VTJavaName", });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getValidPatterns() {
        return new String[] { "org.codekaizen.vtj", "codeKaizen", "org.codekaizen.vtj.names.VTName", };
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getInvalidPatterns() {
        return new String[] { "", "org/codekaizen/vtj", "0codeKaizen", "org.codekaizen.vtj.names.VT@Name", };
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldConstructSuccessfullyForValidJavaName() {
        String s = null;
        VTJavaName nme = null;
        s = "org.codekaizen.vtj";
        nme = new VTJavaName(s);
        assertEquals(nme.getFullName().toString(), s);
        s = "Main";
        nme = new VTJavaName(s);
        assertEquals(nme.getFullName().toString(), s);
        s = "org.codekaizen.vtj.names.VTName.pattern";
        nme = new VTJavaName(s);
        assertEquals(nme.getFullName().toString(), s);
        s = "org.codekaizen.vtj.more_stuff.MegaClass";
        nme = new VTJavaName(s);
        assertEquals(nme.getFullName().toString(), s);
        s = "jndi.properties";
        nme = new VTJavaName(s);
        assertEquals(nme.getFullName().toString(), s);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullConstructorString() {
        new VTJavaName(null);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForBlankConstructorString() {
        new VTJavaName("");
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidJavaNameConstructorString() {
        new VTJavaName("org.codekaizen.vtj.VT#equals");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldAddNamePartsAsExpectedUsingFluentAPI() {
        VTJavaName nme = new VTJavaName("org.codekaizen.vtj");
        nme = nme.plus(".names").plus("VTJavaName");
        assertEquals(nme.toString(), "org.codekaizen.vtj.names.VTJavaName");
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddingNullNamePart() {
        VTJavaName nme = new VTJavaName("org.codekaizen.vtj");
        nme = nme.plus(".names").plus(null);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementStartsWithAsExpectedUsingFluentAPI() {
        final VTJavaName nme = new VTJavaName("org.codekaizen.vtj");
        assertTrue(nme.startsWith("org"));
        assertFalse(nme.startsWith("org."));
        assertFalse(nme.startsWith(null));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementEndsWithAsExpectedUsingFluentAPI() {
        final VTJavaName nme = new VTJavaName("org.codekaizen.vtj");
        assertTrue(nme.endsWith("vtj"));
        assertFalse(nme.endsWith(".vtj"));
        assertFalse(nme.endsWith(null));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldReturnNumberOfNamePartsAsSize() {
        VTJavaName nme = new VTJavaName("org.codekaizen.vtj");
        assertEquals(nme.size(), 3);
        nme = nme.plus(".names").plus("VTJavaName");
        assertEquals(nme.size(), 5);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementCharAtPerCharSequenceAPI() {
        final String s = "org.codekaizen.vtj.names.VTJavaName";
        final VTJavaName nme = new VTJavaName(s);

        for (int i = 0; i < s.length(); i++) {
            assertEquals(nme.charAt(i), s.charAt(i));
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementLengthPerCharSequenceAPI() {
        final String s = "org.codekaizen.vtj.names.VTJavaName";
        final VTJavaName nme = new VTJavaName(s);
        assertEquals(nme.length(), s.length());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementSubSequencePerCharSequenceAPI() {
        final String s = "org.codekaizen.vtj.names.VTJavaName";
        final VTJavaName nme = new VTJavaName(s);
        assertEquals(nme.subSequence(4, 8).toString(), s.subSequence(4, 8).toString());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldIterateOverNameParts() {
        final List<VTString> list = new ArrayList<VTString>();
        list.add(new VTString("org"));
        list.add(new VTString("codekaizen"));
        list.add(new VTString("vtj"));
        list.add(new VTString("names"));
        list.add(new VTString("VTJavaName"));

        VTJavaName nme = null;

        for (final VTString string : list) {

            if (nme == null) {
                nme = new VTJavaName(string.toString());
            } else {
                nme = nme.plus(string);
            }
        }

        final Iterator<VTString> it = list.iterator();

        for (final VTString s : nme) {
            assertEquals(s, it.next());
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTJavaName nme1 = new VTJavaName("org.codekaizen.vtj.names.VTJavaName");
        VTJavaName nme2 = new VTJavaName("org.codekaizen.vtj.names.VTJavaName");
        assertTrue(nme1.compareTo(nme2) == 0);
        nme2 = new VTJavaName("org.codekaizen.vtj.names.VTJavaNameTest");
        assertTrue(nme1.compareTo(nme2) < 0);
        nme2 = new VTJavaName("org.codekaizen.vtj.VT");
        assertTrue(nme1.compareTo(nme2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTJavaName nme1 = new VTJavaName("org.codekaizen.vtj.names.VTJavaName");
        final VTJavaName nme2 = new VTJavaName("org.codekaizen.vtj.names.VTJavaNameTest");
        assertFalse(nme1.equals(nme2));
        assertFalse(nme1.equals("org.codekaizen.vtj.names.VTJavaName"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final String s = "org.codekaizen.vtj.names.VTJavaName";
        final VTJavaName nme = new VTJavaName(s);
        assertEquals(nme.toString(), s);
    }

}

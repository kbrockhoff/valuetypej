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
package org.codekaizen.vtj.net;

import static org.testng.Assert.*;

import org.codekaizen.vtj.names.AbstractNameTypeTest;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTURN}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTURNTest extends AbstractNameTypeTest {

    /**
     * Creates a new VTURNTest object.
     */
    public VTURNTest() {
        super(VTURN.class, new Class<?>[] { String.class, }, new Object[] { "urn:isbn:0-395-36341-1", });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getValidPatterns() {
        return new String[] {
                "urn:isbn:0451450523", "urn:isan:0000-0000-9E59-0000-O-0000-0000-2", "urn:issn:0167-6423",
                "urn:ietf:rfc:2648", "urn:mpeg:mpeg7:schema:2001", "urn:oid:2.16.840",
                "urn:uuid:6e8bc430-9c3a-11d9-9669-0800200c9a66", "urn:uci:I001+SBSi-B10000083052",
                "urn:www.agxml.org:schemas:all:2:0", "urn:sha1:YNCKHTQCWBTRNJIV4WNAE52SJUQCZO5C",
                "urn:tree:tiger:BL5OM7M75DWHAXMFZFJ23MU3LVMRXKFO6HTGUTY",
                "urn:sici:1046-8188(199501)13:1%3C69:FTTHBI%3E2.0.TX;2-4",
            };
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getInvalidPatterns() {
        return new String[] { "", };
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldSucceedIfSuppliedValidURNAsConstructorArgument() {
        final VTURN uri = new VTURN("urn:uuid:713a5e6f-65e3-4f46-9c0e-677c624deb0f");
        assertEquals(uri.getScheme(), new VTString("urn"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldCorrectlyIdentifySubSchema() {
        final VTURN uri = new VTURN("urn:uuid:713a5e6f-65e3-4f46-9c0e-677c624deb0f");
        assertEquals(uri.getSubScheme(), new VTString("uuid"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldAddNamePartsAsExpectedUsingFluentAPI() {
        VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        uri = (VTURN) uri.plus("?pg=234");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementStartsWithAsExpectedUsingFluentAPI() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        assertTrue(uri.startsWith("urn"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementEndsWithAsExpectedUsingFluentAPI() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        assertTrue(uri.endsWith("isbn:0-395-36341-1"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldReturnNumberOfNamePartsAsSize() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        assertEquals(uri.size(), 2);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementCharAtPerCharSequenceAPI() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        assertEquals(uri.charAt(0), 'u');
        assertEquals(uri.charAt(10), '-');
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementLengthPerCharSequenceAPI() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        assertEquals(uri.length(), 22);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementSubSequencePerCharSequenceAPI() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        assertEquals(uri.subSequence(9, 22), new VTString("0-395-36341-1"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldIterateOverNameParts() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");

        for (final VTString part : uri) {
            assertTrue(part.length() > 0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTURN uri1 = new VTURN("urn:isbn:0-395-36341-1");
        VTURN uri2 = new VTURN("urn:isbn:0-395-36341-1");
        assertTrue(uri1.compareTo(uri2) == 0);
        uri2 = new VTURN("urn:isbn:0-395-36342-1");
        assertTrue(uri1.compareTo(uri2) < 0);
        uri2 = new VTURN("urn:isbn:0-395-26341-1");
        assertTrue(uri1.compareTo(uri2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTURN uri1 = new VTURN("urn:isbn:0-395-36341-1");
        final VTURN uri2 = new VTURN("urn:isbn:0-395-36341-2");
        assertFalse(uri1.equals(uri2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTURN uri = new VTURN("urn:isbn:0-395-36341-1");
        assertEquals(uri.toString(), "urn:isbn:0-395-36341-1");
    }

}

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

import java.io.IOException;
import org.codekaizen.vtj.names.AbstractNameTypeTest;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTURL}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTURLTest extends AbstractNameTypeTest {

    /**
     * Creates a new VTURLTest object.
     */
    public VTURLTest() {
        super(VTURL.class, new Class<?>[] { String.class, },
            new Object[] { "http://www.codekaizen.org/valuetypej/vtj/", });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getValidPatterns() {
        return new String[] {
                "https://dev.codekaizen.org:7777/", "ftp://downloads.codekaizen.org:/public/vtj-1.2.1-bin.zip",
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
    public void shouldSuccessIfSuppliedValidURLAsConstructorArgument() {
        final VTURL url = new VTURL("https://dev.codekaizen.org:7777/");
        assertEquals(url.getScheme(), new VTString("https"));
        assertEquals(url.getSchemeSpecificPart(), new VTString("//dev.codekaizen.org:7777/"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldParseProtocolCorrectly() {
        final VTURL url = new VTURL("https://dev.codekaizen.org:7777/");
        assertEquals(url.getProtocol(), new VTString("https"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldReturnCorrectDefaultPortIfDifferentPortInURL() {
        final VTURL url = new VTURL("https://dev.codekaizen.org:7777/");
        assertEquals(url.getDefaultPort(), new VTString("443"));
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test(expectedExceptions = IOException.class)
    public void shouldThrowExceptionIfOpeningStreamOnNoExistentURL() throws IOException {
        final VTURL url = new VTURL("https://dev.codekaizen.org:7777/");
        url.openStream();
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldAddNamePartsAsExpectedUsingFluentAPI() {
        VTURL uri = new VTURL("https://dev.codekaizen.org:7777/");
        uri = (VTURL) uri.plus("/valuetypej/vtj").plus("?reportstyle=status");
        assertEquals(uri.getPath(), new VTString("/valuetypej/"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementStartsWithAsExpectedUsingFluentAPI() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertTrue(uri.startsWith("http"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementEndsWithAsExpectedUsingFluentAPI() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertTrue(uri.endsWith("/valuetypej/vtj/"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldReturnNumberOfNamePartsAsSize() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.size(), 3);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementCharAtPerCharSequenceAPI() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.charAt(0), 'h');
        assertEquals(uri.charAt(10), '.');
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementLengthPerCharSequenceAPI() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.length(), 41);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementSubSequencePerCharSequenceAPI() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.subSequence(25, 41), new VTString("/valuetypej/vtj/"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldIterateOverNameParts() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");

        for (final VTString part : uri) {
            assertTrue(part.length() > 0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTURL uri1 = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        VTURL uri2 = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertTrue(uri1.compareTo(uri2) == 0);
        uri2 = new VTURL("http://www.codekaizen.org/valuetypej/vtj/docs/");
        assertTrue(uri1.compareTo(uri2) < 0);
        uri2 = new VTURL("http://www.codekaizen.org/valuetypej/");
        assertTrue(uri1.compareTo(uri2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTURL uri1 = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        final VTURL uri2 = new VTURL("http://www.codekaizen.org/valuetypej/");
        assertFalse(uri1.equals(uri2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTURL uri = new VTURL("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.toString(), "http://www.codekaizen.org/valuetypej/vtj/");
    }

}

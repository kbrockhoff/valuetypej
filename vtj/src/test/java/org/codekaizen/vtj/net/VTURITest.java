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
 * <p>Unit tests for {@link VTURI}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTURITest extends AbstractNameTypeTest {

    /**
     * Creates a new VTURITest object.
     */
    public VTURITest() {
        super(VTURI.class, new Class<?>[] { String.class, },
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
                "https://dev.codekaizen.org/", "urn:isbn:0-395-36341-1", "mailto:kbrockhoff@codekaizen.org",
                "http://www.codekaizen.org:8080/projectlist?projectid=18", "http://www.codekaizen.org/projectlist#vtj",
                "scm:svn:http://codekaizen.svnrepository.com/svn/valuetypej", "scm:cvs:pserver:kbrockhoff@scm:/cvsroot",
                "scp:anonymous@dev.codekaizen.org:projects", "tag:kbrockhoff@codekaizen.org:blog:200706:post-01#graph1",
                "urn:uuid:713a5e6f-65e3-4f46-9c0e-677c624deb0f", "tel:+1-214-999-9999",
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
    public void shouldSucceedIfSuppliedValidStringConstructorArgument() {
        final VTURI uri = new VTURI("https://dev.codekaizen.org/");
        assertEquals(uri.getScheme(), new VTString("https"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidURIConstructorArgument() {
        final VTURI uri = new VTURI("26.now://dev.codekaizen.org/");
        assertEquals(uri.getScheme(), new VTString("26.now"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldParseURIIntoCorrectParts() {
        final VTURI uri = new VTURI("https://dev.codekaizen.org:7777/valuetypej/vtj" + "?reportstyle=status#summary");
        assertEquals(uri.getScheme(), new VTString("https"));
        assertEquals(uri.getHost(), new VTString("dev.codekaizen.org"));
        assertEquals(uri.getPort(), new VTString("7777"));
        assertEquals(uri.getPath(), new VTString("/valuetypej/vtj"));
        assertEquals(uri.getQuery(), new VTString("reportstyle=status"));
        assertEquals(uri.getFragment(), new VTString("summary"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldAddNamePartsAsExpectedUsingFluentAPI() {
        VTURI uri = new VTURI("https://dev.codekaizen.org:7777/");
        uri = uri.plus("/valuetypej/vtj").plus("?reportstyle=status");
        assertEquals(uri.getPath(), new VTString("/valuetypej/"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldRemoveRelativePathsWhenWhenAdding() {
        final VTURI uri = new VTURI("file:/vtj/org/codekaizen/vtj/net/VTURI.java");
        final VTURI part = new VTURI("../names/VTName.java");
        final VTURI result = uri.plus(part);
        assertEquals("file:/vtj/org/codekaizen/vtj/names/VTName.java", result.toString());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementStartsWithAsExpectedUsingFluentAPI() {
        final VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertTrue(uri.startsWith("http"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementEndsWithAsExpectedUsingFluentAPI() {
        final VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertTrue(uri.endsWith("/valuetypej/vtj/"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldReturnNumberOfNamePartsAsSize() {
        final VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.size(), 3);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementCharAtPerCharSequenceAPI() {
        final VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.charAt(0), 'h');
        assertEquals(uri.charAt(10), '.');
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementLengthPerCharSequenceAPI() {
        final VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.length(), 41);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementSubSequencePerCharSequenceAPI() {
        final VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.subSequence(25, 41), new VTString("/valuetypej/vtj/"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldIterateOverNameParts() {
        final VTURI uri = new VTURI("tag:kbrockhoff@codekaizen.org:blog:200706:post-01#graph1");

        for (final VTString string : uri) {
            assertTrue(string.length() > 0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTURI uri1 = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        VTURI uri2 = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertTrue(uri1.compareTo(uri2) == 0);
        uri2 = new VTURI("http://www.codekaizen.org/valuetypej/vtj/docs/");
        assertTrue(uri1.compareTo(uri2) < 0);
        uri2 = new VTURI("http://www.codekaizen.org/valuetypej/");
        assertTrue(uri1.compareTo(uri2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTURI uri1 = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        final VTURI uri2 = new VTURI("http://www.codekaizen.org/valuetypej/");
        assertFalse(uri1.equals(uri2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/vtj/");
        assertEquals(uri.toString(), "http://www.codekaizen.org/valuetypej/vtj/");
        uri = new VTURI("tag:kbrockhoff@codekaizen.org:blog:200706:post-01#graph1");
        assertEquals(uri.toString(), "tag:kbrockhoff@codekaizen.org:blog:200706:post-01#graph1");
    }

}

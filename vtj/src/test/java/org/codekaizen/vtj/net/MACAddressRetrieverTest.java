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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link MACAddressRetriever}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class MACAddressRetrieverTest {

    /**
     * Creates a new MACAddressRetrieverTest object.
     */
    public MACAddressRetrieverTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testColonRegEx() {
        final MACAddressRetriever retriever = new MACAddressRetriever();
        String s = retriever.parseLine(" 00:13:72:3F:30:04");
        assertNotNull(s);
        assertEquals(s.charAt(0), '0');
        assertEquals(s.length(), 17);
        s = retriever.parseLine(" 00:13:72:3F:30:04 ");
        assertNotNull(s);
        s = retriever.parseLine(" 00:13:72:3F:30:04:99:99");
        assertNull(s);
        s = retriever.parseLine("eth0      Link encap:Ethernet  HWaddr 00:13:72:3F:30:04");
        assertNotNull(s);
        s = retriever.parseLine("ether 8:0:27:db:a8:df");
        assertNotNull(s);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testDashRegEx() {
        final MACAddressRetriever retriever = new MACAddressRetriever();
        String s = retriever.parseLine(" 00-13-72-3F-30-04");
        assertNotNull(s);
        assertEquals(s.charAt(0), '0');
        assertEquals(s.length(), 17);
        s = retriever.parseLine(" 00-13-72-3F-30-04 ");
        assertNotNull(s);
        s = retriever.parseLine(" 00-13-72-3F-30-04-99-99");
        assertNull(s);
        s = retriever.parseLine("        Physical Address. . . . . . . . . : 00-06-5B-EB-6E-9E");
        assertNotNull(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test
    public void testWindowsParseOutput() throws IOException {
        final int cnt = this.checkParsing(MACAddressRetrieverTest.class.getResourceAsStream(
                    "winxp_ipconfig_output.txt"));
        assertEquals(cnt, 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test
    public void testLinuxParseOutput() throws IOException {
        final int cnt = this.checkParsing(MACAddressRetrieverTest.class.getResourceAsStream(
                    "ubuntu7_ifconfig_output.txt"));
        assertEquals(cnt, 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test
    public void testMacOSXParseOutput() throws IOException {
        final int cnt = this.checkParsing(MACAddressRetrieverTest.class.getResourceAsStream(
                    "osx5_ifconfig_output.txt"));
        assertEquals(cnt, 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Test
    public void testOpenSolarisParseOutput() throws IOException {
        final int cnt = this.checkParsing(MACAddressRetrieverTest.class.getResourceAsStream(
                    "sunos200805_ifconfig_output.txt"));
        assertEquals(cnt, 1);
    }

    private int checkParsing(final InputStream is) throws IOException {
        final MACAddressRetriever retriever = new MACAddressRetriever();
        final BufferedReader r = new BufferedReader(new InputStreamReader(is));
        final Set<String> set = new HashSet<String>();
        retriever.parseOutput(set, r);

        return set.size();
    }

}

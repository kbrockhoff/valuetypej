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

import java.util.Arrays;
import org.codekaizen.vtj.AbstractValueTypeTest;
import org.codekaizen.vtj.util.ByteArrayUtils;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTMACAddress}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTMACAddressTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTMACAddressTest object.
     */
    public VTMACAddressTest() {
        super(VTMACAddress.class, new Class<?>[] { byte[].class, },
            new Object[] { ByteArrayUtils.toBytes("00C0F03D5B7C"), });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTEthernetAddressByte() {
        final VTMACAddress addr1 = new VTMACAddress(ByteArrayUtils.toBytes("00-06-5B-EB-6E-9E", '-'));
        VTMACAddress addr2 = new VTMACAddress(ByteArrayUtils.toBytes("00065beb6e9e"));
        assertEquals(addr1, addr2);

        addr2 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:9E", ':'));
        assertEquals(addr1, addr2);

        addr2 = new VTMACAddress(ByteArrayUtils.toBytes("0:6:5B:EB:6E:9E", ':'));
        assertEquals(addr1, addr2);

        final byte[] b = new byte[6];
        b[0] = 0x00;
        b[1] = 0x06;
        b[2] = 0x5B;
        b[3] = (byte) 0xEB;
        b[4] = 0x6E;
        b[5] = (byte) 0x9E;
        addr2 = new VTMACAddress(b);
        assertEquals(addr1, addr2);
        // check conversion back
        assertTrue(Arrays.equals(addr1.toByteArray(), b));
    }

    /**
     * DOCUMENT ME!
     */
    public void testVTEthernetAddressTooBytes() {
        final byte[] b = new byte[6];
        b[0] = 0x64;
        b[1] = 0x06;
        b[2] = 0x5B;
        b[3] = (byte) 0xEB;
        b[4] = 0x6E;
        b[5] = (byte) 0x9E;

        final VTMACAddress addr = new VTMACAddress(b);
        assertEquals(addr.toByteArray()[0], b[0]);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTEthernetAddressLong() {
        final VTMACAddress addr1 = new VTMACAddress(ByteArrayUtils.toBytes("00-06-5B-EB-6E-9E", '-'));
        final long l = Long.parseLong("00065beb6e9e", 16);
        final VTMACAddress addr2 = new VTMACAddress(l);
        assertEquals(addr1, addr2);
        // check conversion back
        assertEquals(addr2.toLong(), l);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTMACAddress addr1 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:9E", ':'));
        VTMACAddress addr2 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:9E", ':'));
        assertTrue(addr1.compareTo(addr2) == 0);
        addr2 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:AE", ':'));
        assertTrue(addr1.compareTo(addr2) < 0);
        addr2 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:3E", ':'));
        assertTrue(addr1.compareTo(addr2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTMACAddress addr1 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:9E", ':'));
        final VTMACAddress addr2 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:AE", ':'));
        assertFalse(addr1.equals(addr2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTMACAddress addr1 = new VTMACAddress(ByteArrayUtils.toBytes("00:06:5B:EB:6E:9E", ':'));
        assertEquals(addr1.toString(), "00:06:5b:eb:6e:9e");
    }

}

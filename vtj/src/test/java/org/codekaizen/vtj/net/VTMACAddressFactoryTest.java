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

import java.util.Set;
import org.codekaizen.vtj.AbstractValueTypeFactoryTest;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTMACAddressFactory}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTMACAddressFactoryTest extends AbstractValueTypeFactoryTest {

    /**
     * Creates a new VTMACAddressFactoryTest object.
     */
    public VTMACAddressFactoryTest() {
        super(VTMACAddressFactory.class);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsCreatable() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        assertTrue(factory.isCreatable(VTMACAddress.class));
        assertFalse(factory.isCreatable(null));
        assertFalse(factory.isCreatable(VTString.class));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestCreateObjectArray() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        final byte[] b = new byte[6];
        b[0] = 0x00;
        b[1] = 0x06;
        b[2] = 0x5B;
        b[3] = (byte) 0xEB;
        b[4] = 0x6E;
        b[5] = (byte) 0x9E;

        final VTMACAddress addr1 = factory.create(b);
        VTMACAddress addr2 = factory.create(b);
        assertEquals(addr2, addr1);
        addr2 = factory.create("00:06:5B:EB:6E:9E");
        assertEquals(addr2, addr1);
        addr2 = factory.create("00-06-5B-EB-6E-9E");
        assertEquals(addr2, addr1);
        addr2 = factory.create("0:6:5B:EB:6E:9E");
        assertEquals(addr2, addr1);
        addr2 = factory.create(new VTString("00065beb6e9e"));
        assertEquals(addr2, addr1);

        final Long l = Long.valueOf("00065beb6e9e", 16);
        addr2 = factory.create(l);
        assertEquals(addr2, addr1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateExtraByteObjectArray() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        final byte[] b = new byte[8];
        b[0] = 0x33;
        b[1] = 0x06;
        b[2] = 0x5B;
        b[3] = (byte) 0xEB;
        b[4] = 0x6E;
        b[5] = (byte) 0x9E;
        b[6] = (byte) 0x9E;
        b[7] = (byte) 0x9E;

        final VTMACAddress addr = factory.create(b);
        assertEquals(addr.copy(), addr);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateInvalidStringObjectArray() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        final VTMACAddress addr = factory.create("A test string");
        assertEquals(addr.copy(), addr);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsParsable() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        assertFalse(factory.isParsable(null));
        assertFalse(factory.isParsable(""));
        assertFalse(factory.isParsable("A test string"));
        assertTrue(factory.isParsable("00-06-5B-EB-6E-9E"));
        assertTrue(factory.isParsable("00065beb6e9e"));
        assertTrue(factory.isParsable("00:06:5B:EB:6E:9E"));
        assertFalse(factory.isParsable("44:06:5B:EB:6E:9E"));
        assertFalse(factory.isParsable("00:06:5B"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestParseString() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        VTMACAddress addr = factory.parse("00-06-5B-EB-6E-9E");
        assertEquals(addr.toByteArray()[0], (byte) 0);
        addr = factory.parse("00065beb6e9e");
        assertEquals(addr.toByteArray()[0], (byte) 0);
        addr = factory.parse("00:06:5B:EB:6E:9E");
        assertEquals(addr.toByteArray()[0], (byte) 0);
        addr = factory.parse("0:6:5B:EB:6E:9E");
        assertEquals(addr.toByteArray()[0], (byte) 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseInvalidString() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        final VTMACAddress addr = factory.parse("00-06-5B-");
        assertEquals(addr.toByteArray()[0], (byte) 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestFormatValueType() {
        final VTMACAddressFactory factory = (VTMACAddressFactory) createFactory();
        final String s = "00:ff:ff:ff:ff:ff";
        final VTMACAddress addr = factory.parse(s);
        assertEquals(s, factory.format(addr));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetAllLocalEthernetAddresses() {
        int cnt = 0;

        for (final VTMACAddress addr : VTMACAddressFactory.getAllLocalEthernetAddresses()) {
            assertEquals(addr.toByteArray()[0], (byte) 0);
            cnt++;
        }

        assertTrue(cnt > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetLocalEthernetAddress() {
        final Set<VTMACAddress> addrs = VTMACAddressFactory.getAllLocalEthernetAddresses();
        VTMACAddress prvAddr = null;
        VTMACAddress curAddr = null;

        for (int i = 0; i < addrs.size(); i++) {
            curAddr = VTMACAddressFactory.getLocalEthernetAddress();
            assertFalse(curAddr.equals(prvAddr));
            prvAddr = curAddr;
        }
    }

}

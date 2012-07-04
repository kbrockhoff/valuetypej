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
package org.codekaizen.vtj.ids;

import static org.testng.Assert.*;

import java.math.BigInteger;
import org.codekaizen.vtj.AbstractValueTypeTest;
import org.codekaizen.vtj.util.ByteArrayUtils;
import org.testng.annotations.Test;


/**
 * <p>Units tests for {@link VTUUID}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTUUIDTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTUUIDTest object.
     */
    public VTUUIDTest() {
        super(VTUUID.class, new Class<?>[] { Long.TYPE, Long.TYPE, },
            new Object[] { 0x8A2C084DBE5F426FL, 0xAB02E56FC73DCA80L, });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldSuccessfulConstructFromByteArray() {
        final byte[] raw = ByteArrayUtils.toBytes("19D9BA1F5B1D4DFC83988980E49B3172");
        final VTUUID uuid = new VTUUID(raw);
        assertEquals(uuid.toByteArray().length, 16);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForUnknownVariantByte() {
        final byte[] raw = ByteArrayUtils.toBytes("19D9BA1F5B1DFFFF83988980E49B3172");
        new VTUUID(raw);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldSuccessfullyConstructFromTwoLongs() {
        VTUUID uuid = null;
        final long m = Long.parseLong("7690EB0462AA47EF", 16);
        final long l = Long.parseLong("7EB8807A40D36299", 16);
        uuid = new VTUUID(m, l);
        assertEquals(m, uuid.getMostSignificantBits());
        assertEquals(l, uuid.getLeastSignificantBits());
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForUnknownVariantWithinLong() {
        new VTUUID(0x19D9BA1F5B1DFFFFL, 0x83988980E49B3172L);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldDecodeVersionAndVariantPerSpec() {
        VTUUID uuid1 = new VTUUID(ByteArrayUtils.toBytes("d35c0594-8cef-4232-8fc8-f12082d67596", '-'));
        assertEquals(VTUUID.VARIANT_RFC4122, uuid1.variant());
        assertEquals(UUIDVersion.RANDOM, uuid1.version());

        uuid1 = new VTUUID(ByteArrayUtils.toBytes("3a0c2a50-c3f4-11d5-902a-00104bfec338", '-'));
        assertEquals(VTUUID.VARIANT_RFC4122, uuid1.variant());
        assertEquals(UUIDVersion.TIME_SPACE, uuid1.version());

        uuid1 = new VTUUID(ByteArrayUtils.toBytes("7fd41633-5f7e-3229-b229-93a67c97c523", '-'));
        assertEquals(VTUUID.VARIANT_RFC4122, uuid1.variant());
        assertEquals(UUIDVersion.NAME_MD5, uuid1.version());

        // IUnknown GUID
        uuid1 = new VTUUID(ByteArrayUtils.toBytes("00000300-0000-0000-C000-000000000046", '-'));
        assertEquals(VTUUID.VARIANT_MICROSOFT, uuid1.variant());

        uuid1 = new VTUUID(ByteArrayUtils.toBytes("d63f028a-864c-4ccf-81c2-77301a9cf41d", '-'));
        assertEquals(VTUUID.VARIANT_RFC4122, uuid1.variant());
        assertEquals(UUIDVersion.RANDOM, uuid1.version());

        uuid1 = new VTUUID(ByteArrayUtils.toBytes("fed27ca6-bdc0-11db-9317-009027861254", '-'));
        assertEquals(VTUUID.VARIANT_RFC4122, uuid1.variant());
        assertEquals(UUIDVersion.TIME_SPACE, uuid1.version());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldOnlyReturnTimestampIfTimeVersion() {
        VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("1cef0eca-3728-11dd-af02-0013723f3004", '-'));
        assertEquals(uuid.timestamp(), 0x11DD37281CEF0ECAL & 0x0FFFFFFFFFFFFFFFL);

        try {
            uuid = new VTUUID(ByteArrayUtils.toBytes("d35c0594-8cef-4232-8fc8-f12082d67596", '-'));
            uuid.timestamp();
            fail("random-number-based version should fail");
        } catch (UnsupportedOperationException uoe) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldOnlyReturnClockSequenceIfTimeVersion() {
        VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("1cef0eca-3728-11dd-af02-0013723f3004", '-'));
        // 2 most significant bits for IEFT variant
        assertEquals(uuid.clockSequence(), (0xAF02 & 0x3FFF));

        try {
            uuid = new VTUUID(ByteArrayUtils.toBytes("d35c0594-8cef-4232-8fc8-f12082d67596", '-'));
            uuid.clockSequence();
            fail("random-number-based version should fail");
        } catch (UnsupportedOperationException uoe) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldOnlyReturnNodeIfTimeVersion() {
        VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("1cef0eca-3728-11dd-af02-0013723f3004", '-'));
        assertEquals(uuid.node(), 0x0013723F3004L);

        try {
            uuid = new VTUUID(ByteArrayUtils.toBytes("d35c0594-8cef-4232-8fc8-f12082d67596", '-'));
            uuid.node();
            fail("random-number-based version should fail");
        } catch (UnsupportedOperationException uoe) {
            // do nothing
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTUUID uuid1 = new VTUUID(ByteArrayUtils.toBytes("c758df94-3746-11dd-b5dd-0013723f3004", '-'));
        VTUUID uuid2 = new VTUUID(ByteArrayUtils.toBytes("8F726A1A-15CE-4452-80FB-B59A1F8AA8F7", '-'));
        assertTrue(uuid1.compareTo(uuid2) > 0);
        uuid2 = new VTUUID(ByteArrayUtils.toBytes("c758df96-3746-11dd-b5dd-0013723f3004", '-'));
        assertTrue(uuid1.compareTo(uuid2) < 0);
        uuid2 = new VTUUID(ByteArrayUtils.toBytes("c758df94-3746-11dd-b5dd-0013723f3004", '-'));
        assertTrue(uuid1.compareTo(uuid2) == 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTUUID uuid1 = new VTUUID(ByteArrayUtils.toBytes("c758df94-3746-11dd-b5dd-0013723f3004", '-'));
        final VTUUID uuid2 = new VTUUID(ByteArrayUtils.toBytes("8F726A1A-15CE-4452-80FB-B59A1F8AA8F7", '-'));
        assertFalse(uuid1.equals(uuid2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("31F4ADC0-64AA-41CA-9610-E8850CFCF49A", '-'));
        assertEquals(uuid.toString(), "31f4adc0-64aa-41ca-9610-e8850cfcf49a");
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldAccuratelyConvertToBase16NumberString() {
        final VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("32F2537A-BB79-4988-9EE2-6F6F83009D81", '-'));
        assertEquals(uuid.toBase16(), "32f2537abb7949889ee26f6f83009d81");
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldAccuratelyConvertToByteArray() {
        final VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("C6AC3927-89C3-4E46-A084-DDE848DF9E2F", '-'));
        final byte[] b = uuid.toByteArray();
        assertEquals(Byte.valueOf(b[0]).byteValue(), (byte) 0xC6);
        assertEquals(Byte.valueOf(b[1]).byteValue(), (byte) 0xAC);
        assertEquals(Byte.valueOf(b[2]).byteValue(), (byte) 0x39);
        assertEquals(Byte.valueOf(b[3]).byteValue(), (byte) 0x27);
        assertEquals(Byte.valueOf(b[4]).byteValue(), (byte) 0x89);
        assertEquals(Byte.valueOf(b[5]).byteValue(), (byte) 0xC3);
        assertEquals(Byte.valueOf(b[6]).byteValue(), (byte) 0x4E);
        assertEquals(Byte.valueOf(b[7]).byteValue(), (byte) 0x46);
        assertEquals(Byte.valueOf(b[8]).byteValue(), (byte) 0xA0);
        assertEquals(Byte.valueOf(b[9]).byteValue(), (byte) 0x84);
        assertEquals(Byte.valueOf(b[10]).byteValue(), (byte) 0xDD);
        assertEquals(Byte.valueOf(b[11]).byteValue(), (byte) 0xE8);
        assertEquals(Byte.valueOf(b[12]).byteValue(), (byte) 0x48);
        assertEquals(Byte.valueOf(b[13]).byteValue(), (byte) 0xDF);
        assertEquals(Byte.valueOf(b[14]).byteValue(), (byte) 0x9E);
        assertEquals(Byte.valueOf(b[15]).byteValue(), (byte) 0x2F);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldAccuratelyConvertToBigInteger() {
        final VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("194322d4-3746-11dd-8c65-0013723f3004", '-'));
        final BigInteger bi = new BigInteger("194322d4374611dd8c650013723f3004", 16);
        assertEquals(uuid.toBigInteger(), bi);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldCorrectlyFormatWindowsRegistryDisplayFormat() {
        final VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes("194322d4-3746-11dd-8c65-0013723f3004", '-'));
        assertEquals(uuid.getRegistryFormat(), "{194322d4-3746-11dd-8c65-0013723f3004}");
    }

}

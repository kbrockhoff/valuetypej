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
package org.codekaizen.vtj.util;

import static org.testng.Assert.*;

import java.math.BigInteger;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link ByteArrayUtils}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class ByteArrayUtilsTest {

    /**
     * Creates a new ByteArrayUtilsTest object.
     */
    public ByteArrayUtilsTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldAppendBytesOnEnd() {
        final byte[] a = new byte[2];
        a[0] = (byte) 0;
        a[1] = (byte) 1;

        final byte[] b = new byte[3];
        b[0] = (byte) 2;
        b[1] = (byte) 3;
        b[2] = (byte) 4;

        final byte[] result = ByteArrayUtils.append(a, b);
        assertEquals(result.length, 5);

        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i], (byte) i);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldConvertLongToByteArrayAndBack() {
        String s = null;
        String b = null;
        byte[] result = null;
        final long[] values = new long[] { 0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE, };

        for (int i = 0; i < values.length; i++) {
            s = Long.toHexString(values[i]);

            while (s.length() < 16) {
                s = "0" + s;
            }

            result = ByteArrayUtils.toBytes(values[i]);
            assertEquals(result.length, 8);

            for (int j = 0; j < 8; j++) {
                b = s.substring(j * 2, (j + 1) * 2);
                assertEquals(result[j], (byte) Integer.parseInt(b, 16));
            }

            // test conversion back to long
            final long l = ByteArrayUtils.toLong(result);
            assertEquals(l, values[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldConvertLongToByteArrayAndAddToEnd() {
        String s = null;
        String b = null;
        byte[] result = null;
        final long[] values = new long[] { 0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE, };

        for (int i = 0; i < values.length; i++) {
            result = new byte[8 + i];
            s = Long.toHexString(values[i]);

            while (s.length() < 16) {
                s = "0" + s;
            }

            result = ByteArrayUtils.toBytes(values[i], result, i);

            for (int j = 0; j < 8; j++) {
                b = s.substring(j * 2, (j + 1) * 2);
                assertEquals(result[i + j], (byte) Integer.parseInt(b, 16));
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldCompareToAsExpected() {
        final byte[] a = ByteArrayUtils.toBytes(1234567890L);
        byte[] b = ByteArrayUtils.toBytes(1234567890L);
        assertTrue(ByteArrayUtils.compareTo(a, b) == 0);
        b = ByteArrayUtils.toBytes(1234567891L);
        assertTrue(ByteArrayUtils.compareTo(a, b) < 0);
        b = ByteArrayUtils.toBytes(1234567889L);
        assertTrue(ByteArrayUtils.compareTo(a, b) > 0);
        b = ByteArrayUtils.toBytes(1234567800L);
        assertTrue(ByteArrayUtils.compareTo(a, b) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldAppendByteToStringByteInHex() {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 256; i++) {
            ByteArrayUtils.appendToString((byte) i, sb);
        }

        assertEquals(sb.length(), 512);
        validateByteToString(sb);
    }

    private void validateByteToString(final CharSequence sb) {
        final String hexChars = "0123456789abcdef";
        int idx = 15;

        for (int i = 0; i < sb.length(); i++) {

            if (i % 2 == 0) {
                idx++;

                if (idx > 15) {
                    idx = 0;
                }

                assertEquals(sb.charAt(i), hexChars.charAt(i / 32));
            } else {
                assertEquals(sb.charAt(i), hexChars.charAt(idx));
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldAppendByteArrayRangeToStringInHex() {
        final StringBuilder sb = new StringBuilder();
        final byte[] raw = new byte[128];

        for (int i = 0; i < 128; i++) {
            raw[i] = (byte) i;
        }

        ByteArrayUtils.appendToString(raw, 0, 8, sb);
        ByteArrayUtils.appendToString(raw, 8, 4, sb);
        ByteArrayUtils.appendToString(raw, 12, 4, sb);
        ByteArrayUtils.appendToString(raw, 16, 4, sb);
        ByteArrayUtils.appendToString(raw, 20, 2, sb);
        ByteArrayUtils.appendToString(raw, 22, 2, sb);
        ByteArrayUtils.appendToString(raw, 24, 12, sb);
        ByteArrayUtils.appendToString(raw, 36, 12, sb);
        ByteArrayUtils.appendToString(raw, 48, 16, sb);
        ByteArrayUtils.appendToString(raw, 64, 64, sb);
        assertEquals(sb.length(), 256);
        validateByteToString(sb);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldAppendByteArrayToStringInHex() {
        final StringBuilder sb = new StringBuilder();
        final byte[] raw = new byte[16];

        for (int i = 0; i < 16; i++) {
            raw[i] = (byte) i;
        }

        ByteArrayUtils.appendToString(raw, sb);
        assertEquals(sb.length(), 32);
        validateByteToString(sb);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldConvertByteArrayToString() {
        final byte[] raw = new byte[16];

        for (int i = 0; i < 16; i++) {
            raw[i] = (byte) i;
        }

        final VTString s = ByteArrayUtils.toString(raw);
        validateByteToString(s);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldValidateAsHexadecimalIgnoringCase() {
        assertTrue(ByteArrayUtils.isValidHexadecimal("0123456789abcdefABCDEF", '\0'));
        assertFalse(ByteArrayUtils.isValidHexadecimal("01234567-89ab-cdef-ABCDEF", '\0'));
        assertTrue(ByteArrayUtils.isValidHexadecimal("01234567-89ab-cdef-ABCDEF", '-'));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldHandleSpecifiedSeparatorWhenConvertingToBytes() {
        byte[] raw = ByteArrayUtils.toBytes("", '-');
        assertEquals(raw.length, 0);
        raw = ByteArrayUtils.toBytes("00:43:86:AF:3E:17", ':');
        assertEquals(raw.length, 6);
        assertEquals(((int) raw[0]) & 0xFF, 0);
        assertEquals(((int) raw[1]) & 0xFF, 0x43);
        assertEquals(((int) raw[2]) & 0xFF, 0x86);
        assertEquals(((int) raw[3]) & 0xFF, 0xAF);
        assertEquals(((int) raw[4]) & 0xFF, 0x3E);
        assertEquals(((int) raw[5]) & 0xFF, 0x17);
        raw = ByteArrayUtils.toBytes("0:6:5B:EB:6E:9E", ':');
        assertEquals(raw.length, 6);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "utilities" }
    )
    public void shouldThrowExceptionIfInvalidSeparator() {
        final byte[] raw = ByteArrayUtils.toBytes("00:43:86:AF:3E:17", '/');
        assertEquals(raw.length, 6);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldConvertStringToBytesExactlyLikeBigIntegerConverts() {
        byte[] raw = ByteArrayUtils.toBytes("");
        assertEquals(raw.length, 0);

        final BigInteger bi = new BigInteger("5849302758392016753423");
        final String s = bi.toString(16);
        raw = ByteArrayUtils.toBytes(s);

        final byte[] other = bi.toByteArray();
        assertEquals(raw.length, other.length);

        for (int i = 0; i < other.length; i++) {
            assertEquals(raw[i], other[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldConvertStringToBytesAndAddToArrayInSpecifiedPosition() {
        final String s = "59227A28-3886-435E-9A19-78029429642C";
        final byte[] raw = new byte[16];
        ByteArrayUtils.toBytes(s, 0, 8, raw, 0);
        ByteArrayUtils.toBytes(s, 9, 4, raw, 4);
        ByteArrayUtils.toBytes(s, 14, 4, raw, 6);
        ByteArrayUtils.toBytes(s, 19, 4, raw, 8);
        ByteArrayUtils.toBytes(s, 24, 12, raw, 10);
        assertEquals(((int) raw[0]) & 0xFF, 0x59);
        assertEquals(((int) raw[4]) & 0xFF, 0x38);
        assertEquals(((int) raw[8]) & 0xFF, 0x9A);
        assertEquals(((int) raw[9]) & 0xFF, 0x19);
    }

}

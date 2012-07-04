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

import java.io.IOException;
import org.codekaizen.vtj.text.VTString;


/**
 * <p>Contains static utility methods for manipulating byte arrays.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class ByteArrayUtils {

    private static final String HEX_CHARS = "0123456789abcdefABCDEF";

    private ByteArrayUtils() {
        // non-instantiable
    }

    /**
     * Appends two bytes array into one.
     *
     * @param  a  the first array
     * @param  b  the following array
     *
     * @return  the combined array
     */
    public static byte[] append(final byte[] a, final byte[] b) {
        final byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }

    /**
     * Returns an 8-byte array built from a long.
     *
     * @param  n  the number to convert
     *
     * @return  the array
     */
    public static byte[] toBytes(final long n) {
        return toBytes(n, new byte[8], 0);
    }

    /**
     * Adds the separated bytes from a long to the next 8 bytes.
     *
     * @param  n  the number to convert
     * @param  raw  the array to fill
     * @param  start  the array index to begin filling at
     *
     * @return  the array supplied as an input parameter
     */
    public static byte[] toBytes(final long n, final byte[] raw, final int start) {
        long work = n;
        raw[start + 7] = (byte) (work);
        work >>>= 8;
        raw[start + 6] = (byte) (work);
        work >>>= 8;
        raw[start + 5] = (byte) (work);
        work >>>= 8;
        raw[start + 4] = (byte) (work);
        work >>>= 8;
        raw[start + 3] = (byte) (work);
        work >>>= 8;
        raw[start + 2] = (byte) (work);
        work >>>= 8;
        raw[start + 1] = (byte) (work);
        work >>>= 8;
        raw[start] = (byte) (work);

        return raw;
    }

    /**
     * Build a long from first 8 bytes of the array.
     *
     * @param  b  the array to convert
     *
     * @return  the long
     */
    public static long toLong(final byte[] b) {
        return ((((long) b[7]) & 0xFF) + ((((long) b[6]) & 0xFF) << 8) + ((((long) b[5]) & 0xFF) << 16) +
                    ((((long) b[4]) & 0xFF) << 24) + ((((long) b[3]) & 0xFF) << 32) + ((((long) b[2]) & 0xFF) << 40) +
                    ((((long) b[1]) & 0xFF) << 48) + ((((long) b[0]) & 0xFF) << 56));
    }

    /**
     * <p>Compares two byte arrays as specified by <code>Comparable</code>.</p>
     *
     * @param  a  - left hand value in the comparison operation.
     * @param  b  - right hand value in the comparison operation.
     *
     * @return  a negative integer, zero, or a positive integer as <code>a</code> is less than, equal to, or greater
     *          than <code>b</code>.
     */
    public static int compareTo(final byte[] a, final byte[] b) {

        if (a == b) {
            return 0;
        }

        if (a == null) {
            return -1;
        }

        if (b == null) {
            return 1;
        }

        if (a.length != b.length) {
            return ((a.length < b.length) ? -1 : 1);
        }

        for (int i = 0; i < a.length; i++) {

            if ((a[i] & 0xFF) < (b[i] & 0xFF)) {
                return -1;
            } else if ((a[i] & 0xFF) > (b[i] & 0xFF)) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * Appends a two-character hexadecimal representation of the supplied byte to the supplied string appender.
     *
     * @param  b  the byte to format
     * @param  ap  the character sequence to append to
     *
     * @return  the same appendable supplied as an input parameter
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public static Appendable appendToString(final byte b, final Appendable ap) {
        final int hex = b & 0xFF;

        try {
            ap.append(HEX_CHARS.charAt(hex >> 4));
            ap.append(HEX_CHARS.charAt(hex & 0x0f));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return ap;
    }

    /**
     * Appends a two-character hexadecimal representation of each byte in the specified range of the supplied byte array
     * to the supplied string appender.
     *
     * @param  raw  the byte array to format
     * @param  start  the array index value to begin at
     * @param  len  the number of bytes to format
     * @param  ap  the character sequence to append to
     *
     * @return  the same appendable supplied as an input parameter
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public static Appendable appendToString(final byte[] raw, final int start, final int len, final Appendable ap) {

        if (start + len > raw.length) {
            throw new IllegalArgumentException("exceeds byte array length");
        }

        for (int i = start; i < start + len; i++) {
            appendToString(raw[i], ap);
        }

        return ap;
    }

    /**
     * Appends a two-character hexadecimal representation of each byte in the supplied byte array to the supplied string
     * appender.
     *
     * @param  raw  the byte array to format
     * @param  ap  the character sequence to append to
     *
     * @return  the same appendable supplied as an input parameter
     */
    public static Appendable appendToString(final byte[] raw, final Appendable ap) {
        return appendToString(raw, 0, raw.length, ap);
    }

    /**
     * Returns a hexadecimal string representation of the supplied byte array.
     *
     * @param  raw  the byte array to format
     *
     * @return  the hexadecimal string
     */
    public static VTString toString(final byte[] raw) {
        final StringBuilder sb = new StringBuilder(raw.length * 2);

        return new VTString(appendToString(raw, sb).toString());
    }

    /**
     * Returns whether the supplied string can be converted into a byte array.
     *
     * @param  cs  the string to validate
     * @param  separator  the byte parts separator or <code>'\0'</code> for none
     *
     * @return  validate or not
     */
    public static boolean isValidHexadecimal(final CharSequence cs, final char separator) {

        for (int i = 0; i < cs.length(); i++) {

            if (cs.charAt(i) != separator && HEX_CHARS.indexOf(cs.charAt(i)) == -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the supplied string with an optional separator between byte parts as a byte array.
     *
     * @param  cs  the string
     * @param  separator  the byte parts separator or <code>'\0'</code> for none
     *
     * @return  the raw bytes
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public static byte[] toBytes(final CharSequence cs, final char separator) {

        if (HEX_CHARS.indexOf(separator) != -1) {
            throw new IllegalArgumentException("cannot be a valid hex character");
        }

        if (!isValidHexadecimal(cs, separator)) {
            throw new IllegalArgumentException("contains non-valid characters");
        }

        // remove separator
        final StringBuilder sb = new StringBuilder(cs.length());
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < cs.length(); i++) {

            if (cs.charAt(i) == separator) {

                if (s.length() % 2 == 1) {
                    sb.append('0').append(s.toString());
                } else {
                    sb.append(s.toString());
                }

                s = new StringBuilder();

                continue;
            }

            s.append(cs.charAt(i));
        }

        if (s.length() % 2 == 1) {
            sb.append('0').append(s.toString());
        } else {
            sb.append(s.toString());
        }

        final byte[] raw = new byte[sb.length() / 2];
        toBytes(sb, 0, sb.length(), raw, 0);

        return raw;
    }

    /**
     * Returns the supplied string as a byte array.
     *
     * @param  cs  the string
     *
     * @return  the raw bytes
     */
    public static byte[] toBytes(final CharSequence cs) {
        return toBytes(cs, '\0');
    }

    /**
     * Converts the specified string fragment into raw bytes.
     *
     * @param  cs  the string
     * @param  start  the character to start parsing at
     * @param  len  the number of characters to parse
     * @param  raw  the byte array to populate
     * @param  arrayStart  the starting position in the byte array to add the parsed bytes to
     */
    public static void toBytes(final CharSequence cs, final int start, final int len, final byte[] raw,
            final int arrayStart) {
        int j = arrayStart;

        for (int i = start; i < start + len; i += 2) {
            parseByte(cs, raw, i, j);
            j++;
        }
    }

    private static void parseByte(final CharSequence cs, final byte[] raw, int i, final int j) {
        char c = cs.charAt(i);

        if (c >= '0' && c <= '9') {
            raw[j] = (byte) ((c - '0') << 4);
        } else if (c >= 'a' && c <= 'f') {
            raw[j] = (byte) ((c - 'a' + 10) << 4);
        } else if (c >= 'A' && c <= 'F') {
            raw[j] = (byte) ((c - 'A' + 10) << 4);
        } else {
            throw new IllegalArgumentException("non-hex character");
        }

        c = cs.charAt(++i);

        if (c >= '0' && c <= '9') {
            raw[j] |= (byte) (c - '0');
        } else if (c >= 'a' && c <= 'f') {
            raw[j] |= (byte) (c - 'a' + 10);
        } else if (c >= 'A' && c <= 'F') {
            raw[j] |= (byte) (c - 'A' + 10);
        } else {
            throw new IllegalArgumentException("non-hex character");
        }
    }

}

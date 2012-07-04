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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.codekaizen.vtj.VT;
import org.codekaizen.vtj.util.ByteArrayUtils;


/**
 * <p>Wraps a 6-byte MAC address defined in the IEEE 802.1 standard.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class VTMACAddress extends VT<VTMACAddress> {

    /** Special form of the address with all 48 bits set to zero. */
    public static final VTMACAddress NIL = new VTMACAddress(0L);

    private static final long serialVersionUID = -8937738015203640271L;

    private final byte[] address = new byte[6];

    /**
     * Constructs an address using the six bytes supplied.
     *
     * @param  addr  the address value
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public VTMACAddress(final byte[] addr) {

        if (addr.length != 6) {
            throw new IllegalArgumentException("ethernet address has to consist of 6 bytes");
        }

        System.arraycopy(addr, 0, this.address, 0, 6);
        this.validateFields();
    }

    /**
     * Constructs an address from the lowest six bytes of the supplied value.
     *
     * @param  addr  the address
     */
    public VTMACAddress(final long addr) {
        long workLong = addr;

        for (int i = 5; i >= 0; i--) {
            this.address[i] = (byte) workLong;
            workLong >>>= 8;
        }

        this.validateFields();
    }

    private void validateFields() {

        if (this.address.length != 6) {
            throw new IllegalArgumentException("must be six bytes");
        }
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.validateFields();
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * Returns the address as a byte array.
     *
     * @return  the address
     */
    public byte[] toByteArray() {
        final byte[] result = new byte[6];
        System.arraycopy(this.address, 0, result, 0, 6);

        return result;
    }

    /**
     * Returns the address as a long integer.
     *
     * @return  the address
     */
    public long toLong() {
        final byte[] b = new byte[8];
        b[0] = (byte) 0;
        b[1] = (byte) 0;

        for (int i = 2; i < b.length; i++) {
            b[i] = this.address[i - 2];
        }

        return ByteArrayUtils.toLong(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMACAddress copy() {
        return new VTMACAddress(this.toByteArray());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  addr  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTMACAddress addr) {
        return ByteArrayUtils.compareTo(this.address, addr.address);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        int result = 11;

        for (int i = 0; i < 6; i++) {
            result = result * 7 + this.address[i];
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  obj  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof VTMACAddress)) {
            return false;
        }

        final VTMACAddress other = (VTMACAddress) obj;

        return Arrays.equals(other.address, this.address);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(17);

        for (int i = 0; i < 6; i++) {

            if (i > 0) {
                buf.append(":");
            }

            ByteArrayUtils.appendToString(this.address[i], buf);
        }

        return buf.toString();
    }

}

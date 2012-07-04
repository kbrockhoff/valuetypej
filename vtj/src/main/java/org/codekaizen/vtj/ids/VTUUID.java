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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import org.codekaizen.vtj.VT;
import org.codekaizen.vtj.util.ByteArrayUtils;


/**
 * <p>Wraps an universally-unique identifier (UUID). A UUID represents a 128-bit value generated without central
 * coordination yet reasonably expected to be unique across all computer systems. Microsoft and Oracle both refer to
 * them as globally-unique identifiers (GUID).</p>
 *
 * <p>The Oracle DBMS function <code>sys_guid()</code> generates GUID's which do not conform with the below mentioned
 * specification and therefore cannot be used with this class.</p>
 *
 * <p>For more information including algorithms used to create <code>UUID</code>s, see the IETF document <a
 * href="http://tools.ietf.org/html/rfc4122">RFC4122 UUID URN Namespace</a> or the joint ITU/ISO definition at <a
 * href="http://www.itu.int/ITU-T/studygroups/com17/oid/X.667-E.pdf">ITU-T Rec. X.667 | ISO/IEC 9834-8:2005</a>.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTUUID extends VT<VTUUID> implements UniqueIdentifierValueType<VTUUID> {

    /** Special form of the UUID with all 128 bits set to zero. */
    public static final VTUUID NIL = new VTUUID(0L, 0L);

    /** NCS backward compatibility variant (0b0xx). */
    public static final int VARIANT_NCS_COMPAT = 0;

    /** The standard IETF / variant (0b10x). */
    public static final int VARIANT_RFC4122 = 2;

    /** Microsoft Corp backward compatibility variant (0b110). */
    public static final int VARIANT_MICROSOFT = 6;

    /** Reserved future use variant (0b111). */
    public static final int VARIANT_FUTURE = 7;

    private static final long serialVersionUID = 8255317938488300690L;

    private final byte[] rawBytes = new byte[16];
    private transient String toStr;
    private transient String toBase16;
    private transient UUIDVersion version = null;
    private transient int variant = -1;
    private transient long timestamp = -1L;
    private transient int sequence = -1;
    private transient long node = -1L;
    private transient int hashCode = -1;
    private transient long mostSigBits = -1L;
    private transient long leastSigBits = -1L;

    /**
     * Constructs a UUID.
     *
     * @param  raw  the bytes to wrap
     */
    public VTUUID(final byte[] raw) {
        System.arraycopy(raw, 0, this.rawBytes, 0, 16);
        this.validateFields();
    }

    /**
     * Constructs a UUID.
     *
     * @param  mostSigBits  the most significant 64 bits.
     * @param  leastSigBits  the least significant 64 bits.
     */
    public VTUUID(final long mostSigBits, final long leastSigBits) {
        final byte[] b = ByteArrayUtils.append(ByteArrayUtils.toBytes(mostSigBits),
                ByteArrayUtils.toBytes(leastSigBits));
        System.arraycopy(b, 0, this.rawBytes, 0, 16);
        this.validateFields();
    }

    private void validateFields() {
        this.validateVariant(this.variant());
        this.validateVersion(this.version());
    }

    private void validateVariant(final int v) {

        switch (v) {
        case VARIANT_NCS_COMPAT:
        case VARIANT_RFC4122:
        case VARIANT_MICROSOFT:
        case VARIANT_FUTURE:

            // do nothing, these are valid
            break;
        default:
            throw new IllegalArgumentException("not defined");
        }
    }

    private void validateVersion(final UUIDVersion v) {

        switch (v) {
        case TIME_SPACE:
        case DCE_SECURITY:
        case NAME_MD5:
        case RANDOM:
        case NAME_SHA1:

            // do nothing, these are valid
            break;
        case NON_CONFORMANT:

            // OK if nil or old MS
            if (this.variant() != VARIANT_NCS_COMPAT && this.variant() != VARIANT_MICROSOFT) {
                throw new IllegalArgumentException("not defined");
            }

            break;
        default:
            throw new IllegalArgumentException("not defined");
        }
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        // set "cached computation" fields to their initial values
        this.version = null;
        this.variant = -1;
        this.timestamp = -1;
        this.sequence = -1;
        this.node = -1;
        this.hashCode = -1;
        this.validateFields();
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * Returns the least significant 64 bits of this UUID's 128 bit value.
     *
     * @return  the least significant 64 bits of this UUID's 128 bit value.
     */
    public long getLeastSignificantBits() {

        if (this.leastSigBits == -1L) {
            final byte[] b = new byte[8];
            System.arraycopy(this.rawBytes, 8, b, 0, 8);
            this.leastSigBits = ByteArrayUtils.toLong(b);
        }

        return this.leastSigBits;
    }

    /**
     * Returns the most significant 64 bits of this UUID's 128 bit value.
     *
     * @return  the most significant 64 bits of this UUID's 128 bit value.
     */
    public long getMostSignificantBits() {

        if (this.mostSigBits == -1L) {
            final byte[] b = new byte[8];
            System.arraycopy(this.rawBytes, 0, b, 0, 8);
            this.mostSigBits = ByteArrayUtils.toLong(b);
        }

        return this.mostSigBits;
    }

    /**
     * The version number associated with this <code>UUID</code>. The version number describes how this <code>
     * UUID</code> was generated.
     *
     * <p>The version number has the following meaning:</p>
     *
     * <ul>
     * <li>1 Time-based UUID</li>
     * <li>2 DCE security UUID</li>
     * <li>3 Name-based UUID MD5 hashed</li>
     * <li>4 Randomly generated UUID</li>
     * <li>5 Name-based UUID SHA1 hashed</li>
     * </ul>
     *
     * @return  the version number of this <code>UUID</code>.
     */
    public UUIDVersion version() {

        if (this.version == null) {
            final int i = (this.rawBytes[6] >>> 4) & 0x0F;

            if (i < 0 || i > UUIDVersion.values().length) {
                this.version = UUIDVersion.NON_CONFORMANT;
            } else {
                this.version = UUIDVersion.values()[i];
            }
        }

        return this.version;
    }

    /**
     * The variant number associated with this <code>UUID</code>. The variant number describes the layout of the <code>
     * UUID</code>.
     *
     * <p>The variant number has the following meaning:</p>
     *
     * <ul>
     * <li>0 Reserved for NCS backward compatibility</li>
     * <li>2 The Leach-Salz variant (used by this class)</li>
     * <li>6 Reserved, Microsoft Corporation backward compatibility</li>
     * <li>7 Reserved for future definition</li>
     * </ul>
     *
     * @return  the variant number of this <code>UUID</code>.
     */
    public int variant() {

        if (this.variant == -1) {

            if ((this.rawBytes[8] & 0x80) == 0x0) {
                this.variant = VARIANT_NCS_COMPAT;
            } else if ((this.rawBytes[8] & 0x40) == 0x0) {
                this.variant = VARIANT_RFC4122;
            } else if ((this.rawBytes[8] & 0x20) == 0x0) {
                this.variant = VARIANT_MICROSOFT;
            } else {
                this.variant = VARIANT_FUTURE;
            }
        }

        return this.variant;
    }

    /**
     * The timestamp value associated with this UUID.
     *
     * <p>The 60 bit timestamp value is constructed from the time_low, time_mid, and time_hi fields of this <code>
     * UUID</code>. The resulting timestamp is measured in 100-nanosecond units since midnight, October 15, 1582 UTC.
     * </p>
     *
     * <p>The timestamp value is only meaningful in a time-based UUID, which has version type 1. If this <code>
     * UUID</code> is not a time-based UUID then this method throws UnsupportedOperationException.</p>
     *
     * @return  the timestamp used to create this <code>UUID</code>.
     *
     * @throws  UnsupportedOperationException  if this UUID is not a version 1 UUID.
     */
    public long timestamp() {

        if (!this.version().equals(UUIDVersion.TIME_SPACE)) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }

        if (this.timestamp == -1L) {
            final byte[] longVal = new byte[8];
            System.arraycopy(this.rawBytes, 6, longVal, 0, 2);
            System.arraycopy(this.rawBytes, 4, longVal, 2, 2);
            System.arraycopy(this.rawBytes, 0, longVal, 4, 4);
            // remove version bits
            longVal[0] &= 0x0F;
            System.out.println();
            this.timestamp = ByteArrayUtils.toLong(longVal);
        }

        return this.timestamp;
    }

    /**
     * The clock sequence value associated with this UUID.
     *
     * <p>The 14 bit clock sequence value is constructed from the clock sequence field of this UUID. The clock sequence
     * field is used to guarantee temporal uniqueness in a time-based UUID.</p>
     *
     * <p>The clockSequence value is only meaningful in a time-based UUID, which has version type 1. If this UUID is not
     * a time-based UUID then this method throws UnsupportedOperationException.</p>
     *
     * @return  the clock sequence of this <code>UUID</code>.
     *
     * @throws  UnsupportedOperationException  if this UUID is not a version 1 UUID.
     */
    public int clockSequence() {

        if (!this.version().equals(UUIDVersion.TIME_SPACE)) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }

        if (this.sequence == -1) {
            final byte[] b = { rawBytes[8], rawBytes[9] };

            // remove the variant bit(s)
            // different variants use a different number of bytes
            // since only time-based version is allowed thru it should
            // always use the first value
            // the others are in here to handle future changes
            switch (this.variant()) {
            case VARIANT_RFC4122:
                b[0] &= 0x3F;

                break;
            case VARIANT_NCS_COMPAT:
                b[0] &= 0x9F;

                break;
            case VARIANT_MICROSOFT:
            case VARIANT_FUTURE:
                b[0] &= 0x1F;

                break;
            }

            this.sequence = (short) ((b[1] & 0xFF) + ((b[0] & 0xFF) << 8));
        }

        return this.sequence;
    }

    /**
     * The node value associated with this UUID.
     *
     * <p>The 48 bit node value is constructed from the node field of this UUID. This field is intended to hold the IEEE
     * 802 address of the machine that generated this UUID to guarantee spatial uniqueness.</p>
     *
     * <p>The node value is only meaningful in a time-based UUID, which has version type 1. If this UUID is not a
     * time-based UUID then this method throws UnsupportedOperationException.</p>
     *
     * @return  the node value of this <code>UUID</code>.
     *
     * @throws  UnsupportedOperationException  if this UUID is not a version 1 UUID.
     */
    public long node() {

        if (!this.version().equals(UUIDVersion.TIME_SPACE)) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }

        if (this.node == -1L) {
            final byte[] b = new byte[8];
            System.arraycopy(rawBytes, 10, b, 2, 6);
            this.node = new Long((ByteArrayUtils.toLong(b) & 0xFFFFFFFFFFFFL));
        }

        return this.node;
    }

    /**
     * Returns the wrapped UUID as a formatted string in Microsoft registry format. Example: <code>
     * {713A5E6F-65E3-4F46-9C0E-677C624DEB0F}</code>.
     *
     * @return  the formatted string
     */
    public String getRegistryFormat() {
        return "{" + this.toString() + "}";
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTUUID copy() {
        return new VTUUID(this.toByteArray());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  uuid  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTUUID uuid) {
        final byte[] thatA = uuid.toByteArray();
        final byte[] thisA = this.rawBytes;

        for (int i = 0; i < 16; ++i) {
            final int cmp = (((int) thisA[i]) & 0xFF) - (((int) thatA[i]) & 0xFF);

            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {

        if (this.hashCode == -1) {
            int result = 11;

            for (int i = 0; i < 16; i++) {
                result = result * 7 + this.rawBytes[i];
            }

            this.hashCode = result;
        }

        return this.hashCode;
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

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof VTUUID)) {
            return false;
        }

        final VTUUID other = (VTUUID) obj;

        return Arrays.equals(other.rawBytes, this.rawBytes);
    }

    /**
     * Returns the wrapped UUID as a formatted string in the format <code>713A5E6F-65E3-4F46-9C0E-677C624DEB0F</code>.
     *
     * @return  the formatted string
     */
    @Override
    public String toString() {

        if (this.toStr == null) {
            final StringBuilder b = new StringBuilder(36);

            for (int i = 0; i < 16; ++i) {

                switch (i) {
                case 4:
                case 6:
                case 8:
                case 10:
                    b.append('-');
                }

                ByteArrayUtils.appendToString(this.rawBytes[i], b);
            }

            this.toStr = b.toString();
        }

        return this.toStr;
    }

    /**
     * Returns the wrapped UUID as an unformatted base16 string in the format <code>
     * 713A5E6F65E34F469C0E677C624DEB0F</code>.
     *
     * @return  the unformatted string
     */
    public String toBase16() {

        if (this.toBase16 == null) {
            final StringBuilder b = new StringBuilder(36);
            ByteArrayUtils.appendToString(this.rawBytes, b);
            this.toBase16 = b.toString();
        }

        return this.toBase16;
    }

    /**
     * Returns the wrapped UUID as a 16-byte array.
     *
     * @return  the byte array
     */
    public byte[] toByteArray() {
        final byte[] raw = new byte[16];
        System.arraycopy(this.rawBytes, 0, raw, 0, 16);

        return raw;
    }

    /**
     * Returns the wrapped UUID as a 128-bit integer.
     *
     * @return  the integer
     */
    public BigInteger toBigInteger() {
        return new BigInteger(this.toByteArray());
    }

}

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
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.VT;


/**
 * <p>Wraps a DBMS-generated, serial, long integer identifier. This identifier is required to be above zero, because as
 * far as I know that is how all DBMS's function. The zero value is reserved for the <code>NIL</code> instance.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTLongSerialId extends VT<VTLongSerialId> implements UniqueIdentifierValueType<VTLongSerialId> {

    /** Special form of the serial id with a value of zero. */
    public static final VTLongSerialId NIL = new VTLongSerialId(0L);

    /** Prefix for output of <code>toString()</code> method. */
    public static final String SID_PREFIX = "sid:";

    private static final long serialVersionUID = -3720242990315057337L;

    private final long value;

    /**
     * Constructs a serial id object.
     *
     * @param  id  the underlying serial int value
     */
    public VTLongSerialId(final long id) {
        this.value = id;
        validateFields();
    }

    private void validateFields() {
        AssertPrecondition.withinRange("id", value, 0L, Long.MAX_VALUE);
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        validateFields();
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * Returns the serial long integer value.
     *
     * @return  the serial id
     */
    public long longValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTLongSerialId copy() {
        return new VTLongSerialId(value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTLongSerialId o) {
        final long diff = value - o.longValue();

        if (diff < 0L) {
            return -1;
        } else if (diff > 0L) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (value ^ (value >>> 32));

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

        if (!(obj instanceof VTLongSerialId)) {
            return false;
        }

        final VTLongSerialId other = (VTLongSerialId) obj;

        return value == other.value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return SID_PREFIX + Long.toString(value);
    }

}

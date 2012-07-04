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
package org.codekaizen.vtj.math;

import org.codekaizen.vtj.AssertPrecondition;


/**
 * <p>Wraps a Java primitive <code>int</code>.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class VTInteger extends VTNumber<VTInteger> {

    private static final long serialVersionUID = 9151352339365507725L;

    private final int value;

    /**
     * Constructs a integer object.
     *
     * @param  value  the value to be represented by the object
     */
    public VTInteger(final int value) {
        this.value = value;
    }

    /**
     * Returns an integer whose value is <code>(this mod m)<code>.</code></code>
     *
     * @param  val  number value by which this value is to be divided
     *
     * @return  the new number object
     */
    public VTInteger mod(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTInteger(value % val.intValue());
    }

    /**
     * Returns the next lower (deincremented) integer.
     *
     * @return  the new number object
     */
    public VTInteger previous() {
        return new VTInteger(value - 1);
    }

    /**
     * Returns the next higher (incremented) integer.
     *
     * @return  the new number object
     */
    public VTInteger next() {
        return new VTInteger(value + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public double doubleValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int intValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public long longValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger abs() {
        return new VTInteger(value < 0 ? value * -1 : value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger negate() {
        return new VTInteger(value * -1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger plus(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTInteger(value + val.intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger minus(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTInteger(value - val.intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger multiply(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTInteger(value * val.intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ArithmeticException  DOCUMENT ME!
     */
    @Override
    public VTInteger div(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        if (val.intValue() == 0) {
            throw new ArithmeticException("Division by zero");
        }

        return new VTInteger(value / val.intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger pow(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTInteger((int) Math.pow(value, val.intValue()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scale  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger round(final int scale) {
        return new VTInteger(value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger truncate() {
        return new VTInteger(value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInteger copy() {
        return new VTInteger(value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTInteger other) {
        return value - other.value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return value;
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

        if (!(obj instanceof VTInteger)) {
            return false;
        }

        final VTInteger vti = (VTInteger) obj;

        return value == vti.value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }

}

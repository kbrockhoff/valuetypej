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

import java.text.DecimalFormat;
import org.codekaizen.vtj.AssertPrecondition;


/**
 * <p>Wraps a Java primitive <code>double</code> displayed in standard IEEE 754 defined scientific format.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class VTDouble extends VTNumber<VTDouble> {

    private static final long serialVersionUID = 6509170049522787460L;

    private final double value;

    /**
     * Constructs a double object.
     *
     * @param  value  the value to be represented by the object
     */
    public VTDouble(final double value) {
        this.value = value;
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
        return (int) value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public long longValue() {
        return (long) value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble abs() {
        return new VTDouble(Math.abs(value));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble negate() {
        return new VTDouble(value * -1.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble plus(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTDouble(value + val.doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble minus(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTDouble(value - val.doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble multiply(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTDouble(value * val.doubleValue());
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
    public VTDouble div(final VTNumber<?> val) throws ArithmeticException {
        AssertPrecondition.notNull("val", val);

        if (val.doubleValue() == 0.0) {
            throw new ArithmeticException("Division by zero");
        }

        return new VTDouble(value / val.doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble pow(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        return new VTDouble(Math.pow(value, val.doubleValue()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scale  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble round(final int scale) {
        AssertPrecondition.withinRange("scale", scale, 0, 8);

        if (scale == 0) {
            return new VTDouble(Math.round(value));
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("0.");

        for (int i = 0; i < scale; i++) {
            sb.append("0");
        }

        final DecimalFormat fmt = new DecimalFormat(sb.toString());
        final String s = fmt.format(value);

        return new VTDouble(Double.parseDouble(s));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble truncate() {
        final long l = (long) value;

        return new VTDouble(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDouble copy() {
        return new VTDouble(value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTDouble other) {

        if (value == other.value) {
            return 0;
        } else if (value < other.value) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        final long bits = Double.doubleToLongBits(value);

        return (int) (bits ^ (bits >>> 32));
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

        if (!(obj instanceof VTDouble)) {
            return false;
        }

        final VTDouble vtd = (VTDouble) obj;

        return value == vtd.value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {

        if (Double.isNaN(value)) {
            return "NaN";
        } else {
            final DecimalFormat fmt = new DecimalFormat("0.############E0");

            return fmt.format(value);
        }
    }

}

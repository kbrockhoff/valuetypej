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

import org.codekaizen.vtj.VT;
import org.codekaizen.vtj.VTContext;


/**
 * <p>Represents a numerical value. Abstract base class for all number-based value types.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class VTNumber<T> extends VT<T> {

    private static final long serialVersionUID = 6365754305204586991L;
    private static VTContext vtContext;

    /**
     * Creates a new VTNumber object.
     */
    protected VTNumber() {
    }

    /**
     * Returns the value of the specified number as a <code>byte</code>. This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion to type <code>byte</code>.
     */
    public final byte byteValue() {
        return (byte) this.intValue();
    }

    /**
     * Returns the value of the specified number as a <code>double</code>. This may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion to type <code>double</code>.
     */
    public abstract double doubleValue();

    /**
     * Returns the value of the specified number as a <code>float</code>. This may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion to type <code>float</code>.
     */
    public final float floatValue() {
        return (float) this.doubleValue();
    }

    /**
     * Returns the value of the specified number as an <code>int</code>. This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion to type <code>int</code>.
     */
    public abstract int intValue();

    /**
     * Returns the value of the specified number as a <code>long</code>. This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion to type <code>long</code>.
     */
    public abstract long longValue();

    /**
     * Returns the value of the specified number as a <code>short</code>. This may involve rounding or truncation.
     *
     * @return  he numeric value represented by this object after conversion to type <code>short</code>.
     */
    public final short shortValue() {
        return (short) this.intValue();
    }

    /**
     * Returns a number whose value is the absolute value of this number.
     *
     * @return  the new number object
     */
    public abstract T abs();

    /**
     * Returns a number whose value is <code>(-this)</code>.
     *
     * @return  the new number object
     */
    public abstract T negate();

    /**
     * Returns a number whose value is <code>(this + val)</code>.
     *
     * @param  val  number value to be added to this value
     *
     * @return  the new number object
     */
    public abstract T plus(VTNumber<?> val);

    /**
     * Returns a number whose value is <code>(this - val)</code>.
     *
     * @param  val  number value to be subtracted from this value
     *
     * @return  the new number object
     */
    public abstract T minus(VTNumber<?> val);

    /**
     * Returns a number whose value is <code>(this * val)</code>.
     *
     * @param  val  number value to be multiplied by this value
     *
     * @return  the new number object
     */
    public abstract T multiply(VTNumber<?> val);

    /**
     * Returns a number whose value is <code>(this / val)</code>.
     *
     * @param  val  number value by which this value is to be divided
     *
     * @return  the new number object
     *
     * @throws  ArithmeticException  if division by zero is attempted
     */
    public abstract T div(VTNumber<?> val) throws ArithmeticException;

    /**
     * Returns a number whose value is <code>(this<sup>val</sup>)</code>.
     *
     * @param  val  the power value
     *
     * @return  the new number object
     */
    public abstract T pow(VTNumber<?> val);

    /**
     * Returns a number whose value is this value rounded to specified number of places.
     *
     * @param  scale  the number of decimal places
     *
     * @return  the new number object
     */
    public abstract T round(int scale);

    /**
     * Returns an integer number whose value is this value with the decimal part truncated.
     *
     * @return  the new number object
     */
    public abstract T truncate();

    /**
     * Sets the system-wide math context.
     *
     * @param  context  the math context
     */
    public static void setVTContext(final VTContext context) {
        vtContext = context;
    }

    /**
     * Returns the system-wide math context.
     *
     * @return  the math context
     */
    public static VTContext getVTContext() {

        if (vtContext == null) {
            vtContext = VTContext.getDefault();
        }

        return vtContext;
    }

}

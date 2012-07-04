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

import java.math.RoundingMode;
import org.codekaizen.vtj.AssertPrecondition;


/**
 * <p>Represents a basic decimal number for use in financial and monetary calculations where rounding errors are not
 * acceptable.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class VTDecimal extends VTNumber<VTDecimal> {

    /** Minimum scale value handled by this class. */
    public static final int MIN_SCALE = 0;

    /** Maximum scale value handled by this class. */
    public static final int MAX_SCALE = 8;

    private static final long[] DIVISORS = {
            1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L,  // one extra to handle
                                                                                                  // rounding
        };
    private static final long serialVersionUID = 1822322147472669772L;

    private final long value;
    private final int scale;

    /**
     * Constructs a decimal object.
     *
     * @param  value  the unscaled value of the <code>VTDecimal</code>
     * @param  scale  scale of the <code>VTDecimal</code> value to be created
     */
    public VTDecimal(final long value, final int scale) {
        validateScale(scale);
        this.value = value;
        this.scale = scale;
    }

    /**
     * Constructs a decimal object.
     *
     * @param  value  <code>double</code> value to be converted to a VTDecimal
     * @param  scale  scale of the <code>VTDecimal</code> value to be created
     * @param  mode  rounding mode to apply
     *
     * @throws  IllegalArgumentException  if <code>val</code> is infinite or NaN or if scale is outside of the allowable
     *                                    range
     * @throws  ArithmeticException  if <code>roundingMode == ROUND_UNNECESSARY</code> and the specified scaling
     *                               operation would require rounding
     */
    public VTDecimal(final double value, final int scale, final RoundingMode mode) {
        AssertPrecondition.nonSpecialFloatingPointNumber("value", value);

        validateScale(scale);

        long val = (long) (value * (double) DIVISORS[scale + 1]);
        long mod = val % 10L;

        if (val < 0) {

            switch (mode) {
            case CEILING:
            case DOWN:
                this.value = val / 10L;

                break;
            case UP:
            case FLOOR:

                if (mod == 0) {
                    this.value = val / 10L;
                } else {
                    this.value = val / 10L - 1L;

                }

                break;
            case HALF_DOWN:

                if (mod >= -5) {
                    this.value = val / 10L;
                } else {
                    this.value = val / 10L - 1L;
                }

                break;
            case HALF_EVEN:

                if (mod > -5) {
                    this.value = val / 10L;
                } else if (mod == -5) {
                    val /= 10L;
                    mod = val % 2L;

                    if (mod == -1) {
                        this.value = val - 1L;
                    } else {
                        this.value = val;
                    }
                } else {
                    this.value = val / 10L - 1L;
                }

                break;
            case HALF_UP:

                if (mod > -5) {
                    this.value = val / 10L;
                } else {
                    this.value = val / 10L - 1L;
                }

                break;
            case UNNECESSARY:

                if (mod == 0) {
                    this.value = val / 10L;
                } else {
                    throw new ArithmeticException("Rounding necessary");
                }

                break;
            default:
                throw new IllegalArgumentException("unknown rounding mode");
            }
        } else {

            switch (mode) {
            case CEILING:
            case UP:

                if (mod == 0) {
                    this.value = val / 10L;
                } else {
                    this.value = val / 10L + 1L;
                }

                break;
            case DOWN:
            case FLOOR:
                this.value = val / 10L;

                break;
            case HALF_DOWN:

                if (mod <= 5) {
                    this.value = val / 10L;
                } else {
                    this.value = val / 10L + 1L;
                }

                break;
            case HALF_EVEN:

                if (mod < 5) {
                    this.value = val / 10L;
                } else if (mod == 5) {
                    val /= 10L;
                    mod = val % 2L;

                    if (mod == 0) {
                        this.value = val;
                    } else {
                        this.value = val + 1L;
                    }
                } else {
                    this.value = val / 10L + 1L;
                }

                break;
            case HALF_UP:

                if (mod < 5) {
                    this.value = val / 10L;
                } else {
                    this.value = val / 10L + 1L;
                }

                break;
            case UNNECESSARY:

                if (mod == 0) {
                    this.value = val / 10L;
                } else {
                    throw new ArithmeticException("");
                }

                break;
            default:
                throw new IllegalArgumentException("unknown rounding mode");
            }
        }

        this.scale = scale;
    }

    private void validateScale(final int scale) {
        AssertPrecondition.withinRange("scale", scale, MIN_SCALE, MAX_SCALE);
    }

    /**
     * Returns the <code>scale</code> of this Decimal. The scale is the number of digits to the right of the decimal
     * point.
     *
     * @return  the scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public double doubleValue() {
        return Double.valueOf(toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int intValue() {
        return (int) (value / DIVISORS[scale]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public long longValue() {
        return value / DIVISORS[scale];
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal abs() {

        if (value < 0) {
            return new VTDecimal(value * -1L, scale);
        } else {
            return new VTDecimal(value, scale);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal negate() {
        return new VTDecimal(value * -1L, scale);
    }

    private VTDecimal constructDecimal(final VTNumber<?> val) {
        VTDecimal d;

        if (val instanceof VTDecimal) {
            d = (VTDecimal) val;

            if (d.scale > scale) {
                final int sc = Math.min(d.scale, calcMaximumScale());
                d = d.round(sc);
            } else {
                d = d.round(scale);
            }
        } else {
            d = new VTDecimal(val.doubleValue(), calcMaximumScale(), RoundingMode.HALF_UP);
        }

        return d;
    }

    private int calcMaximumScale() {
        int sc = scale;

        if (sc < VTNumber.getVTContext().getDecimalScale()) {
            sc = VTNumber.getVTContext().getDecimalScale();
        }

        return sc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal plus(final VTNumber<?> val) {
        final VTDecimal d = constructDecimal(val);

        return new VTDecimal(value * (long) Math.pow(10, d.scale - scale) + d.value, d.scale);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal minus(final VTNumber<?> val) {
        final VTDecimal d = constructDecimal(val);

        return new VTDecimal(value * (long) Math.pow(10, d.scale - scale) - d.value, d.scale);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal multiply(final VTNumber<?> val) {
        final int sc = calcScale(val);

        return new VTDecimal(doubleValue() * val.doubleValue(), sc, VTNumber.getVTContext().getRoundingMode());
    }

    private int calcScale(final VTNumber<?> val) {
        int sc = scale;

        if (val instanceof VTDecimal) {
            sc += ((VTDecimal) val).scale;
        } else {
            sc *= 2;
        }

        if (sc > VTNumber.getVTContext().getDecimalScale()) {
            sc = VTNumber.getVTContext().getDecimalScale();
        }

        return sc;
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
    public VTDecimal div(final VTNumber<?> val) {

        if (val.doubleValue() == 0.0) {
            throw new ArithmeticException("Division by zero");
        }

        final int sc = calcScale(val);

        return new VTDecimal(doubleValue() / val.doubleValue(), sc, VTNumber.getVTContext().getRoundingMode());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal pow(final VTNumber<?> val) {
        final int sc = calcScale(val);

        return new VTDecimal(Math.pow(doubleValue(), val.doubleValue()), sc, VTNumber.getVTContext().getRoundingMode());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scale  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal round(final int scale) {
        validateScale(scale);

        long val;

        if (scale < this.scale) {
            val = value / DIVISORS[this.scale - scale];
        } else {
            val = value * DIVISORS[scale - this.scale];
        }

        return new VTDecimal(val, scale);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal truncate() {
        return new VTDecimal(longValue(), 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTDecimal copy() {
        return new VTDecimal(value, scale);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTDecimal o) {
        return (int) (doubleValue() - o.doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return (int) (value % ((long) Integer.MAX_VALUE - 1));
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

        if (!(obj instanceof VTDecimal)) {
            return false;
        }

        final VTDecimal d = (VTDecimal) obj;

        return value == d.value && scale == d.scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(value);

        if (scale > 0) {
            int offset = 0;

            if (value < 0L) {
                offset = 1;
            }

            if (sb.length() >= scale + offset) {
                sb.insert(sb.length() - scale, '.');
            } else {
                sb.insert(offset, "0.");

                while (sb.length() < scale + 2) {
                    sb.insert(2 + offset, '0');
                }
            }

            int pos = sb.lastIndexOf(".");
            assert (pos != -1);

            while (sb.length() - pos <= scale) {
                sb.append('0');
                pos = sb.lastIndexOf(".");
            }
        }

        return sb.toString();
    }

}

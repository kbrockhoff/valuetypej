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
 * <p>Represents a rational number.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class VTFraction extends VTNumber<VTFraction> {

    private static final long serialVersionUID = 4850409883087571484L;
    private static final double DEFAULT_EPSILON = 0.00001;

    private long numerator;
    private long denominator;

    /**
     * Constructs a rational number object.
     *
     * @param  num  the numerator
     * @param  den  the denominator
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     * @throws  ArithmeticException  DOCUMENT ME!
     */
    public VTFraction(long num, long den) {

        if (den == 0L) {
            throw new IllegalArgumentException("cannot div by zero");
        }

        if (den < 0) {

            if (num == Long.MIN_VALUE || den == Long.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }

            num = -num;
            den = -den;
        }

        // reduce numerator and denominator by greatest common denominator
        final long d = greatestCommonDenom(num, den);

        if (d > 1L) {
            num /= d;
            den /= d;
        }

        this.numerator = num;
        this.denominator = den;
    }

    /**
     * Constructs a rational number object.
     *
     * @param  value  the floating-point value to convert
     * @param  epsilon  the precision required
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public VTFraction(final double value, final double epsilon) {
        AssertPrecondition.withinRange("value", value, Long.MIN_VALUE, Long.MAX_VALUE);

        final long overflow = Long.MAX_VALUE - 1024L;
        double r0 = value;
        long a0 = (long) Math.floor(r0);

        // check for (almost) integer arguments, which should not go
        // to iterations.
        if (Math.abs(a0 - value) < epsilon) {
            this.numerator = (long) a0;
            this.denominator = 1L;

            return;
        }

        long p0 = 1;
        long q0 = 0;
        long p1 = a0;
        long q1 = 1;
        long p2 = 0;
        long q2 = 1;
        int n = 0;
        boolean stop = false;

        do {
            ++n;

            final double r1 = 1.0 / (r0 - a0);
            final long a1 = (long) Math.floor(r1);
            p2 = (a1 * p1) + p0;
            q2 = (a1 * q1) + q0;

            if ((p2 > overflow) || (q2 > overflow)) {
                throw new IllegalArgumentException("unconvertable");
            }

            final double convergent = (double) p2 / (double) q2;

            if (n < 100 && Math.abs(convergent - value) > epsilon && q2 < Integer.MAX_VALUE) {
                p0 = p1;
                p1 = p2;
                q0 = q1;
                q1 = q2;
                a0 = a1;
                r0 = r1;
            } else {
                stop = true;
            }
        } while (!stop);

        if (n >= 100) {
            throw new IllegalArgumentException("unconvertable");
        }

        if (q2 < Integer.MAX_VALUE) {
            this.numerator = (int) p2;
            this.denominator = (int) q2;
        } else {
            this.numerator = (int) p1;
            this.denominator = (int) q1;
        }
    }

    private static long greatestCommonDenom(long u, long v) {

        if (u * v == 0L) {
            return (Math.abs(u) + Math.abs(v));
        }

        // keep u and v negative, as negative integers range down to
        // -2^63, while positive numbers can only be as large as 2^63-1
        // (i.e. we can't necessarily negate a negative number without
        // overflow)
        if (u > 0) {
            u = -u;
        }

        if (v > 0) {
            v = -v;
        }

        // B1. [Find pow of 2]
        int k = 0;

        // while u and v are both even
        while ((u & 1L) == 0 && (v & 1L) == 0 && k < 63) {
            u /= 2L;
            v /= 2L;
            k++;  // cast out twos.
        }

        if (k == 63) {
            throw new ArithmeticException("overflow: gcd is 2^63");
        }

        // B2. Initialize: u and v have been divided by 2^k and at least
        // one is odd.
        long t = ((u & 1L) == 1L) ? v : -(u / 2L);

        // t negative: u was odd, v may be even (t replaces v)
        // t positive: u was even, v is odd (t replaces u)
        do {

            // B4/B3: cast out twos from t.
            while ((t & 1L) == 0) {  // while t is even..
                t /= 2L;  // cast out twos
            }

            // B5 [reset max(u,v)]
            if (t > 0L) {
                u = -t;
            } else {
                v = t;
            }
            // B6/B3. at this point both u and v should be odd.
            t = (v - u) / 2L;
            // |u| larger: t positive (replace u)
            // |v| larger: t negative (replace v)
        } while (t != 0L);

        return -u * (1L << k);  // gcd is u*2^k
    }

    /**
     * Returns the numerator.
     *
     * @return  the numerator
     */
    public long getNumerator() {
        return this.numerator;
    }

    /**
     * Returns the denominator.
     *
     * @return  the denominator
     */
    public long getDenominator() {
        return this.denominator;
    }

    /**
     * Return the multiplicative inverse of this fraction.
     *
     * @return  the reciprocal fraction
     */
    public VTFraction reciprocal() {
        return new VTFraction(this.denominator, this.numerator);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public double doubleValue() {
        return ((double) this.numerator) / ((double) this.denominator);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int intValue() {
        return (int) this.longValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public long longValue() {
        return this.numerator / this.denominator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction abs() {
        return new VTFraction(Math.abs(this.numerator), this.denominator);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction negate() {
        return new VTFraction(this.numerator * -1L, this.denominator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction plus(final VTNumber<?> val) {
        return this.addSub(val, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction minus(final VTNumber<?> val) {
        return this.addSub(val, false);
    }

    private VTFraction addSub(final VTNumber<?> val, final boolean add) {
        final VTFraction fraction = this.makeFraction(val);

        // zero is identity for addition.
        if (this.numerator == 0) {
            return add ? fraction.copy() : fraction.negate();
        }

        if (fraction.numerator == 0) {
            return this.copy();
        }

        final long uvp = safeMultiply(this.numerator, fraction.denominator);
        final long upv = safeMultiply(fraction.numerator, this.denominator);
        final long num = add ? (uvp + upv) : (uvp - upv);
        final long den = safeMultiply(this.denominator, fraction.denominator);

        return new VTFraction(num, den);
    }

    private VTFraction makeFraction(final VTNumber<?> val) {
        AssertPrecondition.notNull("val", val);

        VTFraction fraction = null;

        if (val instanceof VTFraction) {
            fraction = (VTFraction) val;
        } else {
            fraction = new VTFraction(val.doubleValue(), DEFAULT_EPSILON);
        }

        return fraction;
    }

    private static long safeMultiply(final long a, final long b) {

        if (a > b) {
            // use symmetry to reduce boundary cases
            return safeMultiply(b, a);
        }

        if (a < 0L) {

            if (b < 0L) {

                // check for positive overflow with negative a, negative b
                if (a < Long.MAX_VALUE / b) {
                    throw new ArithmeticException("overflow: multiply");
                }
            } else if (b > 0L) {

                // check for negative overflow with negative a, positive b
                if (Long.MIN_VALUE / b > a) {
                    throw new ArithmeticException("overflow: multiply");

                }
            }
        } else if (a > 0L) {

            // check for positive overflow with positive a, positive b
            if (a > Long.MAX_VALUE / b) {
                throw new ArithmeticException("overflow: multiply");
            }
        }

        return a * b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction multiply(final VTNumber<?> val) {
        final VTFraction fraction = this.makeFraction(val);

        if (numerator == 0 || fraction.numerator == 0) {
            return new VTFraction(0L, 1L);
        }

        final long d1 = greatestCommonDenom(numerator, fraction.denominator);
        final long d2 = greatestCommonDenom(fraction.numerator, denominator);
        final long num = safeMultiply(numerator / d1, fraction.numerator / d2);
        final long den = safeMultiply(denominator / d2, fraction.denominator / d1);

        return new VTFraction(num, den);
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
    public VTFraction div(final VTNumber<?> val) {

        if (val.doubleValue() == 0.0) {
            throw new ArithmeticException("Division by zero");
        }

        final VTFraction fraction = this.makeFraction(val);

        return this.multiply(fraction.reciprocal());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction pow(final VTNumber<?> val) {
        final long num = (long) Math.pow(this.numerator, val.doubleValue());
        final long den = (long) Math.pow(this.denominator, val.doubleValue());

        return new VTFraction(num, den);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scale  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction round(final int scale) {
        AssertPrecondition.withinRange("scale", scale, 0, 8);

        if (scale == 0) {
            return new VTFraction(Math.round(doubleValue()), DEFAULT_EPSILON);
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("0.");

        for (int i = 0; i < scale; i++) {
            sb.append("0");
        }

        final DecimalFormat fmt = new DecimalFormat(sb.toString());
        final String s = fmt.format(doubleValue());

        return new VTFraction(Double.parseDouble(s), DEFAULT_EPSILON);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction truncate() {
        final long num = this.numerator / this.denominator;

        return new VTFraction(num, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTFraction copy() {
        return new VTFraction(this.numerator, this.denominator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTFraction o) {
        int result = 0;

        if (this.doubleValue() < o.doubleValue()) {
            result = -1;
        } else if (this.doubleValue() > o.doubleValue()) {
            result = 1;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 41;
        result = prime * result + (int) (this.denominator ^ (this.denominator >>> 32));
        result = prime * result + (int) (this.numerator ^ (this.numerator >>> 32));

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

        if (!(obj instanceof VTFraction)) {
            return false;
        }

        final VTFraction other = (VTFraction) obj;

        return this.denominator == other.denominator && this.numerator == other.numerator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }

}

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

import static org.testng.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTDecimal}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTDecimalTest extends AbstractVTNumberTest {

    // from Java SE 6 javadocs for RoundingMode
    static final RoundingMode[] RND_MODES = {
            null, RoundingMode.UP, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.FLOOR, RoundingMode.HALF_UP,
            RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN, RoundingMode.UNNECESSARY,
        };
    static final double[][] ROUNDING_INPUT = {
            { 5.5, 6, 5, 6, 5, 6, 5, 6, Double.NaN, },
            { 2.5, 3, 2, 3, 2, 3, 2, 2, Double.NaN, },
            { 1.6, 2, 1, 2, 1, 2, 2, 2, Double.NaN, },
            { 1.1, 2, 1, 2, 1, 1, 1, 1, Double.NaN, },
            { 1.0, 1, 1, 1, 1, 1, 1, 1, 1, },
            { -1.0, -1, -1, -1, -1, -1, -1, -1, -1, },
            { -1.1, -2, -1, -1, -2, -1, -1, -1, Double.NaN, },
            { -1.6, -2, -1, -1, -2, -2, -2, -2, Double.NaN, },
            { -2.5, -3, -2, -2, -3, -3, -2, -2, Double.NaN, },
            { -5.5, -6, -5, -5, -6, -6, -5, -6, Double.NaN, },
            { 0.0, 0, 0, 0, 0, 0, 0, 0, 0, },
        };

    /**
     * Creates a new VTDecimalTest object.
     */
    public VTDecimalTest() {
        super(VTDecimal.class, new Class<?>[] { Long.TYPE, Integer.TYPE, }, new Object[] { 1278518L, 2, });
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldNotAllowNegativeScale() {
        final VTDecimal d = new VTDecimal(10000, VTDecimal.MIN_SCALE - 1);
        assertEquals(d.getScale(), VTDecimal.MIN_SCALE - 1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldNotAllowAboveMaxScale() {
        final VTDecimal d = new VTDecimal(10000, VTDecimal.MAX_SCALE + 1);
        assertEquals(d.getScale(), VTDecimal.MAX_SCALE + 1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldNotAllowNegativeScaleUsingDoubleConstructor() {
        final VTDecimal d = new VTDecimal(100.00, -2, RoundingMode.DOWN);
        assertEquals(d.getScale(), -2);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldNotAllowAboveMaxScaleUsingDoubleConstructor() {
        final VTDecimal d = new VTDecimal(100.00, 12, RoundingMode.DOWN);
        assertEquals(d.getScale(), 12);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldNotAllowNaNUsingDoubleConstructor() {
        final VTDecimal d = new VTDecimal(Double.NaN, 2, RoundingMode.DOWN);
        assertEquals(d.getScale(), 2);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldNotAllowPositiveInfinityUsingDoubleConstructor() {
        final VTDecimal d = new VTDecimal(Double.POSITIVE_INFINITY, 2, RoundingMode.DOWN);
        assertEquals(d.getScale(), 2);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        groups = { "construct" }
    )
    public void shouldNotAllowNegativeInfinityUsingDoubleConstructor() {
        final VTDecimal d = new VTDecimal(Double.NEGATIVE_INFINITY, 2, RoundingMode.DOWN);
        assertEquals(d.getScale(), 2);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "construct" })
    public void shouldRoundingModeWorkExactlyLikeBigDecimal() {
        VTDecimal d = new VTDecimal(100.00, 2, RoundingMode.DOWN);
        assertEquals(d.getScale(), 2);

        // check rounding modes work exactly the same as for BigDecimal
        BigDecimal bd = null;

        for (int i = 0; i < ROUNDING_INPUT.length; i++) {
            final double[] row = ROUNDING_INPUT[i];

            for (int j = 1; j < row.length; j++) {

                if (Double.isNaN(row[j])) {

                    try {
                        bd = new BigDecimal(row[0]).setScale(0, RND_MODES[j]);
                        fail("should have thrown ArithmeticException");
                    } catch (ArithmeticException ae) {
                        // do nothing
                    }

                    try {
                        d = new VTDecimal(row[0], 0, RND_MODES[j]);
                        fail("should have thrown ArithmeticException");
                    } catch (ArithmeticException ae) {
                        // do nothing
                    }
                } else {
                    bd = new BigDecimal(row[0]).setScale(0, RND_MODES[j]);
                    assertEquals(bd.doubleValue(), row[j], 0.01);
                    d = new VTDecimal(row[0], 0, RND_MODES[j]);
                    assertEquals(d.doubleValue(), row[j], 0.01);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldReturnSameScaleAsSuppliedConstructor() {
        final VTDecimal d = new VTDecimal(10000, 2);
        assertEquals(d.getScale(), 2);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doAbsTesting() {
        VTDecimal d1 = new VTDecimal(1218, 2);
        VTDecimal d2 = d1.abs();
        assertEquals(d2.doubleValue(), d1.doubleValue(), 0.001);
        d1 = new VTDecimal(-1218, 2);
        d2 = d1.abs();
        assertEquals(d2.doubleValue() * -1.0, d1.doubleValue(), 0.001);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNegateTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = d1.negate();
        assertEquals(d2.intValue() * -1, d1.intValue());
        d1 = new VTDecimal(-1458, 0);
        d2 = d1.negate();
        assertEquals(d2.intValue() * -1, d1.intValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPlusTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = new VTDecimal(1458, 0);
        VTDecimal d3 = d1.plus(d2);
        assertEquals(d3.intValue(), 2916);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1458, 0);
        d2 = new VTDecimal(0, 2);
        d3 = d1.plus(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1245678, 2);
        d2 = new VTDecimal(181752, 2);
        d3 = d1.plus(d2);
        assertEquals(d3.toString(), "14274.30");
        d2 = new VTDecimal(1817523, 3);
        d3 = d1.plus(d2);
        assertEquals(d3.toString(), "14274.303");
        d2 = new VTDecimal(-181752, 2);
        d3 = d1.plus(d2);
        assertEquals(d3.toString(), "10639.26");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMinusTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = new VTDecimal(1458, 0);
        VTDecimal d3 = d1.minus(d2);
        assertEquals(d3.intValue(), 0);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1458, 0);
        d2 = new VTDecimal(0, 2);
        d3 = d1.minus(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1245678, 2);
        d2 = new VTDecimal(181752, 2);
        d3 = d1.minus(d2);
        assertEquals(d3.toString(), "10639.26");
        d2 = new VTDecimal(1817523, 3);
        d3 = d1.minus(d2);
        assertEquals(d3.toString(), "10639.257");
        d2 = new VTDecimal(-181752, 2);
        d3 = d1.minus(d2);
        assertEquals(d3.toString(), "14274.30");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doMultiplyTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = new VTDecimal(1458, 0);
        VTDecimal d3 = d1.multiply(d2);
        assertEquals(d3.intValue(), 2125764);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1458, 0);
        d2 = new VTDecimal(0, 2);
        d3 = d1.multiply(d2);
        assertEquals(d3.intValue(), 0);
        assertFalse(d1.intValue() == d3.intValue());
        assertTrue(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1245678, 2);
        d2 = new VTDecimal(181752, 6);
        d3 = d1.multiply(d2);
        assertEquals(d3.toString(), "2264.044678");
        d2 = new VTDecimal(-181752, 6);
        d3 = d1.multiply(d2);
        assertEquals(d3.toString(), "-2264.044678");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doDivTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = new VTDecimal(1458, 0);
        VTDecimal d3 = d1.div(d2);
        assertEquals(d3.intValue(), 1);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1458, 0);
        d2 = new VTDecimal(100, 2);
        d3 = d1.div(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1245678, 2);
        d2 = new VTDecimal(181752, 6);
        d3 = d1.div(d2);
        assertEquals(d3.toString(), "68537.237554");
        d2 = new VTDecimal(-181752, 6);
        d3 = d1.div(d2);
        assertEquals(d3.toString(), "-68537.237554");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doPowTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = new VTDecimal(3, 0);
        VTDecimal d3 = d1.pow(d2);
        assertEquals(d3.longValue(), 3099363912L);
        // test original objects have not changed
        assertFalse(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(1458, 0);
        d2 = new VTDecimal(100, 2);
        d3 = d1.pow(d2);
        assertEquals(d3.intValue(), 1458);
        assertTrue(d1.intValue() == d3.intValue());
        assertFalse(d2.intValue() == d3.intValue());
        d1 = new VTDecimal(14580000, 4);
        d2 = new VTDecimal(5, 1);
        d3 = d1.pow(d2);
        assertEquals(d3.toString(), "38.18377");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doRoundTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = d1.round(0);
        assertEquals(d2.intValue(), 1458);
        d2 = d1.round(5);
        assertEquals(d2.intValue(), 1458);
        d1 = new VTDecimal(145834, 2);
        d2 = d1.round(0);
        assertEquals(d2.intValue(), 1458);

        long base = 1234567890L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d1 = new VTDecimal(base, i);

            for (int j = VTDecimal.MIN_SCALE; j <= VTDecimal.MAX_SCALE; j++) {
                d2 = d1.round(j);
            }
        }

        base = -1234567890L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d1 = new VTDecimal(base, i);

            for (int j = VTDecimal.MIN_SCALE; j <= VTDecimal.MAX_SCALE; j++) {
                d2 = d1.round(j);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doTruncateTesting() {
        VTDecimal d1 = new VTDecimal(1458, 0);
        VTDecimal d2 = d1.truncate();
        assertEquals(d2.intValue(), 1458);
        d1 = new VTDecimal(145834, 2);
        d2 = d1.truncate();
        assertEquals(d2.intValue(), 1458);

        long base = 1234567890L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d1 = new VTDecimal(base, i);
            d2 = d1.truncate();
            assertEquals(d2.doubleValue(), (double) d1.longValue());
        }

        base = -1234567890L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d1 = new VTDecimal(base, i);
            d2 = d1.truncate();
            assertEquals(d2.doubleValue(), (double) d1.longValue());
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doCompareToTesting() {
        final VTDecimal d1 = new VTDecimal(1458, 1);
        VTDecimal d2 = new VTDecimal(1458, 1);
        assertTrue(d1.compareTo(d2) == 0);
        d2 = new VTDecimal(1458, 0);
        assertTrue(d1.compareTo(d2) < 0);
        d2 = new VTDecimal(1458, 2);
        assertTrue(d1.compareTo(d2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTDecimal d1 = new VTDecimal(1458, 1);
        final VTDecimal d2 = new VTDecimal(1458, 0);
        assertFalse(d1.equals(d2));
        assertFalse(d1.equals(new Integer(1458)));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doToStringTesting() {
        VTDecimal d = null;
        String s = null;
        long base = 1234567890L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d = new VTDecimal(base, i);
            s = d.toString();

            if (i == 0) {
                assertEquals(-1, s.indexOf('.'));
            } else {
                assertEquals(s.length() - i - 1, s.indexOf('.'));
            }
        }

        base = -1234567890L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d = new VTDecimal(base, i);
            s = d.toString();

            if (i == 0) {
                assertEquals(s.indexOf('.'), -1);
            } else {
                assertEquals(s.indexOf('.'), s.length() - i - 1);
            }

            assertEquals(s.indexOf('-'), 0);
        }

        base = -1L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d = new VTDecimal(base, i);
            s = d.toString();

            if (i == 0) {
                assertEquals(s.indexOf('.'), -1);
            } else {
                assertEquals(s.indexOf('.'), s.length() - i - 1);
            }

            assertEquals(s.indexOf('-'), 0);
        }

        base = 1L;

        for (int i = VTDecimal.MIN_SCALE; i <= VTDecimal.MAX_SCALE; i++) {
            d = new VTDecimal(base, i);
            s = d.toString();

            if (i == 0) {
                assertEquals(s.indexOf('.'), -1);
            } else {
                assertEquals(s.indexOf('.'), s.length() - i - 1);
            }
        }
    }

}

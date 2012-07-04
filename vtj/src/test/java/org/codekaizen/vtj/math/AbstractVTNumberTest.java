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

import static org.testng.Assert.assertEquals;

import org.codekaizen.vtj.AbstractValueTypeTest;
import org.testng.annotations.Test;


/**
 * <p>Abstract base class for unit testing the API of descendents of {@link VTNumber}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractVTNumberTest extends AbstractValueTypeTest {

    /**
     * Creates a new AbstractVTNumberTest object.
     *
     * @param  testCls  DOCUMENT ME!
     * @param  cnstrtCls  DOCUMENT ME!
     * @param  cnstrtArgs  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    protected AbstractVTNumberTest(final Class<?> testCls, final Class<?>[] cnstrtCls, final Object[] cnstrtArgs) {
        super(testCls, cnstrtCls, cnstrtArgs);

        if (!VTNumber.class.isAssignableFrom(testCls)) {
            throw new IllegalArgumentException("AbstractVTNumberTest " + "is only for testing subclasses of VTNumber");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected VTNumber<?> getDefaultNumberTestInstance() {
        return (VTNumber<?>) super.getDefaultTestInstance();
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldReturnEquivalentValuesFromAllPrimitiveConversions() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final long val = vt1.longValue();
        assertEquals(vt1.byteValue(), (byte) val);
        assertEquals(vt1.shortValue(), (short) val);
        assertEquals(vt1.intValue(), (int) val);
        assertEquals(val, (long) vt1.floatValue());
        assertEquals(val, (long) vt1.doubleValue());
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldAbsoluteValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.abs();
        assertEquals(Math.abs(vt2.doubleValue()), Math.abs(vt1.doubleValue()), 0.0000001);
        this.doAbsTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doAbsTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldNegateValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.negate();
        assertEquals(vt2.doubleValue(), vt1.doubleValue() * -1.0, 0.0000001);
        this.doNegateTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doNegateTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldAddValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.plus(vt1);
        assertEquals(vt2.doubleValue(), vt1.doubleValue() * 2.0, 0.0000001);
        this.doPlusTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doPlusTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldSubtractValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.minus(vt1);
        assertEquals(vt2.doubleValue(), 0.0, 0.0000001);
        this.doMinusTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doMinusTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldMultiplyValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.multiply(vt1);
        assertEquals(vt2.doubleValue(), vt1.doubleValue() * vt1.doubleValue(), 0.0000001);
        this.doMultiplyTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doMultiplyTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldDivideValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.div(vt1);
        assertEquals(vt2.doubleValue(), 1.0, 0.0000001);
        this.doDivTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doDivTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(
        expectedExceptions = ArithmeticException.class,
        groups = { "api" }
    )
    public void shouldThrowArithmeticExceptionOnDivideByZero() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.div(new VTInteger(0));
        assertEquals(vt2.doubleValue(), 0.0, 0.0000001);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldExponentialValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.pow(new VTInteger(1));
        assertEquals(vt2.doubleValue(), vt1.doubleValue(), 0.0000001);

        final VTNumber<?> vt3 = (VTNumber<?>) vt1.pow(new VTInteger(0));
        assertEquals(vt3.doubleValue(), 1.0, 0.0000001);
        this.doPowTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doPowTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldRoundValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.round(0);
        assertEquals(vt2.longValue(), Math.round(vt1.doubleValue()));
        this.doRoundTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doRoundTesting();

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldTruncateValuesAsExpectedUsingFluentAPI() {
        final VTNumber<?> vt1 = this.getDefaultNumberTestInstance();
        final VTNumber<?> vt2 = (VTNumber<?>) vt1.truncate();
        assertEquals(vt2.longValue(), vt1.longValue());
        this.doTruncateTesting();
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void doTruncateTesting();

}

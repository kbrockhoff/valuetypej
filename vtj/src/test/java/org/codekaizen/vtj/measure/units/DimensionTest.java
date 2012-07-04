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
package org.codekaizen.vtj.measure.units;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link Dimension}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class DimensionTest {

    /**
     * Creates a new DimensionTest object.
     */
    public DimensionTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldMultiplyAsExpectedUsingFluentAPI() {
        final Dimension d = Dimension.MASS.multiply(Dimension.TIME);
        assertEquals(d.toString(), "M*T");
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldDivideAsExpectedUsingFluentAPI() {
        final Dimension d = Dimension.LENGTH.div(Dimension.TIME);
        assertEquals(d.toString(), "L/T");
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldExponentialAsExpectedUsingFluentAPI() {
        final Dimension d = Dimension.LENGTH.pow(3);
        assertEquals(d.toString(), "L^3");
    }

}

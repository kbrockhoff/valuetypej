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
package org.codekaizen.vtj.measure.converters;

import static org.testng.Assert.assertEquals;

import org.codekaizen.vtj.math.VTDecimal;
import org.codekaizen.vtj.math.VTFraction;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link FractionalUnitConverter}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class FractionalUnitConverterTest {

    /**
     * Creates a new FractionalUnitConverterTest object.
     */
    public FractionalUnitConverterTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldConvertToExpectedMultiplicationResults() {
        // metres to feet
        final VTFraction frac = new VTFraction(32808399L, 10000000L);
        final UnitConverter converter = new FractionalUnitConverter(frac);
        final VTDecimal metre = new VTDecimal(1000000L, 6);
        final VTDecimal feet = (VTDecimal) converter.convert(metre);
        assertEquals(feet.doubleValue(), 3.2808399, 0.000001);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldConvertBackToOrignalValueWhenUsingInverse() {
        final UnitConverter converter = new FractionalUnitConverter("32808399/10000000");
        final VTDecimal metre = new VTDecimal(1000000L, 6);
        final VTDecimal feet = (VTDecimal) converter.convert(metre);
        final UnitConverter invConverter = converter.inverse();
        final VTDecimal metreI = (VTDecimal) invConverter.convert(feet);
        assertEquals(metreI, metre);
    }

}

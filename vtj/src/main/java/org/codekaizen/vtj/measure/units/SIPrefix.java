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

import org.codekaizen.vtj.math.VTFraction;
import org.codekaizen.vtj.measure.converters.FractionalUnitConverter;
import org.codekaizen.vtj.measure.converters.UnitConverter;


/**
 * <p>Class description.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public enum SIPrefix {

    deca("da", new VTFraction(10L, 1L)), hecto("h", new VTFraction(100L, 1L)), kilo("k", new VTFraction(1000L, 1L)),
    mega("M", new VTFraction(1000000L, 1L)), giga("G", new VTFraction(1000000000L, 1L)),
    tera("T", new VTFraction(1000000000000L, 1L)), peta("P", new VTFraction(1000000000000000L, 1L)),
    deci("d", new VTFraction(1L, 10L)), centi("c", new VTFraction(1L, 100L)), milli("m", new VTFraction(1L, 1000L)),
    micro("\u00B5", new VTFraction(1L, 1000000L)), nano("n", new VTFraction(1L, 1000000000L)),
    pico("P", new VTFraction(1L, 1000000000000L)), femto("f", new VTFraction(1L, 1000000000000000L));

    private final String abbreviation;
    private final VTFraction ratio;

    private SIPrefix(final String abbreviation, final VTFraction ratio) {
        this.abbreviation = abbreviation;
        this.ratio = ratio;
    }

    /**
     * Returns the official SI abbreviation.
     *
     * @return  the abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Returns the fractional ratio to the base unit.
     *
     * @return  the ratio
     */
    public VTFraction getRatio() {
        return ratio;
    }

    /**
     * Returns the convertter from this unit to the base unit.
     *
     * @return
     */
    public UnitConverter getUnitConverter() {
        return new FractionalUnitConverter(ratio);
    }

}

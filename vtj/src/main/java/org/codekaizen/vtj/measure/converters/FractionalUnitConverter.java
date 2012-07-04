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

import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.codekaizen.vtj.math.VTFraction;
import org.codekaizen.vtj.math.VTFractionFactory;
import org.codekaizen.vtj.math.VTNumber;


/**
 * <p>Converts units based on a fixed fraction.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class FractionalUnitConverter implements UnitConverter {

    private final VTFraction ratio;

    /**
     * Constructs a converter using the supplied ratio.
     *
     * @param  ratio  the ratio
     */
    public FractionalUnitConverter(final String ratio) {
        final VTFractionFactory factory = new VTFractionFactory(new MapContextHandlingStrategy());
        this.ratio = factory.parse(ratio);
    }

    /**
     * Constructs a converter using the supplied ratio.
     *
     * @param  ratio  the ratio
     */
    public FractionalUnitConverter(final VTFraction ratio) {
        this.ratio = ratio;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public UnitConverter inverse() {
        return new FractionalUnitConverter(this.ratio.reciprocal());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  x  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTNumber<?> convert(final VTNumber<?> x) {
        return (VTNumber<?>) x.multiply(ratio);
    }

}

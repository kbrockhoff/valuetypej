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
package org.codekaizen.vtj.measure;

import org.codekaizen.vtj.math.AbstractVTNumberTest;
import org.codekaizen.vtj.math.VTDecimal;
import org.codekaizen.vtj.math.VTNumber;
import org.codekaizen.vtj.measure.units.SI;
import org.codekaizen.vtj.measure.units.Unit;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTMeasure}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTMeasureTest extends AbstractVTNumberTest {

    /**
     * Creates a new VTMeasureTest object.
     */
    public VTMeasureTest() {
        super(VTMeasure.class, new Class<?>[] { VTNumber.class, Unit.class },
            new Object[] { new VTDecimal(10000, 2), SI.KILOGRAM });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseLocaleSensitiveDefaultUnitIfNotSupplied() {

    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldUseFirstUnitWhenDoingMathWithNonMeasure() {

    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldConvertToFirstUnitWhenDoingMathOnSameDimension() {

    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldDoLegalMathOnMeasuresOfDifferingDimensions() {

    }

    /**
     * DOCUMENT ME!
     */
    protected void doAbsTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doNegateTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doPlusTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doMinusTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doMultiplyTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doDivTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doPowTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doRoundTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doTruncateTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doCompareToTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doNonEqualsTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * DOCUMENT ME!
     */
    protected void doToStringTesting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

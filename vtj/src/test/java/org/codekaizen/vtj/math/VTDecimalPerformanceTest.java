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

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


/**
 * <p>Performance tests of {@link VTDecimal} vs {@link BigDecimal}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTDecimalPerformanceTest {

    private Logger logger = LoggerFactory.getLogger(VTDecimalPerformanceTest.class);

    /**
     * Creates a new VTDecimalPerformanceTest object.
     */
    public VTDecimalPerformanceTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "performance" })
    public void shouldCreationPerformanceBeFasterThanBigDecimal() {
        final Random random = new Random();
        random.nextDouble();

        VTDecimal vtd = null;
        BigDecimal bd = null;

        long ms = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            vtd = new VTDecimal(random.nextDouble() * 1000.0, 2, RoundingMode.DOWN);
            vtd.doubleValue();
        }

        final long vtTime = System.currentTimeMillis() - ms;

        ms = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            bd = new BigDecimal(random.nextDouble() * 1000.0).setScale(2, RoundingMode.DOWN);
            bd.doubleValue();
        }

        final long bdTime = System.currentTimeMillis() - ms;
        this.logger.info("VTDecimal time = {}ms; BigDecimalTime = {}ms", vtTime, bdTime);
        assertTrue(vtTime < bdTime);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "performance" })
    public void shouldToStringPerformanceBeFasterThanBigDecimal() {
        final Random random = new Random();
        random.nextDouble();

        VTDecimal vtd = null;
        BigDecimal bd = null;

        long ms = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            vtd = new VTDecimal(random.nextDouble() * 1000.0, 2, RoundingMode.DOWN);
            vtd.toString();
            // do again since BigDecimal stores value and VT does not
            vtd.toString();
        }

        final long vtTime = System.currentTimeMillis() - ms;

        ms = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            bd = new BigDecimal(random.nextDouble() * 1000.0).setScale(2, RoundingMode.DOWN);
            bd.toString();
            // do again since BigDecimal stores value and VT does not
            bd.toString();
        }

        final long bdTime = System.currentTimeMillis() - ms;
        this.logger.info("VTDecimal time = {}ms; BigDecimalTime = {}ms", vtTime, bdTime);
        assertTrue(vtTime < bdTime + 500L);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "performance" })
    public void shouldAccountingMathPerformanceBeFasterThanBigDecimal() {
        final Random random = new Random();
        random.nextDouble();

        VTDecimal vtd = null;
        BigDecimal bd = null;

        long ms = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            vtd = new VTDecimal(random.nextDouble() * 1000.0, 2, RoundingMode.HALF_UP);

            for (int j = 0; j < 20; j++) {
                vtd = vtd.plus(new VTDecimal(random.nextDouble() * 1000.0, 2, RoundingMode.HALF_UP));
            }

            vtd.doubleValue();
        }

        final long vtTime = System.currentTimeMillis() - ms;

        ms = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            bd = new BigDecimal(random.nextDouble() * 1000.0).setScale(2, RoundingMode.HALF_UP);

            for (int j = 0; j < 20; j++) {
                bd = bd.add(new BigDecimal(random.nextDouble() * 1000.0).setScale(2, RoundingMode.HALF_UP));
            }

            bd.doubleValue();
        }

        final long bdTime = System.currentTimeMillis() - ms;
        this.logger.info("VTDecimal time = {}ms; BigDecimalTime = {}ms", vtTime, bdTime);
        assertTrue(vtTime < bdTime);
    }

}

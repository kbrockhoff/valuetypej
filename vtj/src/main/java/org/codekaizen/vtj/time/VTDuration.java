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
package org.codekaizen.vtj.time;

import org.codekaizen.vtj.math.VTNumber;
import org.codekaizen.vtj.measure.VTMeasure;
import org.codekaizen.vtj.measure.quantities.Duration;
import org.codekaizen.vtj.measure.units.Unit;


/**
 * <p>Represents a duration / span of time.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTDuration extends VTMeasure {

    /**
     * Constructs a duration of time value.
     *
     * @param  value  the numerical value
     * @param  unit  the unit of measure the value is stated in
     */
    public VTDuration(final VTNumber<?> value, final Unit<Duration> unit) {
        super(value, unit);
    }

    /**
     * Returns the total number of seconds in this time span or duration.
     *
     * @return  the seconds
     */
    public long getSeconds() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Returns the fraction of a second in nanoseconds of this time span or duration.
     *
     * @return  the nanoseconds within the second, always positive, never exceeds 999,999,999
     */
    public int getNanoOfSecond() {
        // TODO Auto-generated method stub
        return 0;
    }

}

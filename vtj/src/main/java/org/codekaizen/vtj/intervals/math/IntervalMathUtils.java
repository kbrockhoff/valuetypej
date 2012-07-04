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
package org.codekaizen.vtj.intervals.math;

import java.util.Collection;
import org.codekaizen.vtj.intervals.IntervalElementPosition;
import org.codekaizen.vtj.intervals.IntervalRelation;
import org.codekaizen.vtj.intervals.IntervalValueType;


/**
 * <p>Provides static interval algebra functions.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
@SuppressWarnings("unchecked")
public class IntervalMathUtils {

    private IntervalMathUtils() {
        // non-instantiable
    }

    /**
     * Returns the enumerated position the supplied value occupies in relation to the supplied interval.
     *
     * @param  interval  the interval to determine the position in
     * @param  value  the value to determine the position of
     *
     * @return  the enumerated relative position
     */
    public static IntervalElementPosition getPosition(final IntervalValueType interval, final Object value) {
        return null;
    }

    /**
     * Returns an enum for which of the thirteen Allen's interval mathematics relations the first input interval is
     * compared to the second input interval.
     *
     * @param  x  the first interval
     * @param  y  the second interval
     *
     * @return  the enumerated relation
     */
    public static IntervalRelation compareTo(final IntervalValueType x, final IntervalValueType y) {
        return null;
    }

    /**
     * Returns the converse interval relation for the supplied input relation.
     *
     * @param  relation  the relation to determine the converse for
     *
     * @return  the converse relation
     */
    public static IntervalRelation converse(final IntervalRelation relation) {
        return null;
    }

    /**
     * Returns a new interval which is the intersection of the two input intervals.
     *
     * @param  x  the first interval
     * @param  y  the second interval
     *
     * @return  the fully-instantiated intersection interval
     */
    public static IntervalValueType intersection(final IntervalValueType x, final IntervalValueType y) {
        return null;
    }

    /**
     * Returns a new interval which is the union of the two input intervals.
     *
     * @param  x  the first interval
     * @param  y  the second interval
     *
     * @return  the fully-instantiated union interval
     */
    public static IntervalValueType union(final IntervalValueType x, final IntervalValueType y) {
        return null;
    }

    /**
     * Returns two new intervals with the first interval having a lower bound of the supplied interval and a higher
     * bound of the split point and the second interval having a lower bound of the split point and a higher bound of
     * the supplied interval.
     *
     * @param  interval  the interval to split
     * @param  splitPoint  the point at which to split the interval with the higher bound not included and the lower
     *                     bound included
     *
     * @return  the two fully-instantiated intervals
     */
    public static Collection<IntervalValueType> split(final IntervalValueType interval, final Object splitPoint) {
        return null;
    }

}

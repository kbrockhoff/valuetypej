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
package org.codekaizen.vtj.intervals;

/**
 * <p>Represents an interval or range.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public interface IntervalValueType<T> {

    /**
     * Returns the lower bound of the interval. If the interval has no lower bound or is empty ({@link #isEmpty()}
     * returns true), this method returns <code>null</code>.
     *
     * @return  the lower bound of the interval, or <code>null</code> if the interval has no lower bound.
     */
    T getLowerBound();

    /**
     * Returns the higher bound of the interval. If the interval has no higher bound or is empty ({@link #isEmpty()}
     * returns true), this method returns <code>null</code>.
     *
     * @return  the higher bound of the interval, or <code>null</code> if the interval has no higher bound.
     */
    T getHigherBound();

    /**
     * Returns <code>true</code> if the interval contains its lower bound.
     *
     * @return  lower bound included in interval or not
     */
    boolean isLowerBoundIncluded();

    /**
     * Returns <code>true</code> if the interval contains its higher bound.
     *
     * @return  higher bound included or not
     */
    boolean isHigherBoundIncluded();

    /**
     * Returns <code>true</code> if the interval is empty. When an interval is empty, the methods related to its bounds
     * do no mean anything (this includes {@link #getLowerBound()}, {@link #getHigherBound()}, {@link
     * #isLowerBoundIncluded()}, {@link #isHigherBoundIncluded()}.
     *
     * @return  an empty interval or not
     */
    boolean isEmpty();

    /**
     * Returns <code>true</code> if the passed value is contained by the interval. If the interval is empty, this method
     * will always return <code>false</code>.
     *
     * @param  value  the value to check
     *
     * @return  if supplied value falls within the range or not
     */
    boolean contains(T value);

    /**
     * Returns the enumerated position the supplied value occupies in relation to this interval.
     *
     * @param  value  the value to determine the position of
     *
     * @return  the relative position
     */
    IntervalElementPosition getPosition(T value);

    /**
     * Returns an enum for which of the thirteen Allen's interval mathematics relations this interval is compared to the
     * input interval.
     *
     * @param  other  the other interval
     *
     * @return  the enumerated relation
     */
    IntervalRelation compareIntervalTo(IntervalValueType<T> other);

    /**
     * Returns a new interval which is the intersection of this interval and the input parameter interval.
     *
     * @param  other  the other interval to calculate the intersection of
     *
     * @return  the fully-instantiated interval
     */
    IntervalValueType<T> intersection(IntervalValueType<T> other);

    /**
     * Returns a string representing this interval in standard mathematical interval notation.
     *
     * <p>Brackets <code>[a,b]</code> means bound included and parenthesis <code>(a,b)</code> means bound is not
     * included. The lower and higher bound values should be the results of the <code>toString()</code> on the object.
     * If there is no bound on a side, it should be empty with a parenthesis. Examples:</p>
     *
     * <dl>
     * <dt>(3,5) is the set of all numbers greater than 3 and less than 5</dt>
     * <dt>(2,4] is the set of all numbers greater than 2 and less than or equal to 4</dt>
     * <dt>[-1,1] is the set of all numbers greater than or equal to -1 and less than or equal to 1</dt>
     * <dt>[0,) is the set of all numbers greater than 0</dt>
     * </dl>
     *
     * @return  the string representation
     */
    String toString();

}

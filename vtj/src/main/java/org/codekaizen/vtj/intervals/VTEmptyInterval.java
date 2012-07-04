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

import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.VT;
import org.codekaizen.vtj.ValueType;


/**
 * <p>Represents an empty interval or range.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTEmptyInterval<T extends ValueType<T>> extends VT<VTEmptyInterval<T>> implements IntervalValueType<T> {

    private static final long serialVersionUID = 9122726437389944930L;

    /**
     * Creates a new VTEmptyInterval object.
     */
    public VTEmptyInterval() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public T getLowerBound() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public T getHigherBound() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isLowerBoundIncluded() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isHigherBoundIncluded() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isEmpty() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  value  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean contains(final T value) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  value  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public IntervalElementPosition getPosition(final T value) {
        AssertPrecondition.notNull("value", value);

        return IntervalElementPosition.HIGHER;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public IntervalRelation compareIntervalTo(final IntervalValueType<T> other) {
        AssertPrecondition.notNull("other", other);

        return IntervalRelation.PRECEDES;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public IntervalValueType<T> intersection(final IntervalValueType<T> other) {
        AssertPrecondition.notNull("other", other);

        return new VTEmptyInterval<T>();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTEmptyInterval<T> copy() {
        return new VTEmptyInterval<T>();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NullPointerException  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTEmptyInterval<T> other) {

        if (other == null) {
            throw new NullPointerException();
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  obj  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof IntervalValueType)) {
            return false;
        }

        final IntervalValueType<?> i = (IntervalValueType<?>) obj;

        return i.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return "(0,0)";
    }

}

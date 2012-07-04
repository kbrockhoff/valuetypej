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
 * <p>Represents an interval or range.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTInterval<T extends ValueType<T>> extends VT<VTInterval<T>> implements IntervalValueType<T> {

    private static final long serialVersionUID = 797257397838943919L;
    private static final IntervalRelation[][] RELATIONS = {
            {
                IntervalRelation.PRECEDED_BY, IntervalRelation.MET_BY, IntervalRelation.OVERLAPED_BY,
                IntervalRelation.FINISHES, IntervalRelation.DURING,
            },
            {
                null, IntervalRelation.STARTED_BY, IntervalRelation.STARTED_BY, IntervalRelation.EQUALS,
                IntervalRelation.STARTS,
            },
            { null, null, IntervalRelation.CONTAINS, IntervalRelation.FINISHED_BY, IntervalRelation.OVERLAPS, },
            { null, null, null, IntervalRelation.FINISHED_BY, IntervalRelation.MEETS, },
            { null, null, null, null, IntervalRelation.PRECEDES, },
        };

    private final T lowerBound;
    private final T higherBound;
    private final boolean lowerBoundIncluded;
    private final boolean higherBoundIncluded;

    /**
     * Creates a new VTInterval object.
     *
     * @param  lowerBound  DOCUMENT ME!
     * @param  higherBound  DOCUMENT ME!
     * @param  lowerBoundIncluded  DOCUMENT ME!
     * @param  higherBoundIncluded  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public VTInterval(final T lowerBound, final T higherBound, final boolean lowerBoundIncluded,
            final boolean higherBoundIncluded) {

        if (lowerBound != null && higherBound != null && lowerBound.compareTo(higherBound) > 0) {
            throw new IllegalArgumentException("lower bound must be less than higher bound");
        }

        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
        this.lowerBoundIncluded = lowerBoundIncluded;
        this.higherBoundIncluded = higherBoundIncluded;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public T getLowerBound() {
        return this.lowerBound;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public T getHigherBound() {
        return this.higherBound;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isLowerBoundIncluded() {
        return this.lowerBoundIncluded;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isHigherBoundIncluded() {
        return this.higherBoundIncluded;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  value  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean contains(final T value) {

        if (value == null) {
            return false;
        }

        if (this.getLowerBound() != null) {

            if (this.isLowerBoundIncluded()) {

                if (this.getLowerBound().compareTo(value) > 0) {
                    return false;
                }
            } else {

                if (this.getLowerBound().compareTo(value) >= 0) {
                    return false;
                }
            }
        }

        if (this.getHigherBound() != null) {

            if (this.isHigherBoundIncluded()) {

                if (this.getHigherBound().compareTo(value) < 0) {
                    return false;
                }
            } else {

                if (this.getHigherBound().compareTo(value) <= 0) {
                    return false;
                }
            }
        }

        return true;
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

        int lowerCompare = 0;

        if (this.getLowerBound() == null) {
            lowerCompare = 1;
        } else {
            lowerCompare = value.compareTo(this.getLowerBound());
        }

        int higherCompare = 0;

        if (this.getHigherBound() == null) {
            higherCompare = -1;
        } else {
            higherCompare = value.compareTo(this.getHigherBound());
        }

        if (lowerCompare < 0) {
            return IntervalElementPosition.LOWER;
        } else if (lowerCompare == 0) {
            return IntervalElementPosition.LOWER_BOUND;
        } else if (higherCompare > 0) {
            return IntervalElementPosition.HIGHER;
        } else if (higherCompare == 0) {
            return IntervalElementPosition.HIGHER_BOUND;
        } else {
            return IntervalElementPosition.IN_INTERVAL;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public IntervalRelation compareIntervalTo(final IntervalValueType<T> other) {
        IntervalElementPosition loPos = null;
        IntervalElementPosition hiPos = null;

        if (other.getLowerBound() == null) {

            if (this.getLowerBound() == null) {
                loPos = IntervalElementPosition.LOWER_BOUND;
            } else {
                loPos = IntervalElementPosition.LOWER;
            }
        } else {
            loPos = this.getPosition(other.getLowerBound());
        }

        if (other.getHigherBound() == null) {

            if (this.getHigherBound() == null) {
                hiPos = IntervalElementPosition.HIGHER_BOUND;
            } else {
                hiPos = IntervalElementPosition.HIGHER;
            }
        } else {
            hiPos = this.getPosition(other.getHigherBound());
        }

        return RELATIONS[loPos.ordinal()][hiPos.ordinal()];
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public IntervalValueType<T> intersection(final IntervalValueType<T> other) {
        final IntervalRelation relation = this.compareIntervalTo(other);

        switch (relation) {
        case OVERLAPS:
            return new VTInterval<T>(other.getLowerBound(), this.getHigherBound(), other.isLowerBoundIncluded(),
                    this.isHigherBoundIncluded());
        case OVERLAPED_BY:
            return new VTInterval<T>(this.getLowerBound(), other.getHigherBound(), this.isLowerBoundIncluded(),
                    other.isHigherBoundIncluded());
        case STARTED_BY:
        case CONTAINS:
        case FINISHED_BY:
            return new VTInterval<T>(other.getLowerBound(), other.getHigherBound(), other.isLowerBoundIncluded(),
                    other.isHigherBoundIncluded());
        case STARTS:
        case FINISHES:
        case DURING:
        case EQUALS:
            return new VTInterval<T>(this.getLowerBound(), this.getHigherBound(), this.isLowerBoundIncluded(),
                    this.isHigherBoundIncluded());
        case PRECEDES:
        case PRECEDED_BY:
        case MEETS:
        case MET_BY:
        default:
            return new VTEmptyInterval<T>();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInterval<T> copy() {
        return new VTInterval<T>(this.getLowerBound(), this.getHigherBound(), this.isLowerBoundIncluded(),
                this.isHigherBoundIncluded());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  i  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTInterval<T> i) {
        int result = 0;

        if (this.getLowerBound() == null && this.getHigherBound() == null) {

            if (i.getLowerBound() == null && i.getHigherBound() == null) {
                result = 0;
            } else if (i.getLowerBound() == null) {
                result = 1;
            } else {
                result = -1;
            }
        } else if (this.getLowerBound() == null) {

            if (i.getLowerBound() == null && i.getHigherBound() == null) {
                result = -1;
            } else if (i.getLowerBound() == null) {
                result = this.getHigherBound().compareTo(i.getHigherBound());
            } else {
                result = -1;
            }
        } else if (this.getHigherBound() == null) {

            if (i.getLowerBound() == null && i.getHigherBound() == null) {
                result = 1;
            } else if (i.getHigherBound() == null) {
                result = this.getLowerBound().compareTo(i.getLowerBound());
            } else {
                result = 1;
            }
        } else {
            result = this.getLowerBound().compareTo(i.getLowerBound());

            if (result == 0) {

                if (this.isLowerBoundIncluded() && !i.isLowerBoundIncluded()) {
                    result = -1;
                } else if (!this.isLowerBoundIncluded() && i.isLowerBoundIncluded()) {
                    result = 1;
                }
            } else {
                result = this.getHigherBound().compareTo(i.getHigherBound());

                if (result == 0) {

                    if (this.isHigherBoundIncluded() && !i.isHigherBoundIncluded()) {
                        result = 1;
                    } else if (!this.isHigherBoundIncluded() && i.isHigherBoundIncluded()) {
                        result = -1;
                    }
                }
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        int result = 17;

        if (this.getLowerBound() != null) {
            result = result * 37 + this.getLowerBound().hashCode();
        }

        if (this.getHigherBound() != null) {
            result = result * 37 + this.getHigherBound().hashCode();
        }

        return result;
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

        if (this.getLowerBound() == null && this.getHigherBound() == null) {
            return i.getLowerBound() == null && i.getHigherBound() == null;
        } else if ((this.getLowerBound() == null) != (i.getLowerBound() == null)) {
            return false;
        } else if ((this.getHigherBound() == null) != (i.getHigherBound() == null)) {
            return false;
        } else if (this.getLowerBound() == null) {
            return this.getHigherBound().equals(i.getHigherBound());
        } else if (this.getHigherBound() == null) {
            return this.getLowerBound().equals(i.getLowerBound());
        } else {
            return this.getLowerBound().equals(i.getLowerBound()) && this.getHigherBound().equals(i.getHigherBound());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {

        if (this.getLowerBound() == null && this.getHigherBound() == null) {
            return "(,)";
        } else if (this.getLowerBound() == null) {
            return "(," + this.getHigherBound() + (this.isHigherBoundIncluded() ? "]" : ")");
        } else if (this.getHigherBound() == null) {
            return (this.isLowerBoundIncluded() ? "[" : "(") + this.getLowerBound() + ",)";
        } else {
            return (this.isLowerBoundIncluded() ? "[" : "(") + this.getLowerBound() + "," + this.getHigherBound() +
                    (this.isHigherBoundIncluded() ? "]" : ")");
        }
    }

}

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
 * <p>Enumerates the 13 base relations of Allen's Interval Algebra.</p>
 *
 * <p>See: <a href="http://en.wikipedia.org/wiki/Allen's_Interval_Algebra">Wikipedia: Allen's Interval Algebra</a></p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public enum IntervalRelation {

    /** <code>X</code> takes place before <code>Y</code>. */
    PRECEDES,

    /** <code>Y</code> takes place before <code>X</code>. */
    PRECEDED_BY,

    /** <code>X</code> meets <code>Y</code>. */
    MEETS,

    /** <code>Y</code> meets <code>X</code>. */
    MET_BY,

    /** <code>X</code> overlaps with <code>Y</code>. */
    OVERLAPS,

    /** <code>Y</code> overlaps with <code>X</code>. */
    OVERLAPED_BY,

    /** <code>X</code> starts <code>Y</code>. */
    STARTS,

    /** <code>Y</code> starts <code>X</code>. */
    STARTED_BY,

    /** <code>Y</code> during <code>X</code>. */
    CONTAINS,

    /** <code>X</code> during <code>Y</code>. */
    DURING,

    /** <code>X</code> finishes <code>Y</code>. */
    FINISHES,

    /** <code>Y</code> finishes <code>X</code>. */
    FINISHED_BY,

    /** <code>X</code> equals <code>Y</code>. */
    EQUALS;

}

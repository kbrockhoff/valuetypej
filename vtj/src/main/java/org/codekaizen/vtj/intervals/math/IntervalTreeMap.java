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
import java.util.Map;
import org.codekaizen.vtj.intervals.IntervalValueType;


/**
 * <p>Holds an interval tree with the intervals mapped to values.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
@SuppressWarnings("unchecked")
public interface IntervalTreeMap<K extends IntervalValueType, V> extends Map<K, V> {

    /**
     * The maximum depth of the tree.
     *
     * @return  the number of node layers
     */
    int depth();

    /**
     * Returns the total number of nodes in the tree.
     *
     * @return  the number of nodes
     */
    int nodeSize();

    /**
     * Returns all values whose key contains the input parameter.
     *
     * @param  value  the interval value to match
     *
     * @return  the values of all containing keys
     */
    Collection<V> query(Object value);

    /**
     * Returns all values whose key overlaps the input parameter.
     *
     * @param  interval  the interval to match
     *
     * @return  the values of all overlapping keys
     */
    Collection<V> query(IntervalValueType interval);

}

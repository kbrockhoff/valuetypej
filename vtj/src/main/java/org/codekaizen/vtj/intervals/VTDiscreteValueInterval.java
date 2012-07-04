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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.ValueType;


/**
 * <p>Defines an interval made up of discrete values. These values can be iterated through starting from the lower
 * bound.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTDiscreteValueInterval<T extends ValueType<T>> extends VTInterval<T> implements IntervalValueType<T>,
    Iterable<T> {

    private static final long serialVersionUID = -8565497405407059828L;

    private transient Method nextMethod = null;

    /**
     * Creates a new VTDiscreteValueInterval object.
     *
     * @param  lowerBound  DOCUMENT ME!
     * @param  higherBound  DOCUMENT ME!
     */
    public VTDiscreteValueInterval(final T lowerBound, final T higherBound) {
        super(lowerBound, higherBound, true, true);
        AssertPrecondition.notNull("lowerBound", lowerBound);
        AssertPrecondition.notNull("higherBound", higherBound);
        retrieveNextMethod();
    }

    @SuppressWarnings("unchecked")
    private void retrieveNextMethod() throws IllegalArgumentException {
        Class clazz = null;

        try {
            clazz = super.getLowerBound().getClass();
            nextMethod = clazz.getMethod("next");
        } catch (Exception e) {
            throw new IllegalArgumentException("is required to have a next() method", e);
        }
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.retrieveNextMethod();
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {

                private T nextValue = getLowerBound();

                public boolean hasNext() {
                    return nextValue != null && nextValue.compareTo(getHigherBound()) <= 0;
                }

                public T next() {
                    final T currentValue = nextValue;
                    nextValue = increment();

                    return currentValue;
                }

                @SuppressWarnings("unchecked")
                private T increment() {

                    try {
                        return (T) nextMethod.invoke(nextValue);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException("underlying collection is immutable");
                }
            };
    }

}

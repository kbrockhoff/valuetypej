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
package org.codekaizen.vtj;

/**
 * <p>Abstract base class for all value type classes.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class VT<T> implements ValueType<T> {

    private static final long serialVersionUID = -5853389167147890810L;

    /**
     * Constructs an immutable object.
     */
    protected VT() {
        super();
    }

    /**
     * Returns a deep copy of the value type object.
     *
     * @return  a new object equal to this one
     */
    public abstract T copy();

    /**
     * Method from the {@link Comparable} interface which all concrete value types should implement.
     *
     * @param  other  the object to be compared
     *
     * @return  a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *          the specified object
     */
    public abstract int compareTo(T other);

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hashtables such as those
     * provided by <code>java.util.Hashtable</code>. Forced override of the default implementation should be based on
     * value instead of object identity.
     *
     * @return  a hash code value for this object.
     */
    @Override
    public abstract int hashCode();

    /**
     * Indicates whether some other object is "equal to" this one. Forced override of the default implementation should
     * be based on value instead of object identity.
     *
     * @param  obj  he reference object with which to compare
     *
     * @return  <code>true</code> if this object is the same as the obj argument; <code>false</code> otherwise
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns a string representation of the object. In general, the <code>toString</code> method returns a string that
     * "textually represents" this object. The goal of the library is to make each string distinct enough that a text
     * parser can accurately determine what type of object it is.
     *
     * @return  a string representation of the object
     */
    @Override
    public abstract String toString();

}

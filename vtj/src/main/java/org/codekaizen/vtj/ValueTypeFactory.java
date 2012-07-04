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

import java.io.Serializable;


/**
 * Base interface for value type creators, converters and translators.
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public interface ValueTypeFactory<T extends ValueType<T>> extends Serializable {

    /**
     * Sets the context(s) within which new value types should be generated.
     *
     * @param  context  the current execution context
     */
    void setContext(Object context);

    /**
     * Returns the context(s) within which new value types should be generated.
     *
     * @return  the execution context this factory is using
     */
    Object getContext();

    /**
     * Returns whether this factory can create objects of the specified type.
     *
     * @param  clazz  the class of the potential object to create
     *
     * @return  creatable or not
     */
    boolean isCreatable(Class<? extends ValueType<?>> clazz);

    /**
     * Returns a newly constructed value type object wrapping the supplied argument(s).
     *
     * @param  clazz  the class of the potential object to create
     * @param  args  one or more value arguments
     *
     * @return  the new value type object
     */
    T create(Class<? extends ValueType<?>> clazz, Object... args);

    /**
     * Returns whether this factory is capable of parsing the supplied string.
     *
     * @param  s  the string to parse
     *
     * @return  can parse or not
     */
    boolean isParsable(CharSequence s);

    /**
     * Returns a newly constructed value type object created from the supplied string representation. This is the
     * inverse of {@link #format(ValueType)}.
     *
     * @param  s  the string representation
     *
     * @return  the new value type object
     */
    T parse(CharSequence s);

    /**
     * Returns a formatted string representation of the supplied object. This is the inverse of {@link
     * #parse(CharSequence)}.
     *
     * @param  vt  the value type object
     *
     * @return  the string representation
     */
    String format(T vt);

}

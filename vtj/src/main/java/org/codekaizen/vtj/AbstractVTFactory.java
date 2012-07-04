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

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>Abstract base value type factory.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractVTFactory<T extends ValueType<T>> implements ValueTypeFactory<T> {

    private static final long serialVersionUID = 7283337254132050064L;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ContextHandlingStrategy strategy;
    private Object currentContext;
    private Map<String, Object> values;

    /**
     * Constructs a value type factory.
     *
     * @param  strategy  the strategy to use for retrieving values and resources from the execution environment
     */
    protected AbstractVTFactory(final ContextHandlingStrategy strategy) {
        super();
        AssertPrecondition.notNull("strategy", strategy);
        this.strategy = strategy;
    }

    /**
     * Returns the logger.
     *
     * @return  the logger
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Returns the context handling strategy currently in use.
     *
     * @return  the strategy
     */
    protected final ContextHandlingStrategy getContextStrategy() {
        return strategy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  context  DOCUMENT ME!
     */
    public final void setContext(final Object context) {
        currentContext = context;
        values = new HashMap<String, Object>();
        getContextStrategy().setContext(currentContext);
        processContextChange();
        getLogger().debug("Context changed to {}", context);
    }

    /**
     * Override this method if any processing needs to be done when the context changes.
     */
    protected void processContextChange() {

    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final Object getContext() {
        return currentContext;
    }

    /**
     * Returns a named context value, variable, property or resource if one exists. Otherwise, it returns <code>
     * null</code>.
     *
     * @param  name  the name the value is mapped to
     *
     * @return  the value
     */
    protected final Object getValue(final String name) {

        if (values == null) {
            values = new HashMap<String, Object>();
        }

        Object value = values.get(name);

        if (value == null) {
            value = getContextStrategy().getValue(name);

            if (value != null) {
                values.put(name, value);
            }
        }

        if (value == null) {
            value = System.getProperty(name);

            if (value != null) {
                values.put(name, value);
            }
        }

        return value;
    }

    /**
     * Returns whether this factory can create objects of the specified type.
     *
     * @param  clazz  the class of the potential object to create
     *
     * @return  creatable or not
     */
    public abstract boolean isCreatable(Class<? extends ValueType<?>> clazz);

    /**
     * DOCUMENT ME!
     *
     * @param  clazz  DOCUMENT ME!
     * @param  args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public T create(final Class<? extends ValueType<?>> clazz, final Object... args) {

        if (!isCreatable(clazz)) {
            throw new IllegalArgumentException("not creatable by this factory");
        }

        return create(args);
    }

    /**
     * Returns a newly constructed value type object wrapping the supplied argument(s).
     *
     * @param  args  one or more value arguments
     *
     * @return  the new value type object
     */
    protected abstract T create(Object... args);

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract boolean isParsable(CharSequence s);

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public T parse(final CharSequence s) {

        if (!isParsable(s)) {
            throw new IllegalArgumentException("unparsable");
        }

        return doParse(s);
    }

    /**
     * Returns a newly constructed value type object created from the supplied string representation. This is the
     * inverse of {@link #format(ValueType)}.
     *
     * @param  s  the string representation guaranteed to be non-null
     *
     * @return  the new value type object
     */
    protected abstract T doParse(CharSequence s);

    /**
     * DOCUMENT ME!
     *
     * @param  vt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String format(final T vt) {

        if (vt == null) {
            return "";
        } else {
            return vt.toString();
        }
    }

}

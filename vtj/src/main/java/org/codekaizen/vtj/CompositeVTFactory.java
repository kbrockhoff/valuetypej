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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.codekaizen.vtj.enums.VTBoolean;


/**
 * <p>Handles the parsing and formating of all the value types in a domain by successively delegating to the contained
 * value type factories.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
@SuppressWarnings("unchecked")
public class CompositeVTFactory extends AbstractVTFactory {

    private static final long serialVersionUID = 8610867732176827990L;

    private final List<ValueTypeFactory<?>> factories;
    private final List<Method> formatMethods;

    /**
     * Creates a new CompositeVTFactory object.
     *
     * @param  factories  DOCUMENT ME!
     * @param  strategy  DOCUMENT ME!
     */
    public CompositeVTFactory(final List<ValueTypeFactory<?>> factories, final ContextHandlingStrategy strategy) {
        super(strategy);
        this.factories = new ArrayList<ValueTypeFactory<?>>(factories);
        formatMethods = new ArrayList<Method>();

        for (final ValueTypeFactory<?> factory : this.factories) {

            for (final Method method : factory.getClass().getMethods()) {

                if ("format".equals(method.getName())) {
                    formatMethods.add(method);

                    break;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void processContextChange() {
        super.processContextChange();

        for (final ValueTypeFactory<?> factory : factories) {
            factory.setContext(super.getContext());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  clazz  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isCreatable(final Class clazz) {

        for (final ValueTypeFactory<?> factory : factories) {

            if (factory.isCreatable(clazz)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  clazz  DOCUMENT ME!
     * @param  args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public ValueType create(final Class clazz, final Object... args) {

        for (final ValueTypeFactory<?> factory : factories) {

            if (factory.isCreatable(clazz)) {
                return factory.create(clazz, args);
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected VTBoolean create(final Object... args) {
        return null;  // never called
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isParsable(final CharSequence s) {

        for (final ValueTypeFactory<?> factory : factories) {

            if (factory.isParsable(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    public ValueType parse(final CharSequence s) {

        for (final ValueTypeFactory<?> factory : factories) {

            if (factory.isParsable(s)) {
                return factory.parse(s);
            }
        }

        throw new IllegalArgumentException("unrecognizable string");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected ValueType doParse(final CharSequence s) {

        // do nothing
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  vt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    @Override
    public String format(final ValueType vt) {

        for (final Method method : formatMethods) {

            if (method.getParameterTypes()[0].isAssignableFrom(vt.getClass())) {

                try {
                    return (String) method.invoke(vt, new Object[] {});
                } catch (final Exception e) {
                    throw new IllegalStateException(e.getMessage());
                }
            }
        }

        return "";
    }

}

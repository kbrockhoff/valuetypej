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


/**
 * <p>Simple context handling strategy that using a {@link java.util.Map Map} passed in the constructor to hold all
 * values.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class MapContextHandlingStrategy implements ContextHandlingStrategy {

    private static final long serialVersionUID = 1632189976390350895L;

    private Map<String, Object> values;

    /**
     * Constructs a context handling strategy.
     */
    public MapContextHandlingStrategy() {
        super();
        values = new HashMap<String, Object>();
    }

    /**
     * Set the current context.
     *
     * @param  context  the current context
     */
    @SuppressWarnings("unchecked")
    public void setContext(final Object context) {

        if (context instanceof Map) {
            values = (Map<String, Object>) context;
        }
    }

    /**
     * Returns a named context value, variable, property or resource if one exists. Otherwise, it returns <code>
     * null</code>.
     *
     * @param  name  the name the value is mapped to
     *
     * @return  the value
     */
    public Object getValue(final String name) {
        return values.get(name);
    }

}

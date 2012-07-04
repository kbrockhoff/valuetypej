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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link MapContextHandlingStrategy}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class MapContextHandlingStrategyTest {

    /**
     * Creates a new MapContextHandlingStrategyTest object.
     */
    public MapContextHandlingStrategyTest() {
        super();
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldReturnValuesFromTheUnderlyingMap() {
        final String key = "valueOne";
        final String value = "The One Value";
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);

        final ContextHandlingStrategy strategy = new MapContextHandlingStrategy();
        strategy.setContext(map);

        final Object ctxValue = strategy.getValue(key);
        assertEquals(ctxValue, value);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNotThrowExceptionIfSuppliedSomethingOtherThanMap() {
        final String key = "valueOne";
        final ContextHandlingStrategy strategy = new MapContextHandlingStrategy();
        final Object ctxValue = strategy.getValue(key);
        assertNull(strategy.getValue(key));
        strategy.setContext("Not a map");
        assertNull(strategy.getValue(key));
        strategy.setContext(null);
        assertNull(strategy.getValue(key));
    }

}

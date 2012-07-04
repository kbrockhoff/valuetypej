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
package org.codekaizen.vtj.enums;

import static org.testng.Assert.assertTrue;

import org.codekaizen.vtj.AbstractValueTypeTest;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.ValueType;
import org.testng.annotations.Test;


/**
 * <p>Base class for unit testing implementations of {@link EnumValueType}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractEnumValueTypeTest extends AbstractValueTypeTest {

    /**
     * Constructs a {@link org.codekaizen.vtj.enums.EnumValueType} implementation unit test.
     *
     * @param  testCls  the concrete class being tested
     * @param  cnstrtCls  the class(es) of the arguments for the default constructor
     * @param  cnstrtArgs  the constructor arguments to use
     */
    protected AbstractEnumValueTypeTest(final Class<?> testCls, final Class<?>[] cnstrtCls, final Object[] cnstrtArgs) {
        super(testCls, cnstrtCls, cnstrtArgs);
        AssertPrecondition.isAssignable(EnumValueType.class, testCls);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldImplementEnumValueTypeMarker() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        assertTrue(vt1 instanceof EnumValueType);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "api" })
    public void shouldExtendEnum() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        assertTrue(vt1 instanceof Enum);
    }

}

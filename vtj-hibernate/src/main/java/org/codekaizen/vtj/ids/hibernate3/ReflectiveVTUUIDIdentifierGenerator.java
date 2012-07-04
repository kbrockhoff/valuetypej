/*
 * Copyright (c) 2009 Kevin Brockhoff
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
package org.codekaizen.vtj.ids.hibernate3;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.codekaizen.vtj.ids.VTUUID;

/**
 * <p>Hibernate identifier generator that getenerates time version
 * {@link VTUUID} for use as entity identifiers and uses reflection to find
 * out if an identifier has already been assigned.
 * </p>
 *
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class ReflectiveVTUUIDIdentifierGenerator
        extends AbstractVTUUIDIdentifierGenerator {

    public ReflectiveVTUUIDIdentifierGenerator() {
    }

    protected Serializable retrieveExistingIdentifier(final Object o) {
        Serializable result = null;
        for (Method method : o.getClass().getMethods()) {
            if ("getObjectId".equals(method.getName()) ||
                    "getId".equals(method.getName())) {
                try {
                    result = (Serializable) method.invoke(o);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e.getMessage());
                } catch (InvocationTargetException e) {
                    throw new IllegalStateException(e.getCause().getMessage());
                }
            }
        }
        return result;
    }

}

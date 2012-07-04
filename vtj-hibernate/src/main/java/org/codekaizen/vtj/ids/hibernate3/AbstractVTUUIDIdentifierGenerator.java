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
import java.util.Map;
import java.util.HashMap;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.HibernateException;
import org.codekaizen.vtj.ids.VTUUIDFactory;
import org.codekaizen.vtj.ids.UUIDVersion;
import org.codekaizen.vtj.ids.VTUUID;
import org.codekaizen.vtj.MapContextHandlingStrategy;

/**
 * <p>Base abstract Hibernate identifier generator that getenerates time version
 * {@link VTUUID} for use as entity identifiers.
 * </p>
 *
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractVTUUIDIdentifierGenerator
        implements IdentifierGenerator {

    private final VTUUIDFactory factory;

    public AbstractVTUUIDIdentifierGenerator() {
        factory = constructFactory();
    }

    private VTUUIDFactory constructFactory() {
        VTUUIDFactory factory =
                new VTUUIDFactory(new MapContextHandlingStrategy());
        Map<String, Object> configurationValues = new HashMap<String, Object>();
        configurationValues.put("org.codekaizen.vtj.ids.VTUUIDFactory.version",
                UUIDVersion.TIME_SPACE);
        factory.setContext(configurationValues);
        return factory;
    }

    private VTUUIDFactory getFactory() {
        return factory;
    }

    public final Serializable generate(
            final SessionImplementor sessionImplementor,
            final Object o) throws HibernateException {
        Serializable result = retrieveExistingIdentifier(o);
        if (result == null) {
            result = getFactory().nextUuid();
        }
        return result;
    }

    protected abstract Serializable retrieveExistingIdentifier(final Object o);

}

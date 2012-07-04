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

import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Date;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.codekaizen.vtj.ids.VTUUID;
import org.codekaizen.vtj.math.VTNumber;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.engine.SessionImplementor;

/**
 * <p>Unit tests for <code>ReflectiveVTUUIDIdentifierGenerator</code>.
 * </p>
 *
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class ReflectiveVTUUIDIdentifierGeneratorTest {

    public ReflectiveVTUUIDIdentifierGeneratorTest() {
    }

    @Test
    public void shouldGenerateNewValueIfObjectIdIsNull() {
        IdentifierGenerator generator =
                new ReflectiveVTUUIDIdentifierGenerator();
        SessionImplementor sessionImplementor =
                mock(SessionImplementor.class);
        ObjectIdEntity entity = new ObjectIdEntity();
        VTUUID id = (VTUUID) generator.generate(sessionImplementor, entity);
        assertNotNull(id);
    }

    @Test
    public void shouldUseCurrentValueIfObjectIdIsNotNull() {
        IdentifierGenerator generator =
                new ReflectiveVTUUIDIdentifierGenerator();
        SessionImplementor sessionImplementor =
                mock(SessionImplementor.class);
        ObjectIdEntity entity = new ObjectIdEntity();
        VTUUID id = (VTUUID) generator.generate(sessionImplementor, entity);
        assertNotNull(id);
        entity.setObjectId(id);
        VTUUID nextId = (VTUUID) generator.generate(sessionImplementor, entity);
        assertTrue(nextId == id);
    }

    @Test
    public void shouldGenerateNewValueIfIdIsNull() {
        IdentifierGenerator generator =
                new ReflectiveVTUUIDIdentifierGenerator();
        SessionImplementor sessionImplementor =
                mock(SessionImplementor.class);
        IdEntity entity = new IdEntity();
        VTUUID id = (VTUUID) generator.generate(sessionImplementor, entity);
        assertNotNull(id);
    }

    @Test
    public void shouldUseCurrentValueIfIdIsNotNull() {
        IdentifierGenerator generator =
                new ReflectiveVTUUIDIdentifierGenerator();
        SessionImplementor sessionImplementor =
                mock(SessionImplementor.class);
        IdEntity entity = new IdEntity();
        VTUUID id = (VTUUID) generator.generate(sessionImplementor, entity);
        assertNotNull(id);
        entity.setId(id);
        VTUUID nextId = (VTUUID) generator.generate(sessionImplementor, entity);
        assertTrue(nextId == id);
    }

    @Test
    public void shouldGenerateNewValueIfNoIdGetters() {
        IdentifierGenerator generator =
                new ReflectiveVTUUIDIdentifierGenerator();
        SessionImplementor sessionImplementor =
                mock(SessionImplementor.class);
        Date entity = new Date();
        VTUUID id = (VTUUID) generator.generate(sessionImplementor, entity);
        assertNotNull(id);
    }

    class ObjectIdEntity {

        private Serializable objectId;
        private String name;

        public Serializable getObjectId() {
            return objectId;
        }

        public void setObjectId(final Serializable objectId) {
            this.objectId = objectId;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ObjectIdEntity that = (ObjectIdEntity) o;

            if (objectId != null ? !objectId.equals(that.objectId) :
                    that.objectId != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = objectId != null ? objectId.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ObjectIdEntity{" +
                    "objectId=" + objectId +
                    ", name='" + name + '\'' +
                    '}';
        }

    }

    class IdEntity {

        private VTUUID id;
        private VTNumber value;

        public VTUUID getId() {
            return id;
        }

        public void setId(final VTUUID id) {
            this.id = id;
        }

        public VTNumber getValue() {
            return value;
        }

        public void setValue(final VTNumber value) {
            this.value = value;
        }

    }

}

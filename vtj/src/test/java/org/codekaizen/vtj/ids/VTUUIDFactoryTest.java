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
package org.codekaizen.vtj.ids;

import static org.testng.Assert.*;

import java.math.BigInteger;
import org.codekaizen.vtj.AbstractValueTypeFactoryTest;
import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.codekaizen.vtj.time.Clock;
import org.codekaizen.vtj.util.ByteArrayUtils;
import org.testng.annotations.Test;


/**
 * <p>Units tests for {@link VTUUIDFactory}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTUUIDFactoryTest extends AbstractValueTypeFactoryTest {

    /**
     * Creates a new VTUUIDFactoryTest object.
     */
    public VTUUIDFactoryTest() {
        super(VTUUIDFactory.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ver  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected VTUUIDFactory createFactory(final UUIDVersion ver) {
        super.getConfigurationValues().put("java.system.Clock", Clock.system());
        super.getConfigurationValues().put("org.codekaizen.vtj.ids.VTUUIDFactory.version", ver);

        return (VTUUIDFactory) super.createFactory();
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldCreateFactoryGeneratingUuidVersionSpecified() {
        VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        assertEquals(factory.getVersion(), UUIDVersion.TIME_SPACE);
        assertNotNull(factory.getMACAddress());
        factory = createFactory(UUIDVersion.RANDOM);
        assertEquals(factory.getVersion(), UUIDVersion.RANDOM);
        factory = createFactory(UUIDVersion.NAME_MD5);
        assertEquals(factory.getVersion(), UUIDVersion.NAME_MD5);
        factory = createFactory(UUIDVersion.NAME_SHA1);
        assertEquals(factory.getVersion(), UUIDVersion.NAME_SHA1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldCreateFactoryGeneratingTimeVersionIfNotSpecified() {
        // check defaults to TIME_SPACE
        final VTUUIDFactory factory = createFactory(null);
        assertEquals(factory.getVersion(), UUIDVersion.TIME_SPACE);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldCreateFactoryGeneratingTimeVersionIfNoContextSupplied() {
        // check defaults to TIME_SPACE
        final VTUUIDFactory factory = new VTUUIDFactory(new MapContextHandlingStrategy());
        assertEquals(factory.getVersion(), UUIDVersion.TIME_SPACE);

        final VTUUID uuid = factory.nextUuid();
        assertEquals(uuid.version(), UUIDVersion.TIME_SPACE);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsCreatable() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        assertFalse(factory.isCreatable(VTLongSerialId.class));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestCreateObjectArray() {
        VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        VTUUID uuid = factory.create();
        assertEquals(uuid.version(), UUIDVersion.TIME_SPACE);
        factory = createFactory(UUIDVersion.RANDOM);
        uuid = factory.create();
        assertEquals(uuid.version(), UUIDVersion.RANDOM);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenNameVersionGeneratorWhenNoCreateArgsThenThrowException() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.NAME_SHA1);
        final VTUUID uuid = factory.create();
        assertEquals(uuid.version(), UUIDVersion.NAME_SHA1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenRandomVersionGeneratorWhenTwoArgsThenThrowException() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.RANDOM);
        final VTUUID uuid = factory.create("DomainEntity" + ".urn:uuid:fed27ca6-bdc0-11db-9317-009027861254");
        assertEquals(uuid.version(), UUIDVersion.RANDOM);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void givenUuidArgWhenCreateThenReturnEqualUuid() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        final String s1 = "1cef0eca-3728-11dd-af02-0013723f3004";
        final VTUUID uuid1 = new VTUUID(ByteArrayUtils.toBytes(s1, '-'));
        final VTUUID uuid = factory.create(uuid1);
        assertEquals(uuid, uuid1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void givenValidByteArrayArgWhenCreateThenReturnEquivalentUuid() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        final String s1 = "1cef0eca-3728-11dd-af02-0013723f3004";
        final VTUUID uuid1 = new VTUUID(ByteArrayUtils.toBytes(s1, '-'));
        final VTUUID uuid = factory.create(ByteArrayUtils.toBytes(s1, '-'));
        assertEquals(uuid, uuid1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void givenValidStringArgWhenCreateThenReturnEquivalentUuid() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        final String s1 = "1cef0eca-3728-11dd-af02-0013723f3004";
        final VTUUID uuid1 = new VTUUID(ByteArrayUtils.toBytes(s1, '-'));
        final VTUUID uuid = factory.create(s1);
        assertEquals(uuid, uuid1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void givenUriArgWhenCreateThenReturnValidUuid() {
        VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        factory = createFactory(UUIDVersion.NAME_MD5);

        VTUUID uuid = factory.create("DomainEntity" + ".urn:uuid:fed27ca6-bdc0-11db-9317-009027861254");
        assertEquals(uuid.version(), UUIDVersion.NAME_MD5);
        factory = createFactory(UUIDVersion.NAME_SHA1);
        uuid = factory.create("org.codekaizen.dm.DomainEntity" + ".urn:uuid:fed27ca6-bdc0-11db-9317-009027861254");
        assertEquals(uuid.version(), UUIDVersion.NAME_SHA1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void givenValidBigIntegerArgWhenCreateThenReturnEquivalentUuid() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        final VTUUID uuid2 = new VTUUID(ByteArrayUtils.toBytes("194322d4-3746-11dd-8c65-0013723f3004", '-'));
        final BigInteger bi = new BigInteger("194322d4374611dd8c650013723f3004", 16);
        assertEquals(uuid2.toBigInteger(), bi);

        final VTUUID uuid = factory.create(bi);
        assertEquals(uuid, uuid2);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void givenTwoStringArgsWhenCreateThenReturnValidUuid() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.NAME_MD5);
        final VTUUID uuid = factory.create(UUIDVersion.NAME_SHA1,
                "org.codekaizen.dm.DomainEntity" + ".urn:uuid:fed27ca6-bdc0-11db-9317-009027861254");
        assertEquals(uuid.version(), UUIDVersion.NAME_MD5);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsParsable() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        assertTrue(factory.isParsable("19D9BA1F5B1D4DFC83988980E49B3172"));
        assertTrue(factory.isParsable("d35c0594-8cef-4232-8fc8-f12082d67596"));
        assertTrue(factory.isParsable("{00000300-0000-0000-C000-000000000046}"));
        assertTrue(factory.isParsable("urn:uuid:fed27ca6-bdc0-11db-9317-009027861254"));
        assertFalse(factory.isParsable("19D9BA1F5B1D4DFC83988980E4"));
        assertFalse(factory.isParsable(null));
        assertFalse(factory.isParsable(""));
        assertFalse(factory.isParsable("19D9BA1F5B1D4DFC83988980E49B3172AB"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestParseString() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        VTUUID uuid = factory.parse("19D9BA1F5B1D4DFC83988980E49B3172");
        uuid = factory.parse("d35c0594-8cef-4232-8fc8-f12082d67596");
        assertEquals(VTUUID.VARIANT_RFC4122, uuid.variant());
        assertEquals(UUIDVersion.RANDOM, uuid.version());
        uuid = factory.parse("{00000300-0000-0000-C000-000000000046}");
        assertEquals(VTUUID.VARIANT_MICROSOFT, uuid.variant());
        assertEquals(UUIDVersion.NON_CONFORMANT, uuid.version());
        uuid = factory.parse("urn:uuid:fed27ca6-bdc0-11db-9317-009027861254");
        assertEquals(VTUUID.VARIANT_RFC4122, uuid.variant());
        assertEquals(UUIDVersion.TIME_SPACE, uuid.version());
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenInvalidStringWhenParsingThenThrowException() {
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        final VTUUID uuid = factory.parse("19D9BA1F5B1D4DFC83988980E49B31");
        assertEquals(VTUUID.VARIANT_RFC4122, uuid.variant());
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestFormatValueType() {
        final String s1 = "1cef0eca-3728-11dd-af02-0013723f3004";
        final VTUUID uuid = new VTUUID(ByteArrayUtils.toBytes(s1, '-'));
        final VTUUIDFactory factory = createFactory(UUIDVersion.TIME_SPACE);
        final String s2 = factory.format(uuid);
        assertEquals(s2, s1);
    }

}

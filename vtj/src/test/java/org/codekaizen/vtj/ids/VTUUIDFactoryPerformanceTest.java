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

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.codekaizen.vtj.net.VTURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


/**
 * <p>Performance tests for {@link VTUUIDFactory}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTUUIDFactoryPerformanceTest {

    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    private Logger logger = LoggerFactory.getLogger(VTUUIDFactoryPerformanceTest.class);

    /**
     * Creates a new VTUUIDFactoryPerformanceTest object.
     */
    public VTUUIDFactoryPerformanceTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldGenerateRandomUuidsAsFastAsJdkGenerator() {
        // setup factory
        final VTUUIDFactory factory = new VTUUIDFactory(new MapContextHandlingStrategy());
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("org.codekaizen.vtj.ids.VTUUIDFactory.version", UUIDVersion.RANDOM);
        factory.setContext(map);

        // give them a chance to initialize the random generators
        factory.nextUuid();
        UUID.randomUUID();

        long javaTs = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            UUID.randomUUID();
        }

        javaTs = System.currentTimeMillis() - javaTs;

        long vtTs = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            factory.nextUuid();
        }

        vtTs = System.currentTimeMillis() - vtTs;

        logger.info("java.util.UUID random took {} ms.", javaTs);
        logger.info("VTUUIDFactory random took {} ms.", vtTs);
        assertTrue(vtTs < javaTs * 125L / 100L);

    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldGenerateMd5UuidsAsFastAsJdkGenerator() {
        // setup factory
        final VTUUIDFactory factory = new VTUUIDFactory(new MapContextHandlingStrategy());
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("org.codekaizen.vtj.ids.VTUUIDFactory.version", UUIDVersion.NAME_MD5);
        factory.setContext(map);

        // give them a chance to initialize anything
        factory.nextUuid("http://www.codekaizen.org/valuetypej/");
        UUID.nameUUIDFromBytes("http://www.codekaizen.org/valuetypej/".getBytes());

        final String[] names = new String[100000];
        this.generateNames(names);

        long javaTs = System.currentTimeMillis();

        for (final String name : names) {
            UUID.nameUUIDFromBytes(name.getBytes());
        }

        javaTs = System.currentTimeMillis() - javaTs;

        long vtTs = System.currentTimeMillis();

        for (final String name : names) {
            factory.nextUuid(name);
        }

        vtTs = System.currentTimeMillis() - vtTs;

        logger.info("java.util.UUID name took {} ms.", javaTs);
        logger.info("VTUUIDFactory name took {} ms.", vtTs);
        assertTrue(vtTs < javaTs * 125L / 100L);

    }

    private void generateNames(final String[] names) {
        final VTURI uri = new VTURI("http://www.codekaizen.org/valuetypej/object10000");

        for (int i = 0; i < names.length; i++) {
            names[i] = uri.plus("object" + (10000 + i)).toString();
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNeverGenerateDuplicateTimeBasedUuid() {
        // setup factory
        final VTUUIDFactory factory = new VTUUIDFactory(new MapContextHandlingStrategy());
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("org.codekaizen.vtj.ids.VTUUIDFactory.version", UUIDVersion.TIME_SPACE);
        factory.setContext(map);

        final Set<VTUUID> uuids = new HashSet<VTUUID>();

        for (int i = 0; i < 100000; i++) {
            uuids.add(factory.nextUuid());
        }

        assertEquals(uuids.size(), 100000);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNeverGenerateDuplicateRandomBasedUuid() {
        // setup factory
        final VTUUIDFactory factory = new VTUUIDFactory(new MapContextHandlingStrategy());
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("org.codekaizen.vtj.ids.VTUUIDFactory.version", UUIDVersion.RANDOM);
        factory.setContext(map);

        final Set<VTUUID> uuids = new HashSet<VTUUID>();

        for (int i = 0; i < 100000; i++) {
            uuids.add(factory.nextUuid());
        }

        assertEquals(uuids.size(), 100000);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNeverGenerateDuplicateNameBasedUuid() {
        // setup factory
        final VTUUIDFactory factory = new VTUUIDFactory(new MapContextHandlingStrategy());
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("org.codekaizen.vtj.ids.VTUUIDFactory.version", UUIDVersion.NAME_SHA1);
        factory.setContext(map);

        final String[] names = new String[100000];
        this.generateNames(names);

        final Set<VTUUID> uuids = new HashSet<VTUUID>();

        for (int i = 0; i < 100000; i++) {
            uuids.add(factory.nextUuid(names[i]));
        }

        assertEquals(uuids.size(), 100000);

        // test that same uuids are generated
        for (int i = 0; i < 100000; i++) {
            uuids.add(factory.nextUuid(names[i]));
        }

        assertEquals(uuids.size(), 100000);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void givenTimeBasedGeneratorWhenMultipleThreadsThenNoDeadlocks() {
        final TimeUUIDFactoryRunner[] runners = new TimeUUIDFactoryRunner[3];
        final CyclicBarrier barrier = new CyclicBarrier(runners.length + 1);

        for (int i = 0; i < runners.length; i++) {
            runners[i] = new TimeUUIDFactoryRunner(barrier);
        }

        try {

            for (final TimeUUIDFactoryRunner runner : runners) {
                POOL.execute(runner);
            }

            barrier.await();
            barrier.await();
        } catch (InterruptedException ie) {
            fail(ie.getMessage());
        } catch (BrokenBarrierException ie) {
            fail(ie.getMessage());
        }

        final Set<VTUUID> allUuids = new HashSet<VTUUID>();

        for (final TimeUUIDFactoryRunner runner : runners) {
            allUuids.addAll(runner.uuids);
        }

        assertEquals(allUuids.size(), runners.length * 100000);
    }

    class TimeUUIDFactoryRunner implements Runnable {

        CyclicBarrier barrier;
        VTUUIDFactory factory = new VTUUIDFactory(new MapContextHandlingStrategy());
        Set<VTUUID> uuids = new HashSet<VTUUID>();

        TimeUUIDFactoryRunner(final CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        public void run() {
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("org.codekaizen.vtj.ids.VTUUIDFactory.version", UUIDVersion.TIME_SPACE);
            factory.setContext(map);

            try {
                barrier.await();

                for (int i = 0; i < 100000; i++) {
                    uuids.add(factory.create());
                }

                barrier.await();
            } catch (InterruptedException ie) {
                fail(ie.getMessage());
            } catch (BrokenBarrierException ie) {
                fail(ie.getMessage());
            }
        }

    }

}

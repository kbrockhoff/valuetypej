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
package org.codekaizen.vtj.ids;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


/**
 * <p>Unit performance tests for {@link VTLongSerialIdFactory}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTLongSerialIdFactoryPerformanceTest {

    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    private Logger logger = LoggerFactory.getLogger(VTLongSerialIdFactoryPerformanceTest.class);

    /**
     * Creates a new VTLongSerialIdFactoryPerformanceTest object.
     */
    public VTLongSerialIdFactoryPerformanceTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNeverGenerateDuplicateIdWithMultithreadedUsage() {
        final SerialIdFactoryRunner[] runners = new SerialIdFactoryRunner[3];
        final CyclicBarrier barrier = new CyclicBarrier(runners.length + 1);
        final VTLongSerialIdFactory factory = new VTLongSerialIdFactory(new MapContextHandlingStrategy());

        for (int i = 0; i < runners.length; i++) {
            runners[i] = new SerialIdFactoryRunner(barrier, factory);
        }

        try {

            for (final SerialIdFactoryRunner runner : runners) {
                POOL.execute(runner);
            }

            barrier.await();
            barrier.await();
        } catch (InterruptedException ie) {
            fail(ie.getMessage());
        } catch (BrokenBarrierException ie) {
            fail(ie.getMessage());
        }

        final Set<VTLongSerialId> allIds = new HashSet<VTLongSerialId>();

        for (final SerialIdFactoryRunner runner : runners) {
            allIds.addAll(runner.serialIds);
        }

        assertEquals(allIds.size(), runners.length * 100000);
    }

    class SerialIdFactoryRunner implements Runnable {

        CyclicBarrier barrier;
        VTLongSerialIdFactory factory;
        Set<VTLongSerialId> serialIds = new HashSet<VTLongSerialId>();

        SerialIdFactoryRunner(final CyclicBarrier barrier, final VTLongSerialIdFactory factory) {
            this.barrier = barrier;
            this.factory = factory;
        }

        public void run() {

            try {
                barrier.await();

                for (int i = 0; i < 100000; i++) {
                    serialIds.add(factory.create());
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

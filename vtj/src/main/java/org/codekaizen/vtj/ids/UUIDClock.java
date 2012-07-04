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

import java.util.Random;
import java.util.TimeZone;
import org.codekaizen.vtj.time.Clock;
import org.codekaizen.vtj.time.VTInstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>Produces the time stamps required for time-based UUID/GUID's.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
class UUIDClock extends Clock {

    private static final long serialVersionUID = 5676428495000055993L;

    /**
     * Offset between Java time beginning 1970-01-01 and UUID time beginning at the start of the Gregorian calendar
     * (1582-10-15).
     */
    private static final long CLOCK_OFFSET = 122192928000000000L;

    /** Convert from seconds to 100 nsec. */
    private static final long CLOCK_MULTI = 10000000L;

    /** Convert from nanoseconds to 100 nsec. */
    private static final int NANO_DIV = 100;

    private final Logger logger = LoggerFactory.getLogger(UUIDClock.class);
    private final Random random;
    private final Clock clock;
    private short clockSequence = 0;
    private long lastSysTs = 0L;
    private long lastUsedTs = 0L;

    UUIDClock(final Random random, final Clock clock) {
        this.random = random;

        if (clock == null) {
            this.clock = Clock.system();
        } else {
            this.clock = clock;
        }

        initCounters(this.random);
        this.lastSysTs = 0L;
        this.lastUsedTs = 0L;
    }

    private void initCounters(final Random rnd) {
        this.clockSequence = (short) rnd.nextInt(0x1FFF);
    }

    void getTimestamp(final byte[] uuidData) {

        long systime = this.gregorianInstant();

        // verify that the system time is not going backwards
        if (systime < this.lastSysTs) {
            this.logger.warn("System time going backwards! (got value " + systime + ", last " + this.lastSysTs);
            this.initCounters(this.random);
        }

        this.lastSysTs = systime;

        // increment ahead 100 nsec. if already used this time
        if (systime <= this.lastUsedTs) {

            if (systime > (this.lastSysTs + 10000L)) {
                this.initCounters(this.random);
            } else {
                systime = this.lastUsedTs + 1L;
            }
        }

        this.lastUsedTs = systime;

        final int clockHi = (int) (systime >>> 32);
        final int clockLo = (int) systime;

        uuidData[6] = (byte) (clockHi >>> 24);
        uuidData[7] = (byte) (clockHi >>> 16);
        uuidData[4] = (byte) (clockHi >>> 8);
        uuidData[5] = (byte) clockHi;

        uuidData[0] = (byte) (clockLo >>> 24);
        uuidData[1] = (byte) (clockLo >>> 16);
        uuidData[2] = (byte) (clockLo >>> 8);
        uuidData[3] = (byte) clockLo;

        uuidData[8] = (byte) (this.clockSequence >>> 8);
        uuidData[9] = (byte) this.clockSequence;

    }

    /**
     * Returns the number of hundred nanoseconds since the beginning of the Gregorian calendar on 1582-10-15.
     *
     * @return  the number of 100nsecs. since 1582
     */
    public long gregorianInstant() {
        final VTInstant i = this.clock.instant();
        long result = i.getEpochSeconds() * CLOCK_MULTI;
        result += CLOCK_OFFSET + (i.getNanoOfSecond() / NANO_DIV);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTInstant instant() {
        return this.clock.instant();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public TimeZone timeZone() {
        return this.clock.timeZone();
    }

}

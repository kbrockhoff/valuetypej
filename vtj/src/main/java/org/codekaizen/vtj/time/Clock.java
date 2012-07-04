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
package org.codekaizen.vtj.time;

import java.io.Serializable;
import java.util.TimeZone;


/**
 * <p>A facade for accessing the current time. During normal production use, it will return the system time. During
 * testing, it can be set to a specified time in order to observe and test system behavior at a particular point in the
 * day, week, etc.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class Clock implements Serializable {

    private static final long serialVersionUID = 7986993068524330235L;

    /**
     * Creates a new Clock object.
     */
    protected Clock() {
    }

    /**
     * Returns a clock using the Java system millisecond time and the default time zone.
     *
     * @return  the system clock
     */
    public static Clock system() {
        return new SystemMillisClock();
    }

    /**
     * Returns the current instant on the time-line.
     *
     * @return  the current time
     */
    public abstract VTInstant instant();

    /**
     * Returns the time zone being used by this clock.
     *
     * @return  the time zone
     */
    public abstract TimeZone timeZone();

    /**
     * <p>The default implementation of <code>Clock</code>.</p>
     *
     * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
     */
    private static final class SystemMillisClock extends Clock {

        private static final long serialVersionUID = -3483264661482571228L;

        private final TimeZone timeZone = TimeZone.getDefault();

        @Override
        public VTInstant instant() {
            return new VTInstant(System.currentTimeMillis());
        }

        @Override
        public TimeZone timeZone() {
            return this.timeZone;
        }

    }

}

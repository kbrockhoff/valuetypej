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

import java.util.concurrent.atomic.AtomicLong;
import org.codekaizen.vtj.AbstractVTFactory;
import org.codekaizen.vtj.ContextHandlingStrategy;
import org.codekaizen.vtj.ValueType;


/**
 * <p>Creates, parses and formats {@link VTLongSerialId} instances.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTLongSerialIdFactory extends AbstractVTFactory<VTLongSerialId> {

    private AtomicLong counter = new AtomicLong(0L);

    /**
     * Creates a new VTLongSerialIdFactory object.
     *
     * @param  strategy  DOCUMENT ME!
     */
    public VTLongSerialIdFactory(final ContextHandlingStrategy strategy) {
        super(strategy);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void processContextChange() {
        super.processContextChange();

        final Object val = super.getValue("org.codekaizen.vtj.ids.VTLongSerialIdFactory.initialValue");

        if (val != null) {
            final VTLongSerialId starting = create(val);

            synchronized (this) {
                counter = new AtomicLong(starting.longValue());
            }
        }
    }

    /**
     * Returns the last value generated.
     *
     * @return  the raw value
     */
    public long getLastValue() {
        return counter.longValue();
    }

    /**
     * Returns the next incremented value based on the internal counter.
     *
     * @return  factory-unique incremented id
     */
    public VTLongSerialId nextSerialId() {
        return new VTLongSerialId(counter.incrementAndGet());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  clazz  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isCreatable(final Class<? extends ValueType<?>> clazz) {
        return VTLongSerialId.class.equals(clazz);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    protected VTLongSerialId create(final Object... args) {

        if (args == null || args.length == 0 || args[0] == null) {
            return nextSerialId();
        } else if (args[0] instanceof VTLongSerialId) {
            return ((VTLongSerialId) args[0]).copy();
        } else if (args[0] instanceof Number) {
            return new VTLongSerialId(((Number) args[0]).longValue());
        } else if (args[0] instanceof CharSequence) {
            return parse((CharSequence) args[0]);
        } else {
            throw new IllegalArgumentException("cannot convert to a long");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isParsable(final CharSequence s) {

        if (s == null) {
            return false;
        }

        final String str = stripNonDigits(s);
        boolean result = true;

        for (int i = 0; i < str.length(); i++) {

            if (!Character.isDigit(str.charAt(i))) {
                result = false;

                break;
            }
        }

        return result;
    }

    private String stripNonDigits(final CharSequence s) {
        String str = s.toString();

        if (str.startsWith(VTLongSerialId.SID_PREFIX)) {
            str = str.substring(4);
        }

        if (str.charAt(str.length() - 1) == 'L' || str.charAt(str.length() - 1) == 'l') {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    protected VTLongSerialId doParse(final CharSequence s) {

        if (!isParsable(s)) {
            throw new IllegalArgumentException("unrecognizable format");
        }

        final String str = stripNonDigits(s);

        return new VTLongSerialId(Long.parseLong(str));
    }

}

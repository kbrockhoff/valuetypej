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
package org.codekaizen.vtj.net;

import java.util.HashSet;
import java.util.Set;
import org.codekaizen.vtj.AbstractVTFactory;
import org.codekaizen.vtj.ContextHandlingStrategy;
import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.codekaizen.vtj.ValueType;
import org.codekaizen.vtj.util.ByteArrayUtils;


/**
 * <p>Creates, parses and formats {@link VTMACAddress} instances. It also provides static methods for retrieving the
 * ethernet/MAC address for the computer the JVM is running on.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTMACAddressFactory extends AbstractVTFactory<VTMACAddress> {

    private static final long serialVersionUID = -4415361060679728116L;
    private static Set<VTMACAddress> LOCAL_ADDRESSES;
    private static int ADDRESS_INDEX = 0;

    /**
     * Constructs a value type factory.
     *
     * @param  strategy  the strategy to use for retrieving values and resources from the execution environment
     */
    public VTMACAddressFactory(final ContextHandlingStrategy strategy) {
        super(strategy);
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
        return VTMACAddress.class.equals(clazz);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected VTMACAddress create(final Object... args) {

        if (args.length == 0) {
            return getLocalEthernetAddress();
        } else {
            final Object val = args[0];

            if (val instanceof VTMACAddress) {
                return ((VTMACAddress) val).copy();
            } else if (val instanceof byte[]) {
                return new VTMACAddress((byte[]) val);
            } else if (val instanceof Long) {
                return new VTMACAddress((Long) val);
            } else {
                return this.parse(val.toString());
            }
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

        final String val = stripSeparators(s.toString());

        if (val.length() != 10 && val.length() != 12) {
            return false;
        }

        if (val.charAt(0) != '0' || (val.length() == 12 && val.charAt(1) != '0')) {
            return false;
        }

        return ByteArrayUtils.isValidHexadecimal(val, '\0');
    }

    private String stripSeparators(final String s) {
        String val = s.replace(":", "");
        val = val.replace("-", "");

        return val;
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
    protected VTMACAddress doParse(final CharSequence s) {
        final String val = stripSeparators(s.toString());
        final byte[] raw = new byte[6];

        if (val.length() == 12) {
            ByteArrayUtils.toBytes(val, 0, 12, raw, 0);
        } else if (val.length() == 10) {
            // allow missing first byte
            raw[0] = (byte) 0;
            ByteArrayUtils.toBytes(val, 0, 10, raw, 1);
        } else {
            throw new IllegalArgumentException("must be 5 or 6 bytes");
        }

        return new VTMACAddress(raw);
    }

    /**
     * Returns the MAC addresses for all of the network interface cards (NIC) installed in the host computer.
     *
     * @return  the addresses
     */
    public static synchronized Set<VTMACAddress> getAllLocalEthernetAddresses() {

        if (LOCAL_ADDRESSES == null) {
            LOCAL_ADDRESSES = new HashSet<VTMACAddress>();

            final MACAddressRetriever retriever = new MACAddressRetriever();
            final Set<String> set = retriever.retrieveAddresses();
            final VTMACAddressFactory factory = new VTMACAddressFactory(new MapContextHandlingStrategy());
            IllegalArgumentException exception = null;

            for (final String s : set) {

                try {
                    LOCAL_ADDRESSES.add(factory.parse(s));
                } catch (final IllegalArgumentException vte) {
                    exception = vte;
                }
            }

            if (exception != null && LOCAL_ADDRESSES.isEmpty()) {
                throw exception;
            }
        }

        return LOCAL_ADDRESSES;
    }

    /**
     * Returns the MAC address for one of the host computer's NIC's.
     *
     * @return  the address
     */
    public static VTMACAddress getLocalEthernetAddress() {
        final Set<VTMACAddress> addrs = getAllLocalEthernetAddresses();
        ADDRESS_INDEX++;

        if (ADDRESS_INDEX >= addrs.size()) {
            ADDRESS_INDEX = 0;
        }

        int idx = 0;

        for (final VTMACAddress addr : addrs) {

            if (idx == ADDRESS_INDEX) {
                return addr;
            }

            idx++;
        }

        return null;
    }

}

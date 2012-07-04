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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.xml.namespace.QName;
import org.codekaizen.vtj.AbstractVTFactory;
import org.codekaizen.vtj.ContextHandlingStrategy;
import org.codekaizen.vtj.ValueType;
import org.codekaizen.vtj.net.VTMACAddress;
import org.codekaizen.vtj.net.VTMACAddressFactory;
import org.codekaizen.vtj.net.VTURI;
import org.codekaizen.vtj.time.Clock;
import org.codekaizen.vtj.util.ByteArrayUtils;


/**
 * <p>Creates, parses and formats {@link VTUUID} instances.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTUUIDFactory extends AbstractVTFactory<VTUUID> {

    private static final long serialVersionUID = -1874726544741731581L;

    private UUIDVersion version;
    private VTMACAddress macAddress;
    private VTURI namespaceURI;
    private Clock clock;
    private transient byte[] node;
    private transient VTUUID namespaceUUID;
    private transient UUIDClock uuidClock;
    private transient SecureRandom random;
    private transient MessageDigest digest;
    private final Lock nodeLock = new ReentrantLock();
    private final Lock timerLock = new ReentrantLock();
    private final Lock digestLock = new ReentrantLock();

    /**
     * Constructs a value type factory.
     *
     * @param  strategy  the strategy to use for retrieving values and resources from the execution environment
     */
    public VTUUIDFactory(final ContextHandlingStrategy strategy) {
        super(strategy);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void processContextChange() {
        super.processContextChange();

        this.clearGenerators();

        Object val;
        // set system clock
        val = super.getValue("java.system.Clock");

        if (val == null) {
            this.clock = Clock.system();
        } else {
            this.clock = (Clock) val;
        }
        // set default UUID version
        val = super.getValue("org.codekaizen.vtj.ids.VTUUIDFactory.version");

        if (val instanceof UUIDVersion) {
            this.version = (UUIDVersion) val;
        } else if (val instanceof Number) {
            this.version = UUIDVersion.values()[((Number) val).intValue()];
        } else if (val instanceof CharSequence) {
            this.version = UUIDVersion.valueOf(val.toString());
        } else {
            // default to
            this.version = UUIDVersion.TIME_SPACE;
        }
        // set MAC address/node
        val = super.getValue("org.codekaizen.vtj.ids.VTUUIDFactory.macAddress");
        this.macAddress = null;

        if (val instanceof VTMACAddress) {
            this.macAddress = (VTMACAddress) val;
        } else if (val instanceof CharSequence) {
            final VTMACAddressFactory maf = new VTMACAddressFactory(super.getContextStrategy());
            maf.setContext(super.getContext());
            this.macAddress = maf.parse((CharSequence) val);
        } else {
            this.macAddress = VTMACAddressFactory.getLocalEthernetAddress();
        }
        // set namespaceURI
        val = super.getValue("org.codekaizen.vtj.ids.VTUUIDFactory.namespaceURI");
        this.namespaceURI = null;

        if (val instanceof VTURI) {
            this.namespaceURI = (VTURI) val;
        } else if (val instanceof URI) {
            this.namespaceURI = new VTURI(val.toString());
        } else if (val instanceof CharSequence && val.toString().length() > 0) {
            this.namespaceURI = new VTURI(val.toString());
        }

        this.initializeGenerators();
    }

    private void clearGenerators() {
        this.node = null;
        this.namespaceUUID = null;
        this.uuidClock = null;
        this.random = null;
        this.digest = null;
    }

    private void initializeGenerators() {
        this.timerLock.lock();
        this.getRandomNumberGenerator();
        this.getUUIDClock();
        this.timerLock.unlock();
        this.nodeLock.lock();
        this.getNamespaceNode();
        this.getNamespaceUUID();
        this.nodeLock.unlock();
        this.digestLock.lock();
        this.getDigest();
        this.digestLock.unlock();
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.initializeGenerators();
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * Returns the clock currently in use for time-based UUID generation.
     *
     * @return  the clock
     */
    public Clock getClock() {

        if (this.clock == null) {
            this.clock = Clock.system();
        }

        return this.clock;
    }

    /**
     * Returns the UUID version this factory is currently set to generate.
     *
     * @return  the version the UUID version
     */
    public UUIDVersion getVersion() {

        if (this.version == null) {
            this.version = UUIDVersion.TIME_SPACE;
        }

        return this.version;
    }

    /**
     * Returns one of this machine's ethernet addresses.
     *
     * @return  the address
     */
    public VTMACAddress getMACAddress() {
        return this.macAddress;
    }

    /**
     * Returns the namespace URI this factory is currently set to generate UUID's on.
     *
     * @return  the namespace URI
     */
    public VTURI getNamespaceURI() {
        return this.namespaceURI;
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
        return VTUUID.class.equals(clazz);
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
    protected VTUUID create(final Object... args) {
        Object val;

        switch (args.length) {
        case 0:
            return this.nextUuid();
        case 1:
            val = args[0];

            if (val instanceof VTUUID) {
                return ((VTUUID) val).copy();
            } else if (val instanceof byte[]) {
                return new VTUUID((byte[]) val);
            } else if (val instanceof BigInteger) {
                return new VTUUID(((BigInteger) val).toByteArray());
            } else if ((val instanceof CharSequence) || (val instanceof URI)) {
                final String s = args[0].toString();

                if (this.isParsable(s)) {
                    return this.parse(s);
                } else {
                    return this.nextUuid((VTUUID) null, s);
                }
            } else if (val instanceof QName) {
                final QName qn = (QName) val;

                return this.nextUuid(qn.getNamespaceURI(), qn.getLocalPart());
            } else {
                final Class<?> clz = args[0].getClass();
                String s = null;
                Method m = null;
                final Method[] methods = clz.getMethods();

                for (final Method method : methods) {

                    if ("getObjectId".equals(method.getName())) {
                        m = method;

                        break;
                    } else if ("getId".equals(method.getName())) {
                        m = method;

                        break;
                    }
                }

                if (m == null) {
                    s = args[0].toString();
                } else {

                    try {
                        s = m.invoke(args[0]).toString();
                    } catch (Exception e) {
                        s = args[0].toString();
                    }
                }

                return this.nextUuid(clz.getName(), s);
            }
        case 2:
            val = args[0];

            if (val instanceof VTUUID) {
                return this.nextUuid((VTUUID) val, args[1].toString());
            } else {
                return this.nextUuid(val.toString(), args[1].toString());
            }
        default:
            throw new IllegalArgumentException("too many arguments");
        }
    }

    /**
     * Returns a new time-based or random-based UUID if the factory is configured to provide one of those types of
     * UUID's.
     *
     * @return  a new UUID
     *
     * @throws  IllegalArgumentException  if configured for name-based generation
     */
    public VTUUID nextUuid() {

        switch (this.version) {
        case TIME_SPACE:
            return this.nextTimeBasedUuid();
        case RANDOM:
            return this.nextRandomUuid();
        default:
            throw new IllegalArgumentException("configured for name-based generation");
        }
    }

    /**
     * Returns a new name-based UUID if the factory is configured to provide one of the name-based UUID types.
     *
     * @param  name  the name to hash
     *
     * @return  a new UUID
     */
    public VTUUID nextUuid(final CharSequence name) {
        return this.nextUuid((VTUUID) null, name);
    }

    private VTUUID nextUuid(final CharSequence parent, final CharSequence name) {
        VTUUID uuid = null;

        if (parent != null) {
            uuid = this.nextUuid((VTUUID) null, parent);
        }

        return this.nextUuid(uuid, name);
    }

    private VTUUID nextUuid(final VTUUID parent, final CharSequence name) {

        switch (this.version) {
        case NAME_MD5:
        case NAME_SHA1:

            VTUUID p = parent;

            if (p == null) {
                p = this.getNamespaceUUID();
            }

            final byte[] contents;
            this.digestLock.lock();
            this.getDigest().reset();

            if (p != null) {
                this.getDigest().update(parent.toByteArray());
            }

            this.getDigest().update(name.toString().getBytes());
            contents = this.getDigest().digest();
            this.digestLock.unlock();
            this.addVersionAndVariant(contents, this.version.ordinal());

            return new VTUUID(contents);
        default:
            throw new IllegalArgumentException("not configured for name-based generation");
        }
    }

    private UUIDClock getUUIDClock() {

        if (this.uuidClock == null) {
            // reseed random generator
            final SecureRandom rnd = this.getRandomNumberGenerator();
            final byte[] seed = rnd.generateSeed(16);
            this.getRandomNumberGenerator().setSeed(seed);
            this.uuidClock = new UUIDClock(rnd, this.getClock());
        }

        return this.uuidClock;
    }

    private SecureRandom getRandomNumberGenerator() {

        if (this.random == null) {
            this.random = new SecureRandom();
        }

        return this.random;
    }

    private MessageDigest getDigest() {

        try {

            if (this.digest == null) {

                if (this.getVersion().equals(UUIDVersion.NAME_SHA1)) {
                    this.digest = MessageDigest.getInstance("sha1");
                } else {
                    this.digest = MessageDigest.getInstance("md5");
                }
            }
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalStateException("unable to obtain hasher", nsae);
        }

        return this.digest;
    }

    private VTUUID getNamespaceUUID() {

        if (this.namespaceUUID == null && this.getNamespaceURI() != null) {
            final byte[] ns = this.createNamespaceNode(this.getNamespaceURI().toString());
            final byte[] b = new byte[16];
            System.arraycopy(ns, 0, b, 0, 16);
            this.namespaceUUID = new VTUUID(b);
        }

        return this.namespaceUUID;
    }

    private void addNodeBytes(final byte[] dest) {
        final byte[] node = this.getNamespaceNode();
        System.arraycopy(node, 0, dest, 10, 6);
    }

    private void addVersionAndVariant(final byte[] contents, final int ver) {
        contents[6] &= (byte) 0x0F;
        contents[6] |= (byte) (ver << 4);
        // Variant masks first two bits of the clock_seq_hi:
        contents[8] &= (byte) 0x3F;
        contents[8] |= (byte) 0x80;
    }

    private byte[] getNamespaceNode() {

        if (this.node == null) {

            if (this.getMACAddress() == null) {
                final StringBuilder sb = new StringBuilder();

                try {
                    sb.append(InetAddress.getLocalHost().getCanonicalHostName());
                } catch (UnknownHostException e) {
                    sb.append("localhost.localdomain");
                }

                sb.append(super.toString());
                this.node = this.createNamespaceNode(sb.toString());
            } else {
                this.node = this.getMACAddress().toByteArray();
            }
        }

        return this.node;
    }

    private byte[] createNamespaceNode(final String name) {
        final byte[] ba = new byte[256];
        final char[] ca = name.toCharArray();
        final int len = ca.length * 2;

        for (int i = 0; i < ca.length; i++) {
            ba[i * 2] = (byte) (ca[i] >> 8);
            ba[i * 2 + 1] = (byte) ca[i];
        }

        this.getDigest().reset();
        this.getDigest().update(ba, 0, len);

        return this.getDigest().digest();
    }

    private VTUUID nextTimeBasedUuid() {
        final byte[] contents = new byte[16];
        this.addNodeBytes(contents);
        this.timerLock.lock();
        this.getUUIDClock().getTimestamp(contents);
        this.timerLock.unlock();
        this.addVersionAndVariant(contents, UUIDVersion.TIME_SPACE.ordinal());

        return new VTUUID(contents);
    }

    private VTUUID nextRandomUuid() {
        final byte[] contents = new byte[16];
        this.getRandomNumberGenerator().nextBytes(contents);
        this.addVersionAndVariant(contents, UUIDVersion.RANDOM.ordinal());

        return new VTUUID(contents);
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

        String id = s.toString();

        if (id.startsWith("urn:uuid:")) {
            id = id.substring("urn:uuid:".length());
        }

        if (id.length() == 38) {
            id = id.substring(1, 37);  // remove Microsoft {}
        }

        switch (id.length()) {
        case 32:

            if (!ByteArrayUtils.isValidHexadecimal(id, '\0')) {
                return false;
            }

            break;
        case 36:

            if (!ByteArrayUtils.isValidHexadecimal(id, '-')) {
                return false;
            }

            for (int i = 0; i < id.length(); i++) {

                switch (i) {
                case 8:
                case 13:
                case 18:
                case 23:

                    if (id.charAt(i) != '-') {
                        return false;
                    }

                    break;
                }
            }

            break;
        default:
            return false;
        }

        return true;
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
    protected VTUUID doParse(final CharSequence s) {

        String id = s.toString();

        if (id.startsWith("urn:uuid:")) {
            id = id.substring("urn:uuid:".length());
        }

        if (id.length() == 38) {
            id = id.substring(1, 37);  // remove Microsoft {}
        }

        if (id.length() == 32) {
            return parseUnformatted(id);
        } else if (id.length() == 36) {
            return parseFormatted(id);
        } else {
            throw new IllegalArgumentException("unrecognizable format");
        }
    }

    private VTUUID parseFormatted(final String id) {
        return new VTUUID(ByteArrayUtils.toBytes(id, '-'));
    }

    private VTUUID parseUnformatted(final String id) {
        return new VTUUID(ByteArrayUtils.toBytes(id));
    }

}

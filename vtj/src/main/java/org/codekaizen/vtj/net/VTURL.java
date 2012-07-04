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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.text.VTString;


/**
 * <p>Represents a Uniform Resource Locator.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTURL extends VTURI {

    private static final long serialVersionUID = 7514200509941132185L;

    private transient URL url;

    /**
     * Constructs a URL name object.
     *
     * @param  fullName  the complete name
     */
    public VTURL(final String fullName) {
        super(fullName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fullName  DOCUMENT ME!
     * @param  nameParts  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    protected String parseName(final String fullName, final List<String> nameParts) {
        final String s = super.parseName(fullName, nameParts);
        AssertPrecondition.notNull("scheme", getScheme());

        try {
            this.url = new URL(s);
        } catch (MalformedURLException mue) {
            throw new IllegalArgumentException(mue.getMessage());
        }

        return s;
    }

    /**
     * Returns the transport protocol abbreviation.
     *
     * @return  the protocol
     */
    public VTString getProtocol() {
        return this.getScheme();
    }

    /**
     * Returns the default port number for the protocol of this URL.
     *
     * @return  the port number
     */
    public VTString getDefaultPort() {
        return new VTString(Integer.toString(this.url.getDefaultPort()));
    }

    /**
     * Opens a connection to this URL and returns an <code>InputStream</code> for reading from that connection.
     *
     * @return  the stream
     *
     * @throws  IOException  if unable to open the stream
     */
    public InputStream openStream() throws IOException {
        return this.url.openStream();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  namePart  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTURI plus(final CharSequence namePart) {
        return new VTURL(super.plus(namePart).toString());
    }

}

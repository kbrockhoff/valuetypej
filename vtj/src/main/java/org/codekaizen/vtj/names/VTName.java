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
package org.codekaizen.vtj.names;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.VT;
import org.codekaizen.vtj.text.VTString;
import org.codekaizen.vtj.util.StringUtils;


/**
 * <p>Represents a name identifying a particular entity.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class VTName<T> extends VT<T> implements CharSequence, Iterable<VTString> {

    private static final long serialVersionUID = 3213230432383297771L;

    private String fullName;
    private int hashMultiplier;
    private transient List<String> nameParts;

    /**
     * Constructs a name.
     *
     * @param  fullName  the formatted full name
     * @param  hashMultiplier  the prime value to multiply the fullName hash code by to get the hash code for this name
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    protected VTName(final String fullName, final int hashMultiplier) {
        this.validateNotBlank(fullName, "fullName");

        if (!getPattern().matcher(fullName).matches()) {
            throw new IllegalArgumentException("does not match regular expression");
        }

        this.hashMultiplier = hashMultiplier;
        this.nameParts = new ArrayList<String>();
        this.fullName = this.parseName(fullName, this.nameParts);

        if (this.nameParts.size() == 0) {
            throw new IllegalArgumentException("must have at least one name part");
        }
    }

    /**
     * Validates the string is not <code>null</code> and not <code>length() == 0</code>.
     *
     * @param  value  the string to validate
     * @param  name  the parameter name
     *
     * @throws  IllegalArgumentException  if not valid
     */
    protected void validateNotBlank(final CharSequence value, final String name) throws IllegalArgumentException {
        AssertPrecondition.notBlank(name, value);
    }

    /**
     * Parses the formatted full name into its component parts.
     *
     * @param  fullName  the formatted full name
     * @param  nameParts  the list the parts should be stored in from first to last
     *
     * @return  the formatted full name
     */
    protected abstract String parseName(final String fullName, final List<String> nameParts);

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        final String nme = (String) s.readObject();
        this.hashMultiplier = s.readInt();
        this.validateNotBlank(nme, "fullName");
        this.nameParts = new ArrayList<String>();
        this.fullName = this.parseName(nme, this.nameParts);
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.writeObject(this.fullName);
        s.writeInt(this.hashMultiplier);
    }

    /**
     * Returns the formatted full name.
     *
     * @return  the name
     */
    protected VTString getFullName() {
        return new VTString(this.fullName);
    }

    /**
     * Returns the regular expression defining the allowable format for this name.
     *
     * @return  the regex pattern
     */
    public abstract Pattern getPattern();

    /**
     * Adds a name part onto this name and returns the new compound name.
     *
     * @param  namePart  the name part to add
     *
     * @return  the new name
     */
    public abstract T plus(CharSequence namePart);

    /**
     * Determines whether this name starts with a specified prefix.
     *
     * @param  namePart  the name part to check
     *
     * @return  true if <code>namePart</code> is a prefix of this name, false otherwise
     */
    public boolean startsWith(final CharSequence namePart) {

        if (namePart == null) {
            return false;
        }

        return this.nameParts.get(0).equals(namePart.toString());
    }

    /**
     * Determines whether this name ends with a specified suffix.
     *
     * @param  namePart  the name part to check
     *
     * @return  true if <code>namePart</code> is a suffix of this name, false otherwise
     */
    public boolean endsWith(final CharSequence namePart) {

        if (namePart == null) {
            return false;
        }

        return this.nameParts.get(this.nameParts.size() - 1).equals(namePart.toString());
    }

    /**
     * Returns the number of components in this name.
     *
     * @return  the parts count
     */
    public int size() {
        return this.nameParts.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public char charAt(final int index) {
        return this.fullName.charAt(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int length() {
        return this.fullName.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  start  DOCUMENT ME!
     * @param  end  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CharSequence subSequence(final int start, final int end) {
        return new VTString(this.fullName.subSequence(start, end));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Iterator<VTString> iterator() {
        final List<VTString> list = new ArrayList<VTString>();

        for (final String part : this.nameParts) {
            list.add(new VTString(part));
        }

        return list.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return this.hashMultiplier * this.fullName.hashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  obj  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof VTName)) {
            return false;
        }

        final VTName<?> other = (VTName<?>) obj;

        return this.fullName.equals(other.fullName) && this.hashMultiplier == other.hashMultiplier;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return this.fullName;
    }

}

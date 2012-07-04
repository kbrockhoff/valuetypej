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

import static org.testng.Assert.*;

import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTLdapName}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTLdapNameTest extends AbstractNameTypeTest {

    /**
     * Creates a new VTLdapNameTest object.
     */
    public VTLdapNameTest() {
        super(VTLdapName.class, new Class<?>[] { String.class, },
            new Object[] { "cn=Kevin Brockhoff,ou=People,dc=codekaizen,dc=org", });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getValidPatterns() {
        return new String[] {
                "dc=codekaizen,dc=org", "cn=Kevin Brockhoff,ou=People,dc=codekaizen,dc=org",
                "cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org",
            };
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getInvalidPatterns() {
        return new String[] { "", "codekaizen.org", };
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldConstructSuccessfullyForValidLdapName() {
        final VTLdapName ln = new VTLdapName("dc=codekaizen,dc=org");
        assertEquals(ln.toString(), "dc=codekaizen,dc=org");
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullConstructorString() {
        new VTLdapName(null);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForBlankConstructorString() {
        new VTLdapName("");
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidLdapNameConstructorString() {
        final VTLdapName ln = new VTLdapName("codekaizen,org");
        assertTrue(ln.size() > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldAddNamePartsAsExpectedUsingFluentAPI() {
        VTLdapName ln = new VTLdapName("dc=codekaizen,dc=org");
        ln = ln.plus("cn=java").plus("cn=jdbc").plus("cn=TestDS");
        assertEquals(ln.toString(), "cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementStartsWithAsExpectedUsingFluentAPI() {
        final VTLdapName ln = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertTrue(ln.startsWith("dc=org"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementEndsWithAsExpectedUsingFluentAPI() {
        final VTLdapName ln = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertTrue(ln.endsWith("cn=TestDS"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldReturnNumberOfNamePartsAsSize() {
        final VTLdapName ln = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertEquals(ln.size(), 5);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementCharAtPerCharSequenceAPI() {
        final VTLdapName ln = new VTLdapName("dc=codekaizen,dc=org");
        assertEquals(ln.charAt(0), 'd');
        assertEquals(ln.charAt(10), 'z');
        assertEquals(ln.charAt(15), 'c');
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementLengthPerCharSequenceAPI() {
        final VTLdapName ln = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertEquals(ln.length(), 46);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementSubSequencePerCharSequenceAPI() {
        final VTLdapName ln = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        final VTString s = (VTString) ln.subSequence(26, 46);
        assertEquals(s.toString(), "dc=codekaizen,dc=org");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldIterateOverNameParts() {
        final VTLdapName ln = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");

        for (final VTString s : ln) {
            assertTrue(s.toString().indexOf('=') != -1);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTLdapName ln1 = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        VTLdapName ln2 = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertTrue(ln1.compareTo(ln2) == 0);
        ln2 = new VTLdapName("cn=ProdDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertTrue(ln1.compareTo(ln2) > 0);
        ln2 = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=aodekaizen,dc=org");
        assertTrue(ln1.compareTo(ln2) > 0);
        ln2 = new VTLdapName("cn=ZestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertTrue(ln1.compareTo(ln2) < 0);
        ln2 = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=zodekaizen,dc=org");
        assertTrue(ln1.compareTo(ln2) < 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTLdapName ln1 = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        final VTLdapName ln2 = new VTLdapName("cn=ProdDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertFalse(ln1.equals(ln2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTLdapName ln = new VTLdapName("cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
        assertEquals(ln.toString(), "cn=TestDS,cn=jdbc,cn=java,dc=codekaizen,dc=org");
    }

}

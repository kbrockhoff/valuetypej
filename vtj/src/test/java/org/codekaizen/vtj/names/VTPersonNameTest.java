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

import java.util.Locale;
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTPersonName}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTPersonNameTest extends AbstractNameTypeTest {

    /**
     * Creates a new VTPersonNameTest object.
     */
    public VTPersonNameTest() {
        super(VTPersonName.class, new Class<?>[] { String.class, }, new Object[] { "Kevin W. Brockhoff", });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getValidPatterns() {
        return new String[] {
                "Wang Wei", "Mr. John Wayne Smith, Jr.", "Smith, Jr., Mr. John Wayne", "Smith, Jr., John Wayne",
                "Smith, Mr. John Wayne", "Smith, Jr., Mr. John", "DR RANDALL R JONES", "Cher",
            };
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String[] getInvalidPatterns() {
        return new String[] { "", "#&*@&$", "19482 2934jdiw", "Don Y*&#ng", };
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldConstructSuccessfullyIfSuppliedValidPersonName() {
        VTPersonName nme = new VTPersonName("Mr. John Wayne Smith, Jr.");
        assertEquals(nme.getGivenName(), new VTString("John"));
        assertEquals(nme.getFamilyName(), new VTString("Smith"));
        assertEquals(nme.getMiddleName(), new VTString("Wayne"));
        assertEquals(nme.getTitle(), new VTString("Mr."));
        assertEquals(nme.getSuffix(), new VTString("Jr."));
        nme = new VTPersonName("Smith, Jr., Mr. John Wayne");
        assertEquals(nme.getGivenName(), new VTString("John"));
        assertEquals(nme.getFamilyName(), new VTString("Smith"));
        assertEquals(nme.getMiddleName(), new VTString("Wayne"));
        assertEquals(nme.getTitle(), new VTString("Mr."));
        assertEquals(nme.getSuffix(), new VTString("Jr."));
        nme = new VTPersonName("Smith, Jr., John Wayne");
        assertEquals(nme.getGivenName(), new VTString("John"));
        assertEquals(nme.getFamilyName(), new VTString("Smith"));
        assertEquals(nme.getMiddleName(), new VTString("Wayne"));
        assertNull(nme.getTitle());
        assertEquals(nme.getSuffix(), new VTString("Jr."));
        nme = new VTPersonName("Smith, Mr. John Wayne");
        assertEquals(nme.getGivenName(), new VTString("John"));
        assertEquals(nme.getFamilyName(), new VTString("Smith"));
        assertEquals(nme.getMiddleName(), new VTString("Wayne"));
        assertEquals(nme.getTitle(), new VTString("Mr."));
        assertNull(nme.getSuffix());
        nme = new VTPersonName("Smith, Jr., Mr. John");
        assertEquals(nme.getGivenName(), new VTString("John"));
        assertEquals(nme.getFamilyName(), new VTString("Smith"));
        assertNull(nme.getMiddleName());
        assertEquals(nme.getTitle(), new VTString("Mr."));
        assertEquals(nme.getSuffix(), new VTString("Jr."));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBlankConstructorString() {
        new VTPersonName("");
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldRecognizeGivenNameLocationBasedOnLocale() {
        VTPersonName nme = new VTPersonName("Mr. John Wayne Smith, Jr.", Locale.US);
        assertEquals(nme.getGivenName(), new VTString("John"));
        nme = new VTPersonName("Wang Wei", Locale.CHINA);
        assertEquals(nme.getGivenName(), new VTString("Wei"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldAssignNamePartConstructorAgrumentsToCorrectFields() {
        VTPersonName nme = new VTPersonName("Mr.", "John", "Wayne", "Smith", "Jr.");
        assertEquals(nme.getGivenName(), new VTString("John"));
        assertEquals(nme.getFamilyName(), new VTString("Smith"));
        assertEquals(nme.getMiddleName(), new VTString("Wayne"));
        assertEquals(nme.getTitle(), new VTString("Mr."));
        assertEquals(nme.getSuffix(), new VTString("Jr."));
        nme = new VTPersonName(null, "John", null, "Smith", null);
        assertEquals(nme.getGivenName(), new VTString("John"));
        assertEquals(nme.getFamilyName(), new VTString("Smith"));
        assertNull(nme.getMiddleName());
        assertNull(nme.getTitle());
        assertNull(nme.getSuffix());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldIdentifyTitleFromListIfSupplied() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.getTitle(), new VTString("DR"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldIdentifyGivenNameAsSecondPartIfTitleSupplied() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.getGivenName(), new VTString("RANDALL"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldIdentifyMiddleInitialAsThirdPartIfTitleSupplied() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.getMiddleName(), new VTString("R"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldIdentifyFamilyNameAaFourthPartIfTitleSupplied() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.getFamilyName(), new VTString("JONES"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldIdentifySuffixAsFifthPartIfTitleSupplied() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertNull(nme.getSuffix());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldPlaceFamilyNameFirstInSortFormattedName() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.getSortFormattedName(), new VTString("JONES, RANDALL R"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldAddNamePartsAsExpectedUsingFluentAPI() {
        VTPersonName nme = new VTPersonName("Cher");
        assertEquals(nme.getGivenName(), new VTString("Cher"));
        assertEquals(nme.getFamilyName(), new VTString("Cher"));
        nme = nme.plus("Sarkisian");
        assertEquals(nme.getGivenName(), new VTString("Cher"));
        assertEquals(nme.getFamilyName(), new VTString("Sarkisian"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldReturnFullNameMatchingFullNameSuppliedInConstructor() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.getFullName(), new VTString("DR RANDALL R JONES"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementStartsWithAsExpectedUsingFluentAPI() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertTrue(nme.startsWith(new VTString("RANDALL")));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementEndsWithAsExpectedUsingFluentAPI() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertTrue(nme.endsWith(new VTString("JONES")));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldReturnNumberOfNamePartsAsSize() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.size(), 4);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementCharAtPerCharSequenceAPI() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.charAt(0), 'D');
        assertEquals(nme.charAt(3), 'R');
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementLengthPerCharSequenceAPI() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.length(), 18);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldImplementSubSequencePerCharSequenceAPI() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        final VTString first = (VTString) nme.subSequence(3, 10);
        assertEquals(first, new VTString("RANDALL"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void shouldIterateOverNameParts() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");

        for (final VTString namePart : nme) {
            assertTrue(namePart.length() > 0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTPersonName nme1 = new VTPersonName("DR RANDALL R JONES");
        VTPersonName nme2 = new VTPersonName("DR RANDALL R JONES");
        assertTrue(nme1.compareTo(nme2) == 0);
        nme2 = new VTPersonName("ZETA JONES");
        assertTrue(nme1.compareTo(nme2) < 0);
        nme2 = new VTPersonName("MARION JONES");
        assertTrue(nme1.compareTo(nme2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTPersonName nme1 = new VTPersonName("DR RANDALL R JONES");
        final VTPersonName nme2 = new VTPersonName("RANDY R JONES");
        assertFalse(nme1.equals(nme2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTPersonName nme = new VTPersonName("DR RANDALL R JONES");
        assertEquals(nme.toString(), "DR RANDALL R JONES");
    }

}

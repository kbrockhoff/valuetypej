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
package org.codekaizen.vtj.geom;

import static org.testng.Assert.*;

import org.codekaizen.vtj.AbstractValueTypeTest;
import org.codekaizen.vtj.math.VTDecimal;
import org.codekaizen.vtj.math.VTDouble;
import org.codekaizen.vtj.math.VTNumber;
import org.testng.annotations.Test;


/**
 * <p>Units tests for {@link VTCoordinate}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTCoordinateTest extends AbstractValueTypeTest {

    /**
     * Creates a new VTCoordinateTest object.
     */
    public VTCoordinateTest() {
        super(VTCoordinate.class, new Class<?>[] { VTNumber.class, VTNumber.class, VTNumber.class, },
            new Object[] { new VTDouble(-96.6839), new VTDouble(32.767), new VTDouble(169.2), });
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTCoordinateVTNumberOfQVTNumberOfQVTNumberOfQ() {
        final VTDecimal lon = new VTDecimal(-966839L, 4);
        final VTDecimal lat = new VTDecimal(327670, 4);
        final VTDecimal ele = new VTDecimal(1692, 1);
        final VTCoordinate coor = new VTCoordinate(lon, lat, ele);
        assertEquals(coor.getLongitude(), lon);
        assertEquals(coor.getLatitude(), lat);
        assertEquals(coor.getElevation(), ele);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testVTCoordinateVTNumberOfQVTNumberOfQ() {
        final VTDecimal lon = new VTDecimal(-96683900L, 6);
        final VTDecimal lat = new VTDecimal(32767000, 6);
        final VTCoordinate coor = new VTCoordinate(lon, lat);
        assertEquals(coor.getLongitude(), lon);
        assertEquals(coor.getLatitude(), lat);
        // sea level
        assertEquals(coor.getElevation(), new VTDecimal(0L, 1));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testVTCoordinateVTNumberBadLatitude() {
        final VTDecimal lon = new VTDecimal(-96683900L, 6);
        final VTDecimal lat = new VTDecimal(132767000, 6);
        final VTCoordinate coor = new VTCoordinate(lon, lat);
        assertEquals(coor.getLongitude(), lon);
        assertEquals(coor.getLatitude(), lat);
        // sea level
        assertEquals(coor.getElevation(), new VTDecimal(0L, 1));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testVTCoordinateVTNumberBadLongitude() {
        final VTDecimal lon = new VTDecimal(-196683900L, 6);
        final VTDecimal lat = new VTDecimal(32767000, 6);
        final VTCoordinate coor = new VTCoordinate(lon, lat);
        assertEquals(coor.getLongitude(), lon);
        assertEquals(coor.getLatitude(), lat);
        // sea level
        assertEquals(coor.getElevation(), new VTDecimal(0L, 1));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testVTCoordinateVTNumberBadElevation() {
        final VTDecimal lon = new VTDecimal(-966839L, 4);
        final VTDecimal lat = new VTDecimal(327670, 4);
        final VTDecimal ele = new VTDecimal(-20000, 0);
        final VTCoordinate coor = new VTCoordinate(lon, lat, ele);
        assertEquals(coor.getLongitude(), lon);
        assertEquals(coor.getLatitude(), lat);
        assertEquals(coor.getElevation(), ele);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testVTCoordinateVTNumberNullElevation() {
        final VTDecimal lon = new VTDecimal(-966839L, 4);
        final VTDecimal lat = new VTDecimal(327670, 4);
        final VTCoordinate coor = new VTCoordinate(lon, lat, null);
        assertEquals(coor.getLongitude(), lon);
        assertEquals(coor.getLatitude(), lat);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doCompareToTesting() {
        final VTDecimal lon = new VTDecimal(-966839L, 4);
        final VTDecimal lat = new VTDecimal(327670, 4);
        final VTDecimal ele = new VTDecimal(1692, 1);
        final VTCoordinate coor1 = new VTCoordinate(lon, lat, ele);
        VTCoordinate coor2 = new VTCoordinate(lon, lat, ele);
        assertTrue(coor1.compareTo(coor2) == 0);
        coor2 = new VTCoordinate(new VTDecimal(966839L, 4), lat, ele);
        assertTrue(coor1.compareTo(coor2) < 0);
        coor2 = new VTCoordinate(new VTDecimal(-966840L, 4), lat, ele);
        assertTrue(coor1.compareTo(coor2) > 0);
        coor2 = new VTCoordinate(lon, new VTDecimal(40, 0), ele);
        assertTrue(coor1.compareTo(coor2) < 0);
        coor2 = new VTCoordinate(lon, new VTDecimal(-40, 0), ele);
        assertTrue(coor1.compareTo(coor2) > 0);
        coor2 = new VTCoordinate(lon, lat, new VTDecimal(4000, 1));
        assertTrue(coor1.compareTo(coor2) < 0);
        coor2 = new VTCoordinate(lon, lat, new VTDecimal(-4000, 1));
        assertTrue(coor1.compareTo(coor2) > 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void doNonEqualsTesting() {
        final VTDecimal lon = new VTDecimal(-966839L, 4);
        final VTDecimal lat = new VTDecimal(327670, 4);
        final VTDecimal ele = new VTDecimal(1692, 1);
        final VTCoordinate coor1 = new VTCoordinate(lon, lat, ele);
        VTCoordinate coor2 = new VTCoordinate(lon, lat);
        assertFalse(coor1.equals(coor2));
        coor2 = new VTCoordinate(lon, new VTDecimal(327770, 4), ele);
        assertFalse(coor1.equals(coor2));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doToStringTesting() {
        final VTDecimal lon = new VTDecimal(-966839L, 4);
        final VTDecimal lat = new VTDecimal(327670, 4);
        final VTDecimal ele = new VTDecimal(1692, 1);
        VTCoordinate coor = new VTCoordinate(lon, lat, ele);
        assertEquals(coor.toString(), "32\u00B0 46\' 1.2000\" N 96\u00B0 41\' 2.0400\" W 169.2 m");
        coor = new VTCoordinate(lon, lat);
        assertEquals(coor.toString(), "32\u00B0 46\' 1.2000\" N 96\u00B0 41\' 2.0400\" W");
    }

}

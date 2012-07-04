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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.VT;
import org.codekaizen.vtj.math.VTDecimal;
import org.codekaizen.vtj.math.VTNumber;


/**
 * <p>Defines a single point (longitude/latitude/elevation) on the earth's surface using the geographic coordinate
 * system.</p>
 *
 * <p>This coordinate point should be assumed to be based on the North American Datum of 1983 unless otherwise
 * specified.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTCoordinate extends VT<VTCoordinate> {

    private static final long serialVersionUID = -8893570697436003236L;
    private static final NumberFormat FMT = new DecimalFormat("0.0000");
    private static final NumberFormat FMTE = new DecimalFormat("0.0");
    private static final NumberFormat FMTX = new DecimalFormat("0.000000");
    private static final VTDecimal SEA_LEVEL = new VTDecimal(0L, 1);

    private final VTNumber<?> longitude;
    private final VTNumber<?> latitude;
    private final VTNumber<?> elevation;

    /**
     * Creates a new VTCoordinate object.
     *
     * @param  longitude  DOCUMENT ME!
     * @param  latitude  DOCUMENT ME!
     * @param  elevation  DOCUMENT ME!
     */
    public VTCoordinate(final VTNumber<?> longitude, final VTNumber<?> latitude, final VTNumber<?> elevation) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        validateFields();
    }

    /**
     * Creates a new VTCoordinate object.
     *
     * @param  longitude  DOCUMENT ME!
     * @param  latitude  DOCUMENT ME!
     */
    public VTCoordinate(final VTNumber<?> longitude, final VTNumber<?> latitude) {
        this(longitude, latitude, SEA_LEVEL);
    }

    private void validateFields() {
        AssertPrecondition.notNull("longitude", longitude);
        AssertPrecondition.withinRange("longitude", longitude.doubleValue(), -180.0, 180.0);
        AssertPrecondition.notNull("latitude", latitude);
        AssertPrecondition.withinRange("latitude", latitude.doubleValue(), -90.0, 90.0);
        // Dead Sea to Mt. Everest
        AssertPrecondition.notNull("elevation", elevation);
        AssertPrecondition.withinRange("elevation", elevation.doubleValue(), -418.0, 8848.0);
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        validateFields();
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * Returns the longitude.
     *
     * @return  the longitude
     */
    public VTNumber<?> getLongitude() {
        return longitude;
    }

    /**
     * Returns the latitude.
     *
     * @return  the latitude
     */
    public VTNumber<?> getLatitude() {
        return latitude;
    }

    /**
     * Returns the elevation.
     *
     * @return  the elevation
     */
    public VTNumber<?> getElevation() {
        return elevation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTCoordinate copy() {
        return new VTCoordinate(longitude, latitude, elevation);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTCoordinate other) {
        double result = getLongitude().doubleValue() - other.getLongitude().doubleValue();

        if (result == 0.0) {
            result = getLatitude().doubleValue() - other.getLatitude().doubleValue();
        }

        if (result == 0.0) {
            result = getElevation().doubleValue() - other.getElevation().doubleValue();
        }

        if (result > 0.0) {
            return 1;
        } else if (result < 0.0) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 0;
        result = prime * result + ((elevation == null) ? 0 : elevation.hashCode());
        result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
        result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());

        return result;
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

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof VTCoordinate)) {
            return false;
        }

        final VTCoordinate other = (VTCoordinate) obj;

        return getLatitude().equals(other.getLatitude()) && getLongitude().equals(other.getLongitude()) &&
                getElevation().equals(other.getElevation());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(((int) Math.abs(latitude.doubleValue())));
        builder.append("\u00B0 ");

        int m = ((int) (Math.abs(latitude.doubleValue()) * 60.0)) % 60;
        builder.append(m);
        builder.append("\' ");

        double mm = Math.abs(latitude.doubleValue()) * 60.0;
        String s = FMTX.format(mm);
        mm = Double.parseDouble(s.substring(s.indexOf('.')));
        mm *= 60.0;
        builder.append(FMT.format(mm));

        if (latitude.doubleValue() < 0.0) {
            builder.append("\" S ");
        } else {
            builder.append("\" N ");
        }

        builder.append(((int) Math.abs(longitude.doubleValue())));
        builder.append("\u00B0 ");
        m = ((int) (Math.abs(longitude.doubleValue()) * 60.0)) % 60;
        builder.append(m);
        builder.append("\' ");
        mm = Math.abs(longitude.doubleValue()) * 60.0;
        s = FMTX.format(mm);
        mm = Double.parseDouble(s.substring(s.indexOf('.')));
        mm *= 60.0;
        builder.append(FMT.format(mm));

        if (longitude.doubleValue() < 0.0) {
            builder.append("\" W");
        } else {
            builder.append("\" E");
        }

        if (!SEA_LEVEL.equals(elevation)) {
            builder.append(" ");
            builder.append(FMTE.format(elevation.doubleValue()));
            builder.append(" m");
        }

        return builder.toString();
    }

}

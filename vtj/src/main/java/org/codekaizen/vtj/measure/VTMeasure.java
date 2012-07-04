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
package org.codekaizen.vtj.measure;

import java.math.RoundingMode;
import org.codekaizen.vtj.math.VTDecimal;
import org.codekaizen.vtj.math.VTDouble;
import org.codekaizen.vtj.math.VTInteger;
import org.codekaizen.vtj.math.VTNumber;
import org.codekaizen.vtj.measure.units.Unit;


/**
 * <p>Represents a measurement stated in a known unit.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTMeasure extends VTNumber<VTMeasure> {

    private static final long serialVersionUID = -5643180102398995539L;

    private final VTNumber<?> value;
    private final Unit unit;

    /**
     * Constructs a measurement value.
     *
     * @param  value  the numerical value
     * @param  unit  the unit of measure the value is stated in
     */
    public VTMeasure(final VTNumber<?> value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * Returns the measurement value of this measure.
     *
     * @return  the measurement value
     */
    public VTNumber<?> getValue() {
        return this.value;
    }

    /**
     * Returns the measurement unit of this measure.
     *
     * @return  the measurement unit
     */
    public Unit getUnit() {
        return this.unit;
    }

    /**
     * Returns the measure equivalent to this measure but stated in the specified unit. This method may result in lost
     * of precision (e.g. measure of integral value).
     *
     * @param  unit  the new measurement unit
     *
     * @return  the measure stated in the specified unit
     */
    public VTMeasure to(final Unit unit) {

        if (this.unit.equals(unit)) {
            return this;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public double doubleValue() {
        return this.value.doubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int intValue() {
        return this.value.intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public long longValue() {
        return this.value.longValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure abs() {
        return new VTMeasure((VTNumber<?>) this.value.abs(), this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure negate() {
        return new VTMeasure((VTNumber<?>) this.value.negate(), this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    @SuppressWarnings("unchecked")
    public VTMeasure plus(final VTNumber<?> val) {
        VTNumber<?> result = null;

        if (val instanceof VTMeasure) {
            VTMeasure other = (VTMeasure) val;
            other = other.to(this.unit);
            result = other.getValue();
        } else {
            result = val;
        }

        final Class<?> clazz = this.value.getClass();

        if (!clazz.equals(result.getClass())) {

            if (clazz.equals(VTDecimal.class)) {
                result = new VTDecimal(result.doubleValue(), ((VTDecimal) this.value).getScale(), RoundingMode.HALF_UP);
            } else if (clazz.equals(VTDouble.class)) {
                result = new VTDouble(result.doubleValue());
            } else if (clazz.equals(VTInteger.class)) {
                result = new VTInteger(result.intValue());
            }
        }

        result = (VTNumber<?>) this.value.plus(result);

        return new VTMeasure(result, this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    @SuppressWarnings("unchecked")
    public VTMeasure minus(final VTNumber<?> val) {
        VTNumber<?> result = null;

        if (val instanceof VTMeasure) {
            VTMeasure other = (VTMeasure) val;
            other = other.to(this.unit);
            result = other.getValue();
        } else {
            result = val;
        }

        final Class<?> clazz = this.value.getClass();

        if (!clazz.equals(result.getClass())) {

            if (clazz.equals(VTDecimal.class)) {
                result = new VTDecimal(result.doubleValue(), ((VTDecimal) this.value).getScale(), RoundingMode.HALF_UP);
            } else if (clazz.equals(VTDouble.class)) {
                result = new VTDouble(result.doubleValue());
            } else if (clazz.equals(VTInteger.class)) {
                result = new VTInteger(result.intValue());
            }
        }

        result = (VTNumber<?>) this.value.minus(result);

        return new VTMeasure(result, this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure multiply(final VTNumber<?> val) {
        return new VTMeasure((VTNumber<?>) this.value.multiply(val), this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure div(final VTNumber<?> val) {
        return new VTMeasure((VTNumber<?>) this.value.div(val), this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  val  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure pow(final VTNumber<?> val) {
        return new VTMeasure((VTNumber<?>) this.value.pow(val), this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scale  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure round(final int scale) {
        return new VTMeasure((VTNumber<?>) this.value.round(scale), this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure truncate() {
        return new VTMeasure((VTNumber<?>) this.value.truncate(), this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTMeasure copy() {
        return new VTMeasure(this.value, this.unit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTMeasure o) {
        final VTMeasure other = o.to(this.unit);
        int result = 0;

        if (this.value.doubleValue() < other.getValue().doubleValue()) {
            result = -1;
        } else if (this.value.doubleValue() > other.getValue().doubleValue()) {
            result = 1;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return this.value.hashCode() * this.unit.hashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  obj  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof VTMeasure)) {
            return false;
        }

        VTMeasure other = (VTMeasure) obj;
        other = (VTMeasure) other.to((Unit) this.unit);

        return this.value.equals(other.value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return this.getValue().toString() + " " + this.getUnit().toString();
    }

}

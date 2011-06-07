/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.line;

import jgrl.struct.point.Point3D_F64;
import jgrl.struct.point.Vector3D_F64;

/**
 * <p>
 * 3D line parameterized using parametric equation:<br>
 * [x, y, z] = [x_0, y_0, z_0] + t·[slopeX, slopeY, slopeZ]<br>
 * where t specifies the location along the line, (x_0,y_0,z_0) is an arbitrary point on the line,
 * and (slopeX,slopeY).
 * </p> 
 *
 */
public class LineParametric3D_F64 {
    /**
     * A point on the line
     */
    public Point3D_F64 p = new Point3D_F64();
    /**
     * The line's slope
     */
    public Vector3D_F64 slope = new Vector3D_F64();

    public LineParametric3D_F64(double x_0, double y_0, double z_0 ,
                                double slopeX, double slopeY, double slopeZ ) {
        p.set(x_0,y_0,z_0);
        slope.set(slopeX,slopeY,slopeZ);
    }

    public LineParametric3D_F64(Point3D_F64 p, Vector3D_F64 slope) {
        setPoint(p);
        setSlope(slope);
    }

    public LineParametric3D_F64() {
    }

    public void setPoint( Point3D_F64 pt ) {
        this.p.set(pt);
    }

    public void setPoint( double x , double y ) {
        this.p.x = x;
        this.p.y = y;
    }

    public void setSlope( Vector3D_F64 slope ) {
        this.slope.set(slope);
    }

    public void setSlope( double slopeX , double slopeY ) {
        this.slope.x = slopeX;
        this.slope.y = slopeY;
    }

    /**
     * Returns a point along the line.  See parametric equation in class description.
     * 
     * @param t Location along the line.
     * @return Point on the line.
     */
    public Point3D_F64 getPointOnLine( double t ) {
        return new Point3D_F64( slope.x *t + p.x , slope.y *t + p.y , slope.z*t + p.z);
    }

    public Point3D_F64 getPoint() {
        return p;
    }

    public final double getSlopeX() {
        return slope.x;
    }

    public final double getSlopeY() {
        return slope.y;
    }

    public final double getX() {
        return p.x;
    }

    public final double getY() {
        return p.y;
    }

    public LineParametric3D_F64 copy() {
        return new LineParametric3D_F64(p,slope);
    }
}

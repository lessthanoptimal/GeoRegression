/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package georegression.struct.line;

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import lombok.Getter;
import lombok.Setter;
import org.ejml.FancyPrint;

import java.io.Serializable;

/**
 * <p>
 * 3D line parameterized using parametric equation:<br>
 * [x, y, z] = [x_0, y_0, z_0] + t·[slopeX, slopeY, slopeZ]<br>
 * where t specifies the location along the line, (x_0,y_0,z_0) is an arbitrary point on the line,
 * and (slopeX,slopeY,slopeZ).
 * </p>
 *
 * @see georegression.geometry.UtilLine3D_F64
 */
@SuppressWarnings("NullAway.Init")
public class LineParametric3D_F64 implements Serializable {
	/** A point on the line */
	@Getter @Setter public Point3D_F64 p;

	/** The line's slope */
	@Getter public Vector3D_F64 slope;

	public LineParametric3D_F64( double x_0, double y_0, double z_0,
								 double slopeX, double slopeY, double slopeZ ) {
		this();
		p.setTo( x_0, y_0, z_0 );
		slope.setTo( slopeX, slopeY, slopeZ );
	}

	public LineParametric3D_F64( Point3D_F64 p, Vector3D_F64 slope ) {
		this();
		setPoint( p );
		setSlope( slope );
	}

	public LineParametric3D_F64() {
		p = new Point3D_F64();
		slope = new Vector3D_F64();
	}

	public LineParametric3D_F64( LineParametric3D_F64 l ) {
		this();
		this.p.setTo(l.p);
		this.slope.setTo(l.slope);
	}

	/**
	 * Should it declare the point and slope
	 * @param declare true means to declares the data otherwise they are left as null
	 */
	public LineParametric3D_F64( boolean declare ) {
		if( declare ) {
			p = new Point3D_F64();
			slope = new Vector3D_F64();
		}
	}

	/**
	 * where = p + t*slope.
	 */
	public void setPointOnLine( double t , Point3D_F64 where ) {
		where.x = p.x + t*slope.x;
		where.y = p.y + t*slope.y;
		where.z = p.z + t*slope.z;
	}

	public void setPoint( Point3D_F64 pt ) {
		this.p.setTo( pt );
	}

	public void setPoint( double x, double y , double z ) {
		this.p.x = x;
		this.p.y = y;
		this.p.z = z;
	}

	public void setSlope( Vector3D_F64 slope ) {
		this.slope.setTo( slope );
	}

	public void setSlope( double slopeX, double slopeY , double slopeZ ) {
		this.slope.x = slopeX;
		this.slope.y = slopeY;
		this.slope.z = slopeZ;
	}

	/**
	 * Returns a point along the line. See parametric equation in class description.
	 *
	 * @param t Location along the line.
	 * @return Point on the line.
	 */
	public Point3D_F64 getPointOnLine( double t ) {
		return new Point3D_F64( slope.x * t + p.x, slope.y * t + p.y, slope.z * t + p.z );
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

	public final double getSlopeZ() {
		return slope.z;
	}

	public final double getX() {
		return p.x;
	}

	public final double getY() {
		return p.y;
	}

	public final double getZ() {
		return p.z;
	}

	public void setTo(double x_0, double y_0, double z_0,
					  double slopeX, double slopeY, double slopeZ ) {
		p.setTo( x_0, y_0, z_0 );
		slope.setTo( slopeX, slopeY, slopeZ );
	}

	public void setTo(LineParametric3D_F64 o ) {
		this.p.setTo(o.p);
		this.slope.setTo(o.slope);
	}

	public void zero() {
		p.zero();
		slope.zero();
	}

	public LineParametric3D_F64 copy() {
		return new LineParametric3D_F64( p, slope );
	}

	@Override
	public String toString() {
		FancyPrint f = new FancyPrint();

		return getClass().getSimpleName()+" P( "+f.s(p.x)+" "+f.s(p.y)+" "+f.s(p.z)+
				" ) Slope( "+f.s(slope.x)+" "+f.s(slope.y)+" "+f.s(slope.z)+" )";
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(!(obj instanceof LineParametric3D_F64))
			return false;

		var o = (LineParametric3D_F64) obj;
		return p.equals(o.p) && slope.equals(o.slope);
	}

	@Override
	public int hashCode() {
		return p.hashCode() + slope.hashCode();
	}
}

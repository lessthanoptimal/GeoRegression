/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.line;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

/**
 * <p>
 * 3D line parameterized using parametric equation:<br>
 * [x, y, z] = [x_0, y_0, z_0] + tÂ·[slopeX, slopeY, slopeZ]<br>
 * where t specifies the location along the line, (x_0,y_0,z_0) is an arbitrary point on the line,
 * and (slopeX,slopeY,slopeZ).
 * </p>
 */
public class LineParametric3D_F32 {
	/**
	 * A point on the line
	 */
	public Point3D_F32 p = new Point3D_F32();
	/**
	 * The line's slope
	 */
	public Vector3D_F32 slope = new Vector3D_F32();

	public LineParametric3D_F32( float x_0, float y_0, float z_0,
								 float slopeX, float slopeY, float slopeZ ) {
		p.set( x_0, y_0, z_0 );
		slope.set( slopeX, slopeY, slopeZ );
	}

	public LineParametric3D_F32( Point3D_F32 p, Vector3D_F32 slope ) {
		setPoint( p );
		setSlope( slope );
	}

	public LineParametric3D_F32() {
	}

	public void setPoint( Point3D_F32 pt ) {
		this.p.set( pt );
	}

	public void setPoint( float x, float y ) {
		this.p.x = x;
		this.p.y = y;
	}

	public void setSlope( Vector3D_F32 slope ) {
		this.slope.set( slope );
	}

	public void setSlope( float slopeX, float slopeY ) {
		this.slope.x = slopeX;
		this.slope.y = slopeY;
	}

	/**
	 * Returns a point along the line.  See parametric equation in class description.
	 *
	 * @param t Location along the line.
	 * @return Point on the line.
	 */
	public Point3D_F32 getPointOnLine( float t ) {
		return new Point3D_F32( slope.x * t + p.x, slope.y * t + p.y, slope.z * t + p.z );
	}

	public Point3D_F32 getPoint() {
		return p;
	}

	public Vector3D_F32 getSlope() {
		return slope;
	}

	public final float getSlopeX() {
		return slope.x;
	}

	public final float getSlopeY() {
		return slope.y;
	}

	public final float getSlopeZ() {
		return slope.y;
	}

	public final float getX() {
		return p.x;
	}

	public final float getY() {
		return p.y;
	}

	public final float getZ() {
		return p.y;
	}

	public LineParametric3D_F32 copy() {
		return new LineParametric3D_F32( p, slope );
	}
}

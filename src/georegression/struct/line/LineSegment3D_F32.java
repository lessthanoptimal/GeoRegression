/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
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


/**
 * Defines a line segment by its two end points.
 *
 * @author Peter Abeles
 */
public class LineSegment3D_F32 {
	public Point3D_F32 a = new Point3D_F32();
	public Point3D_F32 b = new Point3D_F32();

	public LineSegment3D_F32() {
	}

	public LineSegment3D_F32(Point3D_F32 a, Point3D_F32 b) {
		set( a, b );
	}

	public LineSegment3D_F32(float x0, float y0, float z0, float x1, float y1 , float z1) {
		set( x0, y0, z0, x1, y1, z1 );
	}
	
	public static LineSegment3D_F32 wrap( Point3D_F32 a , Point3D_F32 b ) {
		LineSegment3D_F32 ret = new LineSegment3D_F32();
		ret.a = a;
		ret.b = b;
		return ret;
	}

	public void set( LineSegment3D_F32 l ) {
		this.a.set( l.a );
		this.b.set( l.b );
	}

	public void set( Point3D_F32 a, Point3D_F32 b ) {
		this.a.set( a );
		this.b.set( b );
	}

	public void set( float x0, float y0, float z0, float x1, float y1 , float z1 ) {
		a.set( x0, y0, z0 );
		b.set( x1, y1, z1 );
	}

	public Point3D_F32 getA() {
		return a;
	}

	public void setA( Point3D_F32 a ) {
		this.a = a;
	}

	public Point3D_F32 getB() {
		return b;
	}

	public void setB( Point3D_F32 b ) {
		this.b = b;
	}

	public float slopeX() {
		return b.x-a.x;
	}

	public float slopeY() {
		return b.y-a.y;
	}

	public float getLength() {
		return a.distance(b);
	}

	public float getLength2() {
		return a.distance2(b);
	}

	public LineSegment3D_F32 copy() {
		return new LineSegment3D_F32( a, b );
	}
}

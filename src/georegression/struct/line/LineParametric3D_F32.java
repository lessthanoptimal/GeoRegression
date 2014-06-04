/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

import java.io.Serializable;

/**
 * <p>
 * 3D line parameterized using parametric equation:<br>
 * [x, y, z] = [x_0, y_0, z_0] + tÂ·[slopeX, slopeY, slopeZ]<br>
 * where t specifies the location along the line, (x_0,y_0,z_0) is an arbitrary point on the line,
 * and (slopeX,slopeY,slopeZ).
 * </p>
 */
public class LineParametric3D_F32 implements Serializable {
	/**
	 * A point on the line
	 */
	public Point3D_F32 p;
	/**
	 * The line's slope
	 */
	public Vector3D_F32 slope;

	public LineParametric3D_F32( float x_0, float y_0, float z_0,
								 float slopeX, float slopeY, float slopeZ ) {
		this();
		p.set( x_0, y_0, z_0 );
		slope.set( slopeX, slopeY, slopeZ );
	}

	public LineParametric3D_F32( Point3D_F32 p, Vector3D_F32 slope ) {
		this();
		setPoint( p );
		setSlope( slope );
	}

	public LineParametric3D_F32() {
		p = new Point3D_F32();
		slope = new Vector3D_F32();
	}

	public LineParametric3D_F32( LineParametric3D_F32 l ) {
		this();
		this.p.set(l.p);
		this.slope.set(l.slope);
	}

	/**
	 * Should it declare the point and slope
	 * @param declare true means to declares the data otherwise they are left as null
	 */
	public LineParametric3D_F32( boolean declare ) {
		if( declare ) {
			p = new Point3D_F32();
			slope = new Vector3D_F32();
		}
	}

	public void setPoint( Point3D_F32 pt ) {
		this.p.set( pt );
	}

	public void setPoint( float x, float y , float z ) {
		this.p.x = x;
		this.p.y = y;
		this.p.z = z;
	}

	public void setSlope( Vector3D_F32 slope ) {
		this.slope.set( slope );
	}

	public void setSlope( float slopeX, float slopeY , float slopeZ ) {
		this.slope.x = slopeX;
		this.slope.y = slopeY;
		this.slope.z = slopeZ;
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

	public void set( float x_0, float y_0, float z_0,
					 float slopeX, float slopeY, float slopeZ ) {
		p.set( x_0, y_0, z_0 );
		slope.set( slopeX, slopeY, slopeZ );
	}

	public void set( LineParametric3D_F32 o ) {
		this.p.set(o.p);
		this.slope.set(o.slope);
	}

	public Point3D_F32 getP() {
		return p;
	}

	public void setP(Point3D_F32 p) {
		this.p = p;
	}

	public LineParametric3D_F32 copy() {
		return new LineParametric3D_F32( p, slope );
	}

	public String toString() {
		return getClass().getSimpleName()+" P( "+p.x+" "+p.y+" "+p.z+" ) Slope( "+slope.x+" "+slope.y+" "+slope.z+" )";
	}
}

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

package georegression.struct.shapes;

import georegression.struct.point.Point2D_F32;

/**
 * <p>
 * An ellipse described using its center, semi-axes, and orientation.<br>
 * (x'*cos(phi) + y'*sin(phi))^2/a^2 + (-x'*sin(phi) + y'*cos(phi))^2/b_2 = 1<br>
 * x' = x-x_0, y' = y-y_0<br>
 * where (x_0,y_0) is the center, (a,b) are major and minor axises, and phi is it's orientation.
 * </p>
 *
 * @author Peter Abeles
 */
public class EllipseRotated_F32 {
	/**
	 * Center of the ellipse
	 */
	public Point2D_F32 center = new Point2D_F32();
	/**
	 * semi major-axis
	 */
	public float a;
	/**
	 * semi minor-axis
	 */
	public float b;
	/**
	 * counter clockwise angle of rotation from x-axis to the major axis.  Standard range is from -PI/2 to PI/2
	 */
	public float phi;

	public EllipseRotated_F32(Point2D_F32 center, float a, float b, float phi) {
		this.center.set(center);
		this.a = a;
		this.b = b;
		this.phi = phi;
	}

	public EllipseRotated_F32( float x0 , float y0, float a, float b, float phi) {
		set(x0,y0,a,b,phi);
	}

	public EllipseRotated_F32() {
	}

	public Point2D_F32 getCenter() {
		return center;
	}

	public void setCenter(Point2D_F32 center) {
		this.center.set(center);
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public float getPhi() {
		return phi;
	}

	public void setPhi(float phi) {
		this.phi = phi;
	}

	public void set( float x0 , float y0, float a, float b, float phi) {
		this.center.set(x0,y0);
		this.a = a;
		this.b = b;
		this.phi = phi;
	}

	public void set(EllipseRotated_F32 ellipse ) {
		this.center.set( ellipse.center );
		this.a = ellipse.a;
		this.b = ellipse.b;
		this.phi = ellipse.phi;
	}
}

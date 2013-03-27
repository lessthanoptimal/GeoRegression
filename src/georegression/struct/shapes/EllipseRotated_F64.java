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

import georegression.struct.point.Point2D_F64;

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
public class EllipseRotated_F64 {
	/**
	 * Center of the ellipse
	 */
	public Point2D_F64 center = new Point2D_F64();
	/**
	 * semi major-axis
	 */
	public double a;
	/**
	 * semi minor-axis
	 */
	public double b;
	/**
	 * counter clockwise angle of rotation from x-axis to the major axis
	 */
	public double phi;

	public EllipseRotated_F64(Point2D_F64 center, double a, double b, double phi) {
		this.center.set(center);
		this.a = a;
		this.b = b;
		this.phi = phi;
	}

	public EllipseRotated_F64( double x0 , double y0, double a, double b, double phi) {
		this.center.set(x0,y0);
		this.a = a;
		this.b = b;
		this.phi = phi;
	}

	public EllipseRotated_F64() {
	}

	public Point2D_F64 getCenter() {
		return center;
	}

	public void setCenter(Point2D_F64 center) {
		this.center.set(center);
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getPhi() {
		return phi;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}
}

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

package georegression.struct.shapes;

import georegression.struct.point.Point2D_F64;

import java.io.Serializable;

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
public class EllipseRotated_F64 implements Serializable {
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
	 * counter clockwise angle of rotation from x-axis to the major axis.  Standard range is from -PI/2 to PI/2
	 */
	public double phi;

	public EllipseRotated_F64(Point2D_F64 center, double a, double b, double phi) {
		this.center.set(center);
		this.a = a;
		this.b = b;
		this.phi = phi;
	}

	public EllipseRotated_F64( double x0 , double y0, double a, double b, double phi) {
		set(x0,y0,a,b,phi);
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

	public void set( double x0 , double y0, double a, double b, double phi) {
		this.center.set(x0,y0);
		this.a = a;
		this.b = b;
		this.phi = phi;
	}

	public void set(EllipseRotated_F64 ellipse ) {
		this.center.set( ellipse.center );
		this.a = ellipse.a;
		this.b = ellipse.b;
		this.phi = ellipse.phi;
	}
}

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

/**
 * <p>
 * In general quadratic form, an ellipse is described by 6-coefficients:<br>
 * F(x,y) = a*x^2 + 2*b*x*y + c*y^2 + 2*d*x + 2*e*y + f = 0<br>
 * a*c - b*b > 0<br>
 * where [a,b,c,d,e,f] are the coefficients and [x,y] is the coordinate of a point on the ellipse.
 * </p>
 *
 * <p>
 * NOTE: these parameters are unique only up to a scale factor.
 * </p>
 *
 * @author Peter Abeles
 */
public class EllipseQuadratic_F32 {
	/**
	 * coefficients
	 */
	public float a,b,c,d,e,f;

	public EllipseQuadratic_F32(float a, float b, float c, float d, float e, float f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}

	public EllipseQuadratic_F32() {
	}

	/**
	 * Checks to see if the parameters define an ellipse using the a*c - b*b > 0 constraint.
	 * @return true if it's an ellipse or false if not
	 */
	public boolean isEllipse() {
		return a*c - b*b > 0;
	}
}

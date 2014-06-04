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

import java.io.Serializable;

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
public class EllipseQuadratic_F64 implements Serializable {
	/**
	 * coefficients
	 */
	public double a,b,c,d,e,f;

	public EllipseQuadratic_F64(double a, double b, double c, double d, double e, double f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}

	public EllipseQuadratic_F64() {
	}

	/**
	 * Checks to see if the parameters define an ellipse using the a*c - b*b > 0 constraint.
	 * @return true if it's an ellipse or false if not
	 */
	public boolean isEllipse() {
		return a*c - b*b > 0;
	}
}

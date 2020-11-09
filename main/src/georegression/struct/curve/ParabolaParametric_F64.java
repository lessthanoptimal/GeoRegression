/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.struct.curve;

import georegression.struct.point.Point2D_F64;

/**
 * Parametric form of parabola with 4 parameters.
 *
 * <p> x=A*t<sup>2</sup> + B*t + C<br>
 *     y=D*t<sup>2</sup> + E*t + F</p>
 *
 *
 * @author Peter Abeles
 */
public class ParabolaParametric_F64 {
	public double A,B,C,D,E,F;

	public ParabolaParametric_F64(){}

	public ParabolaParametric_F64(ParabolaParametric_F64 src){setTo(src);}

	public void setTo( ParabolaParametric_F64 src ) {
		this.A = src.A;
		this.B = src.B;
		this.C = src.C;
		this.D = src.D;
		this.E = src.E;
		this.F = src.F;
	}

	public void evaluate(double t , Point2D_F64 location ) {
		location.x = A*t*t + B*t + C;
		location.y = D*t*t + E*t + F;
	}

	public Point2D_F64 evaluate( double t ) {
		Point2D_F64 p = new Point2D_F64();
		evaluate(t,p);
		return p;
	}
}

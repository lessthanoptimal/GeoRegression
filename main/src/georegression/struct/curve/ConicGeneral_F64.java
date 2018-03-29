/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

/**
 * <p>A*x<sup>2</sup> + B*y<sup>2</sup> + C*x*y + D*x + E*y + F=0</p>
 *
 * @author Peter Abeles
 */
public class ConicGeneral_F64 {
	/**
	 * Coefficients
	 */
	public double A,B,C,D,E,F;

	public double evaluate( double x , double y ) {
		return A*x*x + B*y*y + C*x*y + D*x + E*y + F;
	}
}

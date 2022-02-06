/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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
 * Interface for all polynomials
 */
public interface PolynomialCurve_F64 {

	double get( int coefficient );

	void set( int coefficient , double value );

	/**
	 * Number of coeffients
	 */
	int size();

	/**
	 * Polynomial's degree. This is the number of coefficients minus one.
	 */
	int degree();

	/** Sets all coefficients to zero */
	void zero();
}

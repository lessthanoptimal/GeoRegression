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

package georegression.misc;


/**
 * Constants used throughout the library.
 *
 * @author Peter Abeles
 */
public class GrlConstants {

	public static float F_PI = (float)Math.PI;
	public static float F_PI2 = 2f*F_PI;
	public static double PI2 = 2.0*Math.PI;

	// identifies the library version
	public static String VERSION = "ALPHA";

	// standard tolerances used in unit tests
	public static float FLOAT_TEST_TOL = 1e-4f;
	public static double DOUBLE_TEST_TOL = 1e-8f;

}

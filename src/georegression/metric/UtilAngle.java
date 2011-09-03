/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.metric;

import static java.lang.Math.PI;

/**
 * Utility functions relating to angles.  Unless otherwise state all angles
 * are in radians and have a domain of -pi to pi
 */
public class UtilAngle {
	public static final double PI2 = PI * 2.0;

	public static double radianToDegree( double angleRad ) {
		return 180.0 * angleRad / Math.PI;
	}

	public static double degreeToRadian( double angleDegree ) {
		return Math.PI * angleDegree / 180.0;
	}

	/**
	 * Checks to see if it is between -&pi and &pi.
	 *
	 * @param ang Angle being tested
	 * @return true if it is between -&pi and &pi;
	 */
	public static boolean isStandardDomain( double ang ) {
		return ( ang <= Math.PI && ang >= -Math.PI );
	}

	/**
	 * Returns an angle which is equivalent to the one provided, but between (inclusive) -pi and pi.
	 */
	public static double bound( double ang ) {
		ang %= PI2;

		if( ang > PI ) {
			return ang - PI2;
		} else if( ang < -PI ) {
			return ang + PI2;
		}

		return ang;
	}

	/**
	 * <p>
	 * Returns the difference between two angles and bounds the result between -pi and pi:<br>
	 * result = angA - angB<br>
	 * and takes in account boundary conditions.
	 * </p>
	 *
	 * @param angA first angle.  Must be between -pi and pi.
	 * @param angB second angle. Must be between -pi and pi.
	 * @return an angle between -pi and pi
	 */
	public static double minus( double angA, double angB ) {
		double diff = angA - angB;

		if( diff > Math.PI ) {
			return PI2 - diff;
		} else if( diff < -Math.PI )
			return -PI2 - diff;

		return diff;
	}

	/**
	 * <p>
	 * Returns the number of radians two angles are apart.  This is equivalent to
	 * Math.abs(UtilAngle.minus(angA,angB)).
	 * </p>
	 *
	 * @param angA first angle.  Must be between -pi and pi.
	 * @param angB second angle. Must be between -pi and pi.
	 * @return an angle between 0 and pi
	 */
	public static double dist( double angA, double angB ) {
		return Math.abs(minus(angA,angB));
	}
}

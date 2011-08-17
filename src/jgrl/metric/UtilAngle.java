/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package jgrl.metric;

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

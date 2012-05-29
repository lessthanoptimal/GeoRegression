/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

import georegression.misc.GrlConstants;

import static java.lang.Math.PI;

/**
 * Utility functions relating to angles.  Unless otherwise state all angles
 * are in radians and have a domain of -pi to pi
 */
public class UtilAngle {

	public static double radianToDegree( double angleRad ) {
		return 180.0 * angleRad / Math.PI;
	}

	public static double degreeToRadian( double angleDegree ) {
		return Math.PI * angleDegree / 180.0;
	}

	public static double atanSafe( double y , double x ) {
		if( x == 0 )
			return Math.PI/2;
		return Math.atan(y/x);
	}

	public static float atanSafe( float y , float x ) {
		if( x == 0 )
			return GrlConstants.F_PI/2;
		return (float)Math.atan(y/x);
	}

	/**
	 * Converts an angle which is (-pi to pi) into a half circle angle (-pi/2 to pi/2).
	 *
	 * @param angle Angle between -pi and pi.
	 * @return -pi/2 to pi/2
	 */
	public static double toHalfCircle( double angle ) {
		if( angle < 0 )
			angle += Math.PI;
		if( angle > Math.PI/2.0 )
			angle -= Math.PI;
		return angle;
	}

	/**
	 * Converts an angle which is (-pi to pi) into a half circle angle (0 to pi).
	 *
	 * @param angle Angle between -pi and pi.
	 * @return 0 to pi
	 */
	public static float toHalfCircle( float angle ) {
		if( angle >= 0 )
			return angle;
		return GrlConstants.F_PI+angle;
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
	 * Checks to see if it is between -&pi/2 and &pi/2.
	 *
	 * @param ang Angle being tested
	 * @return true if it is between -&pi/2 and &pi/2;
	 */
	public static boolean isHalfDomain( double ang ) {
		return ( ang <= Math.PI/2 && ang >= -Math.PI/2 );
	}

	/**
	 * Returns an angle which is equivalent to the one provided, but between (inclusive) -pi and pi.
	 */
	public static double bound( double ang ) {
		ang %= GrlConstants.PI2;

		if( ang > PI ) {
			return ang - GrlConstants.PI2;
		} else if( ang < -PI ) {
			return ang + GrlConstants.PI2;
		}

		return ang;
	}

	/**
	 * Angular distance in radians to go from angA to angB in counter clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;.
	 *
	 * @param angA First angle. -pi to pi
	 * @param angB Second angle -pi to pi
	 * @return An angle from 0 to 2 &pi;
	 */
	public static double distanceCCW( double angA, double angB ) {
		if( angB > angA )
			return angB-angA;
		else
			return GrlConstants.PI2 - (angA-angB);
	}

	/**
	 * Angular distance in radians to go from angA to angB in clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;.
	 *
	 * @param angA First angle. -pi to pi
	 * @param angB Second angle   -pi to pi
	 * @return An angle from 0 to 2 &pi;
	 */
	public static double distanceCW( double angA, double angB ) {
		if( angA > angB )
			return angA-angB;
		else
			return GrlConstants.PI2-(angB-angA);
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
			return GrlConstants.PI2 - diff;
		} else if( diff < -Math.PI )
			return -GrlConstants.PI2 - diff;

		return diff;
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
	public static float minus( float angA, float angB ) {
		float diff = angA - angB;

		if( diff > GrlConstants.F_PI ) {
			return GrlConstants.F_PI2 - diff;
		} else if( diff < -GrlConstants.F_PI )
			return -GrlConstants.F_PI2 - diff;

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
	public static float dist( float angA, float angB ) {
		return Math.abs(minus(angA,angB));
	}

	/**
	 * Angular distance between two half circle angles.
	 *
	 * @param angA Angle between -pi/2 and pi/2.
	 * @param angB Angle between -pi/2 and pi/2.
	 * @return Acute angle between the two input angles.
	 */
	public static double distHalf( double angA , double angB ) {
		double a = Math.abs(angA-angB);
		if( a <= Math.PI/2 )
			return a;
		else
			return Math.PI-a;
	}
}

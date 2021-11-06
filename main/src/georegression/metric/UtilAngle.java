/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.misc.GrlConstants;

import static java.lang.Math.PI;

/**
 * Utility functions relating to angles. Unless otherwise state all angles
 * are in radians and have a domain of -pi to pi
 */
public class UtilAngle {

	public static double radianToDegree( double angleRad ) {
		return 180.0 * angleRad / Math.PI;
	}

	public static double degreeToRadian( double angleDegree ) {
		return Math.PI * angleDegree / 180.0;
	}

	public static float radianToDegree( float angleRad ) {
		return 180.0f * angleRad / GrlConstants.F_PI;
	}

	public static float degreeToRadian( float angleDegree ) {
		return GrlConstants.F_PI * angleDegree / 180.0f;
	}

	/**
	 * Converts radians into degrees
	 * @param angleDegree angle in degrees
	 * @return angle in radians
	 */
	public static double radian( double angleDegree ) {
		return degreeToRadian(angleDegree);
	}

	/**
	 * Converts degrees into radians
	 * @param angleRad angle in radians
	 * @return angle in degrees
	 */
	public static double degree( double angleRad ) {
		return radianToDegree(angleRad);
	}

	/**
	 * Converts radians into degrees
	 * @param angleDegree angle in degrees
	 * @return angle in radians
	 */
	public static float radian( float angleDegree ) {
		return degreeToRadian(angleDegree);
	}

	/**
	 * Converts degrees into radians
	 * @param angleRad angle in radians
	 * @return angle in degrees
	 */
	public static float degree( float angleRad ) {
		return radianToDegree(angleRad);
	}


	public static double atanSafe( double y , double x ) {
		if( x == 0.0 ) {
			if( y >= 0.0)
				return Math.PI / 2;
			else
				return -Math.PI / 2;
		}
		return Math.atan(y/x);
	}

	public static float atanSafe( float y , float x ) {
		if( x == 0.0 )
			if( y >= 0.0)
				return GrlConstants.F_PId2;
			else
				return -GrlConstants.F_PId2;
		return (float)Math.atan(y/x);
	}

	/**
	 * Computes the average of the two angles. This only works well and is a well defined problem when the two
	 * angles are close. It operates by determining of the angles are closer in CW or CCW directions. The distance
	 * in that direction is found then 1/2 the distance is added to one of them and returned.
	 *
	 * @param a Angle in radians.
	 * @param b Angle in radians.
	 * @return Average angle of the two. a + delta.
	 */
	public static double average( double a , double b ) {
		double dist = UtilAngle.distanceCCW(bound(a),bound(b));
		if( dist > GrlConstants.PI ) {
			dist = GrlConstants.PI2-dist;
			return a - dist/2.0;
		} else {
			return a + dist/2.0;
		}
	}

	/**
	 * Computes the average of the two angles. This only works well and is a well defined problem when the two
	 * angles are close. It operates by determining of the angles are closer in CW or CCW directions. The distance
	 * in that direction is found then 1/2 the distance is added to one of them and returned.
	 *
	 * @param a Angle in radians.
	 * @param b Angle in radians.
	 * @return Average angle of the two. a + delta.
	 */
	public static float average( float a , float b ) {
		float dist = UtilAngle.distanceCCW(bound(a),bound(b));
		if( dist > GrlConstants.F_PI ) {
			dist = GrlConstants.F_PI2-dist;
			return a - dist/2.0f;
		} else {
			return a + dist/2.0f;
		}
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
	 * Checks to see if it is between -&pi; and &pi;.
	 *
	 * @param ang Angle being tested
	 * @return true if it is between -&pi; and &pi;
	 */
	public static boolean isStandardDomain( double ang ) {
		return ( ang <= Math.PI && ang >= -Math.PI );
	}

	/**
	 * Checks to see if it is between -&pi;/2 and &pi;/2.
	 *
	 * @param ang Angle being tested
	 * @return true if it is between -&pi;/2 and &pi;/2;
	 */
	public static boolean isHalfDomain( double ang ) {
		return ( ang <= Math.PI/2 && ang >= -Math.PI/2 );
	}

	/**
	 * Returns an angle which is equivalent to the one provided, but between (inclusive) -&pi; and &pi;.
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
	 * Returns an angle which is equivalent to the one provided, but between (inclusive) -&pi; and &pi;.
	 */
	public static float bound( float ang ) {
		ang %= GrlConstants.F_PI2;

		if( ang > GrlConstants.F_PI ) {
			return ang - GrlConstants.F_PI2;
		} else if( ang < -GrlConstants.F_PI ) {
			return ang + GrlConstants.F_PI2;
		}

		return ang;
	}

	/**
	 * Bounds the angle between -&pi;/2 and &pi;/2
	 * @param angle angle in radians
	 * @return bounded angle
	 */
	public static double boundHalf( double angle ) {
		angle = bound(angle);

		if( angle > GrlConstants.PId2 ) {
			angle -= Math.PI;
		} else if( angle < -GrlConstants.PId2 ){
			angle += Math.PI;
		}
		return angle;
	}

	/**
	 * Bounds the angle between -&pi;/2 and &pi;/2
	 * @param angle angle in radians
	 * @return bounded angle
	 */
	public static float boundHalf( float angle ) {
		angle = bound(angle);

		if( angle > GrlConstants.F_PId2 ) {
			angle -= GrlConstants.F_PI;
		} else if( angle < -GrlConstants.F_PId2 ){
			angle += GrlConstants.F_PI;
		}
		return angle;
	}

	/**
	 * Angular distance in radians to go from angA to angB in counter clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;.
	 *
	 * @param angA First angle. -pi to pi Radians.
	 * @param angB Second angle -pi to pi Radians.
	 * @return An angle from 0 to 2 &pi;
	 */
	public static double distanceCCW( double angA, double angB ) {
		if( angB >= angA )
			return angB-angA;
		else
			return GrlConstants.PI2 - (angA-angB);
	}

	/**
	 * Angular distance in radians to go from angA to angB in counter clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;. Input is unbounded
	 *
	 * @param angA First angle. Radians.
	 * @param angB Second angle  Radians.
	 * @return An angle from 0 to 2 &pi;
	 */
	public static double distanceCCW_u( double angA, double angB ) {
		angA = bound(angA);
		angB = bound(angB);

		if( angB >= angA )
			return angB-angA;
		else
			return GrlConstants.PI2 - (angA-angB);
	}

	/**
	 * Angular distance in radians to go from angA to angB in counter clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;.
	 *
	 * @param angA First angle. -pi to pi Radians.
	 * @param angB Second angle -pi to pi Radians.
	 * @return An angle from 0 to 2 &pi;
	 */
	public static float distanceCCW( float angA, float angB ) {
		if( angB >= angA )
			return angB-angA;
		else
			return GrlConstants.F_PI2 - (angA-angB);
	}

	/**
	 * Angular distance in radians to go from angA to angB in counter clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;. Input is unbounded
	 *
	 * @param angA First angle. Radians.
	 * @param angB Second angle  Radians.
	 * @return An angle from 0 to 2 &pi;
	 */
	public static float distanceCCW_u( float angA, float angB ) {
		angA = bound(angA);
		angB = bound(angB);

		if( angB >= angA )
			return angB-angA;
		else
			return GrlConstants.F_PI2 - (angA-angB);
	}

	/**
	 * Angular distance in radians to go from angA to angB in clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;.
	 *
	 * @param angA First angle. -pi to pi  Radians.
	 * @param angB Second angle   -pi to pi  Radians.
	 * @return An angle from 0 to 2 &pi;
	 */
	public static double distanceCW( double angA, double angB ) {
		if( angA >= angB )
			return angA-angB;
		else
			return GrlConstants.PI2-(angB-angA);
	}

	/**
	 * Angular distance in radians to go from angA to angB in clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;. Input is unbounded.
	 *
	 * @param angA First angle. Radians.
	 * @param angB Second angle. Radians.
	 * @return An angle from 0 to 2 &pi;
	 */
	public static double distanceCW_u( double angA, double angB ) {
		angA = bound(angA);
		angB = bound(angB);

		if( angA >= angB )
			return angA-angB;
		else
			return GrlConstants.PI2-(angB-angA);
	}

	/**
	 * Angular distance in radians to go from angA to angB in clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;.
	 *
	 * @param angA First angle. -pi to pi
	 * @param angB Second angle   -pi to pi
	 * @return An angle from 0 to 2 &pi;
	 */
	public static float distanceCW( float angA, float angB ) {
		if( angA >= angB )
			return angA-angB;
		else
			return GrlConstants.F_PI2-(angB-angA);
	}

	/**
	 * Angular distance in radians to go from angA to angB in clock-wise direction.
	 * The resulting angle will be from 0 to 2&pi;. Input is unbounded.
	 *
	 * @param angA First angle. Radians.
	 * @param angB Second angle. Radians.
	 * @return An angle from 0 to 2 &pi;
	 */
	public static float distanceCW_u( float angA, float angB ) {
		angA = bound(angA);
		angB = bound(angB);

		if( angA >= angB )
			return angA-angB;
		else
			return GrlConstants.F_PI2-(angB-angA);
	}
	
	/**
	 * <p>
	 * Returns the difference between two angles and bounds the result between -pi and pi:<br>
	 * result = angA - angB<br>
	 * and takes in account boundary conditions.
	 * </p>
	 *
	 * @param angA first angle. Must be between -pi and pi.
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
	 * @param angA first angle. Must be between -pi and pi.
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
	 * Returns the number of radians two angles are apart. This is equivalent to
	 * Math.abs(UtilAngle.minus(angA,angB)).
	 * </p>
	 *
	 * @param angA first angle. Must be between -pi and pi.
	 * @param angB second angle. Must be between -pi and pi.
	 * @return an angle between 0 and pi
	 */
	public static double dist( double angA, double angB ) {
		return Math.abs(minus(angA,angB));
	}

	/**
	 * <p>
	 * Returns the number of radians two angles are apart. This is equivalent to
	 * Math.abs(UtilAngle.minus(angA,angB)). Unbounded input.
	 * </p>
	 *
	 * @param angA first angle. Radians.
	 * @param angB second angle. Radians.
	 * @return an angle between 0 and pi
	 */
	public static double dist_u( double angA, double angB ) {
		return Math.abs(minus(bound(angA),bound(angB)));
	}

	/**
	 * <p>
	 * Returns the number of radians two angles are apart. This is equivalent to
	 * Math.abs(UtilAngle.minus(angA,angB)).
	 * </p>
	 *
	 * @param angA first angle. Must be between -pi and pi.
	 * @param angB second angle. Must be between -pi and pi.
	 * @return an angle between 0 and pi
	 */
	public static float dist( float angA, float angB ) {
		return Math.abs(minus(angA,angB));
	}

	/**
	 * <p>
	 * Returns the number of radians two angles are apart. This is equivalent to
	 * Math.abs(UtilAngle.minus(angA,angB)). Unbounded input.
	 * </p>
	 *
	 * @param angA first angle. Radians.
	 * @param angB second angle. Radians.
	 * @return an angle between 0 and pi
	 */
	public static float dist_u( float angA, float angB ) {
		return Math.abs(minus(bound(angA),bound(angB)));
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

	/**
	 * Converts an angle from -pi to pi into 0 to 2*pi domain
	 * @param angle angle from -pi to pi radians
	 * @return angle from 0 to 2*pi radians
	 */
	public static double domain2PI( double angle ) {
		if( angle < 0 ) {
			return angle + 2*Math.PI;
		} else {
			return angle;
		}
	}

	/**
	 * Ensures a wrapping circular bound so that the numbers from 0 to 1, where 0 is inclusive and 1 is exclusive.
	 * <pre>Examples:
	 * 1.5   = 0.5
	 * -0.25 = 0.75
	 * 0     = 0
	 * 1     = 0
	 * 0.999 = 0.999
	 * 2     = 0
	 * -1    = 0
	 * </pre>
	 *
	 * @return A value from 0 to 1. [0,1)
	 */
	public static double wrapZeroToOne(double value ) {
		if( value >= 0 )
			return value % 1.0;
		else {
			return (1.0+(value%1.0))%1.0; // last bit is to ensure that 1.0 is returned as 0.0
		}
	}

	/**
	 * Ensures a wrapping circular bound so that the numbers from 0 to 1, where 0 is inclusive and 1 is exclusive.
	 * <pre>Examples:
	 * 1.5   = 0.5
	 * -0.25 = 0.75
	 * 0     = 0
	 * 1     = 0
	 * 0.999 = 0.999
	 * 2     = 0
	 * -1    = 0
	 * </pre>
	 *
	 * @return A value from 0 to 1. [0,1)
	 */
	public static float wrapZeroToOne(float value ) {
		if( value >= 0 )
			return value % 1.0f;
		else {
			return (1.0f+(value%1.0f))%1.0f; // last bit is to ensure that 1.0 is returned as 0.0
		}
	}

	/**
	 * Ensures a reflective bound so that the numbers from 0 to 1, where 0 is inclusive and 1 is inclusive.
	 * <pre>Examples:
	 * 1.5   = 0.5
	 * -0.25 = 0.25
	 * -0.75 = 0.75
	 * 0     = 0
	 * 1     = 1
	 * 0.999 = 0.999
	 * 2     = 0
	 * -1    = 1
	 * </pre>
	 *
	 * @return A value from 0 to 1. [0,1]
	 */
	public static double reflectZeroToOne(double value ) {
		if( value < 0 )
			value = -value;
		value = value%2.0;
		if( value > 1.0 )
			return 2.0 - value;
		return value;
	}

	/**
	 * Ensures a reflective bound so that the numbers from 0 to 1, where 0 is inclusive and 1 is inclusive.
	 * <pre>Examples:
	 * 1.5   = 0.5
	 * -0.25 = 0.25
	 * -0.75 = 0.75
	 * 0     = 0
	 * 1     = 1
	 * 0.999 = 0.999
	 * 2     = 0
	 * -1    = 1
	 * </pre>
	 *
	 * @return A value from 0 to 1. [0,1]
	 */
	public static float reflectZeroToOne(float value ) {
		if( value < 0 )
			value = -value;
		value = value%2.0f;
		if( value > 1.0f )
			return 2.0f - value;
		return value;
	}
}

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

package georegression.struct.line;


import java.io.Serializable;

/**
 * <p>
 * Defines a line in 2D space based upon the distance of its closest point to the origin
 * and the angle of a line from the origin to that point.  Also referred to as the polar line
 * equation.
 * </p>
 *
 * <p>
 * Equation:<br>
 * x*cos(&theta;) + y*sin(&theta;) = r<br>
 * where r is the distance from the closest point on the line to the origin, and &theta; is
 * an angle tangent to the line.
 * </p>
 * @author Peter Abeles
 */
public class LinePolar2D_F32 implements Serializable{
	/**
	 * Distance from the origin to the closest point on the line.
	 */
	public float distance;
	/**
	 * Angle in radians from the origin to the closest point on the line.
	 */
	public float angle;

	public LinePolar2D_F32(float distance, float angle) {
		this.distance = distance;
		this.angle = angle;
	}

	public LinePolar2D_F32() {
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public String toString() {
		return getClass().getSimpleName()+"{ d = "+distance+" angle = "+angle+" }";
	}
}

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

package georegression.struct.line;


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
public class LinePolar2D_F32 {
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
}

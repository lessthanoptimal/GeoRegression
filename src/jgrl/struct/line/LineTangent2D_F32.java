/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.line;


/**
 * Defines a line in 2D space based upon the distance of its closest point to the origin
 * and the angle of a line from the origin to that point.
 *
 * @author Peter Abeles
 */
public class LineTangent2D_F32 {
	/**
	 * Distance from the origin to the closest point on the line.
	 */
	public float distance;
	/**
	 * Angle in radians from the origin to the closest point on the line.
	 */
	public float angle;
}

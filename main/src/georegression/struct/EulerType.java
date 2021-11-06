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

package georegression.struct;

/**
 * Enumerated types for each possible Euler coordinate composed of three rotations.
 * Axises A,B,C indicate which coordinate axis is rotated first to third. 0 = axis-x, 1 = axis-y, 2 = axis-z.
 * The order in which the rotations are applied is from left to right. E.g. XYZ first applies the rotation around
 * x-axis, followed by y-axis, then z-axis.
 *
 * @author Peter Abeles
 */
public enum EulerType {
	ZYX(2, 1, 0),
	ZYZ(2, 1, 2),
	ZXY(2, 0, 1),
	ZXZ(2, 0, 2),
	YXZ(1, 0, 2),
	YXY(1, 0, 1),
	YZX(1, 2, 0),
	YZY(1, 2, 1),
	XYZ(0, 1, 2),
	XYX(0, 1, 0),
	XZY(0, 2, 1),
	XZX(0, 2, 0);

	EulerType( int axisA, int axisB, int axisC ) {
		this.axisA = axisA;
		this.axisB = axisB;
		this.axisC = axisC;
	}

	/**
	 * Returns the axis in which first rotation is performed around
	 */
	public int getAxisA() {
		return axisA;
	}

	/**
	 * Returns the axis in which second rotation is performed around
	 */
	public int getAxisB() {
		return axisB;
	}

	/**
	 * Returns the axis in which third rotation is performed around
	 */
	public int getAxisC() {
		return axisC;
	}

	final int axisA, axisB, axisC;
}

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

package georegression.transform.se;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

/**
 * <p>
 * Linear interpolation between two {@link georegression.struct.se.Se3_F64} transforms. It is assumed that
 * the rotation able between the two transforms is {@code < 180} degrees. If more than 180 degrees than it will chose
 * the wrong direction to rotate.
 * </p>
 * <p>
 * The rotation and translation are interpolated separately. Translation has straight forward linear interpolation
 * applied to it. Rotations are interpolated by finding the axis of rotation and the rotation angle. Then
 * the angle is linearly interpolated and the rotation matrix found by the angle and axis.
 * </p>
 * @author Peter Abeles
 */
public class InterpolateLinearSe3_F64 {

	// transform at the start
	Se3_F64 initial = new Se3_F64();
	// rodrigues representation of the rotation
	Rodrigues_F64 rotation = new Rodrigues_F64();
	// magnitude of the rotation. between initial and end
	double rotMagnitude;
	// translation between initial and end
	Vector3D_F64 translation = new Vector3D_F64();

	// difference between the initial transform and the end or the output
	DMatrixRMaj R = new DMatrixRMaj(3,3);

	/**
	 * Specify the two transforms which values are to be interpolated between
	 * @param initial first transform
	 * @param end second transform
	 */
	public void setTransforms( Se3_F64 initial , Se3_F64 end) {
		this.initial.setTo(initial);

		translation.x = end.T.x - initial.T.x;
		translation.y = end.T.y - initial.T.y;
		translation.z = end.T.z - initial.T.z;

		CommonOps_DDRM.multTransA(initial.getR(), end.getR(), R);

		ConvertRotation3D_F64.matrixToRodrigues(R,rotation);
		rotMagnitude = rotation.theta;
	}

	/**
	 * Interpolates a value between the first and second transform. A value close to 0 will be
	 * more similar to the initial and 1 more similar to the end.
	 * @param where A value between 0 and 1 which specifies the interpolation location
	 * @param output Resulting transform
	 */
	public void interpolate( double where , Se3_F64 output ) {
		rotation.setTheta(where*rotMagnitude);
		ConvertRotation3D_F64.rodriguesToMatrix(rotation,R);

		output.T.x = initial.T.x + where*translation.x;
		output.T.y = initial.T.y + where*translation.y;
		output.T.z = initial.T.z + where*translation.z;

		CommonOps_DDRM.mult(initial.R,R,output.R);
	}

}

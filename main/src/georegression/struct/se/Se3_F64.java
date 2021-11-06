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

package georegression.struct.se;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.struct.EulerType;
import georegression.struct.GeoTuple2D_F64;
import georegression.struct.GeoTuple3D_F64;
import georegression.struct.RotationType;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.transform.se.SePointOps_F64;
import org.ejml.UtilEjml;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.ops.MatrixIO;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

/**
 * A coordinate system transform composed of a rotation and translation. This transform is
 * a rigid body transform and is a member of the special euclidean group.
 *
 * @author Peter Abeles
 */
public class Se3_F64 implements SpecialEuclidean<Se3_F64> {

	// serialization version
	public static final long serialVersionUID = 1L;

	// rotation matrix
	public DMatrixRMaj R;
	// translation vector
	public Vector3D_F64 T;

	/**
	 * Creates a new transform that does nothing.
	 */
	public Se3_F64() {
		R = CommonOps_DDRM.identity(3);
		T = new Vector3D_F64();
	}

	/**
	 * Initializes the transform with the provided rotation and translation.
	 *
	 * @param R Rotation matrix.
	 * @param T Translation.
	 */
	public Se3_F64( DMatrixRMaj R, Vector3D_F64 T ) {
		this(R, T, false);
	}

	/**
	 * Initializes the Se3_F64 with the rotation matrix and translation vector. If assign
	 * is true the reference to the provided parameters is saved, otherwise a copy is made.
	 *
	 * @param R Rotation matrix.
	 * @param T Translation.
	 * @param assign If a reference is saved (true) or a copy made (false).
	 */
	public Se3_F64( DMatrixRMaj R, Vector3D_F64 T, boolean assign ) {
		if (assign) {
			this.R = R;
			this.T = T;
		} else {
			this.R = R.copy();
			this.T = T.copy();
		}
	}

	/**
	 * Set's 'this' Se3_F64 to be identical to the provided transform.
	 *
	 * @param se The transform that is being copied.
	 */
	@Override
	public void setTo( Se3_F64 se ) {
		R.setTo(se.getR());
		T.setTo(se.getT());
	}

	public void zero() {
		R.zero();
		T.zero();
	}

	/**
	 * Sets the rotation to R.
	 *
	 * @param R New rotation.
	 */
	public void setRotation( DMatrixRMaj R ) {
		this.R.setTo(R);
	}

	/**
	 * Sets the translation to T
	 *
	 * @param T New translation
	 */
	public void setTranslation( Vector3D_F64 T ) {
		this.T.setTo(T);
	}

	/**
	 * Sets the translation to (x,y,z)
	 *
	 * @param x x component of translation
	 * @param y y component of translation
	 * @param z z component of translation
	 */
	public void setTranslation( double x, double y, double z ) {
		this.T.setTo(x, y, z);
	}

	/**
	 * Returns the rotation matrix
	 *
	 * @return rotation matrix
	 */
	public DMatrixRMaj getRotation() {
		return R;
	}

	/**
	 * Returns the translation vector
	 *
	 * @return translation vector
	 */
	public Vector3D_F64 getTranslation() {
		return T;
	}

	public DMatrixRMaj getR() {
		return R;
	}

	public Vector3D_F64 getT() {
		return T;
	}

	public double getX() {
		return T.getX();
	}

	public double getY() {
		return T.getY();
	}

	public double getZ() {
		return T.getZ();
	}

	@Override
	public int getDimension() {
		return 3;
	}

	@Override
	public Se3_F64 createInstance() {
		return new Se3_F64();
	}

	@Override
	public Se3_F64 concat( Se3_F64 second, @Nullable Se3_F64 result ) {
		if (result == null)
			result = new Se3_F64();

		CommonOps_DDRM.mult(second.getR(), getR(), result.getR());
		GeometryMath_F64.mult(second.getR(), getT(), result.getT());
		GeometryMath_F64.add(second.getT(), result.getT(), result.getT());

		return result;
	}

	@Override
	public Se3_F64 invert( @Nullable Se3_F64 inverse ) {

		if (inverse == null)
			inverse = new Se3_F64();

		// To derive the inverse transform solve for P
		// R*P+T = P'
		// P = R^T*P' - R^T*T

		// -R^T*T
		GeometryMath_F64.multTran(R, T, inverse.T);
		GeometryMath_F64.changeSign(inverse.T);

		// R^T
		CommonOps_DDRM.transpose(R, inverse.R);

		return inverse;
	}

	@Override
	public void reset() {
		CommonOps_DDRM.setIdentity(R);
		T.setTo(0, 0, 0);
	}

	/**
	 * Fully specify the transform using Euler angles
	 */
	public void setTo( double x, double y, double z, EulerType type, double rotA, double rotB, double rotC ) {
		T.setTo(x, y, z);
		ConvertRotation3D_F64.eulerToMatrix(type, rotA, rotB, rotC, R);
	}

	/**
	 * Fully specifies the transform using Rodrigues (axis angle) or Quaternions. If Rodrigues then A=axisX, B=axisY,
	 * C=axisZ, D=theta. If Quaternion then A=w, B=x, C=y, D=z.
	 */
	public void setTo( double x, double y, double z, RotationType type, double A, double B, double C, double D ) {
		T.setTo(x, y, z);
		switch (type) {
			case RODRIGUES:
				ConvertRotation3D_F64.rodriguesToMatrix(A, B, C, D, R);
				break;

			case QUATERNION:
				ConvertRotation3D_F64.quaternionToMatrix(A, B, C, D, R);
				break;

			default:
				throw new IllegalArgumentException("Type is not supported. " + type);
		}
	}

	/**
	 * Applies the transform to the src point and stores the result in dst. src and dst can be the same instance
	 *
	 * @param src Input
	 * @param dst Output
	 * @return Output
	 * @see SePointOps_F64#transform(Se3_F64, Point3D_F64, Point3D_F64)
	 */
	public Point3D_F64 transform( Point3D_F64 src, @Nullable Point3D_F64 dst ) {
		return SePointOps_F64.transform(this, src, dst);
	}

	/**
	 * Applies the reverse transform to the src point and stores the result in dst. src and dst can be the same instance
	 *
	 * @param src Input
	 * @param dst Output
	 * @return Output
	 * @see SePointOps_F64#transformReverse(Se3_F64, Point3D_F64, Point3D_F64)
	 */
	public Point3D_F64 transformReverse( Point3D_F64 src, @Nullable Point3D_F64 dst ) {
		return SePointOps_F64.transformReverse(this, src, dst);
	}

	/**
	 * Applies the rotation to the src vector and stores the result in dst. src and dst can be the same instance
	 *
	 * @param src Input
	 * @param dst Output
	 * @see GeometryMath_F64#mult(DMatrixRMaj, GeoTuple2D_F64, GeoTuple2D_F64)
	 */
	public Vector3D_F64 transform( Vector3D_F64 src, @Nullable Vector3D_F64 dst ) {
		return GeometryMath_F64.mult(R, src, dst);
	}

	/**
	 * Applies the reverse rotation to the src vector and stores the result in dst. src and dst can be the same instance
	 *
	 * @param src Input
	 * @param dst Output
	 * @see GeometryMath_F64#multTran(DMatrixRMaj, GeoTuple3D_F64, GeoTuple3D_F64)
	 */
	public Vector3D_F64 transformReverse( Vector3D_F64 src, @Nullable Vector3D_F64 dst ) {
		return GeometryMath_F64.multTran(R, src, dst);
	}

	public Se3_F64 copy() {
		Se3_F64 ret = new Se3_F64();
		ret.setTo(this);

		return ret;
	}

	@Override
	public String toString() {
		String ret = "Se3_F64: T = " + T.toString() + "\n";
		ret += R;

		return ret;
	}

	/**
	 * More compact toString() where the rotation matrix is encoded in one of the specified formats.
	 */
	public String toString( RotationType type ) {
		DecimalFormat format = new DecimalFormat("#");

		final int sig = 4; // number of significant digits
		String tx = UtilEjml.fancyString(T.x, format, false, MatrixIO.DEFAULT_LENGTH, sig);
		String ty = UtilEjml.fancyString(T.y, format, false, MatrixIO.DEFAULT_LENGTH, sig);
		String tz = UtilEjml.fancyString(T.z, format, false, MatrixIO.DEFAULT_LENGTH, sig);
		String ret = "Se3_F64: T=(" + tx + ", " + ty + ", " + tz + "), ";

		switch (type) {
			case EULER: {
				double[] euler = new double[3];
				ConvertRotation3D_F64.matrixToEuler(R, EulerType.XYZ, euler);
				ret += "EulerXYZ=(" +
						UtilEjml.fancyString(euler[0], format, false, MatrixIO.DEFAULT_LENGTH, sig) + ", " +
						UtilEjml.fancyString(euler[1], format, false, MatrixIO.DEFAULT_LENGTH, sig) + ", " +
						UtilEjml.fancyString(euler[2], format, false, MatrixIO.DEFAULT_LENGTH, sig) + ")";
			}
			break;

			case RODRIGUES: {
				var rod = ConvertRotation3D_F64.matrixToRodrigues(R, null);
				ret += "Rodrigues={n=(" +
						UtilEjml.fancyString(rod.unitAxisRotation.x, format, false, MatrixIO.DEFAULT_LENGTH, sig) + ", " +
						UtilEjml.fancyString(rod.unitAxisRotation.y, format, false, MatrixIO.DEFAULT_LENGTH, sig) + ", " +
						UtilEjml.fancyString(rod.unitAxisRotation.z, format, false, MatrixIO.DEFAULT_LENGTH, sig) + "), theta=" +
						UtilEjml.fancyString(rod.theta, format, false, MatrixIO.DEFAULT_LENGTH, sig) + "}";
			}
			break;

			case QUATERNION: {
				var quat = ConvertRotation3D_F64.matrixToQuaternion(R, null);
				ret += "Quaternion=(" +
						UtilEjml.fancyString(quat.x, format, false, MatrixIO.DEFAULT_LENGTH, sig) + ", " +
						UtilEjml.fancyString(quat.y, format, false, MatrixIO.DEFAULT_LENGTH, sig) + ", " +
						UtilEjml.fancyString(quat.z, format, false, MatrixIO.DEFAULT_LENGTH, sig) + ", " +
						UtilEjml.fancyString(quat.w, format, false, MatrixIO.DEFAULT_LENGTH, sig) + ")";
			}
			break;
		}
		return ret;
	}

	public void print() {
		System.out.println(this);
	}

	public void print( RotationType type ) {
		System.out.println(toString(type));
	}
}

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
import georegression.struct.affine.Affine2D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.interfaces.decomposition.SingularValueDecomposition_F64;
import org.jetbrains.annotations.Nullable;

/**
 * Various operations related to {@link SpecialEuclidean} transformations.
 *
 * @author Peter Abeles
 */
public class SpecialEuclideanOps_F64 {

	/**
	 * Sets the provided transform so that it does not transform any points.
	 *
	 * @param se The transform which is to be set to no motion.
	 */
	public static void setToNoMotion( Se3_F64 se ) {
		CommonOps_DDRM.setIdentity(se.getR());
		se.getT().setTo(0, 0, 0);
	}

	/**
	 * Converts {@link Se2_F64} into {@link Affine2D_F64}.
	 *
	 * @param se (Input) Se2
	 * @param affine (Output) Equivalent affine. If null a new object will be declared.
	 * @return Equivalent affine.
	 */
	public static Affine2D_F64 toAffine( Se2_F64 se, @Nullable Affine2D_F64 affine ) {
		if (affine == null)
			affine = new Affine2D_F64();

		affine.a11 = se.c;
		affine.a12 = -se.s;
		affine.a21 = se.s;
		affine.a22 = se.c;

		affine.tx = se.T.x;
		affine.ty = se.T.y;

		return affine;
	}

	/**
	 * Converts it into a 4 by 4 homogeneous matrix.
	 *
	 * @param se original 3D transform
	 * @param ret Where the results will be written to. If null a new matrix is declared. Modified.
	 * @return equivalent homogeneous transform.
	 */
	public static DMatrixRMaj toHomogeneous( Se3_F64 se, @Nullable DMatrixRMaj ret ) {
		if (ret == null)
			ret = new DMatrixRMaj(4, 4);
		else {
			ret.set(3, 0, 0);
			ret.set(3, 1, 0);
			ret.set(3, 2, 0);
		}

		CommonOps_DDRM.insert(se.getR(), ret, 0, 0);
		Vector3D_F64 T = se.getT();

		ret.set(0, 3, T.x);
		ret.set(1, 3, T.y);
		ret.set(2, 3, T.z);
		ret.set(3, 3, 1);

		return ret;
	}

	/**
	 * Converts a homogeneous representation into {@link Se3_F64}.
	 *
	 * @param H Homogeneous 4 by 4 matrix.
	 * @param ret If not null where the results are written to.
	 * @return Se3_F64 transform.
	 */
	public static Se3_F64 toSe3( DMatrixRMaj H, @Nullable Se3_F64 ret ) {
		if (H.numCols != 4 || H.numRows != 4)
			throw new IllegalArgumentException("The homogeneous matrix must be 4 by 4 by definition.");

		if (ret == null)
			ret = new Se3_F64();

		ret.setTranslation((double)H.get(0, 3), (double)H.get(1, 3), (double)H.get(2, 3));

		CommonOps_DDRM.extract(H, 0, 3, 0, 3, ret.getR(), 0, 0);

		return ret;
	}

	/**
	 * Converts it into a 3 by 3 homogeneous matrix.
	 *
	 * @param se original 2D transform
	 * @param ret Where the results will be written to. If null a new matrix is declared. Modified.
	 * @return equivalent homogeneous transform.
	 */
	public static DMatrixRMaj toHomogeneous( Se2_F64 se, @Nullable DMatrixRMaj ret ) {
		if (ret == null)
			ret = new DMatrixRMaj(3, 3);
		else {
			ret.set(2, 0, 0);
			ret.set(2, 1, 0);
		}

		final double c = se.getCosineYaw();
		final double s = se.getSineYaw();

		ret.set(0, 0, c);
		ret.set(0, 1, -s);
		ret.set(1, 0, s);
		ret.set(1, 1, c);
		ret.set(0, 2, se.getX());
		ret.set(1, 2, se.getY());
		ret.set(2, 2, 1);

		return ret;
	}

	/**
	 * Converts a homogeneous representation into {@link Se2_F64}.
	 *
	 * @param H Homogeneous 3 by 3 matrix.
	 * @param ret If not null where the results are written to.
	 * @return Se3_F64 transform.
	 */
	public static Se2_F64 toSe2( DMatrixRMaj H, @Nullable Se2_F64 ret ) {
		if (H.numCols != 3 || H.numRows != 3)
			throw new IllegalArgumentException("The homogeneous matrix must be 3 by 3 by definition.");

		if (ret == null)
			ret = new Se2_F64();

		ret.setTranslation((double)H.get(0, 2), (double)H.get(1, 2));

		double c = (double)H.get(0, 0);
		double s = (double)H.get(1, 0);

		ret.setYaw( (double)Math.atan2(s, c));

		return ret;
	}

	/**
	 * Sets the value of an {@link Se3_F64} using Euler XYZ coordinates for the rotation and
	 * a translation vector.
	 *
	 * @param dx Translation along x-axis.
	 * @param dy Translation along y-axis.
	 * @param dz Translation along z-axis.
	 * @param rotX Rotation around X axis.
	 * @param rotY Rotation around Y axis.
	 * @param rotZ Rotation around Z axis.
	 * @param se If not null then the transform is written here.
	 * @return The transform.
	 */
	public static Se3_F64 eulerXyz( double dx, double dy, double dz, double rotX, double rotY, double rotZ,
									@Nullable Se3_F64 se ) {
		return eulerXyz(dx, dy, dz, EulerType.XYZ, rotX, rotY, rotZ, se);
	}

	public static Se3_F64 eulerXyz( double dx, double dy, double dz,
									EulerType type, double rotX, double rotY, double rotZ,
									@Nullable Se3_F64 se ) {
		if (se == null)
			se = new Se3_F64();

		ConvertRotation3D_F64.eulerToMatrix(type, rotX, rotY, rotZ, se.getR());
		Vector3D_F64 T = se.getT();
		T.x = dx;
		T.y = dy;
		T.z = dz;

		return se;
	}

	/**
	 * Create SE3 using axis-angle for rotation and XYZ tanslation
	 *
	 * @param dx Translation along x-axis.
	 * @param dy Translation along y-axis.
	 * @param dz Translation along z-axis.
	 * @param rotX x-axis component
	 * @param rotY y-axis component
	 * @param rotZ z-axis component
	 * @param se If not null then the transform is written here.
	 * @return The transform.
	 */
	public static Se3_F64 axisXyz( double dx, double dy, double dz, double rotX, double rotY, double rotZ,
								   @Nullable Se3_F64 se ) {
		if (se == null)
			se = new Se3_F64();

		double theta = Math.sqrt(rotX*rotX + rotY + rotY + rotZ*rotZ);
		if (theta == 0) {
			CommonOps_DDRM.setIdentity(se.R);
		} else {
			ConvertRotation3D_F64.rodriguesToMatrix(rotX/theta, rotY/theta, rotZ/theta, theta, se.getR());
		}

		Vector3D_F64 T = se.getT();
		T.x = dx;
		T.y = dy;
		T.z = dz;
		return se;
	}

	public static Se3_F64 quatXyz( double dx, double dy, double dz,
								   double qw, double qx, double qy, double qz,
								   @Nullable Se3_F64 se ) {
		if (se == null)
			se = new Se3_F64();


		ConvertRotation3D_F64.quaternionToMatrix(qw, qx, qy, qz, se.getR());

		Vector3D_F64 T = se.getT();
		T.x = dx;
		T.y = dy;
		T.z = dz;
		return se;
	}

	/**
	 * Can be used to see if two transforms are identical to within tolerance
	 *
	 * @param a transform
	 * @param b tranform
	 * @param tolT Tolerance for translation
	 * @param tolR Tolerance for rotation in radians
	 * @return true if identical or false if not
	 */
	public static boolean isIdentical( Se3_F64 a, Se3_F64 b, double tolT, double tolR ) {
		if (Math.abs(a.T.x - b.T.x) > tolT)
			return false;
		if (Math.abs(a.T.y - b.T.y) > tolT)
			return false;
		if (Math.abs(a.T.z - b.T.z) > tolT)
			return false;

		DMatrixRMaj D = new DMatrixRMaj(3, 3);
		CommonOps_DDRM.multTransA(a.R, b.R, D);

		Rodrigues_F64 rod = new Rodrigues_F64();
		ConvertRotation3D_F64.matrixToRodrigues(D, rod);

		return rod.theta <= tolR;
	}

	/**
	 * Finds the best fit projection of 'a' onto SE(3). This is useful when a was estimated using a linear algorithm.
	 *
	 * <p>See page 280 of "An Invitation to 3-D Vision, From Images to Geometric Models" 1st Ed. 2004. Springer.</p>
	 *
	 * @param a Approximate SE(3). Modified.
	 * @return true if successful
	 */
	public static boolean bestFit( Se3_F64 a ) {
		SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(true, true, true);

		if (!svd.decompose(a.R))
			throw new RuntimeException("SVD Failed");

		CommonOps_DDRM.multTransB(svd.getU(null, false), svd.getV(null, false), a.R);

		// determinant should be +1
		double det = CommonOps_DDRM.det(a.R);

		if (det < 0) {
			CommonOps_DDRM.scale(-1, a.R);
		}

		// compute the determinant of the singular matrix
		double b = 1.0;
		double s[] = svd.getSingularValues();

		for (int i = 0; i < svd.numberOfSingularValues(); i++) {
			b *= s[i];
		}

		b = Math.signum(det)/ (double) Math.pow(b, 1.0/3.0);

		GeometryMath_F64.scale(a.T, b);

		return true;
	}
}

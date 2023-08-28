/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import georegression.struct.so.Quaternion_F64;
import org.ejml.data.DMatrix3x3;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF3;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.interfaces.decomposition.SingularValueDecomposition_F64;
import org.ejml.ops.DConvertMatrixStruct;

import java.util.List;

/**
 * <p>Finds the average of a set of {@link Quaternion_F64 quaternions} by using a modification of the
 * method proposed in [1]. It works by finding the average rotation matrix then finds the best fit matrix
 * in SO-3, see page 200 of [2].</p>
 *
 * <p>[1] MLA Markley, F. Landis, et al. "Quaternion averaging." (2007)</p>
 * <p>[2] "An Invitation to 3-D Vision, From Images to Geometric Models" 1st Ed. 2004. Springer.</p>
 *
 * @author Peter Abeles
 */
public class AverageRotationMatrix_F64 {

	// Internal workspace to store the average
	private DMatrixRMaj M = new DMatrixRMaj(3, 3);
	private DMatrix3x3 F = new DMatrix3x3();

	// Used to store an extracted matrix
	private DMatrix3x3 ftmp = new DMatrix3x3();

	SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(3, 3, true, true, true);

	// Storage for U,V in SVD
	private DMatrixRMaj U = new DMatrixRMaj(3, 3);
	private DMatrixRMaj V = new DMatrixRMaj(3, 3);

	/**
	 * Finds average rotation matrix for matrices stored in {@link DMatrixRMaj} format.
	 *
	 * @param list (Input) rotation matrices
	 * @param average (Output) average rotation.
	 * @return true if successful or false if it failed
	 */
	public boolean process( List<DMatrixRMaj> list, DMatrixRMaj average ) {

		if (list.isEmpty())
			throw new IllegalArgumentException("Input list is empty");
		if (average == null)
			throw new IllegalArgumentException("average is null");

		M.zero();

		for (int i = 0; i < list.size(); i++) {
			DMatrixRMaj m = list.get(i);

			// unroll to make it faster. M = M + m
			//@formatter:off
			M.data[0]  += m.data[0]; M.data[1]  += m.data[1]; M.data[2]  += m.data[2]; // row 0
			M.data[3]  += m.data[3]; M.data[4]  += m.data[4]; M.data[5]  += m.data[5]; // row 1
			M.data[6]  += m.data[6]; M.data[7]  += m.data[7]; M.data[8]  += m.data[8]; // row 2
			//@formatter:on
		}

		CommonOps_DDRM.divide(M, list.size());

		if (!svd.decompose(M))
			return false;

		CommonOps_DDRM.multTransB(svd.getU(U, false), svd.getV(V, false), average);

		// determinant should be +1
		double det = CommonOps_DDRM.det(average);

		if (det < 0)
			CommonOps_DDRM.scale(-1, average);

		return true;
	}

	/**
	 * Finds average rotation matrix for matrices stored in {@link DMatrix3x3} format.
	 *
	 * @param list (Input) rotation matrices
	 * @param average (Output) average rotation.
	 * @return true if successful or false if it failed
	 */
	public boolean process( List<DMatrix3x3> list, DMatrix3x3 average ) {

		if (list.isEmpty())
			throw new IllegalArgumentException("Input list is empty");
		if (average == null)
			throw new IllegalArgumentException("average is null");

		CommonOps_DDF3.fill(F, 0);

		for (int i = 0; i < list.size(); i++) {
			CommonOps_DDF3.add(F, list.get(i), F);
		}

		CommonOps_DDF3.divide(F, list.size());

		DConvertMatrixStruct.convert(F, M);
		if (!svd.decompose(M))
			return false;

		CommonOps_DDRM.multTransB(svd.getU(U, false), svd.getV(V, false), M);

		// determinant should be +1
		double det = CommonOps_DDRM.det(M);

		if (det < 0)
			CommonOps_DDRM.scale(-1, M);

		DConvertMatrixStruct.convert(M, average);

		return true;
	}

	/**
	 * Computes average rotation matrix given a list with an arbitrary data type that can be converted into
	 * a {@link DMatrix3x3}.
	 *
	 * @param list List of data structures with rotation matrix in it
	 * @param average (Output) Storage for computed average matrix
	 * @param op Converts input data type to {@link DMatrix3x3}
	 * @return true if successful or false if it failed
	 */
	public <T> boolean processOp( List<T> list, DMatrixRMaj average, Convert<T> op ) {
		if (list.isEmpty())
			throw new IllegalArgumentException("Input list is empty");
		if (average == null)
			throw new IllegalArgumentException("average is null");

		CommonOps_DDF3.fill(F, 0);

		for (int i = 0; i < list.size(); i++) {
			op.convert(list.get(i), ftmp);
			CommonOps_DDF3.add(F, ftmp, F);
		}

		CommonOps_DDF3.divide(F, list.size());

		DConvertMatrixStruct.convert(F, average);
		if (!svd.decompose(average))
			return false;

		CommonOps_DDRM.multTransB(svd.getU(U, false), svd.getV(V, false), average);

		// determinant should be +1
		double det = CommonOps_DDRM.det(average);

		if (det < 0)
			CommonOps_DDRM.scale(-1, average);

		return true;
	}

	public interface Convert<T> {
		void convert( T src, DMatrix3x3 dst );
	}
}

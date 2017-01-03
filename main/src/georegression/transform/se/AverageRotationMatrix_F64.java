/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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
import org.ejml.alg.fixed.FixedOps3_D64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.data.FixedMatrix3x3_64F;
import org.ejml.factory.DecompositionFactory_D64;
import org.ejml.interfaces.decomposition.SingularValueDecomposition_F64;
import org.ejml.ops.CommonOps_D64;
import org.ejml.ops.ConvertMatrixType_F64;

import java.util.List;

/**
 * <p>Finds the average of a set of {@link Quaternion_F64 quaternions} by using a modification of the
 * method proposed in [1].  It works by finding the average rotation matrix then finds the best fit matrix
 * in SO-3, see page 200 of [2].</p>
 *
 * <p>[1] MLA Markley, F. Landis, et al. "Quaternion averaging." (2007)</p>
 * <p>[2] "An Invitation to 3-D Vision, From Images to Geometric Models" 1st Ed. 2004. Springer.</p>
 *
 * @author Peter Abeles
 */
public class AverageRotationMatrix_F64 {

	DenseMatrix64F M = new DenseMatrix64F(3,3);
	FixedMatrix3x3_64F F = new FixedMatrix3x3_64F();

	SingularValueDecomposition_F64<DenseMatrix64F> svd = DecompositionFactory_D64.svd(3,3,true,true,true);

	public boolean process(List<DenseMatrix64F> list , DenseMatrix64F average ) {

		if( list.isEmpty() )
			throw new IllegalArgumentException("Input list is empty");
		if( average == null )
			throw new IllegalArgumentException("average is null");

		M.zero();

		for (int i = 0; i < list.size(); i++) {
			DenseMatrix64F m = list.get(i);

			// unroll to make it faster.  M = M + m
			// row 0
			M.data[0]  += m.data[0]; M.data[1]  += m.data[1]; M.data[2]  += m.data[2];
			// row 1
			M.data[3]  += m.data[3]; M.data[4]  += m.data[4]; M.data[5]  += m.data[5];
			// row 2
			M.data[6]  += m.data[6]; M.data[7]  += m.data[7]; M.data[8]  += m.data[8];
		}

		CommonOps_D64.divide(M,list.size());

		if( !svd.decompose(M) )
			return false;

		CommonOps_D64.multTransB(svd.getU(null,false),svd.getV(null,false),average);

		// determinant should be +1
		/**/double det = CommonOps_D64.det(average);

		if( det < 0 )
			CommonOps_D64.scale(-1,average);

		return true;
	}

	public boolean process(List<FixedMatrix3x3_64F> list , FixedMatrix3x3_64F average ) {

		if( list.isEmpty() )
			throw new IllegalArgumentException("Input list is empty");
		if( average == null )
			throw new IllegalArgumentException("average is null");

		FixedOps3_D64.fill(F,0);

		for (int i = 0; i < list.size(); i++) {
			FixedMatrix3x3_64F m = list.get(i);

			// unroll to make it faster.  M = M + m
			// row 0
			F.a11  += m.a11; F.a12  += m.a12; F.a13  += m.a13;
			// row 1
			F.a21  += m.a21; F.a22  += m.a22; F.a23  += m.a23;
			// row 2
			F.a31  += m.a31; F.a32  += m.a32; F.a33  += m.a33;
		}

		FixedOps3_D64.divide(F,list.size());

		ConvertMatrixType_F64.convert(F,M);
		if( !svd.decompose(M) )
			return false;

		CommonOps_D64.multTransB(svd.getU(null,false),svd.getV(null,false),M);

		// determinant should be +1
		/**/double det = CommonOps_D64.det(M);

		if( det < 0 )
			CommonOps_D64.scale(-1,M);

		ConvertMatrixType_F64.convert(M,average);

		return true;
	}
}

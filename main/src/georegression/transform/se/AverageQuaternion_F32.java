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

import georegression.struct.so.Quaternion_F32;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory_D64;
import org.ejml.interfaces.decomposition.EigenDecomposition_F64;

import java.util.List;

/**
 * <p>Finds the average of a set of {@link Quaternion_F32 quaternions} by using a method proposed in [1].</p>
 *
 * <p>[1] MLA Markley, F. Landis, et al. "Quaternion averaging." (2007)</p>
 *
 * @author Peter Abeles
 */
public class AverageQuaternion_F32 {

	DenseMatrix64F M = new DenseMatrix64F(4,4);

	EigenDecomposition_F64<DenseMatrix64F> eig = DecompositionFactory_D64.eig(4,true,true);

	public boolean process(List<Quaternion_F32> list , Quaternion_F32 average ) {

		if( list.isEmpty() )
			throw new IllegalArgumentException("Input list is empty");
		if( average == null )
			throw new IllegalArgumentException("average is null");

		M.zero();

		for (int i = 0; i < list.size(); i++) {
			Quaternion_F32 q = list.get(i);

			// Perform M = M + q*q^T
			// Where q is a column [w,x,y,z] vector

			// row 0
			M.data[0]  += q.w*q.w; M.data[1]  += q.w*q.x; M.data[2]  += q.w*q.y; M.data[3]  += q.w*q.z;
			// row 1
			M.data[4]  += q.x*q.w; M.data[5]  += q.x*q.x; M.data[6]  += q.x*q.y; M.data[7]  += q.x*q.z;
			// row 2
			M.data[8]  += q.y*q.w; M.data[9]  += q.y*q.x; M.data[10] += q.y*q.y; M.data[11] += q.y*q.z;
			// row 3
			M.data[12] += q.z*q.w; M.data[13] += q.z*q.x; M.data[14] += q.z*q.y; M.data[15] += q.z*q.z;
		}

		if( !eig.decompose(M) )
			return false;

		// the largest eigenvector is the quaternion
		int largest = 0;
		/**/double largestMag = eig.getEigenvalue(0).getMagnitude2();
		for (int i = 1; i < 4; i++) {
			/**/double mag = eig.getEigenvalue(i).getMagnitude2();
			if( mag > largestMag ) {
				largestMag = mag;
				largest = i;
			}
		}

		DenseMatrix64F v = eig.getEigenVector(largest);

		// this will be a normalized quaternion due to properties of eigenvectors
		average.w = (float) v.get(0);
		average.x = (float) v.get(1);
		average.y = (float) v.get(2);
		average.z = (float) v.get(3);

		return true;
	}
}

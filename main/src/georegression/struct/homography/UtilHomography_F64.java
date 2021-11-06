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

package georegression.struct.homography;


import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.jetbrains.annotations.Nullable;

/**
 * Various useful functions related to homographies.
 *
 * @author Peter Abeles
 */
public class UtilHomography_F64 {

	/**
	 * Converts the provided 3x3 matrix into a {@link Homography2D_F64}.
	 *
	 * @param m Input 3x3 matrix.
	 * @param ret Storage for output. If null then a new instance is created.
	 * @return Equivalent homography.
	 */
	public static Homography2D_F64 convert( DMatrixRMaj m , @Nullable Homography2D_F64 ret ) {
		if( m.numCols != 3 || m.numRows != 3)
			throw new IllegalArgumentException("Expected a 3 by 3 matrix.");

		if( ret == null )
			ret = new Homography2D_F64();

		ret.a11 = m.unsafe_get(0,0);
		ret.a12 = m.unsafe_get(0,1);
		ret.a13 = m.unsafe_get(0,2);
		ret.a21 = m.unsafe_get(1,0);
		ret.a22 = m.unsafe_get(1,1);
		ret.a23 = m.unsafe_get(1,2);
		ret.a31 = m.unsafe_get(2,0);
		ret.a32 = m.unsafe_get(2,1);
		ret.a33 = m.unsafe_get(2,2);

		return ret;
	}

	/**
	 * Converts a {@link Homography2D_F64} into a 3x3 matrix.
	 *
	 * @param m Homography
	 * @param ret Storage for output. If null then a new instance is created.
	 * @return Equivalent matrix.
	 */
	public static DMatrixRMaj convert( Homography2D_F64 m , @Nullable DMatrixRMaj ret ) {
		if( ret == null ) {
			ret = new DMatrixRMaj(3,3);
		} else if( ret.numCols != 3 || ret.numRows != 3)
			throw new IllegalArgumentException("Expected a 3 by 3 matrix.");


		ret.unsafe_set(0,0,m.a11);
		ret.unsafe_set(0,1,m.a12);
		ret.unsafe_set(0,2,m.a13);
		ret.unsafe_set(1,0,m.a21);
		ret.unsafe_set(1,1,m.a22);
		ret.unsafe_set(1,2,m.a23);
		ret.unsafe_set(2,0,m.a31);
		ret.unsafe_set(2,1,m.a32);
		ret.unsafe_set(2,2,m.a33);

		return ret;
	}

	public static Homography2D_F64 invert( Homography2D_F64 orig , @Nullable Homography2D_F64 inverted ) {
		if( inverted == null )
			inverted = new Homography2D_F64();

		DMatrixRMaj A = new DMatrixRMaj(3,3);
		convert(orig,A);
		CommonOps_DDRM.invert(A);
		convert(A,inverted);

		return inverted;
	}

	public static void scale( Homography2D_F64 h , double scale ) {
		h.a11 *= scale;
		h.a12 *= scale;
		h.a13 *= scale;
		h.a21 *= scale;
		h.a22 *= scale;
		h.a23 *= scale;
		h.a31 *= scale;
		h.a32 *= scale;
		h.a33 *= scale;
	}

   public static void print(Homography2D_F64 h)
   {
      for( int y = 0; y < 3; y++ ) {
         for( int x = 0; x < 3; x++ ) {
            System.out.printf("%8.1e ",h.get(x,y));
         }
         System.out.println();
      }
   }
}

/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.struct.homo;


import georegression.struct.Matrix3x3_F64;

/**
 * Describes a homography transform in 2D.
 *
 * @author Peter Abeles
 */
public class Homography2D_F64 extends Matrix3x3_F64 implements Homography<Homography2D_F64>{


	public Homography2D_F64(double a11, double a12, double a13,
							double a21, double a22, double a23,
							double a31, double a32, double a33) {
		this.a11 = a11;
		this.a12 = a12;
		this.a13 = a13;
		this.a21 = a21;
		this.a22 = a22;
		this.a23 = a23;
		this.a31 = a31;
		this.a32 = a32;
		this.a33 = a33;
	}

	public Homography2D_F64( Homography2D_F64 a ) {
		set(a);
	}

	public Homography2D_F64() {
		reset();
	}


	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Homography2D_F64 createInstance() {
		return new Homography2D_F64();
	}

	@Override
	public void set(Homography2D_F64 target) {
		super.set(target);
	}

	@Override
	public Homography2D_F64 concat(Homography2D_F64 second, Homography2D_F64 ret) {
		if( ret == null )
			ret = new Homography2D_F64();

		ret.a11 = second.a11 * a11 + second.a12 * a21 + second.a13 * a31;
		ret.a12 = second.a11 * a12 + second.a12 * a22 + second.a13 * a32;
		ret.a13 = second.a11 * a13 + second.a12 * a23 + second.a13 * a33;
		ret.a21 = second.a21 * a11 + second.a22 * a21 + second.a23 * a31;
		ret.a22 = second.a21 * a12 + second.a22 * a22 + second.a23 * a32;
		ret.a23 = second.a21 * a13 + second.a22 * a23 + second.a23 * a33;
		ret.a31 = second.a31 * a11 + second.a32 * a21 + second.a33 * a31;
		ret.a32 = second.a31 * a12 + second.a32 * a22 + second.a33 * a32;
		ret.a33 = second.a31 * a13 + second.a32 * a23 + second.a33 * a33;

		return ret;
	}

	@Override
	public Homography2D_F64 invert(Homography2D_F64 ret) {
	    if( ret == null )
			ret = new Homography2D_F64();

		// invert the matrix using the inverse from minor technique
		double m11 = a22*a33 - a23*a32;
		double m12 = -( a21*a33 - a23*a31);
		double m13 = a21*a32 - a22*a31;
		double m21 = -( a12*a33 - a13*a32);
		double m22 = a11*a33 - a13*a31;
		double m23 = -( a11*a32 - a12*a31);
		double m31 = a12*a23 - a13*a22;
		double m32 = -( a11*a23 - a13*a21);
		double m33 = a11*a22 - a12*a21;

		double det = (a11*m11 + a12*m12 + a13*m13);

		ret.a11 = m11 / det;
		ret.a12 = m21 / det;
		ret.a13 = m31 / det;
		ret.a21 = m12 / det;
		ret.a22 = m22 / det;
		ret.a23 = m32 / det;
		ret.a31 = m13 / det;
		ret.a32 = m23 / det;
		ret.a33 = m33 / det;

		return ret;
	}

	@Override
	public void reset() {
		a11 = a22 = a33 = 1;
		a12 = a13 = a21 = a23 = a31 = a32 = 0;
	}

   public double get( int row , int col ) {
      int index = row*3+col;
      switch( index ) {
         case 0:
            return a11;
         case 1:
            return a12;
         case 2:
            return a13;
         case 3:
            return a21;
         case 4:
            return a22;
         case 5:
            return a23;
         case 6:
            return a31;
         case 7:
            return a32;
         case 8:
            return a33;
      }
      throw new IllegalArgumentException("Invalid coordinate: "+row+"  "+col);
   }

	public Homography2D_F64 copy() {
		return new Homography2D_F64(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+String.format("[ %5.2e %5.2e %5.2e ; %5.2e %5.2e %5.2e ; %5.2e %5.2e %5.2e ]",
				a11,a12,a13,a21,a22,a31, a31,a32,a33);
	}
}

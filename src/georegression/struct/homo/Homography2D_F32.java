/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.homo;


import georegression.struct.Matrix3x3_F32;

/**
 * Describes a homography transform in 2D.
 *
 * @author Peter Abeles
 */
public class Homography2D_F32 extends Matrix3x3_F32 implements Homography<Homography2D_F32>{


	public Homography2D_F32(float a11, float a12, float a13,
							float a21, float a22, float a23,
							float a31, float a32, float a33) {
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

	public Homography2D_F32( Homography2D_F32 a ) {
		set(a);
	}

	public Homography2D_F32() {
		reset();
	}


	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Homography2D_F32 createInstance() {
		return new Homography2D_F32();
	}

	@Override
	public void set(Homography2D_F32 target) {
		super.set(target);
	}

	@Override
	public Homography2D_F32 concat(Homography2D_F32 second, Homography2D_F32 ret) {
		if( ret == null )
			ret = new Homography2D_F32();

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
	public Homography2D_F32 invert(Homography2D_F32 ret) {
	    if( ret == null )
			ret = new Homography2D_F32();

		// invert the matrix using the inverse from minor technique
		float m11 = a22*a33 - a23*a32;
		float m12 = -( a21*a33 - a23*a31);
		float m13 = a21*a32 - a22*a31;
		float m21 = -( a12*a33 - a13*a32);
		float m22 = a11*a33 - a13*a31;
		float m23 = -( a11*a32 - a12*a31);
		float m31 = a12*a23 - a13*a22;
		float m32 = -( a11*a23 - a13*a21);
		float m33 = a11*a22 - a12*a21;

		float det = (a11*m11 + a12*m12 + a13*m13);

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

   public float get( int row , int col ) {
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

	public Homography2D_F32 copy() {
		return new Homography2D_F32(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+String.format("[ %5.2fe %5.2fe %5.2fe ; %5.2fe %5.2fe %5.2fe ; %5.2fe %5.2fe %5.2fe ]",
				a11,a12,a13,a21,a22,a31, a31,a32,a33);
	}
}

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

package georegression.struct;

/**
 * Matrix with a fixed size of 3 by 3.
 *
 * @author Peter Abeles
 */
public class Matrix3x3_F64 {
	public double a11,a12,a13;
	public double a21,a22,a23;
	public double a31,a32,a33;

	public void zero() {
		a11=a12=a13=a21=a22=a23=a31=a32=a33=0;
	}

	public void set( Matrix3x3_F64 b ) {
		a11=b.a11; a12=b.a12; a13=b.a13;
		a21=b.a21; a22=b.a22; a23=b.a23;
		a31=b.a31; a32=b.a32; a33=b.a33;
	}

	public void set( double a11, double a12, double a13 ,
					 double a21, double a22, double a23 ,
					 double a31, double a32, double a33 ) {
		this.a11=a11;
		this.a12=a12;
		this.a13=a13;
		this.a21=a21;
		this.a22=a22;
		this.a23=a23;
		this.a31=a31;
		this.a32=a32;
		this.a33=a33;
	}

	public void scale( double value ) {
		a11 *= value;
		a12 *= value;
		a13 *= value;
		a21 *= value;
		a22 *= value;
		a23 *= value;
		a31 *= value;
		a32 *= value;
		a33 *= value;
	}
}

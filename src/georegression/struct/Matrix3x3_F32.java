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

package georegression.struct;

import java.io.Serializable;

/**
 * Matrix with a fixed size of 3 by 3.
 *
 * @author Peter Abeles
 */
public class Matrix3x3_F32 implements Serializable {
	public float a11,a12,a13;
	public float a21,a22,a23;
	public float a31,a32,a33;

	public void zero() {
		a11=a12=a13=a21=a22=a23=a31=a32=a33=0;
	}

	public void set( Matrix3x3_F32 b ) {
		a11=b.a11; a12=b.a12; a13=b.a13;
		a21=b.a21; a22=b.a22; a23=b.a23;
		a31=b.a31; a32=b.a32; a33=b.a33;
	}

	public void set( float a11, float a12, float a13 ,
					 float a21, float a22, float a23 ,
					 float a31, float a32, float a33 ) {
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

	public void scale( float value ) {
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

	public float getA11() {
		return a11;
	}

	public void setA11(float a11) {
		this.a11 = a11;
	}

	public float getA12() {
		return a12;
	}

	public void setA12(float a12) {
		this.a12 = a12;
	}

	public float getA13() {
		return a13;
	}

	public void setA13(float a13) {
		this.a13 = a13;
	}

	public float getA21() {
		return a21;
	}

	public void setA21(float a21) {
		this.a21 = a21;
	}

	public float getA22() {
		return a22;
	}

	public void setA22(float a22) {
		this.a22 = a22;
	}

	public float getA23() {
		return a23;
	}

	public void setA23(float a23) {
		this.a23 = a23;
	}

	public float getA31() {
		return a31;
	}

	public void setA31(float a31) {
		this.a31 = a31;
	}

	public float getA32() {
		return a32;
	}

	public void setA32(float a32) {
		this.a32 = a32;
	}

	public float getA33() {
		return a33;
	}

	public void setA33(float a33) {
		this.a33 = a33;
	}
}

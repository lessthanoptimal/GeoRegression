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

package georegression.struct.plane;

import java.io.Serializable;

/**
 * <p>
 * Represents the line using four parameters such that any point on the planes obeys the
 * following formula, A*x + B*y + C*z = D.  Any 3D plane can be represented using this notation.
 * This formulation is also known as scalar.
 * </p>
 *
 * <p>
 * If in Hessian normal form, then (A,B,C) is the unit normal, and D is distance of the plane from the origin.  The
 * sign of D determines the side on the plane on which the origin is located.
 * </p>
 *
 * <p>
 * NOTE: The normal of the plane is the vector (A,B,C)
 * </p>
 *
 * @author Peter Abeles
 */
public class PlaneGeneral3D_F32 implements Serializable {
	/**
	 * Coefficients which define the plane.
	 */
	public float A,B,C,D;

	public PlaneGeneral3D_F32( PlaneGeneral3D_F32 src ) {
		set(src);
	}

	public PlaneGeneral3D_F32(float a, float b, float c, float d) {
		set(a,b,c,d);
	}

	public PlaneGeneral3D_F32() {
	}

	public float getA() {
		return A;
	}

	public float getB() {
		return B;
	}

	public float getC() {
		return C;
	}

	public float getD() {
		return D;
	}

	public void set(float a, float b, float c, float d) {
		this.A = a;
		this.B = b;
		this.C = c;
		this.D = d;
	}

	public void set( PlaneGeneral3D_F32 src ) {
		this.A = src.A;
		this.B = src.B;
		this.C = src.C;
		this.D = src.D;
	}

	public void setA(float a) {
		A = a;
	}

	public void setB(float b) {
		B = b;
	}

	public void setC(float c) {
		C = c;
	}

	public void setD(float d) {
		D = d;
	}

	public String toString() {
		return getClass().getSimpleName()+"( A = "+A+" B = "+B+" C = "+C+" D = "+D+" )";
	}
}

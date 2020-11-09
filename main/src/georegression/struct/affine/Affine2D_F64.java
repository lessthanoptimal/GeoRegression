/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.struct.affine;


import org.jetbrains.annotations.Nullable;

/**
 * 2D affine transform for 64-bit floats.
 *
 * @author Peter Abeles
 */
public class Affine2D_F64 implements Affine<Affine2D_F64> {

	// rotational, sheer, enlarge, ... etc
	public double a11, a12, a21, a22;
	// translational components
	public double tx, ty;

	public Affine2D_F64( double a11, double a12, double a21, double a22, double tx, double ty ) {
		setTo(a11,a12,a21,a22,tx,ty);
	}

	public Affine2D_F64() {
		reset();
	}

	public void setTo(double a11, double a12, double a21, double a22, double tx, double ty ) {
		this.a11 = a11;
		this.a12 = a12;
		this.a21 = a21;
		this.a22 = a22;
		this.tx = tx;
		this.ty = ty;
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Affine2D_F64 createInstance() {
		return new Affine2D_F64();
	}

	@Override
	public void setTo(Affine2D_F64 target ) {
		this.a11 = target.a11;
		this.a12 = target.a12;
		this.a21 = target.a21;
		this.a22 = target.a22;
		this.tx = target.tx;
		this.ty = target.ty;
	}

	@Override
	public Affine2D_F64 concat( Affine2D_F64 second, @Nullable Affine2D_F64 ret ) {

		if( ret == null )
			ret = new Affine2D_F64();

		ret.a11 = second.a11 * a11 + second.a12 * a21;
		ret.a12 = second.a11 * a12 + second.a12 * a22;
		ret.a21 = second.a21 * a11 + second.a22 * a21;
		ret.a22 = second.a21 * a12 + second.a22 * a22;

		ret.tx = second.a11 * tx + second.a12 * ty + second.tx;
		ret.ty = second.a21 * tx + second.a22 * ty + second.ty;

		return ret;
	}

	@Override
	public Affine2D_F64 invert( @Nullable Affine2D_F64 inverse ) {
		if( inverse == null )
			inverse = new Affine2D_F64();

		double div = a11 * a22 - a12 * a21;

		inverse.a11 = a22 / div;
		inverse.a12 = -a12 / div;
		inverse.a21 = -a21 / div;
		inverse.a22 = a11 / div;

		inverse.tx = -( inverse.a11 * tx + inverse.a12 * ty );
		inverse.ty = -( inverse.a21 * tx + inverse.a22 * ty );

		return inverse;
	}

	@Override
	public void reset() {
		a11 = a22 = 1;
		a12 = a21 = 0;
		tx = ty = 0;
	}

	public Affine2D_F64 copy() {
		return new Affine2D_F64(a11,a12,a21,a22,tx,ty);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+String.format("[ %5.2e %5.2e %5.2e ; %5.2e %5.2e %5.2e ]",
				a11,a12,tx,a21,a22,ty);
	}
}

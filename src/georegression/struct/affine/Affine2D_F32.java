/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.affine;


/**
 * 2D affine transform for 64-bit floats.
 *
 * @author Peter Abeles
 */
public class Affine2D_F32 implements Affine<Affine2D_F32> {

	// rotational, sheer, enlarge, ... etc
	public float a11, a12, a21, a22;
	// translational components
	public float tx, ty;

	public Affine2D_F32( float a11, float a12, float a21, float a22, float tx, float ty ) {
		this.a11 = a11;
		this.a12 = a12;
		this.a21 = a21;
		this.a22 = a22;
		this.tx = tx;
		this.ty = ty;
	}

	public Affine2D_F32() {
		reset();
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Affine2D_F32 createInstance() {
		return new Affine2D_F32();
	}

	@Override
	public void set( Affine2D_F32 target ) {
		this.a11 = target.a11;
		this.a12 = target.a12;
		this.a21 = target.a21;
		this.a22 = target.a22;
		this.tx = target.tx;
		this.ty = target.ty;
	}

	@Override
	public Affine2D_F32 concat( Affine2D_F32 second, Affine2D_F32 ret ) {

		if( ret == null )
			ret = new Affine2D_F32();

		ret.a11 = second.a11 * a11 + second.a12 * a21;
		ret.a12 = second.a11 * a12 + second.a12 * a22;
		ret.a21 = second.a21 * a11 + second.a22 * a21;
		ret.a22 = second.a21 * a12 + second.a22 * a22;

		ret.tx = second.a11 * tx + second.a12 * ty + second.tx;
		ret.ty = second.a21 * tx + second.a22 * ty + second.ty;

		return ret;
	}

	@Override
	public Affine2D_F32 invert( Affine2D_F32 inverse ) {
		Affine2D_F32 inv = new Affine2D_F32();

		float div = a11 * a22 - a12 * a21;

		inv.a11 = a22 / div;
		inv.a12 = -a12 / div;
		inv.a21 = -a21 / div;
		inv.a22 = a11 / div;

		inv.tx = -( inv.a11 * tx + inv.a12 * ty );
		inv.ty = -( inv.a21 * tx + inv.a22 * ty );

		return inv;
	}

	@Override
	public void reset() {
		a11 = a22 = 1;
		a12 = a21 = 0;
		tx = ty = 0;
	}
}

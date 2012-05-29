/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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
public class Affine2D_F64 implements Affine<Affine2D_F64> {

	// rotational, sheer, enlarge, ... etc
	public double a11, a12, a21, a22;
	// translational components
	public double tx, ty;

	public Affine2D_F64( double a11, double a12, double a21, double a22, double tx, double ty ) {
		this.a11 = a11;
		this.a12 = a12;
		this.a21 = a21;
		this.a22 = a22;
		this.tx = tx;
		this.ty = ty;
	}

	public Affine2D_F64() {
		reset();
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
	public void set( Affine2D_F64 target ) {
		this.a11 = target.a11;
		this.a12 = target.a12;
		this.a21 = target.a21;
		this.a22 = target.a22;
		this.tx = target.tx;
		this.ty = target.ty;
	}

	@Override
	public Affine2D_F64 concat( Affine2D_F64 second, Affine2D_F64 ret ) {

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
	public Affine2D_F64 invert( Affine2D_F64 inverse ) {
		Affine2D_F64 inv = new Affine2D_F64();

		double div = a11 * a22 - a12 * a21;

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

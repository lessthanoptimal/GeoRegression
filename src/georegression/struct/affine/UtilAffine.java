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
 * @author Peter Abeles
 */
public class UtilAffine {

	public static Affine2D_F32 convert( Affine2D_F64 m , Affine2D_F32 ret ) {
		if( ret == null )
			ret = new Affine2D_F32();

		ret.a11 = (float)m.a11;
		ret.a12 = (float)m.a12;
		ret.a21 = (float)m.a21;
		ret.a22 = (float)m.a22;
		ret.tx = (float)m.tx;
		ret.ty = (float)m.ty;

		return ret;
	}
}

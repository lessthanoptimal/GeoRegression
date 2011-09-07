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

package georegression.transform;

import georegression.struct.affine.Affine2D_F32;
import georegression.struct.se.Se2_F32;


/**
 * Functions for converting one type of geometric transform into another type.  A conversion
 * function is only provided if a conversion can be performed with no approximation.
 *
 * @author Peter Abeles
 */
public class ConvertTransform_F32 {

	public static Affine2D_F32 convert( Se2_F32 src , Affine2D_F32 dst ) {
		if( dst == null )
			dst = new Affine2D_F32();

		dst.a11 = src.c;
		dst.a12 = -src.s;
		dst.a21 = src.s;
		dst.a22 = src.c;
		dst.tx = src.tran.x;
		dst.ty = src.tran.y;

		return dst;
	}
}

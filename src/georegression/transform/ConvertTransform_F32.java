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
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.transform;

import georegression.struct.InvertibleTransform;
import georegression.struct.affine.Affine2D_F32;
import georegression.struct.homo.Homography2D_F32;
import georegression.struct.se.Se2_F32;


/**
 * Functions for converting one type of geometric transform into another type.  A conversion
 * function is only provided if a conversion can be performed with no approximation.
 *
 * @author Peter Abeles
 */
public class ConvertTransform_F32 {

	public static <A extends InvertibleTransform, B extends InvertibleTransform>
	B convert( A src , B dst ) 
	{
		if( src == null || dst == null )
			throw new IllegalArgumentException("Both inputs must not be null");
		
		if( src instanceof Se2_F32 ) {
			if( dst instanceof Affine2D_F32 ) {
				return (B)convert((Se2_F32)src,(Affine2D_F32)dst);
			} else if( dst instanceof Homography2D_F32 ) {
				return (B)convert((Se2_F32)src,(Homography2D_F32)dst);
			} else if( dst instanceof Se2_F32 ) {
				dst.set(src);
				return dst;
			}
		} else if( src instanceof Affine2D_F32 ) {
			if( dst instanceof Homography2D_F32 ) {
				return (B)convert((Affine2D_F32)src,(Homography2D_F32)dst);
			} else if( dst instanceof Affine2D_F32 ) {
				dst.set(src);
				return dst;
			}
		} else if( src instanceof  Homography2D_F32 ) {
			if( dst instanceof Homography2D_F32 ) {
				dst.set(src);
				return dst;
			}
		}
		
		throw new IllegalArgumentException("The specified transform is not supported");
	}
	
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

	public static Homography2D_F32 convert( Se2_F32 src , Homography2D_F32 dst ) {
		if( dst == null )
			dst = new Homography2D_F32();

		dst.a11 = src.c;
		dst.a12 = -src.s;
		dst.a13 = src.tran.x;
		dst.a21 = src.s;
		dst.a22 = src.c;
		dst.a23 = src.tran.y;
		dst.a31 = 0;
		dst.a32 = 0;
		dst.a33 = 1;

		return dst;
	}

	public static Homography2D_F32 convert( Affine2D_F32 src , Homography2D_F32 dst ) {
		if( dst == null )
			dst = new Homography2D_F32();

		dst.a11 = src.a11;
		dst.a12 = src.a12;
		dst.a13 = src.tx;
		dst.a21 = src.a21;
		dst.a22 = src.a22;
		dst.a23 = src.ty;
		dst.a31 = 0;
		dst.a32 = 0;
		dst.a33 = 1;

		return dst;
	}
}

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

package georegression.transform;

import georegression.struct.InvertibleTransform;
import georegression.struct.affine.Affine2D_F64;
import georegression.struct.homo.Homography2D_F64;
import georegression.struct.se.Se2_F64;


/**
 * Functions for converting one type of geometric transform into another type.  A conversion
 * function is only provided if a conversion can be performed with no approximation.
 *
 * @author Peter Abeles
 */
public class ConvertTransform_F64 {

	public static <A extends InvertibleTransform, B extends InvertibleTransform>
	B convert( A src , B dst ) 
	{
		if( src == null || dst == null )
			throw new IllegalArgumentException("Both inputs must not be null");
		
		if( src instanceof Se2_F64 ) {
			if( dst instanceof Affine2D_F64 ) {
				return (B)convert((Se2_F64)src,(Affine2D_F64)dst);
			} else if( dst instanceof Homography2D_F64 ) {
				return (B)convert((Se2_F64)src,(Homography2D_F64)dst);
			} else if( dst instanceof Se2_F64 ) {
				dst.set(src);
				return dst;
			}
		} else if( src instanceof Affine2D_F64 ) {
			if( dst instanceof Homography2D_F64 ) {
				return (B)convert((Affine2D_F64)src,(Homography2D_F64)dst);
			} else if( dst instanceof Affine2D_F64 ) {
				dst.set(src);
				return dst;
			}
		} else if( src instanceof  Homography2D_F64 ) {
			if( dst instanceof Homography2D_F64 ) {
				dst.set(src);
				return dst;
			}
		}
		
		throw new IllegalArgumentException("The specified transform is not supported");
	}
	
	public static Affine2D_F64 convert( Se2_F64 src , Affine2D_F64 dst ) {
		if( dst == null )
			dst = new Affine2D_F64();

		dst.a11 = src.c;
		dst.a12 = -src.s;
		dst.a21 = src.s;
		dst.a22 = src.c;
		dst.tx = src.T.x;
		dst.ty = src.T.y;

		return dst;
	}

	public static Homography2D_F64 convert( Se2_F64 src , Homography2D_F64 dst ) {
		if( dst == null )
			dst = new Homography2D_F64();

		dst.a11 = src.c;
		dst.a12 = -src.s;
		dst.a13 = src.T.x;
		dst.a21 = src.s;
		dst.a22 = src.c;
		dst.a23 = src.T.y;
		dst.a31 = 0;
		dst.a32 = 0;
		dst.a33 = 1;

		return dst;
	}

	public static Homography2D_F64 convert( Affine2D_F64 src , Homography2D_F64 dst ) {
		if( dst == null )
			dst = new Homography2D_F64();

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

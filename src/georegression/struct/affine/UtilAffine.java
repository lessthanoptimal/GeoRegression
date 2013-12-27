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

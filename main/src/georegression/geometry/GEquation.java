/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

package georegression.geometry;

import georegression.struct.*;
import org.ejml.data.DMatrixRMaj;
import org.ejml.equation.Equation;

/**
 * Extends Equations with support for geometric data types
 *
 * @author Peter Abeles
 */
public class GEquation extends Equation {
	public GEquation(Object ...args) {
		super(args);
	}

	public GEquation() {
	}

	@Override
	protected void aliasGeneric( Object variable , String name ) {
		if( variable instanceof GeoTuple2D_F32) {
			GeoTuple2D_F32 P = (GeoTuple2D_F32)variable;
			DMatrixRMaj M = new DMatrixRMaj(2,1);
			M.data[0] = P.x;
			M.data[1] = P.y;
			alias(M,name);
		} else if( variable instanceof GeoTuple2D_F64 ) {
			GeoTuple2D_F64 P = (GeoTuple2D_F64)variable;
			DMatrixRMaj M = new DMatrixRMaj(2,1);
			M.data[0] = P.x;
			M.data[1] = P.y;
			alias(M,name);
		} else if( variable instanceof GeoTuple3D_F32) {
			GeoTuple3D_F32 P = (GeoTuple3D_F32)variable;
			DMatrixRMaj M = new DMatrixRMaj(3,1);
			M.data[0] = P.x;
			M.data[1] = P.y;
			M.data[2] = P.z;
			alias(M,name);
		} else if( variable instanceof GeoTuple3D_F64 ) {
			GeoTuple3D_F64 P = (GeoTuple3D_F64)variable;
			DMatrixRMaj M = new DMatrixRMaj(3,1);
			M.data[0] = P.x;
			M.data[1] = P.y;
			M.data[2] = P.z;
			alias(M,name);
		} else if( variable instanceof GeoTuple4D_F32) {
			GeoTuple4D_F32 P = (GeoTuple4D_F32)variable;
			DMatrixRMaj M = new DMatrixRMaj(4,1);
			M.data[0] = P.x;
			M.data[1] = P.y;
			M.data[2] = P.z;
			M.data[3] = P.w;
			alias(M,name);
		} else if( variable instanceof GeoTuple4D_F64 ) {
			GeoTuple4D_F64 P = (GeoTuple4D_F64)variable;
			DMatrixRMaj M = new DMatrixRMaj(4,1);
			M.data[0] = P.x;
			M.data[1] = P.y;
			M.data[2] = P.z;
			M.data[3] = P.w;
			alias(M,name);
		} else {
			super.aliasGeneric(variable,name);
		}
	}
}

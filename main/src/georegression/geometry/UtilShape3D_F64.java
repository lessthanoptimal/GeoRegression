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

package georegression.geometry;

import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.transform.se.SePointOps_F64;
import org.ddogleg.struct.DogArray;

/**
 * @author Peter Abeles
 */
public class UtilShape3D_F64 {
	/**
	 * Converts a 2D polygon into a 3D polygon. The 2D points will lie on the x-y plane (e.g. (x,y,0)) and are
	 * converted to 3D using polyToWorld.
	 *
	 * @param polygon2D (Input) polygon
	 * @param polyToWorld (Output) transform from 2D to 3D coordinate system.
	 * @param output
	 */
	public static void polygon2Dto3D(Polygon2D_F64 polygon2D , Se3_F64  polyToWorld , DogArray<Point3D_F64> output )
	{
		output.resize(polygon2D.size());

		for (int i = 0; i < polygon2D.size(); i++) {
			Point2D_F64 p2 = polygon2D.get(i);
			Point3D_F64 p3 = output.get(i);
			p3.setTo(p2.x, p2.y,0);
			SePointOps_F64.transform(polyToWorld,p3,p3);
		}
	}
}

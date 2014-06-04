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

package georegression.struct.shapes;

import georegression.struct.point.Point2D_F64;
import org.ddogleg.struct.FastQueue;

import java.io.Serializable;

/**
 * Describes a polygon in 2D.
 *
 * @author Peter Abeles
 */
public class Polygon2D_F64 implements Serializable {

	// vertexes in the polygon
	public FastQueue<Point2D_F64> vertexes;

	public Polygon2D_F64( int numVertexes ) {
		vertexes = new FastQueue<Point2D_F64>(Point2D_F64.class,true);

		vertexes.growArray(numVertexes);
		vertexes.size = numVertexes;
	}

	public Polygon2D_F64() {
		vertexes = new FastQueue<Point2D_F64>(Point2D_F64.class,true);
	}

	public int size() {
		return vertexes.size();
	}
}

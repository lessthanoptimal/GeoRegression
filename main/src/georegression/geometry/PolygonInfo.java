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

/**
 * Different classifications of polygons
 *
 * @author Peter Abeles
 */
public class PolygonInfo {
	/** What type of polygon it is. */
	public Type type = Type.UNKNOWN;
	/** true if polygon is counter-clock-wise. if complex then this parameter has no meaning */
	public boolean ccw;

	public void reset() {
		type = Type.UNKNOWN;
		ccw = false;
	}

	public enum Type {
		UNKNOWN,
		CONVEX,
		SIMPLE_CONCAVE,
		COMPLEX
	}
}

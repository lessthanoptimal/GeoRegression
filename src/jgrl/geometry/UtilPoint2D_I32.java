/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package jgrl.geometry;

import jgrl.struct.point.Point2D_I32;

/**
 *
 *
 */
public class UtilPoint2D_I32 {

	public static double distance( Point2D_I32 a , Point2D_I32 b ) {
		int dx = b.x - a.x;
		int dy = b.y - a.y;

		return Math.sqrt( dx * dx + dy * dy );
	}

	public static double distance( int x0, int y0, int x1, int y1 ) {
		int dx = x1 - x0;
		int dy = y1 - y0;

		return Math.sqrt( dx * dx + dy * dy );
	}
}

/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

package georegression.geometry.algs;

import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.sorting.QuickSortComparator;
import org.ddogleg.struct.FastArray;

/**
 * Computes the convex hull of a set of points using Andrew's monotone chain algorithm.  O(n log n) for sort and
 * O(N) for convex hull computation, where N is number of input points.
 *
 * @author Peter Abeles
 */
public class AndrewMonotoneConvexHull_F64 {
	// Use this sorting routine to avoid declaring memory each time its called
	QuickSortComparator<Point2D_F64> sorter;

	FastArray<Point2D_F64> work = new FastArray<>(Point2D_F64.class);

	public AndrewMonotoneConvexHull_F64() {

		// Sort the points based on their x value.  If the same then use y value
		sorter = new QuickSortComparator<>((a, b) -> {
			if( a.x < b.x )
				return -1;
			else if( a.x > b.x )
				return 1;
			else if( a.y < b.y )
				return -1;
			else if( a.y > b.y )
				return 1;
			return 0;
		});
	}

	/**
	 * Computes the convex hull.  The output will be in counter-clockwise order.
	 *
	 * @param input List of input points.  The list will be modified by sorting
	 * @param length Number of valid elements in list
	 * @param hull (Output) Where the complex hull is written to
	 */
	public void process( Point2D_F64[] input , int length , Polygon2D_F64 hull )
	{
		// ahdnle special cases
		if( length == 2 ) {
			hull.vertexes.resize(length);
			for (int i = 0; i < length; i++) {
				hull.get(i).set(input[i]);
			}
			return;
		}

		sorter.sort(input,length);
		work.reset();

		// construct the lower hull
		for (int i = 0; i < length; i++) {
			Point2D_F64 p = input[i];
			//Contains at least 2 points and the last two points and 'p' do not make a counter-clockwise turn
			while( work.size() >= 2 && subtractThenCross(p,work.getTail(0),work.getTail(1)) >= 0) {
				// remove the last points from the hull
				work.removeTail();
			}
			// append p to the end
			work.add(p);
		}

		work.removeTail();
		int minSize = work.size+2;

		// construct upper hull
		for(int i = length-1 ; i >= 0 ; i --)                // Finding top layer from hull
		{
			//Contains at least 2 points and the last two points and 'p' do not make a counter-clockwise turn
			Point2D_F64 p = input[i];
			while( work.size() >= minSize && subtractThenCross(p,work.getTail(0),work.getTail(1)) >= 0 ) {
				work.removeTail();
			}
			// append p to the end
			work.add(p);
		}
		work.removeTail();

		// create a copy for the output
		// the work buffer contains references to the input points, but to be safe the output should have its
		// own instances
		hull.vertexes.resize(work.size);
		for (int i = 0; i < work.size(); i++) {
			hull.vertexes.data[i].set(work.get(i));
		}
	}

	/**
	 * Performs the following operation: output = z-component[ (a-b) cross (a-c) ]
	 */
	private static double subtractThenCross( Point2D_F64 a , Point2D_F64 b , Point2D_F64 c ) {
		double x0 = b.x - a.x;
		double y0 = b.y - a.y;

		double x1 = c.x - a.x;
		double y1 = c.y - a.y;

		return x0 * y1 - y0 * x1;
	}

}

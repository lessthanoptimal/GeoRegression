/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.polygon;

import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.sorting.QuickSortComparator;
import org.ddogleg.struct.FastAccess;
import org.ddogleg.struct.FastArray;

/**
 * Computes the convex hull of a set of points using Andrew's monotone chain algorithm. O(n log n) for sort and
 * O(N) for convex hull computation, where N is number of input points.
 *
 * @author Peter Abeles
 */
public class ConvexHullAndrewMonotone_F64 implements FitConvexHull_F64{
	// Use this sorting routine to avoid declaring memory each time its called
	QuickSortComparator<Point2D_F64> sorter;

	FastArray<Point2D_F64> stack = new FastArray<>(Point2D_F64.class);

	public ConvexHullAndrewMonotone_F64() {
		// Sort the points based on their x value. If the same then use y value
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
	 * Computes the convex hull. The output will be in counter-clockwise order.
	 *
	 * @param points List of input points. The list will be modified by sorting
	 * @param output (Output) Where the complex hull is written to.
	 */
	@Override
	public void process(FastAccess<Point2D_F64> points, Polygon2D_F64 output )
	{
		output.vertexes.reset();

		// Handle special cases
		if( points.size <= 2 ) {
			output.vertexes.resize(points.size);
			for (int i = 0; i < points.size; i++) {
				output.get(i).setTo(points.data[i]);
			}
			return;
		}

		final int length = points.size;
		sorter.sort(points.data, length);
		stack.reset();

		// construct the lower hull
		for (int i = 0; i < length; i++) {
			Point2D_F64 p = points.data[i];
			// Contains at least 2 points and the last two points and 'p' do not make a counter-clockwise turn
			while( stack.size() >= 2 && subtractThenCross(p, stack.getTail(), stack.getTail(1)) >= 0) {
				// remove the last points from the hull
				stack.removeTail();
			}
			// append p to the end
			stack.add(p);
		}

		stack.removeTail();
		int minSize = stack.size+2;

		// construct upper hull
		for(int i = length-1 ; i >= 0 ; i --) { // Finding top layer from hull
			// Contains at least 2 points and the last two points and 'p' do not make a counter-clockwise turn
			Point2D_F64 p = points.data[i];
			while( stack.size() >= minSize && subtractThenCross(p, stack.getTail(), stack.getTail(1)) >= 0 ) {
				stack.removeTail();
			}
			// append p to the end
			stack.add(p);
		}
		stack.removeTail();

		// Copy the stack into the output polygon
		output.vertexes.resize(stack.size());
		for (int i = 0; i < stack.size; i++) {
			output.vertexes.get(i).setTo(stack.get(i));
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

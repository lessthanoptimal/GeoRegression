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

package georegression.fitting.polygon;

import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.sorting.QuickSortComparator;
import org.ddogleg.struct.FastAccess;
import org.ddogleg.struct.FastArray;

import java.util.Comparator;

/**
 * Finds the convex hull using Graham Scan. Runtime complexity of O(n log n).
 *
 * @author Peter Abeles
 */
public class ConvexHullGrahamScan_F64 implements FitConvexHull_F64 {

	// Point that all the other points are sorted based on their relative angle to it
	Point2D_F64 pivot = new Point2D_F64();

	// use a linked list to store the points because O(1) cost to removing an element
	final FastArray<Point2D_F64> stack = new FastArray<>(Point2D_F64.class);

	// Sorts the array. Java's sort method is not used to avoid memory creation/destruction
	final CompareAngle compareAngle = new CompareAngle();
	final QuickSortComparator<Point2D_F64> sorter = new QuickSortComparator<>(compareAngle);

	/**
	 * Fits a convex hull to the provided set of points. The list is modified by changing the order of points inside
	 * of it. If the input is a degenerate case where there is no clear solution it will do the following: A single
	 * point will return a single point. If all points lie along a line (2 or more points) then a polygon that's
	 * composed of two points will be returned.
	 *
	 * @param points (Input, Output) Point that the convex hull is fit to. This list will be re-ordered.
	 * @param output (Output) The found convex hull.
	 */
	@Override
	public void process(FastAccess<Point2D_F64> points, Polygon2D_F64 output) {
		output.vertexes.reset();

		if (points.isEmpty())
			return;
		stack.clear();

		int indexLowestX = findLowestX(points);
		pivot = points.get(indexLowestX);

		// Sort the points according to Graham Scan's approach below
		sorter.sort(points.data,points.size);
		if (points.get(0) != pivot)
			throw new RuntimeException("BUG!");

		for (int i = 0; i < points.size; i++) {
			// Pop the last points from the stack until we turn counter-clockwise
			while (stack.size >= 2 && isCW(stack.getTail(1), stack.getTail(), points.get(i)) >= 0) {
				stack.removeTail();
			}
			stack.add(points.get(i));
		}

		// Copy the stack into the output polygon
		output.vertexes.resize(stack.size());
		for (int i = 0; i < stack.size; i++) {
			output.vertexes.get(i).setTo(stack.get(i));
		}
	}

	/**
	 * Finds the point with the lowest x-axis value. If two have the same value then the one with the lowest y value
	 * breaks the dead lock
	 */
	private int findLowestX(FastAccess<Point2D_F64> points) {
		int selectedIndex = 0;
		Point2D_F64 pivot = points.get(selectedIndex);
		for (int i = 1; i < points.size(); i++) {
			Point2D_F64 p = points.get(i);
			if (p.x <= pivot.x) {
				if (p.x == pivot.x) {
					if (p.y < pivot.y) {
						pivot = p;
						selectedIndex = i;
					}
				} else {
					pivot = p;
					selectedIndex = i;
				}
			}
		}
		return selectedIndex;
	}

	/**
	 * Returns 1 if (a-pivot) is cw of (b-pivot).
	 */
	static int isCW(Point2D_F64 pivot, Point2D_F64 a, Point2D_F64 b) {
		double dxa = a.x - pivot.x;
		double dya = a.y - pivot.y;

		double dxb = b.x - pivot.x;
		double dyb = b.y - pivot.y;

		double cross = dxa*dyb - dya*dxb;

		return Double.compare(0.0, cross);
	}

	/**
	 * Compares two points. If a is counter-clockwise of b then it return 1. If they have the same angle then return -1
	 * if 'a' is closer to the pivot than 'b'.
	 */
	class CompareAngle implements Comparator<Point2D_F64> {
		@Override
		public int compare(Point2D_F64 a, Point2D_F64 b) {
			double dxa = a.x - pivot.x;
			double dya = a.y - pivot.y;

			double dxb = b.x - pivot.x;
			double dyb = b.y - pivot.y;

			double cross = dxa*dyb - dya*dxb;

			if (cross < 0.0)
				return 1;
			else if (cross > 0.0)
				return -1;
			else {
				double ra = dxa*dxa + dya*dya;
				double rb = dxb*dxb + dyb*dyb;

				return Double.compare(ra, rb);
			}
		}
	}
}

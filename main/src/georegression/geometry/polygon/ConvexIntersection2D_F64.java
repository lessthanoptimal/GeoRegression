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

package georegression.geometry.polygon;

import georegression.geometry.UtilLine2D_F64;
import georegression.geometry.UtilPolygons2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.KBoolean;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.line.LineSegmentIntersection2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import georegression.struct.shapes.Polygon2D_F64;

/**
 * Finds the convex polygon which describes the intersection of two convex polygons. Heavily inspired from [1].
 *
 * <p>[1] Joseph O'Rourke, Computational Geometry in C. 2nd Ed. Chapter 1</p>
 *
 * @author Peter Abeles
 */
public class ConvexIntersection2D_F64 {

	double tol = 0.0;

	// Used to flip signed tests of polyA and B is clockwise. 1 == ccw. -1 = cw
	double ccwA, ccwB;

	Point2D_F64 origin = new Point2D_F64(0, 0);

	Vector2D_F64 vectorA = new Vector2D_F64();
	Vector2D_F64 vectorB = new Vector2D_F64();

	// first point
	Point2D_F64 p0 = new Point2D_F64();

	LineSegmentIntersection2D_F64 intersection = new LineSegmentIntersection2D_F64();

	LineSegment2D_F64 segmentA = new LineSegment2D_F64();
	LineSegment2D_F64 segmentB = new LineSegment2D_F64();

	public boolean process(Polygon2D_F64 polyA, Polygon2D_F64 polyB, Polygon2D_F64 output) {
		output.vertexes.reset();

		// Figure out the ordering of the two polygons
		ccwA = UtilPolygons2D_F64.isCCW(polyA) ? 1 : -1;
		ccwB = UtilPolygons2D_F64.isCCW(polyB) ? 1 : -1;

		KBoolean inflag = KBoolean.UNKNOWN; // Pin = true. Qin = false
		boolean firstPoint = true;

		int idxA = 0, idxB = 0;
		int a = 0, b = 0;

		do {
			int a1 = (idxA + polyA.size() - 1) % polyA.size();
			int b1 = (idxB + polyB.size() - 1) % polyB.size();

			vectorA.minus(polyA.get(a), polyA.get(a1));
			vectorB.minus(polyB.get(b), polyB.get(b1));

			int cross = UtilLine2D_F64.areaSign(origin, vectorA, vectorB, tol);
			int aHB = UtilLine2D_F64.areaSign(polyB.get(b1), polyB.get(b), polyA.get(a), tol);
			int bHA = UtilLine2D_F64.areaSign(polyA.get(a1), polyA.get(a), polyB.get(b), tol);

			segmentA.setTo(polyA.get(a1), polyA.get(a));
			segmentB.setTo(polyB.get(b1), polyB.get(b));

			// See if A and B intersect
			int code = Intersection2D_F64.intersection(segmentA, segmentB, intersection);
			if (code == 1) {
				if (inflag == KBoolean.UNKNOWN && firstPoint) {
					firstPoint = false;
					idxA = 0;
					idxB = 0;
					p0.setTo(intersection.pa);
					output.vertexes.grow().setTo(p0);
				}
				if (aHB > 0) {
					inflag = KBoolean.TRUE;
				} else if (bHA > 0) {
					inflag = KBoolean.FALSE;
				}
			}
			if (code == 2 && vectorA.dot(vectorB) < 0) {
				// A & B overlap and oppositely oriented
				output.vertexes.grow().setTo(intersection.pa);
				output.vertexes.grow().setTo(intersection.pb);
				// TODO make sure this is unit tested
				// TODO should it return here?
			}
			if (cross == 0 && (aHB < 0 && bHA < 0)) {
				// A & B parallel and separated
				// TODO should it return here?
			} else if (cross == 0 && (aHB == 0 && bHA == 0)) {
				// A&B are colinear
				// Advance but don't add to output polygon
				if (inflag == KBoolean.TRUE) {
					b = (b + 1) % polyB.size();
					idxB++;
				} else {
					a = (a + 1) % polyA.size();
					idxA++;
				}
			} else if (cross >= 0) {
				Point2D_F64 next;
				if (bHA > 0) {
					next = inflag == KBoolean.TRUE ? polyA.get(a) : null;
					a = (a + 1) % polyA.size();
					idxA++;
				} else {
					next = inflag == KBoolean.FALSE ? polyB.get(b) : null;
					b = (b + 1) % polyB.size();
					idxB++;
				}
				if (next != null)
					output.vertexes.grow().setTo(next);
			} else {
				Point2D_F64 next;
				if (aHB > 0) {
					next = inflag == KBoolean.FALSE ? polyB.get(b) : null;
					b = (b + 1) % polyB.size();
					idxB++;
				} else {
					next = inflag == KBoolean.TRUE ? polyA.get(a) : null;
					a = (a + 1) % polyA.size();
					idxA++;
				}
				if (next != null)
					output.vertexes.grow().setTo(next);
			}

		} while ((idxA < polyA.size() || idxB < polyB.size()) && (idxA < 2 * polyA.size() && idxB < 2 * polyB.size()));

		if (!firstPoint) {
			// TODO abla
			return true;
		}

		return inflag != KBoolean.UNKNOWN;
	}
}

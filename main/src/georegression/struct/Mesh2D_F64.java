/*
 * Copyright (C) 2025, Peter Abeles. All Rights Reserved.
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

package georegression.struct;

import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.packed.PackedArrayPoint2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Triangle2D_F64;
import org.ddogleg.struct.DogArray_I32;
import org.ejml.UtilEjml;

/// Specifies a mesh in 2D. List of vertexes (points) and triangles. Triangles are sets of 3 integers that
/// specify the 3 vertexes that are its corners.
public class Mesh2D_F64 {
	/// List of all the points, which are the mesh's vertexes
	public PackedArrayPoint2D_F64 points = new PackedArrayPoint2D_F64();

	/// List of point indexes that specifies the triangles. 3 points per triangle
	public DogArray_I32 triangles = new DogArray_I32();

	/// Resets data structure into its original state
	public void reset() {
		points.reset();
		triangles.reset();
	}

	/// Number of triangles in the mesh
	public int triangleCount() {
		return triangles.size()/3;
	}

	/// Number of points in the mesh
	public int pointCount() {
		return points.size();
	}

	/// Removes all the unused points that are not referenced by any triangle and updates triangle point references
	public Mesh2D_F64 stripUnusedPoints() {
		var oldToNew = new int[points.size()];

		// Identify points that have been used by setting them to 1
		for (int i = 0; i < triangles.size; i++) {
			oldToNew[triangles.get(i)] = 1;
		}

		// Assign new index values to the points
		int count = 0;
		for (int i = 0; i < oldToNew.length; i++) {
			if (oldToNew[i] == 1) {
				oldToNew[i] = count++;
			} else {
				oldToNew[i] = -1;
			}
		}

		// Remap triangle references to points
		for (int i = 0; i < triangles.size; i++) {
			triangles.set(i, oldToNew[triangles.get(i)]);
		}

		// Create a new set of points from only the points that are needed
		var tmp = new PackedArrayPoint2D_F64();
		tmp.reserve(count);
		for (int i = 0; i < oldToNew.length; i++) {
			if (oldToNew[i] == -1)
				continue;
			tmp.append(points.getTemp(i));
		}
		points = tmp;
		return this;
	}

	/// Adds a new triangle by specifying which parts are its corners
	///
	/// @return Location of triangle in the array. This also acts as the triangle's unique ID.
	public int addTriangle( int v0, int v1, int v2 ) {
		triangles.add(v0);
		triangles.add(v1);
		triangles.add(v2);
		return triangles.size() - 3;
	}

	public void getTriangle( int id, Triangle2D_F64 shape ) {
		points.getCopy(triangles.get(id), shape.v0);
		points.getCopy(triangles.get(id + 1), shape.v1);
		points.getCopy(triangles.get(id + 2), shape.v2);
	}

	/// Exhaustively checks every edge in the mesh to see if the edges intersect. Only strict intersections
	/// are considered. End points touching don't count
	public boolean doEdgesIntersect() {
		var results = new Results();
		results.found = false;

		forLines(( triA, sideA, p0, p1, lineA ) -> {
			forLines(triA + 1, triangleCount(), ( triB, sideB, p2, p3, lineB ) -> {
				if (Intersection2D_F64.intersectsEx(lineA, lineB, UtilEjml.TEST_F64)) {
					// identical edges will be found in multiple triangles
					if ((p0 == p2 && p1 == p3) || (p0 == p3 && p1 == p2))
						return true;

					results.found = true;
					return false;
				}
				return true;
			});
			return !results.found;
		});

		return results.found;
	}

	/// Verifies the mesh is meets one property of a Delaunay triangulation by ensuring that only the points which
	/// are point of a triangle can be inside the triangle's circumcircle. Triangles must be in CCW order.
	///
	/// @param tol Tolerance for a point being exactly on the circumcircle
	/// @return true if every point passes, false if at least one fails
	public boolean checkCircumcircleCCW( double tol ) {
		var triangle = new Triangle2D_F64();
		for (int triangleID = 0; triangleID < triangles.size; triangleID += 3) {
			getTriangle(triangleID, triangle);

			for (int i = 0; i < points.size(); i++) {
				Point2D_F64 p = points.getTemp(i);

				// Points exactly on the circumcircle will be ignored. If you have a regular grid it is degenerate
				// since there are multiple valid solutions.
				if (Intersection2D_F64.insideCircumcircleCCW(triangle, p.x, p.y, tol) <= 0)
					continue;

				return false;
			}
		}
		return true;
	}

	/// Counts how many points are included in at least one triangle
	public int countPointsInTriangles() {
		var found = new boolean[points.size()];
		for (int i = 0; i < triangles.size; i++) {
			found[triangles.get(i)] = true;
		}

		int total = 0;
		for (int i = 0; i < found.length; i++) {
			if (found[i])
				total++;
		}

		return total;
	}

	/// Process all line segments.
	public void forLines( LineOp op ) {
		forLines(0, triangleCount(), op);
	}

	/// Process all line segments for only the specified triangles.
	///
	/// @param idx0 First triangle to process, inclusive
	/// @param idx1 Upper limit, exclusive
	/// @param op The operator
	public void forLines( int idx0, int idx1, LineOp op ) {
		var line = new LineSegment2D_F64();

		escape:
		for (int idxTri = idx0*3; idxTri < idx1*3; idxTri += 3) {
			for (int i = 0, j = 2; i < 3; j = i, i++) {
				int p0 = triangles.get(idxTri + j);
				int p1 = triangles.get(idxTri + i);

				points.getCopy(p0, line.a);
				points.getCopy(p1, line.b);

				if (!op.op(idxTri, j, p0, p1, line))
					break escape;
			}
		}
	}

	private static class Results {
		boolean found;
	}

	@FunctionalInterface public interface LineOp {
		/// line segment for the given triangle and side on the triangle
		///
		/// @return true if it should continue processing or false if its done
		boolean op( int triangleID, int side, int p0, int p1, LineSegment2D_F64 line );
	}
}

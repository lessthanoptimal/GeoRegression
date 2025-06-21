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

package georegression.delaunay;

import georegression.geometry.UtilPolygons2D_F64;
import georegression.struct.TriIndex;
import georegression.struct.packed.PackedArrayPoint2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Triangle2D_F64;
import lombok.Getter;
import org.ddogleg.struct.DogArray;
import org.ddogleg.struct.DogArray_I32;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Triangles and graph structures used to build a Delaunay triangulation using incremental method. Whenever possible
 * arrays are used instead of structures for memory efficiency. All triangles have their corners specified in
 * a counter-clockwise direction.
 */
public class PackedDelaunayGraph {
	/// Points provided by the user plus fake points far away
	public final PackedArrayPoint2D_F64 points = new PackedArrayPoint2D_F64();

	/// triangles specified by 3 corners, which reference the point IDs. Full of -1 if no longer in use
	/// [0 corner 0][0corner 1][ 0 corner 2][1corner 0][1corner 1] ...
	public final DogArray_I32 triangles = new DogArray_I32();

	/// Offsets / IDs of triangles that are not being used anymore
	public final DogArray_I32 unusedTriangles = new DogArray_I32();

	/// Graph which relates points to triangles
	public final DogArray<Vertex> vertexes = new DogArray<>(Vertex::new, Vertex::reset);

	/// ID of the most recently added triangle
	@Getter int mostRecentlyAddedTriangle;

	/// Puts it back into its original state
	public void reset() {
		points.reset();
		triangles.reset();
		unusedTriangles.reset();
		vertexes.reset();
		mostRecentlyAddedTriangle = -1;
	}

	/// Allocate enough space for X number of points. While option, it's a good idea to call this function after
	/// reset()
	public void reservePoints( int size ) {
		points.reserve(size);
		vertexes.reserve(size);

		// also reserve space for triangles. There typically are more triangles than points.
		triangles.reserve(2*size);
	}

	/// Total number of valid triangles it has stored
	public int triangleCount() {
		return triangles.size()/3 - unusedTriangles.size();
	}

	/// Adds a point and creates a new vertex for the point
	public void addPoint( double x, double y ) {
		points.append(x, y);
		vertexes.grow();
	}

	/// Makes sure none of the triangles are clockwise
	///
	/// @return true if it passes the test
	public boolean verifyTrianglesAreCCW() {
		var t = new Triangle2D_F64();
		for (int triangleID = 0; triangleID < triangles.size; triangleID += 3) {
			if (triangles.get(triangleID) < 0)
				continue;

			points.getCopy(triangles.get(triangleID), t.v0);
			points.getCopy(triangles.get(triangleID + 1), t.v1);
			points.getCopy(triangles.get(triangleID + 2), t.v2);

			if (!UtilPolygons2D_F64.isCCW(t))
				return false;
		}
		return true;
	}

	/**
	 * Adds a new triangle to the triangle list
	 *
	 * @param a Index of point A
	 * @param b Index of point B
	 * @param c Index of point C
	 * @return reference to the new triangle
	 */
	public int addTriangle( int a, int b, int c ) {
		// Recycle memory if possible
		int triangleID;
		if (unusedTriangles.isEmpty()) {
			// Add triangle to end of the array
			triangleID = triangles.size;

			// allocate space for the triangle
			triangles.add(-1);
			triangles.add(-1);
			triangles.add(-1);

			// add() instead of resize() so that it will attempt to preallocate memory
//			triangles.resize(triangles.size + 3);
		} else {
			// pop the next free space in the queue
			triangleID = unusedTriangles.removeSwap(0);
		}

		// Connect the triangle to its vertexes
		vertexes.get(a).connect(triangleID, 0);
		vertexes.get(b).connect(triangleID, 1);
		vertexes.get(c).connect(triangleID, 2);

		// Add the triangle to the list of triangles
		triangles.set(triangleID, a);
		triangles.set(triangleID + 1, b);
		triangles.set(triangleID + 2, c);

		mostRecentlyAddedTriangle = triangleID;

		return triangleID;
	}

	/// Removes the triangle and all references to it
	public void removeTriangle( int reference ) {
		// Disconnect the triangle from all the vertexes it's attached to
		int pointA = triangles.get(reference);
		int pointB = triangles.get(reference + 1);
		int pointC = triangles.get(reference + 2);

		vertexes.get(pointA).disconnect(reference);
		vertexes.get(pointB).disconnect(reference);
		vertexes.get(pointC).disconnect(reference);

		// Mark this space as unused so it can be recycled later on
		unusedTriangles.add(reference);

		// Fill the triangle with invalid values so that it will blow up if there's a bug and it's used
		triangles.set(reference, -1);
		triangles.set(reference + 1, -1);
		triangles.set(reference + 2, -1);
	}

	/// Gets indexes of all the corners in a triangle
	///
	/// @param reference Reference to the triangle
	/// @param triangle Storage for triangle corner's point indexes
	public TriIndex getTriangle( int reference, @Nullable TriIndex triangle ) {
		if (triangle == null)
			triangle = new TriIndex();
		triangle.a = triangles.data[reference];
		triangle.b = triangles.data[reference + 1];
		triangle.c = triangles.data[reference + 2];

		return triangle;
	}

	/// Searches for a triangle with these exact vertexes in this order. Returns the triangle's ID or -1
	/// if there are no matches
	public int findTriangle( int v0, int v1, int v2 ) {
		for (int i = 0; i < triangles.size; i += 3) {
			if (v0 != triangles.data[i])
				continue;
			if (v1 != triangles.data[i + 1])
				continue;
			if (v2 != triangles.data[i + 2])
				continue;

			return i;
		}
		return -1;
	}

	/// Loads location of all the corners in a triangle
	public Triangle2D_F64 getCorners( TriIndex corners, @Nullable Triangle2D_F64 triangle ) {
		if (triangle == null)
			triangle = new Triangle2D_F64();

		points.getCopy(corners.a, triangle.v0);
		points.getCopy(corners.b, triangle.v1);
		points.getCopy(corners.c, triangle.v2);

		return triangle;
	}

	/// Loads location of all the corners in a triangle
	public Triangle2D_F64 getCorners( int reference, @Nullable Triangle2D_F64 triangle ) {
		if (triangle == null)
			triangle = new Triangle2D_F64();

		int pointA = triangles.get(reference);
		int pointB = triangles.get(reference + 1);
		int pointC = triangles.get(reference + 2);

		points.getCopy(pointA, triangle.v0);
		points.getCopy(pointB, triangle.v1);
		points.getCopy(pointC, triangle.v2);

		return triangle;
	}

	/// Gets location of a triangle's corner
	public void getTriangleCorner( int reference, int corner, Point2D_F64 point ) {
		points.getCopy(cornerPoint(reference, corner), point);
	}

	/// Returns the point ID of the specified corner on the specified triangle.
	/// 0 = side(a,b), 1 = side(b,c), 2 = side(c,a)
	///
	/// @param reference Which triangle
	/// @param corner Which corner on the triangle. 0 to 2
	public int cornerPoint( int reference, int corner ) {
		if (corner < 0 || corner > 2)
			throw new IllegalArgumentException("Corner is out of bounds. " + corner);
		return triangles.get(reference + corner);
	}

	/// Same as [#cornerPoint(int, int)], but it will apply modulus to the corner to ensure
	/// its always in bounds. Useful when you are referencing a corner relative to another.
	public int cornerPointW( int reference, int corner ) {
		return triangles.get(reference + (corner%3));
	}

	/// Graph vertex that relates point to the triangles they are connected to
	/// Edges in the graph reference a triangle and a specific corner on said triangle
	public static class Vertex {
		/// List of edges. Interleaved list of \[triangle ref, corner\]
		/// Corner references the corner index. 0 to 2
		public final DogArray_I32 edges = new DogArray_I32();

		/// Resets vertex into its original state
		public final void reset() {
			edges.reset();
		}

		/// Number of edges that connect to this vertex
		public final int size() {
			return edges.size/2;
		}

		/// Get ID of the triangle
		public final int getTriangle( int i ) {
			return edges.data[i*2];
		}

		/// Get which corner on triangle that this references. 0 to 2.
		public final int getCorner( int i ) {
			return edges.data[i*2 + 1];
		}

		/// Connects the specified triangle using the specified corner on the triangle
		///
		/// @param corner Corner on triangle from 0 to 2
		public final void connect( int triangleID, int corner ) {
			edges.add(triangleID);
			edges.add(corner);
		}

		/// Disconnects the triangle from this edge. Throws an exception if the
		/// triangle is not connected.
		public void disconnect( int triangleID ) {
			int idx = findTriangle(triangleID);
			if (idx < 0)
				throw new RuntimeException("BUG! Triangle not found");

			if (edges.size > 2) {
				// copy the tail into the connection we are removing
				edges.data[idx] = edges.data[edges.size - 2];
				edges.data[idx + 1] = edges.data[edges.size - 1];
			}
			edges.size -= 2;
		}

		/// Edges for triangle in edge list. Returns location or -1 if not found.
		public int findTriangle( int triangleID ) {
			for (int i = 0; i < edges.size; i += 2) {
				if (edges.data[i] == triangleID) {
					return i;
				}
			}
			return -1;
		}

		/// Retrieves a set of all triangles that reference this vertex
		public void getAllTriangles( Set<Integer> found ) {
			for (int i = 0; i < edges.size; i += 2) {
				found.add(edges.data[i]);
			}
		}
	}
}

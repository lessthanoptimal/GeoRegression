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
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.struct.DogArray;
import org.ddogleg.struct.DogLinkedList;
import org.ddogleg.struct.DogLinkedList.Element;
import org.ejml.UtilEjml;

/**
 * Triangulates a simple polygon by removing ears. Triangulation breaks a polygon up into triangle components.
 * There are many different ways to triangulate polygons and in most situations there are multiple solutions.
 * The approach employed here is described in [1] and works by selecting "ears" in the polygon and breaking
 * those off into triangles. The overall complexity is O(N^2).
 *
 * If the orientation of the polygon is known then the step where it determines CW or CCW can be skipped by
 * setting {@link #knownOrder} to true.
 *
 * <p>[1] Joseph O'Rourke, Computational Geometry in C. 2nd Ed. Chapter 1</p>
 *
 * @author Peter Abeles
 */
@SuppressWarnings("ConstantConditions")
public class TriangulateSimpleRemoveEars_F64 {

	/** Tolerance for line segments being colinear */
	public double tol = UtilEjml.TEST_F64;

	/** If true the value of ccw will be assumed to be the polygon's order. Saving some computations */
	public boolean knownOrder = false;
	/** If the polygon is in ccw order */
	public boolean ccw = true;

	// Use a linked list to store the polygon since it's easier to add and remove elements
	final protected CyclicalLinkedList<Vertex> polygon = new CyclicalLinkedList<>();

	// Storage for vertexes so that memory can be recycled
	final protected DogArray<Vertex> vertexes = new DogArray<>(Vertex::new, Vertex::reset);

	/**
	 * Converts the polygon into a set of triangles.
	 *
	 * @param input (Input) Input polygon
	 * @param output (Output) Storage for triangulation results. Reset is called.
	 */
	public void process(Polygon2D_F64 input, DogArray<ThreeIndexes> output) {
		output.reset();
		if (input.size() < 3) {
			// Too few points to create one triangle
			return;
		} else if (input.size() == 3) {
			// it's already a triangle
			output.grow().set(0,1,2);
			return;
		}

		// If not known set the polygon's order
		if (!knownOrder)
			ccw = UtilPolygons2D_F64.isCCW(input);

		// Convert into a format that's easier to work with, i.e. fast remove
		convert(input,polygon);

		// Precompute all the ears
		findEars();

		// Triangulate by breaking off ears
		while (polygon.size() > 3) {
			// Search until it finds an "ear"
			Element<Vertex> v2 = polygon.getHead();
			do {
				if (!v2.object.ear) {
					v2 = v2.next;
					continue;
				}

				Element<Vertex> v3 = v2.next, v4 = v2.next.next;
				Element<Vertex> v1 = v2.prev, v0 = v2.prev.prev;

				// Copy this ear into the output triagonalization
				ThreeIndexes tri = output.grow();
				tri.idx0 = v1.object.index;
				tri.idx1 = v2.object.index;
				tri.idx2 = v3.object.index;

				// Remove v2 from the polygon
				polygon.remove(v2);

				// Update diagonal endpoints
				v1.object.ear = isDiagonal(v0, v3);
				v3.object.ear = isDiagonal(v1, v4);
				break;
			} while (v2 != polygon.getHead());
		}

		// add the last triangle
		Element<Vertex> e = polygon.getHead();
		ThreeIndexes tri = output.grow();
		tri.idx0 = e.object.index;
		e = e.next;
		tri.idx1 = e.object.index;
		e = e.next;
		tri.idx2 = e.object.index;
	}

	/**
	 * Checks every vertex to see if it is an ear.
	 */
	void findEars() {
		Element<Vertex> e = polygon.getHead();
		do {
			e.object.ear = isDiagonal(e.prev,e.next);
			e = e.next;
		} while (e != polygon.getHead());
	}

	/** Returns true if the vector 'ab' are inside the polygon and no other edge intersects them. */
	boolean isDiagonal( Element<Vertex> a, Element<Vertex> b ) {
		return isInCone(a,b) && isInCone(b,a) && isDiagonalie(a,b);
	}

	/**
	 * Searches all edges, excluding the ones which include 'a' or 'b', and sees if they intersect with
	 * the edge 'ab'.
	 *
	 * @return true if nothing intersects the vector 'ab'
	 */
	boolean isDiagonalie( Element<Vertex> a, Element<Vertex> b ) {
		Element<Vertex> c = polygon.getHead();

		do {
			Element<Vertex> c1 = c.next;

			// skip edges incident to 'a' or 'b'
			if ( (c!=a && c1 != a) && (c!=b && c1!=b) &&
					Intersection2D_F64.intersects(a.object, b.object, c.object, c1.object,tol))
				return false;

			c = c.next;
		} while( c != polygon.getHead());

		return true;
	}

	/**
	 * Checks to see if the vector defined by 'a' and 'b' lies between the cone defined using the vectors
	 * before 'a' and after 'b'.
	 *
	 * @return if the vector 'ab' is inside the cone
	 */
	boolean isInCone( Element<Vertex> a, Element<Vertex> b ) {
		Element<Vertex> a1 = a.next;
		Element<Vertex> a0 = a.prev;

		// Check if 'a' is a convex vertex
		if (isLeftOn(a.object, a1.object, a0.object) ) {
			return isLeft(a.object, b.object, a0.object) && isLeft(b.object, a.object, a1.object);
		}

		// Otherwise it is a reflex vertex
		return !(isLeftOn(a.object, b.object, a1.object) && isLeftOn(b.object, a.object, a0.object));
	}

	/** Checks to see if vector 'bc' is > 'ab' */
	boolean isLeft( Vertex a, Vertex b, Vertex c ) {
		if (ccw)
			return UtilLine2D_F64.area2(a,b,c) > 0.0;
		else
			return UtilLine2D_F64.area2(a,b,c) < 0.0;
	}

	/** Checks to see if vector 'bc' is &ge; 'ab' */
	boolean isLeftOn( Vertex a, Vertex b, Vertex c ) {
		if (ccw)
			return UtilLine2D_F64.area2(a,b,c) >= 0.0;
		else
			return UtilLine2D_F64.area2(a,b,c) <= 0.0;
	}

	/** Convert the polygon into the internal format for ese of processing */
	void convert(Polygon2D_F64 input, DogLinkedList<Vertex> output) {
		output.reset();
		// Predeclare memory
		vertexes.resize(input.size());
		vertexes.reset();

		for (int i = 0; i < input.size(); i++) {
			Point2D_F64 p = input.get(i);

			Vertex v = vertexes.grow();
			v.index = i;
			v.setTo(p.x,p.y);
			polygon.pushTail(v);
		}
	}

	/** Specifies a point on the polygon and extra information used in triangulation */
	static class Vertex extends Point2D_F64 {
		// index of this vertex in the original input vector
		public int index;
		// true if this point is an ear. A point is an ear if the previous and next point are diagonal
		public boolean ear;

		public void reset() {
			index = -1;
			x = y = Double.NaN;
			ear = false;
		}

		@Override
		public String toString() {
			return "Vertex{" +
					"index=" + index +
					", ear=" + ear +
					", x=" + x +
					", y=" + y +
					'}';
		}
	}
}

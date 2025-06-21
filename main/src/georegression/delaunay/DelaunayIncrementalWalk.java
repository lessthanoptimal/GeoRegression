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

import georegression.delaunay.PackedDelaunayGraph.Vertex;
import georegression.metric.Intersection2D_F64;
import georegression.struct.Mesh2D_F64;
import georegression.struct.TriIndex;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Triangle2D_F64;
import lombok.Getter;
import lombok.Setter;
import org.ddogleg.struct.DogArray_I32;
import org.ddogleg.struct.VerbosePrint;
import org.ddogleg.util.VerboseUtils;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Incremental Delaunay triangulation based on [1]. The triangle which contains each point is found by walking
 * through the graph. The points are automatically shuffled to avoid degenerate configurations.
 *
 * <ul>
 *     <li>Triangles corners are in counter-clockwise order.</li>
 * </ul>
 *
 * Citations:
 * <ol>
 *     <li>L. J. Guibas, D. E. Knuth, M. Sharir, "Randomized incremental construction of Delaunay and Voronoi diagrams"
 *     Algorithmica, 7, 1992, 381-413</li>
 * </ol>
 */
public class DelaunayIncrementalWalk implements VerbosePrint {
	/// Delaunay graph structure
	@Getter private PackedDelaunayGraph graph = new PackedDelaunayGraph();

	/// Maximum number of iterations it will try before giving up and throwing an exception when searching
	@Getter @Setter int stuckInLoop = 1_000_000;

	/// Tolerance for deciding if a point is exactly on a triangles circumcircle
	@Getter @Setter private double circumcircleTol = 1e-8;

	/// Random number generator used to shuffle input points
	private Random rand = new Random(0xDEADBEEFL);

	/// Mapping from point current index to original index
	final DogArray_I32 reorderToOriginal = new DogArray_I32();

	//---- Internal workspace. Declared once to avoid excessive memory creation

	// triangle that's being considered for having the point inserted into
	final CandidateTriangle candidate = new CandidateTriangle();

	final Point2D_F64 point = new Point2D_F64();
	final LineSegment2D_F64 lineSegment = new LineSegment2D_F64();

	// Storage for results of calling findOnOtherWise
	final OtherSide otherSide = new OtherSide();
	// Workspace for check then swap
	final Triangle2D_F64 csTriangle = new Triangle2D_F64();

	@Nullable PrintStream verbose;

	/// Changes the random seed
	public void setRandomSeed( long seed ) {
		rand = new Random(seed);
	}

	/// Computes Delaunay triangulation of the passed in points
	public void process( AccessList<Point2D_F64> points, int size ) {
		graph.reset();
		graph.reservePoints(size);

		shufflePointIndexes(size);

		// Pass in the shuffled points to the helper
		for (int i = 0; i < size; i++) {
			points.get(reorderToOriginal.get(i), point);
			graph.addPoint(point.x, point.y);
		}

		// Create very large triangle to hold everything. Triangle must be CCW.
		addInitialLargeTriangles();

		// Add each point one at a time, be careful not to add the points at "infinity" used to define the large
		// triangle
		for (int idxPoint = 0; idxPoint < size; idxPoint++) {
			traverseThenInsert(idxPoint);
		}

		// Remove triangles which reference the very far away fake points
		removeInitialLargeTriangles();
	}

	/// After computing the triangulation, call this function convert results into a mesh
	///
	/// @param mesh (Output) storage for the mesh
	public Mesh2D_F64 toMesh( @Nullable Mesh2D_F64 mesh ) {
		if (mesh == null)
			mesh = new Mesh2D_F64();

		mesh.points.reset().reserve(graph.points.size());
		mesh.triangles.reset().reserve(graph.triangles.size());

		// Create a lookup table to add vertexes in the original order
		var originalToReorder = new int[reorderToOriginal.size];
		for (int i = 0; i < reorderToOriginal.size; i++) {
			originalToReorder[reorderToOriginal.get(i)] = i;
		}

		for (int i = 0; i < graph.points.size(); i++) {
			mesh.points.append(graph.points.getTemp(originalToReorder[i]));
		}

		for (int src = 0; src < graph.triangles.size(); src += 3) {
			int reorder1 = graph.triangles.get(src);
			if (reorder1 < 0)
				continue;

			int corner0 = reorderToOriginal.get(reorder1);
			int corner1 = reorderToOriginal.get(graph.triangles.get(src + 1));
			int corner2 = reorderToOriginal.get(graph.triangles.get(src + 2));

			mesh.triangles.add(corner0);
			mesh.triangles.add(corner1);
			mesh.triangles.add(corner2);
		}

		return mesh;
	}

	/// Add two triangle that will contain all the points
	protected void addInitialLargeTriangles() {

		// Find the AABB for the points. While it was suggested that a very large value be used that results
		// in numerical issues, so we will instead dynamically compute a bounding box.
		double x0, y0, x1, y1;
		x0 = y0 = Double.MAX_VALUE;
		x1 = y1 = -Double.MAX_VALUE;
		for (int i = 0; i < graph.points.size(); i++) {
			graph.points.getCopy(i, point);
			x0 = Math.min(x0, point.x);
			y0 = Math.min(y0, point.y);
			x1 = Math.max(x1, point.x);
			y1 = Math.max(y1, point.y);
		}

		// need a bounding box large enough to contain all the points and so that the convex hull will not
		// be removed when the large triangles are removed
		double maxPossibleDistanceBetween = Math.sqrt((x1 - x0)*(x1 - x0) + (y1 - y0)*(y1 - y0));
		double amount = 10;
		x0 -= maxPossibleDistanceBetween*amount;
		y0 -= maxPossibleDistanceBetween*amount;
		x1 += maxPossibleDistanceBetween*amount;
		y1 += maxPossibleDistanceBetween*amount;

		if (verbose != null) verbose.printf("bounding-box: (%.2e, %.2e) -> (%.2e, %.2e)\n", x0, y0, x1, y1);

		// Add the points and add the triangles
		graph.addPoint(x0, y0);
		graph.addPoint(x1, y0);
		graph.addPoint(x1, y1);
		graph.addPoint(x0, y1);

		int o = graph.points.size() - 4;
		graph.addTriangle(o, o + 1, o + 2);
		graph.addTriangle(o, o + 2, o + 3);
	}

	/// Remove all triangles that reference the very far away points
	protected void removeInitialLargeTriangles() {
		var fakeTriangles = new HashSet<Integer>();

		// Get the set of unique triangles which reference the far away vertexes
		for (int i = 0; i < 4; i++) {
			graph.vertexes.getTail(i).getAllTriangles(fakeTriangles);
		}

		// Remove the triangles
		fakeTriangles.forEach(triangleID -> graph.removeTriangle(triangleID));

		// Remove the last 4 points
		graph.points.removeSwap(graph.points.size() - 1);
		graph.points.removeSwap(graph.points.size() - 1);
		graph.points.removeSwap(graph.points.size() - 1);
		graph.points.removeSwap(graph.points.size() - 1);
	}

	/// Shuffle the original index mapping
	///
	/// @param size Number of points
	protected void shufflePointIndexes( int size ) {
		// Shuffle the input points and remember their original indexes
		reorderToOriginal.reset().reserve(size);
		for (int i = 0; i < size; i++) {
			reorderToOriginal.add(i);
		}
		// remove swap will swap the select index with the last and reducing the size by one
		while (reorderToOriginal.size > 0) {
			int selected = rand.nextInt(reorderToOriginal.size);
			int tmp = reorderToOriginal.get(selected);
			reorderToOriginal.set(selected, reorderToOriginal.getTail());
			reorderToOriginal.setTail(0, tmp);
			reorderToOriginal.size--;
		}
		// reset the size to contain all the shuffled points
		reorderToOriginal.size = size;
	}

	/// Searches for a triangle that contains the specified point then inserts it. Handles the case where
	/// designed to hande the case where the point lies exactly on the border between two triangles.
	///
	/// @param pointID Which point is being inserted
	protected void traverseThenInsert( int pointID ) {
		// Read in the point's location
		graph.points.getCopy(pointID, point);
		double px = point.x;
		double py = point.y;

		// Use the last triangle that was added. Alternatively this could be random, but often points are added in
		// a structured way
		candidate.id = graph.mostRecentlyAddedTriangle;

		if (verbose != null)
			verbose.println("inserting: tri=" + candidate.id + " pt=" + pointID +
					" (" + px + ", " + py + ") total=" + graph.triangleCount());

		// Continue searching until it finds a triangle that the point is inside of
		for (int stuck = 0; stuck < stuckInLoop; stuck++) {
			graph.getTriangle(candidate.id, candidate.corners);
			graph.getCorners(candidate.corners, candidate.location);

			// Determine the relative location of the point is to each side on the triangle
			double orientation01 = cross(candidate.location.v0, candidate.location.v1, px, py);
			double orientation12 = cross(candidate.location.v1, candidate.location.v2, px, py);
			double orientation20 = cross(candidate.location.v2, candidate.location.v0, px, py);

			// Test to see if the point is outside of triangle, if so which edge does it need to cross
			if (orientation01 < 0.0) {
				candidate.id = triangleOnOtherSide(candidate.id, 0, otherSide);
			} else if (orientation12 < 0.0) {
				candidate.id = triangleOnOtherSide(candidate.id, 1, otherSide);
			} else if (orientation20 < 0.0) {
				candidate.id = triangleOnOtherSide(candidate.id, 2, otherSide);
			} else {
				// It's inside the triangle. Is it exactly on a line or strictly inside?

				// First test for it being on a line
				if (orientation01 == 0.0) {
					insertPointOnLine(candidate, 0, pointID, px, py);
				} else if (orientation12 == 0.0) {
					insertPointOnLine(candidate, 1, pointID, px, py);
				} else if (orientation20 == 0.0) {
					insertPointOnLine(candidate, 2, pointID, px, py);
				} else {
					// Point is strictly inside the triangle
					insertPointInTriangle(candidate, pointID, px, py);
				}
				return;
			}
			if (verbose != null) verbose.println("_ moved to tri=" + candidate.id);
		}
		throw new RuntimeException("BUG! Taking too many iterations to find a triangle");
	}

	private static double cross( Point2D_F64 a, Point2D_F64 b, double px, double py ) {
		return (b.x - a.x)*(py - a.y) - (b.y - a.y)*(px - a.x);
	}

	/// Inserts the point inside the specified triangle
	///
	/// @param pointID Point that is being inserted
	/// @param px coordinate of point being inserted
	/// @param py coordinate of point being inserted
	protected void insertPointInTriangle( CandidateTriangle candidate, int pointID, double px, double py ) {
		if (verbose != null) verbose.println("_ done: inside tri=" + candidate.id);

		// Remove the triangle
		graph.removeTriangle(candidate.id);

		// Add 3 new triangles. Since the point being added is inside and the original
		// triangle is in CCW order, the new triangles will also be in CCW order
		int triABP = graph.addTriangle(candidate.corners.a, candidate.corners.b, pointID);
		int triBCP = graph.addTriangle(candidate.corners.b, candidate.corners.c, pointID);
		int triCAP = graph.addTriangle(candidate.corners.c, candidate.corners.a, pointID);

		// See if we need to swap edges to maintain the Delaunay condition
		checkThenSwapEdge(triABP, 0, px, py);
		checkThenSwapEdge(triBCP, 0, px, py);
		checkThenSwapEdge(triCAP, 0, px, py);
	}

	/// The point lies on a line between two triangles and needs to be inserted
	///
	/// @param pointID Point that is being inserted
	/// @param px coordinate of point being inserted
	/// @param py coordinate of point being inserted
	protected void insertPointOnLine( CandidateTriangle candidate, int side, int pointID, double px, double py ) {
		if (verbose != null) verbose.println("_ done: line tri=" + candidate.id + " side=" + side);

		// Find the other triangle
		if (-1 == triangleOnOtherSide(candidate.id, side, otherSide))
			throw new RuntimeException("BUG! No triangle on other side");

		// Get point ID for their corners
		int pa = graph.cornerPointW(candidate.id, side);
		int pc = graph.cornerPointW(candidate.id, side + 1);
		int pd = graph.cornerPointW(candidate.id, side + 2);
		int pb = graph.cornerPointW(otherSide.triangleID, otherSide.cornerID);

		// Remove both triangles
		graph.removeTriangle(candidate.id);
		graph.removeTriangle(otherSide.triangleID);

		// Create new triangles from those 4 corners
		int triABP = graph.addTriangle(pa, pb, pointID);
		int triBCP = graph.addTriangle(pb, pc, pointID);
		int triCDP = graph.addTriangle(pc, pd, pointID);
		int triDAP = graph.addTriangle(pd, pa, pointID);

		// See if we need to swap edges to maintain the Delaunay condition
		checkThenSwapEdge(triABP, 0, px, py);
		checkThenSwapEdge(triBCP, 0, px, py);
		checkThenSwapEdge(triCDP, 0, px, py);
		checkThenSwapEdge(triDAP, 0, px, py);
	}

	/// Triangle (b,c,p) was just added. We need to look at the triangle (a,b,c) on the other see if the
	/// edge (a,b) needs to be swapped for (b, c) to maintain the local Delaunay condition based on
	/// circumcircle. New triangles will be (a,d,b) and (a,c,d)
	///
	/// If swapping is required then two triangles are removed and two new ones added with the new side.
	///
	/// @param triangleID Triangle that was just added
	/// @param side Which side on "triangleID" that is being tested
	/// @param px Location of point that was inserted. x-axis.
	/// @param py Location of point that was inserted. y-axis
	protected void checkThenSwapEdge( int triangleID, int side, double px, double py ) {
		// See if it's at border / very large triangles
		if (-1 == triangleOnOtherSide(triangleID, side, otherSide))
			return;

		// look up the other triangle and get its coordinates
		graph.getCorners(otherSide.triangleID, csTriangle);

		// If a point is outside the circumcircle then no change is needed
		if (Intersection2D_F64.insideCircumcircleCCW(csTriangle, px, py, circumcircleTol) <= 0)
			return;

		// Get point ID for their corners
		int pb = graph.cornerPointW(triangleID, side);
		int pc = graph.cornerPointW(triangleID, side + 1);
		int pd = graph.cornerPointW(triangleID, side + 2);
		int pa = graph.cornerPoint(otherSide.triangleID, otherSide.cornerID);

		// Remove both triangles
		graph.removeTriangle(triangleID);
		graph.removeTriangle(otherSide.triangleID);

		// create two new triangles
		int ref0 = graph.addTriangle(pb, pa, pd);
		int ref1 = graph.addTriangle(pa, pc, pd);

		// swap check (a,c) and (a,b)
		checkThenSwapEdge(ref0, 0, px, py);
		checkThenSwapEdge(ref1, 0, px, py);

		// NOTE: This would ideally not be recursive but use a stack of triangle that need to be tested instead.
		//       I am worried that if a triangle is in the stack and it gets modified then there will be problem
		//       this might be a non issues but will require a bit of thought. Even with the current scenario
		//       there might be an issue where the swapping chain works it's way around to one of the just created
		//       triangles and deletes it before we can examine it. That would be bad.
	}

	/// Finds the triangle on the edge's other side. This is done by looking at all the triangles connected the
	/// two corners.
	///
	/// @param triangleID Triangle which this is a side of and should not be returned.
	/// @param side Specified the side in question. See {@link PackedDelaunayGraph#cornerPoint}
	/// @return The triangleID on the other side or -1 if there is no tringle.
	protected int triangleOnOtherSide( int triangleID, int side, OtherSide results ) {
		int point0 = graph.cornerPoint(triangleID, side);
		int point1 = graph.cornerPointW(triangleID, side + 1);

		// Get spatial location of line segment end points
		graph.points.getCopy(point0, lineSegment.a);
		graph.points.getCopy(point1, lineSegment.b);

		// Get graph information so we can look up connected triangles
		Vertex vertexA = graph.vertexes.get(point0);
		Vertex vertexB = graph.vertexes.get(point1);

		// Note: This is a hotspot in the profiler. Is there a way to speed this up?

		// Search for a triangle that both vertexes have in common, that's not triangleID
		final int sizeA = vertexA.size();
		for (int i = 0; i < sizeA; i++) {
			int candidate = vertexA.getTriangle(i);
			if (candidate == triangleID)
				continue;
			int cornerI = vertexA.getCorner(i);

			final int sizeB = vertexB.size();
			for (int j = 0; j < sizeB; j++) {
				if (vertexB.getTriangle(j) != candidate)
					continue;

				int cornerJ = vertexB.getCorner(j);
				if (cornerI == cornerJ)
					throw new RuntimeException("BUG! Corners cannot be the same!");

				results.triangleID = candidate;
				results.cornerID = 3 - cornerI - cornerJ;
				return candidate;
			}
		}

		return -1;
	}

	@Override public void setVerbose( @Nullable PrintStream out, @Nullable Set<String> configuration ) {
		this.verbose = VerboseUtils.addPrefix(this, out);
	}

	/// Results from finding the triangle on the other side
	protected static class OtherSide {
		/// The triangle on the other side
		public int triangleID;
		/// The corner on "triangle" which is not in common. Can have a value of 0 to 2.
		public int cornerID;
	}

	/// Contains information about the triangle that's being considered to insert the point into
	protected static class CandidateTriangle {
		/// Triangle ID being added
		public int id = -1;
		/// PointIDs of its corner
		public TriIndex corners = new TriIndex();
		/// Where the corners are
		public Triangle2D_F64 location = new Triangle2D_F64();
	}
}

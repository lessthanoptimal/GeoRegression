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

package georegression.struct.shapes;

import georegression.geometry.UtilPolygons2D_F64;
import georegression.metric.Area2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import org.ddogleg.struct.DogArray;
import org.ejml.ops.MatrixIO;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.ejml.UtilEjml.fancyString;

/**
 * Describes a polygon in 2D.
 *
 * @author Peter Abeles
 */
public class Polygon2D_F64 implements Serializable {

	// vertexes in the polygon
	public DogArray<Point2D_F64> vertexes;

	public Polygon2D_F64( double[][] a ) {
		this(a.length);

		for (int i = 0; i < a.length; i++) {
			vertexes.get(i).setTo(a[i][0], a[i][1]);
		}
	}

	public Polygon2D_F64( Polygon2D_F64 a ) {
		vertexes = new DogArray<>(a.size(), Point2D_F64::new);
		for (int i = 0; i < a.size(); i++) {
			vertexes.grow().setTo(a.get(i));
		}
	}

	public Polygon2D_F64( int numVertexes ) {
		vertexes = new DogArray<>(numVertexes, Point2D_F64::new);

		vertexes.reserve(numVertexes);
		vertexes.size = numVertexes;
	}

	public Polygon2D_F64( double... xy ) {
		if (xy.length%2 == 1)
			throw new IllegalArgumentException("Expected an even number");
		vertexes = new DogArray<>(xy.length/2, Point2D_F64::new);
		vertexes.reserve(xy.length/2);
		vertexes.size = xy.length/2;

		int count = 0;
		for (int i = 0; i < xy.length; i += 2) {
			vertexes.data[count++].setTo(xy[i], xy[i + 1]);
		}
	}

	public Polygon2D_F64() {
		vertexes = new DogArray<>(Point2D_F64::new);
	}

	/**
	 * Sets the value of every vertex to be zero
	 */
	public void zero() {
		for (int i = 0; i < vertexes.size; i++) {
			vertexes.get(i).setTo(0, 0);
		}
	}

	public void setTo( Polygon2D_F64 orig ) {
		vertexes.resize(orig.size());
		for (int i = 0; i < orig.size(); i++) {
			vertexes.data[i].setTo(orig.vertexes.data[i]);
		}
	}

	public void set( int index, double x, double y ) {
		vertexes.data[index].setTo(x, y);
	}

	/**
	 * Resturns the length of the specified side that is composed of point index and index+1
	 *
	 * @return Euclidean length of the side
	 */
	public double getSideLength( int index ) {
		Point2D_F64 a = vertexes.get(index);
		Point2D_F64 b = vertexes.get((index + 1)%vertexes.size);

		return (double)a.distance(b);
	}

	public Point2D_F64 get( int index ) {
		return vertexes.data[index];
	}

	public int size() {
		return vertexes.size();
	}

	public Polygon2D_F64 copy() {
		return new Polygon2D_F64(this);
	}

	/**
	 * Returns the area for simply polygons. Non-self intersecting convex or concave.
	 *
	 * @return area
	 */
	public double areaSimple() {
		return Area2D_F64.polygonSimple(this);
	}

	/**
	 * Returns true if the point is inside the polygon. Points along the border are ambiguously considered inside
	 * or outside.
	 *
	 * @param p A point
	 * @return true if inside and false if outside
	 * @see Intersection2D_F64#containsConvex(Polygon2D_F64, Point2D_F64)
	 * @see Intersection2D_F64#containsConcave(Polygon2D_F64, Point2D_F64)
	 */
	public boolean isInside( Point2D_F64 p ) {
		if (isConvex()) {
			return Intersection2D_F64.containsConvex(this, p);
		} else {
			return Intersection2D_F64.containsConcave(this, p);
		}
	}

	/**
	 * true if the order of vertexes is in counter-clockwise order.
	 *
	 * @return true if ccw or false if cw
	 */
	public boolean isCCW() {
		return UtilPolygons2D_F64.isCCW(vertexes.toList());
	}

	public boolean isConvex() {
		return UtilPolygons2D_F64.isConvex(this);
	}

	public boolean isIdentical( Polygon2D_F64 a, double tol ) {
		return UtilPolygons2D_F64.isIdentical(this, a, tol);
	}

	public boolean isEquivalent( Polygon2D_F64 a, double tol ) {
		return UtilPolygons2D_F64.isEquivalent(this, a, tol);
	}

	public void flip() {
		UtilPolygons2D_F64.flip(this);
	}

	/** Creates a copy of 'this' and flips it */
	public Polygon2D_F64 flip( @Nullable Polygon2D_F64 storage ) {
		if (storage == null)
			storage = new Polygon2D_F64(this.size());
		storage.setTo(this);
		storage.flip();
		return storage;
	}

	/**
	 * Returns the line/edge defined by vertex index and index+1.
	 *
	 * @param index Index of the line
	 * @return A new instance of the line segment.
	 */
	public LineSegment2D_F64 getLine( int index, LineSegment2D_F64 storage ) {
		if (storage == null)
			storage = new LineSegment2D_F64();

		int j = (index + 1)%vertexes.size;

		storage.a.setTo(get(index));
		storage.b.setTo(get(j));

		return storage;
	}

	/**
	 * Converts the polygon into a list.
	 *
	 * @param storage (Optional) storage to put the vertexes into
	 * @param copy If points will be copied otherwise a reference of points will be returned
	 * @return List of vertexes
	 */
	public List<Point2D_F64> convert( @Nullable List<Point2D_F64> storage, boolean copy ) {
		if (storage == null)
			storage = new ArrayList<>();
		else
			storage.clear();

		if (copy) {
			for (int i = 0; i < vertexes.size; i++) {
				storage.add(vertexes.get(i).copy());
			}
		} else {
			storage.addAll(vertexes.toList());
		}
		return storage;
	}

	/**
	 * Sets the polygon to be the same as the list. A true copy is created and no references
	 * to points in the list are saved.
	 *
	 * @param list List which the polygon will be set to
	 */
	public void setTo( List<Point2D_F64> list ) {
		vertexes.resize(list.size());
		for (int i = 0; i < list.size(); i++) {
			vertexes.data[i].setTo(list.get(i));
		}
	}

	@Override public String toString() {

		final int length = MatrixIO.DEFAULT_LENGTH;
		DecimalFormat format = new DecimalFormat("#");
		String out = getClass().getSimpleName() + "{ order=" + vertexes.size + ", [ ";

		for (int i = 0; i < vertexes.size; i++) {
			Point2D_F64 p = vertexes.get(i);
			out += "(" + fancyString(p.x, format, false, length, 4) + ", " + fancyString(p.y, format, false, length, 4) + ") ";
		}

		out += "] }";

		return out;
	}
}

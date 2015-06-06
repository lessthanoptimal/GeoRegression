/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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
import org.ddogleg.struct.FastQueue;

import java.io.Serializable;

/**
 * Describes a polygon in 2D.
 *
 * @author Peter Abeles
 */
public class Polygon2D_F64 implements Serializable {

	// vertexes in the polygon
	public FastQueue<Point2D_F64> vertexes;

	public Polygon2D_F64( Polygon2D_F64 a ) {
		vertexes = new FastQueue<Point2D_F64>(a.size(),Point2D_F64.class,true);
		for (int i = 0; i < a.size(); i++) {
			vertexes.grow().set(a.get(i));
		}
	}

	public Polygon2D_F64( int numVertexes ) {
		vertexes = new FastQueue<Point2D_F64>(numVertexes, Point2D_F64.class,true);

		vertexes.growArray(numVertexes);
		vertexes.size = numVertexes;
	}

	public Polygon2D_F64( double... points ) {
		if( points.length % 2 == 1 )
			throw new IllegalArgumentException("Expected an even number");
		vertexes = new FastQueue<Point2D_F64>(points.length/2,Point2D_F64.class,true);
		vertexes.growArray(points.length/2);
		vertexes.size = points.length/2;

		int count = 0;
		for (int i = 0; i < points.length; i += 2) {
			vertexes.data[count++].set( points[i],points[i+1]);
		}
	}

	public Polygon2D_F64() {
		vertexes = new FastQueue<Point2D_F64>(Point2D_F64.class,true);
	}

	public void set( Polygon2D_F64 orig ) {
		vertexes.resize(orig.size());
		for (int i = 0; i < orig.size(); i++) {
			vertexes.data[i].set( orig.vertexes.data[i]);
		}
	}

	public void set( int index , double x , double y ) {
		vertexes.data[index].set(x,y);
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

	public double area() {
		if( isConvex())
			return Area2D_F64.polygonConvex(this);
		else
			throw new RuntimeException("Doesn't support area for concave polygons yet");
	}

	/**
	 * Returns true if the point is inside the polygon.  Points along the border are ambiguously considered inside
	 * or outside.
	 *
	 * @see {@link Intersection2D_F64#containConcave(Polygon2D_F64, Point2D_F64)}
	 * @see {@link Intersection2D_F64#containConcave(Polygon2D_F64, Point2D_F64)}
	 *
	 * @param p A point
	 * @return true if inside and false if outside
	 */
	public boolean isInside( Point2D_F64 p ) {
		if( isConvex() ) {
			return Intersection2D_F64.containConvex(this,p);
		} else {
			return Intersection2D_F64.containConcave(this,p);
		}
	}

	/**
	 * true if the order of vertexes is in counter clockwise order.
	 * @return true if ccw or false if cw
	 */
	public boolean isCCW() {
		return UtilPolygons2D_F64.isCCW(vertexes.toList());
	}

	public boolean isConvex() {
		return UtilPolygons2D_F64.isConvex(this);
	}

	public boolean isIdentical( Polygon2D_F64 a , double tol ) {
		return UtilPolygons2D_F64.isIdentical(this,a,tol);
	}

	public LineSegment2D_F64 getLine( int index , LineSegment2D_F64 storage ) {
		if( storage == null )
			storage = new LineSegment2D_F64();

		int j = (index+1)%vertexes.size;

		storage.a.set(get(index));
		storage.b.set(get(j));

		return storage;
	}

	@Override
	public String toString() {
		String out = getClass().getSimpleName()+"{ order "+vertexes.size+" : vertexes [ ";

		for (int i = 0; i < vertexes.size; i++) {
			Point2D_F64 p = vertexes.get(i);
			out += p.x+" , "+p.y+" ; ";
		}

		out += " ] }";

		return out;
	}
}

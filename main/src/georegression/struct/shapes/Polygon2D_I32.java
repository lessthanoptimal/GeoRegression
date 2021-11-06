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

import georegression.geometry.UtilPolygons2D_I32;
import georegression.struct.point.Point2D_I32;
import org.ddogleg.struct.DogArray;

import java.io.Serializable;

/**
 * Describes a polygon in 2D.
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
public class Polygon2D_I32 implements Serializable, Cloneable {

	// vertexes in the polygon
	public DogArray<Point2D_I32> vertexes;

	public Polygon2D_I32( int numVertexes ) {
		vertexes = new DogArray<>(Point2D_I32::new);

		vertexes.reserve(numVertexes);
		vertexes.size = numVertexes;
	}

	public Polygon2D_I32() {
		vertexes = new DogArray<>(Point2D_I32::new);
	}

	public Polygon2D_I32( Polygon2D_I32 original ) {
		setTo(original);
	}

	public Polygon2D_I32( int... points ) {
		if (points.length%2 == 1)
			throw new IllegalArgumentException("Expected an even number");
		vertexes = new DogArray<>(points.length/2, Point2D_I32::new);
		vertexes.reserve(points.length/2);
		vertexes.size = points.length/2;

		int count = 0;
		for (int i = 0; i < points.length; i += 2) {
			vertexes.data[count++].setTo(points[i], points[i + 1]);
		}
	}

	public int size() {
		return vertexes.size();
	}

	public Point2D_I32 get( int index ) {
		return vertexes.data[index];
	}

	public void flip() {
		UtilPolygons2D_I32.flip(this);
	}

	public boolean isCCW() {
		return UtilPolygons2D_I32.isCCW(vertexes.toList());
	}

	public boolean isConvex() {
		return UtilPolygons2D_I32.isConvex(this);
	}

	public boolean isIdentical( Polygon2D_I32 a ) {
		return UtilPolygons2D_I32.isIdentical(this, a);
	}

	public boolean isEquivalent( Polygon2D_I32 a, double tol ) {
		return UtilPolygons2D_I32.isEquivalent(this, a);
	}

	public void setTo( Polygon2D_I32 orig ) {
		vertexes.resize(orig.size());
		for (int i = 0; i < orig.size(); i++) {
			vertexes.data[i].setTo(orig.vertexes.data[i]);
		}
	}

	/**
	 * Sets the value of every vertex to be zero
	 */
	public void zero() {
		for (int i = 0; i < vertexes.size; i++) {
			vertexes.get(i).setTo(0, 0);
		}
	}

	public void set( int index, int x, int y ) {
		vertexes.data[index].setTo(x, y);
	}

	public Polygon2D_I32 copy() {
		return new Polygon2D_I32(this);
	}
}

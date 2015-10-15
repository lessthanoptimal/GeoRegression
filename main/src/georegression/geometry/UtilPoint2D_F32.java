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

package georegression.geometry;

import georegression.struct.GeoTuple2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import georegression.struct.shapes.RectangleLength2D_F32;
import org.ddogleg.sorting.QuickSort_F32;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 *
 */
public class UtilPoint2D_F32 {

	public static List<Point2D_F32> copy( List<Point2D_F32> pts ) {
		List<Point2D_F32> ret = new ArrayList<Point2D_F32>();

		for( Point2D_F32 p : pts ) {
			ret.add( p.copy() );
		}

		return ret;
	}

	public static void noiseNormal( List<Point2D_F32> pts, float sigma, Random rand ) {
		for( Point2D_F32 p : pts ) {
			p.x += (float)rand.nextGaussian() * sigma;
			p.y += (float)rand.nextGaussian() * sigma;
		}
	}

	public static float distance( float x0, float y0, float x1, float y1 ) {
		float dx = x1 - x0;
		float dy = y1 - y0;

		return (float)Math.sqrt( dx * dx + dy * dy );
	}

	public static float distanceSq( float x0, float y0, float x1, float y1 ) {
		float dx = x1 - x0;
		float dy = y1 - y0;

		return dx * dx + dy * dy;
	}

	/**
	 * Finds the point which has the mean location of all the points in the list. This is also known
	 * as the centroid.
	 *
	 * @param list List of points
	 * @param mean Storage for mean point.  If null then a new instance will be declared
	 * @return The found mean
	 */
	public static Point2D_F32 mean( List<Point2D_F32> list , Point2D_F32 mean ) {
		if( mean == null )
			mean = new Point2D_F32();

		float x = 0;
		float y = 0;

		for( Point2D_F32 p : list ) {
			x += p.getX();
			y += p.getY();
		}

		x /= list.size();
		y /= list.size();

		mean.set(x, y);
		return mean;
	}


	/**
	 * Computes the mean/average of two points.
	 *
	 * @param a (input) Point A
	 * @param b (input) Point B
	 * @param mean (output) average of 'a' and 'b'
	 *
	 */
	public static Point2D_F32 mean( Point2D_F32 a , Point2D_F32 b, Point2D_F32 mean ) {
		if( mean == null )
			mean = new Point2D_F32();

		mean.x = (a.x + b.x)/2.0f;
		mean.y = (a.y + b.y)/2.0f;

		return mean;
	}

	public static List<Point2D_F32> random( float min, float max, int num, Random rand ) {
		List<Point2D_F32> ret = new ArrayList<Point2D_F32>();

		float d = max - min;

		for( int i = 0; i < num; i++ ) {
			Point2D_F32 p = new Point2D_F32();
			p.x = rand.nextFloat() * d + min;
			p.y = rand.nextFloat() * d + min;

			ret.add( p );
		}

		return ret;
	}

	public static boolean isEquals( GeoTuple2D_F32 a, GeoTuple2D_F32 b, float tol ) {
		return ( (float)Math.abs( a.x - b.x ) <= tol && (float)Math.abs( a.x - b.x ) <= tol );
	}

	/**
	 * Finds the minimal volume {@link georegression.struct.shapes.RectangleLength2D_F32} which contains all the points.
	 *
	 * @param points Input: List of points.
	 * @param bounding Output: Bounding rectangle
	 */
	public static RectangleLength2D_F32 bounding(List<Point2D_F32> points, RectangleLength2D_F32 bounding) {
		if( bounding == null )
			bounding = new RectangleLength2D_F32();

		float minX=Float.MAX_VALUE,maxX=-Float.MAX_VALUE;
		float minY=Float.MAX_VALUE,maxY=-Float.MAX_VALUE;

		for( int i = 0; i < points.size(); i++ ) {
			Point2D_F32 p = points.get(i);
			if( p.x < minX )
				minX = p.x;
			if( p.x > maxX )
				maxX = p.x;
			if( p.y < minY )
				minY = p.y;
			if( p.y > maxY )
				maxY = p.y;
		}

		bounding.x0 = minX;
		bounding.y0 = minY;
		bounding.width = maxX-minX;
		bounding.height = maxY-minY;

		// make sure rounding doesn't cause a point to be out of bounds
		bounding.width  += (float)Math.max(0,(maxX-(bounding.x0+bounding.width ))*10.0f);
		bounding.height += (float)Math.max(0,(maxY-(bounding.y0+bounding.height))*10.0f);

		return bounding;
	}

	/**
	 * Finds the minimal volume {@link georegression.struct.shapes.RectangleLength2D_F32} which contains all the points.
	 *
	 * @param points Input: List of points.
	 * @param bounding Output: Bounding rectangle
	 */
	public static Rectangle2D_F32 bounding(List<Point2D_F32> points, Rectangle2D_F32 bounding) {
		if( bounding == null )
			bounding = new Rectangle2D_F32();

		float minX=Float.MAX_VALUE,maxX=-Float.MAX_VALUE;
		float minY=Float.MAX_VALUE,maxY=-Float.MAX_VALUE;

		for( int i = 0; i < points.size(); i++ ) {
			Point2D_F32 p = points.get(i);
			if( p.x < minX )
				minX = p.x;
			if( p.x > maxX )
				maxX = p.x;
			if( p.y < minY )
				minY = p.y;
			if( p.y > maxY )
				maxY = p.y;
		}

		bounding.set(minX,minY,maxX,maxY);

		return bounding;
	}

	/**
	 * Puts the points into counter-clockwise order around their center.
	 *
	 * @param points List of points. Not modified.
	 * @return ordered list
	 */
	public static List<Point2D_F32> orderCCW( List<Point2D_F32> points ) {
		Point2D_F32 center = mean(points,null);

		float angles[] = new float[ points.size() ];
		for (int i = 0; i < angles.length; i++) {
			Point2D_F32 p = points.get(i);

			float dx = p.x - center.x;
			float dy = p.y - center.y;

			angles[i] = (float)Math.atan2(dy,dx);
		}

		int order[] = new int[ points.size() ];

		QuickSort_F32 sorter = new QuickSort_F32();
		sorter.sort(angles,points.size(),order);

		List<Point2D_F32> out = new ArrayList<Point2D_F32>(points.size());
		for (int i = 0; i < points.size(); i++) {
			out.add(points.get(order[i]));
		}

		return out;
	}
}

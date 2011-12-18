/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.metric;

import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Rectangle2D_F32;


/**
 * Functions relating to finding the points at which two shapes intersect with each other.
 *
 * @author Peter Abeles
 */
public class Intersection2D_F32 {

	// todo comment
	// todo how are parallel lines handled?

	/**
	 * Finds the point of intersection between two lines.
	 *
	 * @param a Line.
	 * @param b Line.
	 * @return The location along 'target'.  If the lines do not intersect or have infinite intersections Float.NaN is returned.
	 */
	public static Point2D_F32 intersection( LineParametric2D_F32 a, LineParametric2D_F32 b ) {
		float t_b = a.getSlopeX() * ( b.getY() - a.getY() ) - a.getSlopeY() * ( b.getX() - a.getX() );
		float bottom = a.getSlopeY() * b.getSlopeX() - b.getSlopeY() * a.getSlopeX();

		if( bottom == 0 )
			return null;

		t_b /= bottom;

		float x = b.getSlopeX() * t_b + b.getX();
		float y = b.getSlopeY() * t_b + b.getY();

		return new Point2D_F32( x, y );
	}

	/**
	 * Finds the point of intersection between two lines segments.
	 *
	 * @param l_0 Line segment.
	 * @param l_1 line segment.
	 * @param ret storage for the point of intersection. If null a new point will be declared.
	 * @return If the two lines intersect it returns the point of intersection.  null if they don't intersect or have infinite intersections.
	 */
	public static Point2D_F32 intersection( LineSegment2D_F32 l_0, LineSegment2D_F32 l_1,
											Point2D_F32 ret ) {

		float a0 = l_0.b.x - l_0.a.x;
		float b0 = l_0.b.y - l_0.a.y;
		float a1 = l_1.b.x - l_1.a.x;
		float b1 = l_1.b.y - l_1.a.y;

		float top = b0 * ( l_1.a.x - l_0.a.x ) + a0 * ( l_0.a.y - l_1.a.y );
		float bottom = a0 * b1 - b0 * a1;

		if( bottom == 0 )
			return null;
		float t_1 = top / bottom;

		// does not intersect along the second line segment
		if( t_1 < 0 || t_1 > 1 )
			return null;

		top = b1 * ( l_0.a.x - l_1.a.x ) + a1 * ( l_1.a.y - l_0.a.y );
		bottom = a1 * b0 - b1 * a0;

		float t_0 = top / bottom;

		// does not intersect along the first line segment
		if( t_0 < 0 || t_0 > 1 )
			return null;

		if( ret == null ) {
			ret = new Point2D_F32();
		}

		ret.set( l_1.a.x + a1 * t_1, l_1.a.y + b1 * t_1 );

		return ret;
	}

	/**
	 * Finds the point of intersection between a line and a line segment.  The point of intersection
	 * is specified as the distance along the parametric line.  If no intersection is found then
	 * Float.NaN is returned.
	 *
	 * @param target A line whose location along which the point of intersection is being found.  Not modified.
	 * @param l	  Line segment which is being tested for intersection. Not modified.
	 * @return The location along 'target'.  If the lines do not intersect or have infinite intersections Float.NaN is returned.
	 */
	public static float intersection( LineParametric2D_F32 target, LineSegment2D_F32 l ) {
		float a1 = l.b.x - l.a.x;
		float b1 = l.b.y - l.a.y;

		float top = target.slope.y * ( l.a.x - target.p.x ) + target.slope.x * ( target.p.y - l.a.y );
		float bottom = target.slope.x * b1 - target.slope.y * a1;

		if( bottom == 0 )
			return Float.NaN;

		float t_1 = top / bottom;

		// does not intersect along the second line segment
		if( t_1 < 0 || t_1 > 1 )
			return Float.NaN;

		top = b1 * ( target.p.x - l.a.x ) + a1 * ( l.a.y - target.p.y );
		bottom = a1 * target.slope.y - b1 * target.slope.x;

		return top / bottom;
	}

	public static boolean contains( Rectangle2D_F32 a, float x, float y ) {
		if( a.getX() <= x && a.getX() + a.getWidth() > x ) {
			return a.getY() <= y && a.getY() + a.getHeight() > y;
		}
		return false;
	}

	public static Rectangle2D_F32 intersection( Rectangle2D_F32 a, Rectangle2D_F32 b ) {
		float tl_x, tl_y, w, h;

		if( a.getX() >= b.getX() ) {
			if( a.getX() >= b.getX() + b.getWidth() )
				return null;

			tl_x = a.getX();
			w = b.getX() + b.getWidth() - a.getX();
		} else {
			if( a.getX() + a.getWidth() <= b.getX() )
				return null;

			tl_x = b.getX();
			w = a.getX() + a.getWidth() - b.getX();
		}

		if( a.getY() >= b.getY() ) {
			if( a.getY() >= b.getY() + b.getHeight() )
				return null;

			tl_y = a.getY();
			h = b.getY() + b.getHeight() - a.getY();
		} else {
			if( a.getY() + a.getHeight() <= b.getY() )
				return null;

			tl_y = b.getY();
			h = a.getY() + a.getHeight() - b.getY();
		}


		return new Rectangle2D_F32( tl_x, tl_y, w, h );
	}
}

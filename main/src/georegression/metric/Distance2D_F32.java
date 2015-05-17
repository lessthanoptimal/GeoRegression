/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.geometry.UtilPoint2D_F32;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;


/**
 * Functions related to finding the distance of one shape from another shape.  This is often
 * closely related to finding the {@link ClosestPoint3D_F32 closest point}.
 *
 * @author Peter Abeles
 */
// TODO distance between two line segments, line lines
// handle parallel overlapping cases by returning zero
public class Distance2D_F32 {


	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on the line from a point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Distance the closest point on the line is away from the point.
	 */
	public static float distance( LineParametric2D_F32 line, Point2D_F32 p ) {
		return (float)Math.sqrt(distanceSq(line, p));
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared of the closest point on the line from a point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance squared to the closest point on the line is away from the point.
	 */
	public static float distanceSq( LineParametric2D_F32 line, Point2D_F32 p ) {
		float t = ClosestPoint2D_F32.closestPointT( line, p );

		float a = line.slope.x * t + line.p.x;
		float b = line.slope.y * t + line.p.y;

		float dx = p.x - a;
		float dy = p.y - b;

		return dx * dx + dy * dy;
	}

	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on a line segment to the specified point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance of the closest point on a line is away from a point.
	 */
	public static float distance( LineSegment2D_F32 line, Point2D_F32 p ) {
		return (float)Math.sqrt(distanceSq(line, p));
	}

	/**
	 * <p>
	 * Returns the Euclidean distance squared of the closest point on a line segment to the specified point.
	 * </p>
	 *
	 * @param line A line segment. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance squared of the closest point on a line is away from a point.
	 */
	public static float distanceSq( LineSegment2D_F32 line, Point2D_F32 p ) {
		float a = line.b.x - line.a.x;
		float b = line.b.y - line.a.y;

		float t = a * ( p.x - line.a.x ) + b * ( p.y - line.a.y );
		t /= ( a * a + b * b );

		// if the point of intersection is past the end points return the distance
		// from the closest end point
		if( t < 0 ) {
			return UtilPoint2D_F32.distanceSq(line.a.x, line.a.y, p.x, p.y);
		} else if( t > 1.0f )
			return UtilPoint2D_F32.distanceSq(line.b.x, line.b.y, p.x, p.y);

		// return the distance of the closest point on the line
		return UtilPoint2D_F32.distanceSq(line.a.x + t * a, line.a.y + t * b, p.x, p.y);
	}

	/**
	 * Finds the distance between the two line segments
	 * @param segmentA Line segment. Not modified.
	 * @param segmentB Line segment. Not modified.
	 * @return Euclidean distance of the closest point between the two line segments.
	 */
	public static float distance( LineSegment2D_F32 segmentA , LineSegment2D_F32 segmentB ) {
		return (float)Math.sqrt(distanceSq(segmentA,segmentB));
	}

	/**
	 * Finds the distance squared between the two line segments
	 * @param segmentA Line segment. Not modified.
	 * @param segmentB Line segment. Not modified.
	 * @return Euclidean distance squared of the closest point between the two line segments.
	 */
	public static float distanceSq( LineSegment2D_F32 segmentA , LineSegment2D_F32 segmentB ) {

		// intersection of the two lines relative to A
		float slopeAX = segmentA.slopeX();
		float slopeAY = segmentA.slopeY();
		float slopeBX = segmentB.slopeX();
		float slopeBY = segmentB.slopeY();

		float ta = slopeBX*( segmentA.a.y - segmentB.a.y ) - slopeBY*( segmentA.a.x - segmentB.a.x );
		float bottom = slopeBY*slopeAX - slopeAY*slopeBX;

		// see they intersect
		if( bottom != 0 ) {
			// see if the intersection is inside of lineA
			ta /= bottom;
			if( ta >= 0 && ta <= 1.0f ) {
				// see if the intersection is inside of lineB
				float tb = slopeAX*( segmentB.a.y - segmentA.a.y ) - slopeAY*( segmentB.a.x - segmentA.a.x );
				tb /= slopeAY*slopeBX - slopeBY*slopeAX;
				if( tb >= 0 && tb <= 1.0f )
					return 0;
			}
		}

		float closest = Float.MAX_VALUE;
		closest = (float)Math.min(closest,distanceSq(segmentA, segmentB.a));
		closest = (float)Math.min(closest,distanceSq(segmentA, segmentB.b));
		closest = (float)Math.min(closest,distanceSq(segmentB, segmentA.a));
		closest = (float)Math.min(closest,distanceSq(segmentB, segmentA.b));

		return closest;
	}

	/**
	 * <p>
	 * Returns the Euclidean distance of the closest point on the line to the specified point.
	 * </p>
	 *
	 * @param line A line. Not modified.
	 * @param p The point. Not modified.
	 * @return Euclidean distance of the closest point on the line to the specified point.
	 */
	public static float distance( LineGeneral2D_F32 line , Point2D_F32 p ) {
		return (Math.abs(line.A*p.x + line.B*p.y + line.C) / (float)Math.sqrt( line.A*line.A + line.B*line.B ));
	}
}

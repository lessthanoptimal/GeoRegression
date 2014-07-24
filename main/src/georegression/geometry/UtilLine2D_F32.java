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

package georegression.geometry;


import georegression.metric.ClosestPoint2D_F32;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;

/**
 * Various functions related to lines
 *
 * @author Peter Abeles
 */
public class UtilLine2D_F32 {

	/**
	 * Converts a line from polar form to parametric.
	 *
	 * @param src
	 * @param ret
	 * @return
	 */
	public static LineParametric2D_F32 convert( LinePolar2D_F32 src , LineParametric2D_F32 ret )
	{
		if( ret == null )
			ret = new LineParametric2D_F32();

		float c = (float) (float)Math.cos(src.angle);
		float s = (float) (float)Math.sin(src.angle);

		ret.p.set(c*src.distance,s*src.distance);
		ret.slope.set(-s,c);

		return ret;
	}

	/**
	 * Converts a line segment into a parametric line.
	 *
	 * @param src
	 * @param ret
	 * @return
	 */
	public static LineParametric2D_F32 convert( LineSegment2D_F32 src , LineParametric2D_F32 ret )
	{
		if( ret == null )
			ret = new LineParametric2D_F32();

		ret.p.set(src.a);
		ret.slope.set(src.slopeX(),src.slopeY());

		return ret;
	}

	/**
	 * Converts a line from parametric to polar.
	 *
	 * @param src
	 * @param ret
	 * @return
	 */
	public static LinePolar2D_F32 convert( LineParametric2D_F32 src , LinePolar2D_F32 ret )
	{
		if( ret == null )
			ret = new LinePolar2D_F32();

		float t = ClosestPoint2D_F32.closestPointT(src, new Point2D_F32());

		float cpX = src.slope.x * t + src.p.x;
		float cpY = src.slope.y * t + src.p.y;

		ret.angle = (float)Math.atan2(cpY,cpX);
		ret.distance = (float)Math.sqrt(cpX*cpX + cpY*cpY);

		return ret;
	}

	/**
	 * Converts a line from parametric to general
	 *
	 * @param src
	 * @param ret
	 * @return
	 */
	public static LineGeneral2D_F32 convert( LineParametric2D_F32 src , LineGeneral2D_F32 ret ) {
		if( ret == null ) {
			ret = new LineGeneral2D_F32();
		}

		float x1 = src.p.x + src.slope.x;
		float y1 = src.p.y + src.slope.y;

		ret.A = (src.p.y - y1);
		ret.B = (x1 - src.p.x);
		ret.C = src.p.x*y1 - x1*src.p.y;

		return ret;
	}

	/**
	 * Converts a line from general to parametric
	 *
	 * @param src
	 * @param ret
	 * @return
	 */
	public static LineParametric2D_F32 convert( LineGeneral2D_F32 src ,  LineParametric2D_F32 ret ) {
		if( ret == null ) {
			ret = new LineParametric2D_F32();
		}

		ret.slope.x = src.B;
		ret.slope.y = -src.A;

		// find a point on the line
		if( (float)Math.abs(src.B) > (float)Math.abs(src.A) ) {
			ret.p.y = -src.C/src.B;
			ret.p.x = 0;
		} else {
			ret.p.x = -src.C/src.A;
			ret.p.y = 0;
		}

		return ret;
	}
}

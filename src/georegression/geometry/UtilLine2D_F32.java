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

package georegression.geometry;


import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.line.LineSegment2D_F32;

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
}

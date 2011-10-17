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

package georegression.fitting.line;


import georegression.struct.line.LinePolar2D_F64;
import georegression.struct.point.Point2D_F64;

import java.util.List;

/**
 * Finds the best file line given a set of observations.
 *
 * @author Peter Abeles
 */
public class FitLine_F64 {

	/**
	 * Computes the best fit line to the set of points using the polar line equation.
	 *
	 * @param points Set of points on the line.
	 * @param ret Storage for the line.  If null a new line will be declared.
	 * @return Best fit line.
	 */
	public static LinePolar2D_F64 polar( List<Point2D_F64> points , LinePolar2D_F64 ret ) {
		if( ret == null )
			ret = new LinePolar2D_F64();

		double meanX = 0;
		double meanY = 0;

		final int N = points.size();
		for( int i = 0; i < N; i++ ) {
			Point2D_F64 p = points.get(i);
			meanX += p.x;
			meanY += p.y;
		}
		meanX /= N;
		meanY /= N;

		double top = 0;
		double bottom = 0;

		for( int i = 0; i < N; i++ ) {
			Point2D_F64 p = points.get(i);
			double dx = meanX - p.x;
			double dy = meanY - p.y;

			top += dx*dy;
			bottom += dy*dy - dx*dx;
		}

		ret.angle = Math.atan2(-2.0*top , bottom)/2.0;
		ret.distance = (double)( meanX*Math.cos(ret.angle) + meanY*Math.sin(ret.angle));

		return ret;
	}
}

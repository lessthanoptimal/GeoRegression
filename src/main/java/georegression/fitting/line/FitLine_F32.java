/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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


import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.point.Point2D_F32;

import java.util.List;

/**
 * Finds the best file line given a set of observations.
 *
 * @author Peter Abeles
 */
public class FitLine_F32 {

	/**
	 * <p>
	 * Computes the unweighted best fit line to the set of points using the polar line equation. The solution
	 * is optimal in the Euclidean sense, see [1] for more details.
	 * </p>
	 *
	 * <p>
	 * [1] K. Arras, R. Siegwart, "Feature Extraction and Scene Interpretation for Map-Based Navigation
	 * and Map Building" Proc. SPIE, Mobile Robotics XIII, Vol. 3210, 1997
	 * </p>
	 *
	 *
	 * @param points Set of points on the line.
	 * @param ret Storage for the line.  If null a new line will be declared.
	 * @return Best fit line.
	 */
	public static LinePolar2D_F32 polar( List<Point2D_F32> points , LinePolar2D_F32 ret ) {
		if( ret == null )
			ret = new LinePolar2D_F32();

		float meanX = 0;
		float meanY = 0;

		final int N = points.size();
		for( int i = 0; i < N; i++ ) {
			Point2D_F32 p = points.get(i);
			meanX += p.x;
			meanY += p.y;
		}
		meanX /= N;
		meanY /= N;

		float top = 0;
		float bottom = 0;

		for( int i = 0; i < N; i++ ) {
			Point2D_F32 p = points.get(i);
			float dx = meanX - p.x;
			float dy = meanY - p.y;

			top += dx*dy;
			bottom += dy*dy - dx*dx;
		}

		ret.angle = (float)Math.atan2(-2.0f*top , bottom)/2.0f;
		ret.distance = (float)( meanX*Math.cos(ret.angle) + meanY*Math.sin(ret.angle));

		return ret;
	}

	/**
	 * <p>
	 * Computes the weighted best fit line to the set of points using the polar line equation. The solution
	 * is optimal in the Euclidean sense, see [1] for more details.
	 * </p>
	 *
	 * <p>
	 * [1] K. Arras, R. Siegwart, "Feature Extraction and Scene Interpretation for Map-Based Navigation
	 * and Map Building" Proc. SPIE, Mobile Robotics XIII, Vol. 3210, 1997
	 * </p>
	 *
	 *
	 * @param points Set of points on the line.
	 * @oaran weights Weight for each point.  weights[i] >= 0
	 * @param ret Storage for the line.  If null a new line will be declared.
	 * @return Best fit line.
	 */
	public static LinePolar2D_F32 polar( List<Point2D_F32> points , float weights[] , LinePolar2D_F32 ret ) {
		if( ret == null )
			ret = new LinePolar2D_F32();

		float totalWeight = 0;
		for( int i = 0; i < weights.length; i++ ) {
			totalWeight += weights[i];
		}

		float meanX = 0;
		float meanY = 0;

		final int N = points.size();
		for( int i = 0; i < N; i++ ) {
			Point2D_F32 p = points.get(i);
			float w = weights[i];
			meanX += w*p.x;
			meanY += w*p.y;
		}
		meanX /= totalWeight;
		meanY /= totalWeight;

		float top = 0;
		float bottom = 0;

		for( int i = 0; i < N; i++ ) {
			Point2D_F32 p = points.get(i);
			float w = weights[i];
			float dx = meanX - p.x;
			float dy = meanY - p.y;

			top += w*dx*dy;
			bottom += w*(dy*dy - dx*dx);
		}
		top /= totalWeight;
		bottom /= totalWeight;

		ret.angle = (float)Math.atan2(-2.0f*top , bottom)/2.0f;
		ret.distance = (float)( meanX*Math.cos(ret.angle) + meanY*Math.sin(ret.angle));

		return ret;
	}
}

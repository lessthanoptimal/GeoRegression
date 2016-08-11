/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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

package georegression.geometry.algs;

import georegression.geometry.UtilEllipse_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;

import static georegression.geometry.UtilEllipse_F64.tangentLines;

/**
 * Iterative algorithm for finding the 4 pairs of tangent lines between two ellipses.  The ellipses are assumed
 * to not intersect.
 *
 * Algorithm:<br>
 * While a closed form solution does exist, it is very complex and an iterative solution is used
 * here instead.
 *
 * @author Peter Abeles
 */
public class TangentLinesTwoEllipses_F64 {

	// convergence parameters
	private double convergenceTol;
	private int maxIterations = 10;

	// storage
	private Point2D_F64 temp0 = new Point2D_F64();
	private Point2D_F64 temp1 = new Point2D_F64();

	// storage for the change in point positions
	double sumDifference;

	// true of the optimization converged before it ran out of iterations
	private boolean converged;

	/**
	 * Constructor that configures optimization parameters
	 *
	 *
	 * @param convergenceTol  Tolerance for when the iterations will stop.  Try 1e-8 for doubles and 1e-4 for floats
	 * @param maxIterations Maximum number of iterations
	 */
	public TangentLinesTwoEllipses_F64(double convergenceTol,
									   int maxIterations) {
		this.convergenceTol = convergenceTol;
		this.maxIterations = maxIterations;
	}

	/**
	 * Selects 4 pairs of points.  Each point in the pair represents an end point in a line segment which is tangent
	 * to both ellipseA and ellipseB.  Both ellipses are assumed to not intersect each other.  If a fatal error
	 * occurs the function will return false.  However it can return true and did not converge.  To check for
	 * convergence call {@link #isConverged()}.
	 *
	 * @param ellipseA (Input) An ellipse
	 * @param ellipseB (Input) An ellipse
	 * @param tangentA0 (Output) Tangent point on A for line segment 0
	 * @param tangentA1 (Output) Tangent point on A for line segment 1
	 * @param tangentA2 (Output) Tangent point on A for line segment 2
	 * @param tangentA3 (Output) Tangent point on A for line segment 3
	 * @param tangentB0 (Output) Tangent point on B for line segment 0
	 * @param tangentB1 (Output) Tangent point on B for line segment 1
	 * @param tangentB2 (Output) Tangent point on B for line segment 2
	 * @param tangentB3 (Output) Tangent point on B for line segment 3
	 * @return true if no fatal error, false if one happened.
	 */
	public boolean process(EllipseRotated_F64 ellipseA , EllipseRotated_F64 ellipseB ,
						   Point2D_F64 tangentA0 , Point2D_F64 tangentA1 ,
						   Point2D_F64 tangentA2 , Point2D_F64 tangentA3 ,
						   Point2D_F64 tangentB0 , Point2D_F64 tangentB1 ,
						   Point2D_F64 tangentB2 , Point2D_F64 tangentB3 )
	{
		converged = false;

		// initialize by picking an arbitrary point on A and then finding the points on B in which
		// a line is tangent to B and passes through the point on A
		if (!initialize(ellipseA, ellipseB,
				tangentA0, tangentA1, tangentA2, tangentA3,
				tangentB0, tangentB1, tangentB2, tangentB3))
			return false;


		// update the location of each point until it converges or the maximum number of iterations has been exceeded
		int iteration = 0;
		for( ;iteration < maxIterations; iteration++ ) {
			boolean allGood = false;
			sumDifference = 0;

			if( !selectTangent(tangentB0,tangentA0,ellipseA,tangentA0) )
				return false;
			if( !selectTangent(tangentB1,tangentA1,ellipseA,tangentA1) )
				return false;
			if( !selectTangent(tangentB2,tangentA2,ellipseA,tangentA2) )
				return false;
			if( !selectTangent(tangentB3,tangentA3,ellipseA,tangentA3) )
				return false;

			if( Math.sqrt(sumDifference)/4.0 < convergenceTol ) {
				allGood = true;
			}
			sumDifference = 0;

			if( !selectTangent(tangentA0,tangentB0,ellipseB,tangentB0) )
				return false;
			if( !selectTangent(tangentA1,tangentB1,ellipseB,tangentB1) )
				return false;
			if( !selectTangent(tangentA2,tangentB2,ellipseB,tangentB2) )
				return false;
			if( !selectTangent(tangentA3,tangentB3,ellipseB,tangentB3) )
				return false;

			if( allGood && Math.sqrt(sumDifference)/4.0 < convergenceTol ) {
				break;
			}
		}

		converged = iteration < maxIterations;

		return true;
	}

	/**
	 * Select the initial tangent points on the ellipses.  This is done by:
	 *
	 * 1) picking an arbitrary point on ellipseA.
	 * 2) Find tangent points on B using point from step 1
	 * 3) Use those two tangent points on B to find 4 points on ellipse A
	 * 4) Use those 4 points to select 4 points on B.
	 */
	boolean initialize(EllipseRotated_F64 ellipseA, EllipseRotated_F64 ellipseB,
							   Point2D_F64 tangentA0, Point2D_F64 tangentA1,
							   Point2D_F64 tangentA2, Point2D_F64 tangentA3,
							   Point2D_F64 tangentB0, Point2D_F64 tangentB1,
							   Point2D_F64 tangentB2, Point2D_F64 tangentB3) {

		// arbitrary point on ellipseA
		UtilEllipse_F64.computePoint(0,ellipseA,tangentA0);

		if( !tangentLines(tangentA0,ellipseB,tangentB0,tangentB3) )
			return false;

		// Find the initial seed of 4 points on ellipse A
		if( !tangentLines(tangentB0,ellipseA,tangentA0,tangentA1) )
			return false;
		if( !tangentLines(tangentB3,ellipseA,tangentA2,tangentA3) )
			return false;

		// Find initial seed of 4 points on ellipse B
		if( !selectTangent(tangentA1,tangentB0,ellipseB,tangentB1))
			return false;
		if( !selectTangent(tangentA0,tangentB0,ellipseB,tangentB0))
			return false;
		if( !selectTangent(tangentA2,tangentB3,ellipseB,tangentB2))
			return false;

		return selectTangent(tangentA3, tangentB3, ellipseB, tangentB3);
	}

	/**
	 * Selects a tangent point on the ellipse which is closest to the original source point of A.
	 * @param a Point that the tangent lines pass through
	 * @param srcA Source point which generated 'a'
	 * @param ellipse Ellipse which the lines will be tangent to
	 * @param tangent (Output) Storage for the selected tangent point
	 * @return true if everythign went well or false if finding tangent lines barfed
	 */
	boolean selectTangent( Point2D_F64 a , Point2D_F64 srcA , EllipseRotated_F64 ellipse, Point2D_F64 tangent )
	{
		if( !tangentLines(a,ellipse,temp0,temp1) )
			return false;

		double d0 = srcA.distance2(temp0);
		double d1 = srcA.distance2(temp1);

		if( d0 < d1 ) {
			sumDifference += d0;
			tangent.set(temp0);
		} else {
			sumDifference += d1;
			tangent.set(temp1);
		}

		return true;
	}

	public boolean isConverged() {
		return converged;
	}

	public double getConvergenceTol() {
		return convergenceTol;
	}

	public void setConvergenceTol(double convergenceTol) {
		this.convergenceTol = convergenceTol;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
}

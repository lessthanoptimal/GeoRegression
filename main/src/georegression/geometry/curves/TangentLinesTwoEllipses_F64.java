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

package georegression.geometry.curves;

import georegression.geometry.UtilLine2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;

import static georegression.geometry.UtilEllipse_F64.tangentLines;

/**
 * <p>
 * Iterative algorithm for finding the 4 pairs of tangent lines between two ellipses. The ellipses are assumed
 * to not intersect. Line 0 and line 3 will not intersect the line joining the center of the two
 * ellipses while line 1 and 2 will.
 * </p>
 *
 * Algorithm: While a closed form solution does exist, it is very complex and an iterative solution is used
 * here instead.
 * <ol>
 *     <li>Initialize by finding four lines which are approximately tangent. Two will cross the center line
 *     and two will not. See code for details</li>
 *     <li>For each end point on a line find the two tangent points on the other ellipse. Keep the line
 *     which either crosses or does not cross the center line.</li>
 *     <li>Repeat unit the location of each end points does not change significantly or the maximum number of
 *     iterations has been exceeded</li>
 * </ol>
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

	LineSegment2D_F64 centerLine = new LineSegment2D_F64();

	// storage for local workspace
	LineSegment2D_F64 tempLine = new LineSegment2D_F64();
	LineGeneral2D_F64 lineGeneral = new LineGeneral2D_F64();
	Point2D_F64 junk = new Point2D_F64();


	/**
	 * Constructor that configures optimization parameters
	 *
	 *
	 * @param convergenceTol  Tolerance for when the iterations will stop. Try 1e-8 for doubles and 1e-4 for floats
	 * @param maxIterations Maximum number of iterations
	 */
	public TangentLinesTwoEllipses_F64(double convergenceTol,
									   int maxIterations) {
		this.convergenceTol = convergenceTol;
		this.maxIterations = maxIterations;
	}

	/**
	 * <p>Selects 4 pairs of points. Each point in the pair represents an end point in a line segment which is tangent
	 * to both ellipseA and ellipseB. Both ellipses are assumed to not intersect each other. If a fatal error
	 * occurs the function will return false. However it can return true and did not converge. To check for
	 * convergence call {@link #isConverged()}.</p>
	 *
	 * <p>Line 0 and line 3 will not intersect the line joining the center of the two ellipses while line 1 and 2 will.</p>
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

			if( !selectTangent(tangentA0,tangentB0,ellipseB,tangentB0, false) )
				return false;
			if( !selectTangent(tangentA1,tangentB1,ellipseB,tangentB1, true) )
				return false;
			if( !selectTangent(tangentA2,tangentB2,ellipseB,tangentB2, true) )
				return false;
			if( !selectTangent(tangentA3,tangentB3,ellipseB,tangentB3, false) )
				return false;

			if( Math.sqrt(sumDifference)/4.0 <= convergenceTol ) {
				allGood = true;
			}
			sumDifference = 0;

			if( !selectTangent(tangentB0,tangentA0,ellipseA,tangentA0, false) )
				return false;
			if( !selectTangent(tangentB1,tangentA1,ellipseA,tangentA1, true) )
				return false;
			if( !selectTangent(tangentB2,tangentA2,ellipseA,tangentA2, true) )
				return false;
			if( !selectTangent(tangentB3,tangentA3,ellipseA,tangentA3, false) )
				return false;

			if( allGood && Math.sqrt(sumDifference)/4.0 <= convergenceTol ) {
				break;
			}

		}

		converged = iteration < maxIterations;

		return true;
	}

	/**
	 * Select the initial tangent points on the ellipses. This is done by:
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

		centerLine.setTo(ellipseA.center,ellipseB.center);

		UtilLine2D_F64.convert(centerLine, lineGeneral);

		Intersection2D_F64.intersection(lineGeneral, ellipseA, temp0, temp1, -1);
		if (temp0.distance2(ellipseB.center) < temp1.distance2(ellipseB.center)) {
			tangentA0.setTo(temp0);
		} else {
			tangentA0.setTo(temp1);
		}

		// Two seed points for B. This points will be on two different sides of center line
		if( !tangentLines(tangentA0,ellipseB,tangentB0,tangentB1) )
			return false;

		// Find initial seed of 4 points on ellipse A. Careful which pairs of points cross or
		// don't cross the center line
		if( !selectTangent(tangentB0,tangentA0,ellipseA,tangentA0, false))
			return false;
		if( !selectTangent(tangentB0,tangentA0,ellipseA,tangentA1, true))
			return false;
		if( !selectTangent(tangentB1,tangentA0,ellipseA,tangentA2, true))
			return false;
		if( !selectTangent(tangentB1,tangentA0,ellipseA,tangentA3, false))
			return false;

		// not all of the B's have been initialized. That's ok. It will just have a large error
		// the first iteration

		return true;
	}

	/**
	 * Selects a tangent point on the ellipse which is closest to the original source point of A.
	 * @param a Point that the tangent lines pass through
	 * @param previousTangent Source point which generated 'a'
	 * @param ellipse Ellipse which the lines will be tangent to
	 * @param tangent (Output) Storage for the selected tangent point
	 * @return true if everything went well or false if finding tangent lines barfed
	 */
	boolean selectTangent( Point2D_F64 a , Point2D_F64 previousTangent ,
						   EllipseRotated_F64 ellipse, Point2D_F64 tangent ,
						   boolean cross )
	{
		if( !tangentLines(a,ellipse,temp0,temp1) )
			return false;

		tempLine.a = a;

		tempLine.b = temp0;
		boolean crossed0 = Intersection2D_F64.intersection(centerLine,tempLine,junk) != null;
		tempLine.b = temp1;
		boolean crossed1 = Intersection2D_F64.intersection(centerLine,tempLine,junk) != null;

		if( crossed0 == crossed1 )
			throw new RuntimeException("Well this didn't work");

		if( cross == crossed0 ) {
			sumDifference += previousTangent.distance2(temp0);
			tangent.setTo(temp0);
		} else {
			sumDifference += previousTangent.distance2(temp1);
			tangent.setTo(temp1);
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

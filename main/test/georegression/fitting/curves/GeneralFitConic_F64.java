/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.curves;

import georegression.fitting.FitShapeToPoints_F64;
import georegression.misc.GrlConstants;
import georegression.struct.curve.ConicGeneral_F64;
import georegression.struct.point.Point2D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public abstract class GeneralFitConic_F64 {

	public abstract FitShapeToPoints_F64<Point2D_F64,ConicGeneral_F64> createAlg();

	/**
	 * Fit to the minimum possible while testing several specific geometric configurations
	 */
	@Test
	void fit_points_3() {
		FitShapeToPoints_F64<Point2D_F64,ConicGeneral_F64> fitter = createAlg();
		ConicGeneral_F64 found = new ConicGeneral_F64();

		List<Point2D_F64> points = new ArrayList<>();

		points.add( new Point2D_F64(1,1));
		points.add( new Point2D_F64(4,2));
		points.add( new Point2D_F64(-1,8));

		assertTrue(fitter.process(points,found));
		assertFalse(found.hasUncountable());
		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}

		// Now see if it can fit a straight diagonal
		points.get(1).setTo(3,3);
		points.get(2).setTo(4,4);

		assertTrue(fitter.process(points,found));
		assertFalse(found.hasUncountable());
		// it's a line so a2 needs to be zero
		assertEquals(0,found.F,GrlConstants.TEST_F64); // goes through the origin
		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}

		// Now see if it can fit a a straight line along x-axis
		points.get(1).setTo(3,1);
		points.get(2).setTo(4,1);
		assertTrue(fitter.process(points,found));
		assertFalse(found.hasUncountable());
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}

		// Now see if it can fit a a straight line along y-axis
		points.get(1).setTo(1,3);
		points.get(2).setTo(1,4);
		assertTrue(fitter.process(points,found));
		assertFalse(found.hasUncountable());
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}
	}

	/**
	 * Same as above but with weights. Note that this really shouldn't change the results. Mostly a sanity check
	 * to see if the weights are completely messed up.
	 */
	@Test
	void fit_points_3_weighted() {
		double weights[] = {0.8,1.2,0.2};
		FitShapeToPoints_F64<Point2D_F64,ConicGeneral_F64> fitter = createAlg();
		ConicGeneral_F64 found = new ConicGeneral_F64();

		List<Point2D_F64> points = new ArrayList<>();

		points.add( new Point2D_F64(1,1));
		points.add( new Point2D_F64(4,2));
		points.add( new Point2D_F64(-1,8));

		assertTrue(fitter.process(points,weights,found));
		assertFalse(found.hasUncountable());
		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}

		// Now see if it can fit a straight diagonal
		points.get(1).setTo(3,3);
		points.get(2).setTo(4,4);

		assertTrue(fitter.process(points,weights,found));
		assertFalse(found.hasUncountable());
		// it's a line so a2 needs to be zero
		assertEquals(0,found.F,GrlConstants.TEST_F64); // goes through the origin
		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}

		// Now see if it can fit a a straight line along x-axis
		points.get(1).setTo(3,1);
		points.get(2).setTo(4,1);
		assertTrue(fitter.process(points,weights,found));
		assertFalse(found.hasUncountable());
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}

		// Now see if it can fit a a straight line along y-axis
		points.get(1).setTo(1,3);
		points.get(2).setTo(1,4);
		assertTrue(fitter.process(points,weights,found));
		assertFalse(found.hasUncountable());
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),GrlConstants.TEST_F64);
		}
	}
}

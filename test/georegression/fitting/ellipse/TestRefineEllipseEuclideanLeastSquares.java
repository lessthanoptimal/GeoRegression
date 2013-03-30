/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.fitting.ellipse;

import georegression.geometry.UtilEllipse_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;
import org.ddogleg.optimization.JacobianChecker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestRefineEllipseEuclideanLeastSquares {

	Random rand = new Random(234);

	@Test
	public void perfectEllipse() {
		checkPerfect(0,0,2,1,0);
		checkPerfect(1,-2,2,1,0);
		checkPerfect(0.5,3,2,1,0.1);
	}

	@Test
	public void perfectCircle() {
		checkPerfect(0,0,2,2,0);
		checkPerfect(1,-2,2,2,0);
		checkPerfect(0.5,3,2,2,0.1);
	}

	@Test
	public void perfectDataBadGuess() {
		EllipseRotated_F64 trueModel = new EllipseRotated_F64(-1,1.5,3,2,-0.3);

		checkIncorrect(-1, 1.5, 3, 2, -0.2, trueModel,false);
		checkIncorrect(-1, 1.5, 3, 2, -0.3, trueModel,false);
		checkIncorrect(-0.5,1.5,3,2,-0.3,trueModel,false);
		checkIncorrect(-1,2,3,2,-0.3,trueModel,false);
		checkIncorrect(-1,1.5,2.5,1.5,-0.3,trueModel,false);

		// test circle case
		trueModel = new EllipseRotated_F64(-1,1.5,2,2,-0.3);

		checkIncorrect(-0.5,2,1.5,2.5,-0.25,trueModel,true);
	}

	@Test
	public void noisyEllipse() {
		double sigma = 0.05;

		checkNoisy(0,0,2,1,0 , sigma);
		checkNoisy(1, -2, 2, 1, 0, sigma);
		checkNoisy(0.5, 3, 2, 1, 0.1, sigma);
	}

	public void checkPerfect( double x0 , double y0, double a, double b, double phi ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);

		List<Point2D_F64> points = new ArrayList<Point2D_F64>();
		for( int i = 0; i < 20; i++ ) {
			double theta = 2.0*(double)Math.PI*i/20;
			points.add(UtilEllipse_F64.computePoint(theta, rotated, null));
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		RefineEllipseEuclideanLeastSquares alg = new RefineEllipseEuclideanLeastSquares();

		assertTrue(alg.refine(rotated, points));

		EllipseRotated_F64 found = alg.getFound();

		assertEquals( rotated.center.x , found.center.x , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( rotated.center.y , found.center.y , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( rotated.a , found.a , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( rotated.b , found.b , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( rotated.phi , found.phi , GrlConstants.DOUBLE_TEST_TOL );
	}

	/**
	 * Perfect observations but crappy initial model
	 */
	public void checkIncorrect( double x0 , double y0, double a, double b, double phi , EllipseRotated_F64 trueModel ,
								boolean isCircle ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);

		List<Point2D_F64> points = new ArrayList<Point2D_F64>();
		for( int i = 0; i < 20; i++ ) {
			double theta = 2.0*(double)Math.PI*i/20;
			points.add(UtilEllipse_F64.computePoint(theta, trueModel, null));
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		RefineEllipseEuclideanLeastSquares alg = new RefineEllipseEuclideanLeastSquares();

		assertTrue(alg.refine(rotated, points));

		EllipseRotated_F64 found = alg.getFound();

		assertEquals( trueModel.center.x , found.center.x , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( trueModel.center.y , found.center.y , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( trueModel.a , found.a , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( trueModel.b , found.b , GrlConstants.DOUBLE_TEST_TOL );
		if( !isCircle )
			assertEquals( trueModel.phi , found.phi , GrlConstants.DOUBLE_TEST_TOL );
	}

	public void checkNoisy( double x0 , double y0, double a, double b, double phi , double sigma ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);

		List<Point2D_F64> points = new ArrayList<Point2D_F64>();
		for( int i = 0; i < 20; i++ ) {
			double theta = 2.0*(double)Math.PI*i/20;
			Point2D_F64 p = UtilEllipse_F64.computePoint(theta, rotated, null);
			p.x += rand.nextGaussian()*sigma;
			p.y += rand.nextGaussian()*sigma;

			points.add(p);
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		RefineEllipseEuclideanLeastSquares alg = new RefineEllipseEuclideanLeastSquares();

		assertTrue(alg.refine(rotated, points));

		double after = alg.optimizer.getFunctionValue();
		assertTrue(after<alg.initialError);
	}

	@Test
	public void checkJacobian() {
		EllipseRotated_F64 model = new EllipseRotated_F64(1,2,3,2,0.1);

		List<Point2D_F64> points = new ArrayList<Point2D_F64>();
		for( int i = 0; i < 20; i++ ) {
			double theta = 2.0*(double)Math.PI*i/20;
			points.add(UtilEllipse_F64.computePoint(theta, model, null));
		}

		model = new EllipseRotated_F64(0.5,2.1,2.9,1.5,0.15);

		RefineEllipseEuclideanLeastSquares alg = new RefineEllipseEuclideanLeastSquares();

		alg.refine(model,points);

		RefineEllipseEuclideanLeastSquares.Error error = alg.createError();
		RefineEllipseEuclideanLeastSquares.Jacobian jacobian = alg.createJacobian();

		JacobianChecker.jacobian(error,jacobian,alg.initialParam,1e-5);
	}

}

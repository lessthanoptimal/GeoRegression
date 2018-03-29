/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilEllipse_F32;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseRotated_F32;
import georegression.struct.point.Point2D_F32;
import org.ddogleg.optimization.DerivativeChecker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestRefineEllipseEuclideanLeastSquares_F32 {

	Random rand = new Random(234);

	@Test
	public void perfectEllipse() {
		checkPerfect(0,0,2,1,0);
		checkPerfect(1,-2,2,1,0);
		checkPerfect(0.5f,3,2,1,0.1f);
	}

	@Test
	public void perfectCircle() {
		checkPerfect(0,0,2,2,0);
		checkPerfect(1,-2,2,2,0);
		checkPerfect(0.5f,3,2,2,0.1f);
	}

	@Test
	public void perfectDataBadGuess() {
		EllipseRotated_F32 trueModel = new EllipseRotated_F32(-1,1.5f,3,2,-0.3f);

		checkIncorrect(-1, 1.5f, 3, 2, -0.2f, trueModel,false);
		checkIncorrect(-1, 1.5f, 3, 2, -0.3f, trueModel,false);
		checkIncorrect(-0.5f,1.5f,3,2,-0.3f,trueModel,false);
		checkIncorrect(-1,2,3,2,-0.3f,trueModel,false);
		checkIncorrect(-1,1.5f,2.5f,1.5f,-0.3f,trueModel,false);

		// test circle case
		trueModel = new EllipseRotated_F32(-1,1.5f,2,2,-0.3f);

		checkIncorrect(-0.5f,2,1.5f,2.5f,-0.25f,trueModel,true);
	}

	@Test
	public void noisyEllipse() {
		float sigma = 0.05f;

		checkNoisy(0,0,2,1,0 , sigma);
		checkNoisy(1, -2, 2, 1, 0, sigma);
		checkNoisy(0.5f, 3, 2, 1, 0.1f, sigma);
	}

	public void checkPerfect( float x0 , float y0, float a, float b, float phi ) {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(x0,y0,a,b,phi);

		List<Point2D_F32> points = new ArrayList<Point2D_F32>();
		for( int i = 0; i < 20; i++ ) {
			float theta = 2.0f*(float)Math.PI*i/20;
			points.add(UtilEllipse_F32.computePoint(theta, rotated, null));
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		RefineEllipseEuclideanLeastSquares_F32 alg = new RefineEllipseEuclideanLeastSquares_F32();

		assertTrue(alg.refine(rotated, points));

		EllipseRotated_F32 found = alg.getFound();

		assertEquals( rotated.center.x , found.center.x , GrlConstants.TEST_F32);
		assertEquals( rotated.center.y , found.center.y , GrlConstants.TEST_F32);
		assertEquals( rotated.a , found.a , GrlConstants.TEST_F32);
		assertEquals( rotated.b , found.b , GrlConstants.TEST_F32);
		assertEquals( rotated.phi , found.phi , GrlConstants.TEST_F32);
	}

	/**
	 * Perfect observations but crappy initial model
	 */
	public void checkIncorrect( float x0 , float y0, float a, float b, float phi , EllipseRotated_F32 trueModel ,
								boolean isCircle ) {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(x0,y0,a,b,phi);

		List<Point2D_F32> points = new ArrayList<Point2D_F32>();
		for( int i = 0; i < 20; i++ ) {
			float theta = 2.0f*(float)Math.PI*i/20;

			// well naerly perfect data to avoid numerical instability
			Point2D_F32 p = UtilEllipse_F32.computePoint(theta, trueModel, null);

			// give it just a little bit of noise so that it will converge
			p.x += (float)rand.nextGaussian()*GrlConstants.TEST_F32;
			p.y += (float)rand.nextGaussian()*GrlConstants.TEST_F32;

			points.add(p);
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		RefineEllipseEuclideanLeastSquares_F32 alg = new RefineEllipseEuclideanLeastSquares_F32();

		assertTrue(alg.refine(rotated, points));

		EllipseRotated_F32 found = alg.getFound();

		assertEquals( trueModel.center.x , found.center.x , GrlConstants.TEST_F32);
		assertEquals( trueModel.center.y , found.center.y , GrlConstants.TEST_F32);
		assertEquals( trueModel.a , found.a , GrlConstants.TEST_F32);
		assertEquals( trueModel.b , found.b , GrlConstants.TEST_F32);
		if( !isCircle )
			assertEquals( trueModel.phi , found.phi , GrlConstants.TEST_F32);
	}

	public void checkNoisy( float x0 , float y0, float a, float b, float phi , float sigma ) {
		EllipseRotated_F32 rotated = new EllipseRotated_F32(x0,y0,a,b,phi);

		List<Point2D_F32> points = new ArrayList<Point2D_F32>();
		for( int i = 0; i < 20; i++ ) {
			float theta = 2.0f*(float)Math.PI*i/20;
			Point2D_F32 p = UtilEllipse_F32.computePoint(theta, rotated, null);
			p.x += (float)rand.nextGaussian()*sigma;
			p.y += (float)rand.nextGaussian()*sigma;

			points.add(p);
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		RefineEllipseEuclideanLeastSquares_F32 alg = new RefineEllipseEuclideanLeastSquares_F32();

		assertTrue(alg.refine(rotated, points));

		/**/double after = alg.optimizer.getFunctionValue();
		assertTrue(after<alg.initialError);
	}

	@Test
	public void checkJacobian() {
		EllipseRotated_F32 model = new EllipseRotated_F32(1,2,3,2,0.1f);

		List<Point2D_F32> points = new ArrayList<Point2D_F32>();
		for( int i = 0; i < 20; i++ ) {
			float theta = 2.0f*(float)Math.PI*i/20;
			points.add(UtilEllipse_F32.computePoint(theta, model, null));
		}

		model = new EllipseRotated_F32(0.5f,2.1f,2.9f,1.5f,0.15f);

		RefineEllipseEuclideanLeastSquares_F32 alg = new RefineEllipseEuclideanLeastSquares_F32();

		alg.refine(model,points);

		RefineEllipseEuclideanLeastSquares_F32.Error error = alg.createError();
		RefineEllipseEuclideanLeastSquares_F32.Jacobian jacobian = alg.createJacobian();

		DerivativeChecker.jacobian(error,jacobian,alg.initialParam,GrlConstants.TEST_SQ_F32);
	}

}

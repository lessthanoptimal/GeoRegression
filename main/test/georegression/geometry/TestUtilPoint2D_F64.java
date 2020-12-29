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

package georegression.geometry;

import georegression.metric.Intersection2D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import georegression.struct.shapes.RectangleLength2D_F64;
import org.ejml.data.DMatrix2x2;
import org.ejml.dense.fixed.CommonOps_DDF2;
import org.ejml.dense.fixed.NormOps_DDF2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint2D_F64 {

	Random rand = new Random(234);

	@Test void findClosestIdx() {
		List<Point2D_F64> list = new ArrayList<>();
		assertEquals(-1, UtilPoint2D_F64.findClosestIdx(1,2,list,2));

		list.add(new Point2D_F64(5,2));
		assertEquals(-1, UtilPoint2D_F64.findClosestIdx(1,2,list,2));
		assertEquals(0, UtilPoint2D_F64.findClosestIdx(1,2,list,4));

		list.add(new Point2D_F64(5,2));
		list.add(new Point2D_F64(6,2));
		list.add(new Point2D_F64(8,2));

		assertEquals(2, UtilPoint2D_F64.findClosestIdx(6,2.1,list,2));
		assertEquals(-1, UtilPoint2D_F64.findClosestIdx(100,2,list,2));
	}

	@Test
	void noiseNormal_single() {
		Point2D_F64 mean = new Point2D_F64(3,4);
		double sx=1,sy=0.5;

		List<Point2D_F64> points = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			points.add( UtilPoint2D_F64.noiseNormal(mean,sx,sy,rand,null));
		}

		Point2D_F64 found = UtilPoint2D_F64.mean(points,null);

		assertEquals(mean.x,found.x,0.01);
		assertEquals(mean.y,found.y,0.01);

		double stdevX=0,stdevY=0;

		for (int i = 0; i < points.size(); i++) {
			Point2D_F64 p = points.get(i);
			double dx = p.x-found.x;
			double dy = p.y-found.y;

			stdevX += dx*dx;
			stdevY += dy*dy;
		}

		assertEquals(sx,Math.sqrt(stdevX/points.size()),sx/20);
		assertEquals(sy,Math.sqrt(stdevY/points.size()),sy/20);
	}

	@Test
	void mean_list() {
		List<Point2D_F64> list = new ArrayList<Point2D_F64>();

		double X=0,Y=0;
		for( int i = 0; i < 20; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			X += p.x = rand.nextDouble()*100-50;
			Y += p.y = rand.nextDouble()*100-50;

			list.add(p);
		}

		Point2D_F64 found = UtilPoint2D_F64.mean(list, null);

		assertEquals(X/20, found.x , GrlConstants.TEST_F64);
		assertEquals(Y/20, found.y , GrlConstants.TEST_F64);
	}

	@Test
	void mean_array() {
		Point2D_F64[] list = new Point2D_F64[20];

		double X=0,Y=0;
		for( int i = 0; i < list.length; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			X += p.x = rand.nextDouble()*100-50;
			Y += p.y = rand.nextDouble()*100-50;

			list[i] = p;
		}

		Point2D_F64 found = UtilPoint2D_F64.mean(list,0,list.length, null);

		assertEquals(X/20, found.x , GrlConstants.TEST_F64);
		assertEquals(Y/20, found.y , GrlConstants.TEST_F64);
	}

	@Test
	void mean_2pt() {
		Point2D_F64 a = new Point2D_F64(3,8);
		Point2D_F64 b = new Point2D_F64(-4,7.8);

		Point2D_F64 ave = new Point2D_F64();

		UtilPoint2D_F64.mean(a,b,ave);

		assertEquals((a.x+b.x)/2.0, ave.x , GrlConstants.TEST_F64);
		assertEquals((a.y+b.y)/2.0, ave.y , GrlConstants.TEST_F64);
	}

	@Test
	void bounding_length() {
		List<Point2D_F64> list = new ArrayList<Point2D_F64>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			p.x = rand.nextDouble()*100-50;
			p.y = rand.nextDouble()*100-50;

			list.add(p);
		}

		RectangleLength2D_F64 bounding = UtilPoint2D_F64.bounding(list,(RectangleLength2D_F64)null);

		for( int i = 0; i < list.size(); i++ ) {
			Point2D_F64 p = list.get(i);

			assertTrue(Intersection2D_F64.contains2(bounding, p.x, p.y));
		}
	}

	@Test
	void bounding() {
		List<Point2D_F64> list = new ArrayList<Point2D_F64>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			p.x = rand.nextDouble()*100-50;
			p.y = rand.nextDouble()*100-50;

			list.add(p);
		}

		Rectangle2D_F64 bounding = UtilPoint2D_F64.bounding(list,(Rectangle2D_F64)null);

		for( int i = 0; i < list.size(); i++ ) {
			Point2D_F64 p = list.get(i);

			assertTrue(Intersection2D_F64.contains2(bounding, p.x, p.y));
		}
	}

	@Test
	void orderCCW() {
		List<Point2D_F64> input = new ArrayList<Point2D_F64>();
		input.add(new Point2D_F64(2,-3));
		input.add(new Point2D_F64(2,1));
		input.add(new Point2D_F64(-2,-3));
		input.add(new Point2D_F64(-2,1));

		List<Point2D_F64> found = UtilPoint2D_F64.orderCCW(input);

		assertTrue(found.get(0) == input.get(2));
		assertTrue(found.get(1) == input.get(0));
		assertTrue(found.get(2) == input.get(1));
		assertTrue(found.get(3) == input.get(3));
	}

	@Test
	void create_and_compute_normal2D() {
		Point2D_F64 mean = new Point2D_F64(-9,4);
		DMatrix2x2 covar = new DMatrix2x2(2,0.1,0.1,1.5);

		List<Point2D_F64> list = UtilPoint2D_F64.randomNorm(mean,covar,5000,rand,null);

		Point2D_F64 foundMean = new Point2D_F64();
		DMatrix2x2 foundCovar = new DMatrix2x2();

		UtilPoint2D_F64.computeNormal(list,foundMean,foundCovar);

		assertEquals(0.0,foundMean.distance(mean), 0.05);

		DMatrix2x2 difference = new DMatrix2x2();
		CommonOps_DDF2.subtract(covar,foundCovar,difference);
		assertEquals(0,NormOps_DDF2.normF(difference),0.1);
	}

}

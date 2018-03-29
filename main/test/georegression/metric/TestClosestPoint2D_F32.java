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

package georegression.metric;

import georegression.fitting.curves.ClosestPointEllipseAngle_F32;
import georegression.geometry.UtilLine2D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseRotated_F32;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestClosestPoint2D_F32 {

	@Test
	public void closestPoint_general() {
		LineParametric2D_F32 line = new LineParametric2D_F32( 1, 2, -1, 1 );
		LineGeneral2D_F32 general = UtilLine2D_F32.convert(line,(LineGeneral2D_F32)null);
		Point2D_F32 pt = new Point2D_F32( 1, 0 );

		Point2D_F32 found = ClosestPoint2D_F32.closestPoint( general, pt, null );
		assertEquals( 2, found.getX(), GrlConstants.TEST_F32);
		assertEquals( 1, found.getY(), GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_parametric() {
		LineParametric2D_F32 line = new LineParametric2D_F32( 1, 2, -1, 1 );
		Point2D_F32 pt = new Point2D_F32( 1, 0 );

		Point2D_F32 found = ClosestPoint2D_F32.closestPoint( line, pt, null );
		assertEquals( 2, found.getX(), GrlConstants.TEST_F32);
		assertEquals( 1, found.getY(), GrlConstants.TEST_F32);
	}

	@Test
	public void closestPointT_parametric() {
		LineParametric2D_F32 line = new LineParametric2D_F32( 1, 2, -1, 1 );
		Point2D_F32 pt = new Point2D_F32( 1, 0 );

		float found = ClosestPoint2D_F32.closestPointT( line, pt );
		assertEquals( -1, found, GrlConstants.TEST_F32);
	}

	@Test
	public void closestPointT_parametric_xy() {
		LineParametric2D_F32 line = new LineParametric2D_F32( 1, 2, -1, 1 );

		float found = ClosestPoint2D_F32.closestPointT( line, 1,0 );
		assertEquals( -1, found, GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_LS() {
		LineSegment2D_F32 ls; Point2D_F32 found;
		// test middle
		ls = new LineSegment2D_F32(1,2,4,5);
		found = ClosestPoint2D_F32.closestPoint(ls,new Point2D_F32(2-0.1f,3+0.1f),null);
		assertEquals(2,found.x,GrlConstants.TEST_F32);
		assertEquals(3,found.y,GrlConstants.TEST_F32);


		// test before a
		found = ClosestPoint2D_F32.closestPoint(ls,new Point2D_F32(0,0),null);
		assertEquals(1,found.x,GrlConstants.TEST_F32);
		assertEquals(2,found.y,GrlConstants.TEST_F32);

		// test after b
		found = ClosestPoint2D_F32.closestPoint(ls,new Point2D_F32(10,20),null);
		assertEquals(4,found.x,GrlConstants.TEST_F32);
		assertEquals(5,found.y,GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_ellipse() {
		EllipseRotated_F32 ellipse = new EllipseRotated_F32(1,2,3,2,0.1f);

		Point2D_F32 p = new Point2D_F32(6,7);

		Point2D_F32 found = ClosestPoint2D_F32.closestPoint(ellipse,p);

		// compare to a known algorithm
		ClosestPointEllipseAngle_F32 alg = new ClosestPointEllipseAngle_F32( GrlConstants.TEST_F32, 100 );
		alg.setEllipse(ellipse);
		alg.process(p);

		assertEquals(alg.getClosest().x,found.x,GrlConstants.TEST_F32);
		assertEquals(alg.getClosest().y,found.y,GrlConstants.TEST_F32);
	}
}

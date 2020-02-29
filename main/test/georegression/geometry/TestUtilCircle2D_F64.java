/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.trig.Circle2D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilCircle2D_F64 {

	@Test
	void evaluate() {
		Circle2D_F64 circle = new Circle2D_F64(5,2,3);

		assertEquals(0,UtilCircle2D_F64.evaluate(2-5,3,circle),GrlConstants.TEST_F64);
		assertEquals(0,UtilCircle2D_F64.evaluate(2+5,3,circle),GrlConstants.TEST_F64);
		assertEquals(0,UtilCircle2D_F64.evaluate(2,3-5,circle),GrlConstants.TEST_F64);
		assertEquals(0,UtilCircle2D_F64.evaluate(2,3+5,circle),GrlConstants.TEST_F64);
	}

	@Test
	void circle_3pts() {
		Point2D_F64 x0 = new Point2D_F64(5,4);
		Point2D_F64 x1 = new Point2D_F64(3,8);
		Point2D_F64 x2 = new Point2D_F64(-3,-1);

		Circle2D_F64 circle = new Circle2D_F64();

		UtilCircle2D_F64.circle(x0,x1,x2,circle);

		assertEquals(0,UtilCircle2D_F64.evaluate(x0.x,x0.y,circle), GrlConstants.TEST_SQ_F64);
		assertEquals(0,UtilCircle2D_F64.evaluate(x1.x,x1.y,circle), GrlConstants.TEST_SQ_F64);
		assertEquals(0,UtilCircle2D_F64.evaluate(x2.x,x2.y,circle), GrlConstants.TEST_SQ_F64);
	}

	@Test
	void circleRadiusSq_3pts() {
		Point2D_F64 x0 = new Point2D_F64(5,4);
		Point2D_F64 x1 = new Point2D_F64(3,8);
		Point2D_F64 x2 = new Point2D_F64(-3,-1);

		Circle2D_F64 circle = new Circle2D_F64();

		UtilCircle2D_F64.circle(x0,x1,x2,circle);

		double expected = circle.radius*circle.radius;

		double found = UtilCircle2D_F64.circleRadiusSq(x0,x1,x2);

		assertEquals(expected,found, GrlConstants.TEST_SQ_F64);
	}
}
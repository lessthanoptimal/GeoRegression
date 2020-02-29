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

package georegression.transform.affine;

import georegression.misc.GrlConstants;
import georegression.struct.affine.Affine2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestAffinePointOps_F64 {
	@Test
	void transform_vector() {
		Affine2D_F64 transform = new Affine2D_F64(1,2,3,4,5,6);
		Vector2D_F64 input = new Vector2D_F64(2,3);

		double expectedX = transform.a11*input.x + transform.a12*input.y;
		double expectedY = transform.a21*input.x + transform.a22*input.y;

		Vector2D_F64 found = AffinePointOps_F64.transform(transform,input,(Vector2D_F64)null);

		assertEquals(expectedX,found.x, GrlConstants.TEST_F64);
		assertEquals(expectedY,found.y, GrlConstants.TEST_F64);
	}

	@Test
	void transform_point() {
		Affine2D_F64 transform = new Affine2D_F64(1,2,3,4,5,6);
		Point2D_F64 input = new Point2D_F64(2,3);

		double expectedX = transform.a11*input.x + transform.a12*input.y + transform.tx;
		double expectedY = transform.a21*input.x + transform.a22*input.y + transform.ty;

		Point2D_F64 found = AffinePointOps_F64.transform(transform,input,(Point2D_F64)null);

		assertEquals(expectedX,found.x, GrlConstants.TEST_F64);
		assertEquals(expectedY, found.y, GrlConstants.TEST_F64);
	}

	@Test
	void transform_point_xy() {
		Affine2D_F64 transform = new Affine2D_F64(1,2,3,4,5,6);
		double x = 2;
		double y = 3;

		double expectedX = transform.a11*x + transform.a12*y + transform.tx;
		double expectedY = transform.a21*x + transform.a22*y + transform.ty;

		Point2D_F64 found = AffinePointOps_F64.transform(transform,x,y,(Point2D_F64)null);

		assertEquals(expectedX,found.x, GrlConstants.TEST_F64);
		assertEquals(expectedY, found.y, GrlConstants.TEST_F64);
	}
}

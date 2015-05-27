/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

package georegression.transform.homography;

import georegression.geometry.GeometryMath_F32;
import georegression.misc.GrlConstants;
import georegression.struct.homography.Homography2D_F32;
import georegression.struct.homography.UtilHomography;
import georegression.struct.point.Point2D_F32;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestHomographyPointOps_F32 {

	DenseMatrix64F M = new DenseMatrix64F(3,3);
	Homography2D_F32 tran = new Homography2D_F32();

	public TestHomographyPointOps_F32() {
		Random rand = new Random(234);
		
		tran.a11 = (float)rand.nextGaussian();
		tran.a12 = (float)rand.nextGaussian();
		tran.a13 = (float)rand.nextGaussian();
		tran.a21 = (float)rand.nextGaussian();
		tran.a22 = (float)rand.nextGaussian();
		tran.a23 = (float)rand.nextGaussian();
		tran.a31 = (float)rand.nextGaussian();
		tran.a32 = (float)rand.nextGaussian();
		tran.a33 = (float)rand.nextGaussian();

		UtilHomography.convert(tran,M);
	}

	@Test
	public void transform_Point_F32() {
		Point2D_F32 src = new Point2D_F32(1,2);
		Point2D_F32 dst = new Point2D_F32();
		Point2D_F32 expected = new Point2D_F32();

		HomographyPointOps_F32.transform(tran, src, dst);

		GeometryMath_F32.mult(M,src,expected);
		
		assertEquals(expected.x,dst.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.y,dst.y, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void transform_DD_F32() {
		Point2D_F32 src = new Point2D_F32(1,2);
		Point2D_F32 dst = new Point2D_F32();
		Point2D_F32 expected = new Point2D_F32();

		HomographyPointOps_F32.transform(tran, 1, 2, dst);

		GeometryMath_F32.mult(M,src,expected);

		assertEquals(expected.x,dst.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.y,dst.y, GrlConstants.FLOAT_TEST_TOL);
	}
}

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

package georegression.transform;

import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.affine.Affine2D_F64;
import georegression.struct.homography.Homography2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.affine.AffinePointOps_F64;
import georegression.transform.homography.HomographyPointOps_F64;
import georegression.transform.se.SePointOps_F64;
import org.junit.jupiter.api.Test;


/**
 * @author Peter Abeles
 */
public class TestConvertTransform_F64 {
	@Test
	void Se_To_Affine_2D() {
		Se2_F64 a = new Se2_F64(2,3,0.5);
		Affine2D_F64 b = ConvertTransform_F64.convert(a,new Affine2D_F64());

		Point2D_F64 pt = new Point2D_F64(3,4);
		Point2D_F64 expected = SePointOps_F64.transform(a,pt,null);
		Point2D_F64 found = AffinePointOps_F64.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected,found, GrlConstants.TEST_F64);
	}

	@Test
	void Se_To_Homography_2D() {
		Se2_F64 a = new Se2_F64(2,3,0.5);
		Homography2D_F64 b = ConvertTransform_F64.convert(a,new Homography2D_F64());

		Point2D_F64 pt = new Point2D_F64(3,4);
		Point2D_F64 expected = SePointOps_F64.transform(a,pt,null);
		Point2D_F64 found = HomographyPointOps_F64.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.TEST_F64);
	}

	@Test
	void Affine_To_Homography_2D() {
		Affine2D_F64 a = new Affine2D_F64(1,2,3,4,5,6);
		Homography2D_F64 b = ConvertTransform_F64.convert(a,new Homography2D_F64());

		Point2D_F64 pt = new Point2D_F64(3,4);
		Point2D_F64 expected = AffinePointOps_F64.transform(a, pt, null);
		Point2D_F64 found = HomographyPointOps_F64.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.TEST_F64);
	}
}

/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.transform;

import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.affine.Affine2D_F32;
import georegression.struct.homo.Homography2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.se.Se2_F32;
import georegression.transform.affine.AffinePointOps;
import georegression.transform.homo.HomographyPointOps_F32;
import georegression.transform.se.SePointOps_F32;
import org.junit.Test;


/**
 * @author Peter Abeles
 */
public class TestConvertTransform_F32 {
	@Test
	public void Se_To_Affine_2D() {
		Se2_F32 a = new Se2_F32(2,3,0.5f);
		Affine2D_F32 b = ConvertTransform_F32.convert(a,new Affine2D_F32());

		Point2D_F32 pt = new Point2D_F32(3,4);
		Point2D_F32 expected = SePointOps_F32.transform(a,pt,null);
		Point2D_F32 found = AffinePointOps.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected,found, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void Se_To_Homography_2D() {
		Se2_F32 a = new Se2_F32(2,3,0.5f);
		Homography2D_F32 b = ConvertTransform_F32.convert(a,new Homography2D_F32());

		Point2D_F32 pt = new Point2D_F32(3,4);
		Point2D_F32 expected = SePointOps_F32.transform(a,pt,null);
		Point2D_F32 found = HomographyPointOps_F32.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void Affine_To_Homography_2D() {
		Affine2D_F32 a = new Affine2D_F32(1,2,3,4,5,6);
		Homography2D_F32 b = ConvertTransform_F32.convert(a,new Homography2D_F32());

		Point2D_F32 pt = new Point2D_F32(3,4);
		Point2D_F32 expected = AffinePointOps.transform(a,pt,null);
		Point2D_F32 found = HomographyPointOps_F32.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.FLOAT_TEST_TOL);
	}
}

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
import georegression.struct.affine.Affine2D_F64;
import georegression.struct.homo.Homography2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.affine.AffinePointOps;
import georegression.transform.homo.HomographyPointOps_F64;
import georegression.transform.se.SePointOps_F64;
import org.junit.Test;


/**
 * @author Peter Abeles
 */
public class TestConvertTransform_F64 {
	@Test
	public void Se_To_Affine_2D() {
		Se2_F64 a = new Se2_F64(2,3,0.5);
		Affine2D_F64 b = ConvertTransform_F64.convert(a,new Affine2D_F64());

		Point2D_F64 pt = new Point2D_F64(3,4);
		Point2D_F64 expected = SePointOps_F64.transform(a,pt,null);
		Point2D_F64 found = AffinePointOps.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected,found, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void Se_To_Homography_2D() {
		Se2_F64 a = new Se2_F64(2,3,0.5);
		Homography2D_F64 b = ConvertTransform_F64.convert(a,new Homography2D_F64());

		Point2D_F64 pt = new Point2D_F64(3,4);
		Point2D_F64 expected = SePointOps_F64.transform(a,pt,null);
		Point2D_F64 found = HomographyPointOps_F64.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void Affine_To_Homography_2D() {
		Affine2D_F64 a = new Affine2D_F64(1,2,3,4,5,6);
		Homography2D_F64 b = ConvertTransform_F64.convert(a,new Homography2D_F64());

		Point2D_F64 pt = new Point2D_F64(3,4);
		Point2D_F64 expected = AffinePointOps.transform(a,pt,null);
		Point2D_F64 found = HomographyPointOps_F64.transform(b, pt, null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.DOUBLE_TEST_TOL);
	}
}

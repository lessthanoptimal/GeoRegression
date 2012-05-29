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

package georegression.transform.homo;

import georegression.geometry.GeometryMath_F32;
import georegression.geometry.GeometryMath_F64;
import georegression.struct.homo.Homography2D_F32;
import georegression.struct.homo.Homography2D_F64;
import georegression.struct.homo.UtilHomography;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_F64;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestHomographyPointOps {

	DenseMatrix64F M = new DenseMatrix64F(3,3);
	Homography2D_F64 tran_F64 = new Homography2D_F64();
	Homography2D_F32 tran_F32 = new Homography2D_F32();

	public TestHomographyPointOps() {
		Random rand = new Random(234);
		
		tran_F64.a11 = rand.nextGaussian();
		tran_F64.a12 = rand.nextGaussian();
		tran_F64.a13 = rand.nextGaussian();
		tran_F64.a21 = rand.nextGaussian();
		tran_F64.a22 = rand.nextGaussian();
		tran_F64.a23 = rand.nextGaussian();
		tran_F64.a31 = rand.nextGaussian();
		tran_F64.a32 = rand.nextGaussian();
		tran_F64.a33 = rand.nextGaussian();

		UtilHomography.convert(tran_F64,M);
		UtilHomography.convert(tran_F64,tran_F32);
	}

	@Test
	public void transform_Point_F64() {
		Point2D_F64 src = new Point2D_F64(1,2);
		Point2D_F64 dst = new Point2D_F64();
		Point2D_F64 expected = new Point2D_F64();

		HomographyPointOps.transform(tran_F64,src,dst);

		GeometryMath_F64.mult(M,src,expected);
		
		assertEquals(expected.x,dst.x,1e-8);
		assertEquals(expected.y,dst.y,1e-8);
	}

	@Test
	public void transform_DD_F64() {
		Point2D_F64 src = new Point2D_F64(1,2);
		Point2D_F64 dst = new Point2D_F64();
		Point2D_F64 expected = new Point2D_F64();

		HomographyPointOps.transform(tran_F64,1,2,dst);

		GeometryMath_F64.mult(M,src,expected);

		assertEquals(expected.x,dst.x,1e-8);
		assertEquals(expected.y,dst.y,1e-8);
	}

	@Test
	public void transform_Point_F32() {
		Point2D_F32 src = new Point2D_F32(1,2);
		Point2D_F32 dst = new Point2D_F32();
		Point2D_F32 expected = new Point2D_F32();

		HomographyPointOps.transform(tran_F32,src,dst);

		GeometryMath_F32.mult(M, src, expected);

		assertEquals(expected.x,dst.x,1e-4);
		assertEquals(expected.y,dst.y,1e-4);
	}

	@Test
	public void transform_FF_F32() {
		Point2D_F32 src = new Point2D_F32(1,2);
		Point2D_F32 dst = new Point2D_F32();
		Point2D_F32 expected = new Point2D_F32();

		HomographyPointOps.transform(tran_F32,1,2,dst);

		GeometryMath_F32.mult(M, src, expected);

		assertEquals(expected.x,dst.x,1e-4);
		assertEquals(expected.y,dst.y,1e-4);
	}
}

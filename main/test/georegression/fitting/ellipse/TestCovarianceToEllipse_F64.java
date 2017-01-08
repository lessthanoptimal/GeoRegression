/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.ellipse;

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import org.ejml.data.RowMatrix_F64;
import org.ejml.ops.CommonOps_R64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestCovarianceToEllipse_F64 {
	@Test
	public void noRotation() {

		CovarianceToEllipse_F64 alg = new CovarianceToEllipse_F64();

		alg.setCovariance(100,0,9);

		assertEquals(10,alg.getMajorAxis(), GrlConstants.TEST_F64);
		assertEquals(3 ,alg.getMinorAxis(), GrlConstants.TEST_F64);
		assertEquals(0,alg.getAngle(), GrlConstants.TEST_F64);

		alg.setCovariance(9,0,100);
		assertEquals(10,alg.getMajorAxis(), GrlConstants.TEST_F64);
		assertEquals(3 ,alg.getMinorAxis(), GrlConstants.TEST_F64);
		assertEquals(Math.PI/2.0,alg.getAngle(), GrlConstants.TEST_F64);
	}

	@Test
	public void rotation() {

		CovarianceToEllipse_F64 alg = new CovarianceToEllipse_F64();

		for (int i = 0; i < 10; i++) {
			double angle = (double)(0.5*Math.PI*i/9.0);

			double c = Math.cos(angle);
			double s = Math.sin(angle);

			RowMatrix_F64 Q = new RowMatrix_F64(2,2,true,100,0,0,9);
			RowMatrix_F64 R = new RowMatrix_F64(2,2,true,c,-s,s,c);
			RowMatrix_F64 QR = new RowMatrix_F64(2,2);

			CommonOps_R64.mult(R,Q,QR);
			CommonOps_R64.multTransB(QR,R,Q);

			alg.setCovariance((double)Q.get(0,0),(double)Q.get(0,1),(double)Q.get(1,1));

			assertEquals(10,alg.getMajorAxis(), GrlConstants.TEST_F64);
			assertEquals(3 ,alg.getMinorAxis(), GrlConstants.TEST_F64);
			assertEquals(0, UtilAngle.distHalf(angle,alg.getAngle()), GrlConstants.TEST_F64);
		}
	}
}
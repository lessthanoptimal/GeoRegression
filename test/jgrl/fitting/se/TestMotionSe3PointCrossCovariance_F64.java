/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.fitting.se;

import jgrl.autocode.JgrlConstants;
import jgrl.geometry.RotationMatrixGenerator;
import jgrl.struct.point.Point3D_F64;
import jgrl.struct.point.UtilPoint3D_F64;
import jgrl.struct.point.Vector3D_F64;
import jgrl.struct.se.Se3_F64;
import jgrl.test.GeometryUnitTest;
import jgrl.transform.se.SePointOps_F64;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestMotionSe3PointCrossCovariance_F64 {

	Random rand = new Random(434324);

	@Test
	public void noiseless() {
		DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ(0.1, -0.5, 2.3, null);
		Vector3D_F64 T = new Vector3D_F64(5, 10, -6);

		Se3_F64 tran = new Se3_F64(R, T);

		List<Point3D_F64> from = UtilPoint3D_F64.random(-10, 10, 30, rand);
		List<Point3D_F64> to = new ArrayList<Point3D_F64>();
		for (Point3D_F64 p : from) {
			to.add(SePointOps_F64.transform(tran, p, null));
		}

		MotionSe3PointCrossCovariance_F64 alg = new MotionSe3PointCrossCovariance_F64();

		assertTrue(alg.process(from, to));

//        R.print();
//        foundR.print();
//        T.print();
//        foundT.print();

		Se3_F64 tranFound = alg.getMotion();

		checkTransform(from, to, tranFound, JgrlConstants.DOUBLE_TEST_TOL);
	}

	public static void checkTransform(List<Point3D_F64> from, List<Point3D_F64> to, Se3_F64 tranFound, double tol) {
		Point3D_F64 foundPt = new Point3D_F64();
		for (int i = 0; i < from.size(); i++) {

			Point3D_F64 p = from.get(i);

			SePointOps_F64.transform(tranFound, p, foundPt);

			GeometryUnitTest.assertEquals(to.get(i), foundPt, tol);
		}
	}

}

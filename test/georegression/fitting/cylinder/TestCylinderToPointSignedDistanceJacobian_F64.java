/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.fitting.cylinder;

import georegression.fitting.sphere.SphereToPointSignedDistanceJacobian_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import org.ddogleg.optimization.JacobianChecker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestCylinderToPointSignedDistanceJacobian_F64 {

	@Test
	public void compareToNumerical() {

		CylinderToPointSignedDistance_F64 function = new CylinderToPointSignedDistance_F64();
		CylinderToPointSignedDistanceJacobian_F64 jacobian = new CylinderToPointSignedDistanceJacobian_F64();

		// sphere
		/**/double param[] = new /**/double[]{1,2,3,0,0,2,4};

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();

		// inside, should be negative
		points.add(new Point3D_F64(1.1,1.95,4.2));
		// outside, should be positive
		points.add(new Point3D_F64(0.96,-2.2,3.001));
		points.add(new Point3D_F64(5.2,2.05,3.1));

		function.setPoints(points);
		jacobian.setPoints(points);

//		JacobianChecker.jacobianPrint(function, jacobian, param, 100.0*GrlConstants.DOUBLE_TEST_TOL,
//				GrlConstants.DOUBLE_TEST_TOL);
		assertTrue(JacobianChecker.jacobian(function, jacobian, param, 100.0 * GrlConstants.DOUBLE_TEST_TOL,
				GrlConstants.DOUBLE_TEST_TOL));
	}

	@Test
	public void getN_and_getM() {
		SphereToPointSignedDistanceJacobian_F64 alg = new SphereToPointSignedDistanceJacobian_F64();

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();
		points.add(new Point3D_F64(1,2,3.5));
		points.add(new Point3D_F64(1,2,3.5));
		points.add(new Point3D_F64(1,2,3.5));

		alg.setPoints(points);

		assertEquals(4,alg.getN());
		assertEquals(points.size(), alg.getM());
	}

}

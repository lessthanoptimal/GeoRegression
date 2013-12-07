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

package georegression.fitting.sphere;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F32;
import org.ddogleg.optimization.JacobianChecker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestSphereToPointSignedDistanceJacobian_F32 {

	@Test
	public void compareToNumerical() {

		SphereToPointSignedDistance_F32 function = new SphereToPointSignedDistance_F32();
		SphereToPointSignedDistanceJacobian_F32 jacobian = new SphereToPointSignedDistanceJacobian_F32();

		// sphere
		/**/double param[] = new /**/double[]{1,2,3,4};

		List<Point3D_F32> points = new ArrayList<Point3D_F32>();

		// inside, should be negative
		points.add(new Point3D_F32(1.1f,1.95f,7.2f));
		// outside, should be positive
		points.add(new Point3D_F32(0.96f,-2.1f,3.001f));
		points.add(new Point3D_F32(5.2f,2.05f,3.1f));

		function.setPoints(points);
		jacobian.setPoints(points);

//		JacobianChecker.jacobianPrint(function, jacobian, param, 100.0f*GrlConstants.FLOAT_TEST_TOL,
//				GrlConstants.FLOAT_TEST_TOL);
		assertTrue(JacobianChecker.jacobian(function, jacobian, param, 100.0f * GrlConstants.FLOAT_TEST_TOL,
				GrlConstants.FLOAT_TEST_TOL));
	}

	@Test
	public void getN_and_getM() {
		SphereToPointSignedDistanceJacobian_F32 alg = new SphereToPointSignedDistanceJacobian_F32();

		List<Point3D_F32> points = new ArrayList<Point3D_F32>();
		points.add(new Point3D_F32(1,2,3.5f));
		points.add(new Point3D_F32(1,2,3.5f));
		points.add(new Point3D_F32(1,2,3.5f));

		alg.setPoints(points);

		assertEquals(4,alg.getNumOfInputsN());
		assertEquals(points.size(),alg.getNumOfOutputsM());
	}
}

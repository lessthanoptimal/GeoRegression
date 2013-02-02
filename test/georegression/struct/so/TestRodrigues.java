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

package georegression.struct.so;

import georegression.misc.GrlConstants;
import georegression.struct.point.Vector3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestRodrigues {

	@Test
	public void setParamVector() {
		Vector3D_F64 v = new Vector3D_F64(1,2,3);
		double theta = v.norm();
		v.normalize();

		Rodrigues a = new Rodrigues(theta,v.x,v.y,v.z);
		Rodrigues b = new Rodrigues();
		b.setParamVector(v.x*theta,v.y*theta,v.z*theta);
		
		assertEquals(a.theta,b.theta, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(a.unitAxisRotation.x,b.unitAxisRotation.x, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(a.unitAxisRotation.y,b.unitAxisRotation.y, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(a.unitAxisRotation.z,b.unitAxisRotation.z, GrlConstants.DOUBLE_TEST_TOL);
	}
}

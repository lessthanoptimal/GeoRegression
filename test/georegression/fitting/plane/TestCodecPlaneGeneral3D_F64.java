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

package georegression.fitting.plane;

import georegression.misc.GrlConstants;
import georegression.struct.plane.PlaneGeneral3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestCodecPlaneGeneral3D_F64 {

	@Test
	public void basicCheck() {
		PlaneGeneral3D_F64 plane = new PlaneGeneral3D_F64(1,2,3,4);
		PlaneGeneral3D_F64 found = new PlaneGeneral3D_F64();
		/**/double param[] = new /**/double[ 4 ];

		CodecPlaneGeneral3D_F64 alg = new CodecPlaneGeneral3D_F64();

		alg.encode(plane,param);
		alg.decode(param,found);

		assertEquals(plane.A,found.A, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(plane.B,found.B, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(plane.C,found.C, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(plane.D,found.D, GrlConstants.DOUBLE_TEST_TOL);
	}

}

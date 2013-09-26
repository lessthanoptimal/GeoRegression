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

import georegression.misc.GrlConstants;
import georegression.struct.shapes.Cylinder3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestCodecCylinder3D_F64 {

	@Test
	public void basicCheck() {
		Cylinder3D_F64 cylinder = new Cylinder3D_F64(1,2,3,4,5,6,7);
		Cylinder3D_F64 found = new Cylinder3D_F64();
		/**/double param[] = new /**/double[ 7 ];

		CodecCylinder3D_F64 alg = new CodecCylinder3D_F64();

		alg.encode(cylinder,param);
		alg.decode(param,found);

		assertEquals(0, cylinder.line.p.distance(found.line.p), GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0, cylinder.line.slope.distance(found.line.slope), GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(cylinder.radius,found.radius, GrlConstants.DOUBLE_TEST_TOL);
	}

}

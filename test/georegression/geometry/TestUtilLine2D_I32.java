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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.line.LineSegment2D_I32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_I32 {

	@Test
	public void acuteAngle() {
		LineSegment2D_I32 line0 = new LineSegment2D_I32(0,0,1,0);
		LineSegment2D_I32 line1 = new LineSegment2D_I32(0,0,0,1);

		assertEquals(Math.PI/2,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.DOUBLE_TEST_TOL);

		line0.set(2,2,3,2);
		line1.set(3,2,2,2);

		assertEquals(Math.PI, UtilLine2D_I32.acuteAngle(line0, line1), GrlConstants.DOUBLE_TEST_TOL);

		line0.set(2,2,1,2);
		line1.set(3,2,4,2);

		assertEquals(Math.PI,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.DOUBLE_TEST_TOL);

		line0.set(2,2,3,2);
		line1.set(3,2,4,2);

		assertEquals(0,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.DOUBLE_TEST_TOL);
	}

}

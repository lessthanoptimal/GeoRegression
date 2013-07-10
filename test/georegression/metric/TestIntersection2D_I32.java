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

package georegression.metric;

import georegression.struct.shapes.RectangleCorner2D_I32;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestIntersection2D_I32 {
	@Test
	public void intersects_rect_corners() {
		// check several positive cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,100,120),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(10,12,99,119),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(50,50,200,200),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,-10,10,10),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,-10,105,1),true);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,5,105,105),true);

		// negative cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(200,200,300,305),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-200,-200,-10,-10),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-20,100,-5),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,125,100,130),false);

		// edge cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,0,0),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,120,100,120),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,0,0,120),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,0,105,120),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-10,100,0),false);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,120,100,125),false);
	}

	private void check( RectangleCorner2D_I32 a , RectangleCorner2D_I32 b , boolean expected ) {
		assertTrue(expected==Intersection2D_I32.intersects(a,b));
	}

	@Test
	public void intersection_rect_corners() {
		// check several positive cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,100,120),
				new RectangleCorner2D_I32(0,0,100,120));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(10,12,99,119),
				new RectangleCorner2D_I32(10,12,99,119));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(50,50,200,200),
				new RectangleCorner2D_I32(50,50,100,120));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,-10,10,10),
				new RectangleCorner2D_I32(0,0,10,10));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,-10,105,1),
				new RectangleCorner2D_I32(90,0,100,1));
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(90,5,105,105),
				new RectangleCorner2D_I32(90,5,100,105));

		// negative cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(200,200,300,305),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-200,-200,-10,-10),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-20,100,-5),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,125,100,130),null);

		// edge cases
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,0,0,0),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,120,100,120),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(-10,0,0,120),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(100,0,105,120),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,-10,100,0),null);
		check( new RectangleCorner2D_I32(0,0,100,120),new RectangleCorner2D_I32(0,120,100,125),null);
	}

	private void check( RectangleCorner2D_I32 a , RectangleCorner2D_I32 b , RectangleCorner2D_I32 expected ) {
		if( expected == null ) {
			assertFalse(Intersection2D_I32.intersection(a, b, null));
			return;
		}

		RectangleCorner2D_I32 found = new RectangleCorner2D_I32();
		assertTrue(Intersection2D_I32.intersection(a, b, found));

		assertEquals(expected.x0,found.x0);
		assertEquals(expected.x1,found.x1);
		assertEquals(expected.y0,found.y0);
		assertEquals(expected.y1,found.y1);
	}
}

/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

package georegression.struct.shapes;

import georegression.GeoRegressionJUnit;
import georegression.struct.point.Point2D_F64;
import org.ejml.MapPrintFormat;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuadrilateral_F64 extends GeoRegressionJUnit {
	@Test void convert() {
		var polygon = new Quadrilateral_F64();
		polygon.a.setTo(1,2);
		polygon.b.setTo(2,3);
		polygon.c.setTo(3,4);
		polygon.d.setTo(4,5);

		List<Point2D_F64> list = polygon.convert(null,false);
		assertEquals(4,list.size());
		for ( int i = 0; i < list.size(); i++ ) {
			assertSame(list.get(i), polygon.get(i));
		}

		list = polygon.convert(null,true);
		assertEquals(4,list.size());
		for ( int i = 0; i < list.size(); i++ ) {
			assertNotSame(list.get(i), polygon.get(i));
			assertEquals(list.get(i), polygon.get(i));
		}
	}

	@Test void set_list() {
		var list = new ArrayList<Point2D_F64>();

		list.add( new Point2D_F64(2,3));
		list.add( new Point2D_F64(3,4));
		list.add( new Point2D_F64(4,5));
		list.add( new Point2D_F64(5,6));

		var polygon = new Quadrilateral_F64();
		polygon.setTo(list);
		assertEquals(4,list.size());
		for ( int i = 0; i < list.size(); i++ ) {
			assertNotSame(list.get(i), polygon.get(i));
			assertEquals(list.get(i), polygon.get(i));
		}
	}

	@Test void formatMap() {
		var polygon = new Quadrilateral_F64();
		polygon.a.setTo(1,2);
		polygon.b.setTo(2,3.1234);
		polygon.c.setTo(3,4);
		polygon.d.setTo(4,5);

		String found = polygon.formatMap(new MapPrintFormat().withPrecision(2));
		assertEquals("{a: {x: 1, y: 2}, b: {x: 2, y: 3.12}, c: {x: 3, y: 4}, d: {x: 4, y: 5}}", found);
	}
}
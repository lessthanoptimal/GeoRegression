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

package georegression.struct.point;

import georegression.GeoRegressionJUnit;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPoint3D_I32 extends GeoRegressionJUnit {

	@Test void getDimension() {
		assertEquals(3, new Point3D_I32().getDimension());
	}

	@Test void set() {
		Point3D_I32 p = new Point3D_I32().setTo(1, 2, 3);

		assertEquals(1, p.x);
		assertEquals(2, p.y);
		assertEquals(3, p.z);
	}

	@Test void isIdentical() {
		var a = new Point3D_I32(1, 2, 3);
		var b = new Point3D_I32(1, 2, 3);

		assertTrue(a.isIdentical(b));
		assertFalse(a.isIdentical(new Point3D_I32(2, 2, 3)));
		assertFalse(a.isIdentical(new Point3D_I32(1, 3, 3)));
		assertFalse(a.isIdentical(new Point3D_I32(1, 2, 4)));
	}

	@Test void createNewInstance() {
		assertNotNull(new Point3D_I32().createNewInstance());
	}

	@Test void copy() {
		var p = new Point3D_I32(1, 2, 3).copy();

		assertEquals(1, p.x);
		assertEquals(2, p.y);
		assertEquals(3, p.z);
	}

	@Test void format_Matrix() {
		var a = new Point3D_I32(1, 2, 3);
		String found = a.format(new MatrixPrintFormat());
		assertEquals("{1, 2, 3}", found);
	}

	@Test void formatMap() {
		var a = new Point3D_I32(1, 2, 3);
		String found = a.formatMap(new MapPrintFormat());
		assertEquals("{x: 1, y: 2, z: 3}", found);
	}
}

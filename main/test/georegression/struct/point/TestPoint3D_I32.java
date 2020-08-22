/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestPoint3D_I32 {

	@Test
	void getDimension() {
		assertEquals(3,new Point3D_I32().getDimension());
	}

	@Test
	void set() {
		Point3D_I32 p = new Point3D_I32();
		p.setTo(1,2,3);

		assertEquals(1,p.x);
		assertEquals(2,p.y);
		assertEquals(3,p.z);
	}

	@Test
	void isIdentical() {
		Point3D_I32 a = new Point3D_I32(1,2,3);
		Point3D_I32 b = new Point3D_I32(1,2,3);

		assertTrue(a.isIdentical(b));
		assertFalse(a.isIdentical(new Point3D_I32(2,2,3)));
		assertFalse(a.isIdentical(new Point3D_I32(1,3,3)));
		assertFalse(a.isIdentical(new Point3D_I32(1,2,4)));
	}

	@Test
	void createNewInstance() {
		assertNotNull(new Point3D_I32().createNewInstance());
	}

	@Test
	void copy() {
		Point3D_I32 p = new Point3D_I32(1,2,3).copy();

		assertEquals(1,p.x);
		assertEquals(2,p.y);
		assertEquals(3,p.z);
	}

	@Test
	void setTo() {
		Point3D_I32 a = new Point3D_I32(1,2,3);
		Point3D_I32 b = new Point3D_I32();
		b.setTo(a);

		assertEquals(1,b.x);
		assertEquals(2,b.y);
		assertEquals(3,b.z);
	}
}

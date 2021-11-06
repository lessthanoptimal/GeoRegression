/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.struct.shapes.Box3D_I32;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestIntersection3D_I32 {
	@Test
	void contained_box_box() {
		Box3D_I32 box = new Box3D_I32(2,3,4,4,6,8);

		// identical
		assertTrue(Intersection3D_I32.contains(box,new Box3D_I32(2,3,4,3,5,7)));
		// smaller
		assertTrue(Intersection3D_I32.contains(box,new Box3D_I32(3,4,5,3,5,7)));

		// partial x-axis
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(1,3,4,4,6,8)));
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(2,3,4,5,6,8)));
		// partial y-axis
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(2,2,4,4,6,8)));
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(2,3,4,4,7,8)));
		// partial z-axis
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(2,3,2,4,6,8)));
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(2,3,4,4,6,9)));
	}

	@Test
	void intersect_box_box() {
		Box3D_I32 box = new Box3D_I32(2,3,4,4,6,8);

		// identical
		assertTrue(Intersection3D_I32.contains(box,new Box3D_I32(2,3,4,4,6,8)));
		// outside
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(10,10,10,12,12,12)));
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(10,3,4, 12,6,8)));
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(2,10,4, 4,12,8)));
		assertFalse(Intersection3D_I32.contains(box,new Box3D_I32(2,3,10, 4,6,12)));

		// assume the 1D tests are sufficient. the above tests do check to see if each axis is handled
		// individually
	}

	@Test
	void intersect_1d() {
		// identical
		assertTrue(Intersection3D_I32.intersects(0,0,1,1));
		// bigger
		assertTrue(Intersection3D_I32.intersects(0,-1,1,2));
		assertTrue(Intersection3D_I32.intersects(-1,0,2,1));
		// shifted
		assertTrue(Intersection3D_I32.intersects(0,1,2,3));
		assertTrue(Intersection3D_I32.intersects(1,0,3,2));
		assertTrue(Intersection3D_I32.intersects(0,-1,2,1));
		assertTrue(Intersection3D_I32.intersects(-1,0,1,2));
		// graze
		assertFalse(Intersection3D_I32.intersects(0,1,1,2));
		assertFalse(Intersection3D_I32.intersects(1,0,2,1));
		// outside
		assertFalse(Intersection3D_I32.intersects(0,2,1,3));
	}
}

/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.geometry.polygon;

import georegression.geometry.polygon.TriangulateSimpleRemoveEars_F64.Vertex;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.struct.DogArray;
import org.ddogleg.struct.DogLinkedList;
import org.ddogleg.struct.FastAccess;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
@SuppressWarnings("ConstantConditions")
public class TestTriangulateSimpleRemoveEars_F64 {
	/**
	 * When there's less than three nodes it should be empty
	 */
	@Test void lessThanThree() {
		var original = new Polygon2D_F64(new double[][]{{2,0},{2,2}});
		var found = new DogArray<>(ThreeIndexes::new);
		var alg = new TriangulateSimpleRemoveEars_F64();

		found.grow(); // make it not empty to see if it was cleared
		alg.process(original,found);

		assertEquals(0, found.size);
	}

	/**
	 * If it's already a triangle there's nothing else to do
	 */
	@Test void triangle() {
		var original = new Polygon2D_F64(new double[][]{{2,0},{2,2},{1,5}});
		var found = new DogArray<>(ThreeIndexes::new);
		var alg = new TriangulateSimpleRemoveEars_F64();
		found.grow(); // make it not empty to see if it was cleared
		alg.process(original,found);
		assertEquals(1, found.size);
		contains(found,0,1,2);
	}

	/**
	 * See if it can handle a simple rectangle
	 */
	@Test void rectangle() {
		var original = new Polygon2D_F64(new double[][]{{0,0},{2,0},{2,1},{0,1}});
		var found = new DogArray<>(ThreeIndexes::new);
		var alg = new TriangulateSimpleRemoveEars_F64();
		found.grow(); // make it not empty to see if it was cleared
		alg.process(original,found);
		assertEquals(2, found.size);
		// hand computed
		contains(found,3,0,1);
		contains(found,1,2,3);
	}

	/**
	 * Concave quadrilateral
	 */
	@Test void concaveQuad() {
		var original = new Polygon2D_F64(new double[][]{{0,0},{2,0},{0.5,0.5},{0,2}});
		var found = new DogArray<>(ThreeIndexes::new);
		var alg = new TriangulateSimpleRemoveEars_F64();
		found.grow(); // make it not empty to see if it was cleared
		alg.process(original,found);
		assertEquals(2, found.size);
		// hand computed
		contains(found,0,1,2);
		contains(found,0,2,3);
	}

	@Test void fiveSidesConcave() {
		var original = new Polygon2D_F64(new double[][]{{0,0},{0,2},{1,1},{2,2},{2,0}});
		var found = new DogArray<>(ThreeIndexes::new);
		var alg = new TriangulateSimpleRemoveEars_F64();
		found.grow(); // make it not empty to see if it was cleared
		alg.process(original,found);
		assertEquals(3, found.size);
		// hand computed
		contains(found,0,1,2);
		contains(found,4,0,2);
		contains(found,2,3,4);
	}

	@Test void findEars() {
		var alg = new TriangulateSimpleRemoveEars_F64();
		alg.knownOrder = true;

		// sketch this polygon to figure out why this is a decent test
		var original = new Polygon2D_F64(new double[][]{{2,0},{2,2},{0,2},{0,1},{1.5,1.5},{0,0}});
		alg.convert(original, alg.polygon);

		alg.findEars();

		DogLinkedList.Element<Vertex> e = alg.polygon.getHead();
		assertFalse(e.object.ear);
		e = e.next;
		assertFalse(e.object.ear);
		e = e.next;
		assertTrue(e.object.ear);
		e = e.next;
		assertTrue(e.object.ear);
		e = e.next;
		assertFalse(e.object.ear);
		e = e.next;
		assertTrue(e.object.ear);
	}

	@Test void isDiagonal() {
		var alg = new TriangulateSimpleRemoveEars_F64();
		alg.knownOrder = true;

		// sketch this polygon to figure out why this is a decent test
		var original = new Polygon2D_F64(new double[][]{{2,0},{2,2},{0,2},{0,1},{1.5,1.5},{0,0}});
		alg.convert(original, alg.polygon);

		DogLinkedList.Element<Vertex> e = alg.polygon.getHead();
		assertFalse(alg.isDiagonal(e,e.next.next));
		e = e.next;
		assertTrue(alg.isDiagonal(e,e.next.next));
		e = e.next;
		assertTrue(alg.isDiagonal(e,e.next.next));
		e = e.next;
		assertFalse(alg.isDiagonal(e,e.next.next));
		e = e.next;
		assertTrue(alg.isDiagonal(e,e.next.next));
	}

	/** None of the edges intersect any of the other edges */
	@Test void isDiagonalie_easy() {
		var alg = new TriangulateSimpleRemoveEars_F64();
		alg.knownOrder = true;
		alg.ccw = false;

		var original = new Polygon2D_F64(new double[][]{{0,0},{0,2},{1,1},{2,2},{2,0}});
		alg.convert(original, alg.polygon);

		DogLinkedList.Element<Vertex> e = alg.polygon.getHead();
		do {
			assertTrue(alg.isDiagonalie(e,e.next));
			e = e.next;
		} while (e != alg.polygon.getHead());
	}

	/**
	 * Non-simple polygon for testing. The crosses should fail
	 */
	@Test void isDiagonalie_cross() {
		var alg = new TriangulateSimpleRemoveEars_F64();
		alg.knownOrder = true;

		var original = new Polygon2D_F64(new double[][]{{2,0},{2,2},{0,0},{0,2}});
		alg.convert(original, alg.polygon);

		DogLinkedList.Element<Vertex> e = alg.polygon.getHead();

		assertTrue(alg.isDiagonalie(e,e.next));
		assertTrue(alg.isDiagonalie(e.next,e)); // order should not matter
		e = e.next;
		assertFalse(alg.isDiagonalie(e,e.next));
		assertFalse(alg.isDiagonalie(e.next,e));
		e = e.next;
		assertTrue(alg.isDiagonalie(e,e.next));
		e = e.next;
		assertFalse(alg.isDiagonalie(e,e.next));
	}

	@Test void isInCone() {
		var alg = new TriangulateSimpleRemoveEars_F64();
		alg.knownOrder = true;
		alg.ccw = false;

		var original = new Polygon2D_F64(new double[][]{{0,0},{0,2},{1,1},{2,2},{2,0}});
		alg.convert(original, alg.polygon);
		DogLinkedList.Element<Vertex> e0 = alg.polygon.getHead();
		DogLinkedList.Element<Vertex> e1 = e0.next;
		DogLinkedList.Element<Vertex> e2 = e1.next;
		DogLinkedList.Element<Vertex> e3 = e2.next;
		DogLinkedList.Element<Vertex> e4 = e3.next;

		assertTrue(alg.isInCone(e0,e2));
		assertTrue(alg.isInCone(e0,e3));
		assertTrue(alg.isInCone(e2,e0));
		assertTrue(alg.isInCone(e2,e4));
		assertFalse(alg.isInCone(e1,e3));
		assertFalse(alg.isInCone(e1,e4));
	}

	@Test void convert() {
		var alg = new TriangulateSimpleRemoveEars_F64();

		var original = new Polygon2D_F64(1,2,3,4,5,6,7,8);
		alg.convert(original, alg.polygon);

		assertEquals(4, alg.polygon.size());
		DogLinkedList.Element<Vertex> e = alg.polygon.getHead();
		for (int i = 0; i < 4; i++, e=e.next) {
			assertFalse(e.object.ear);
			assertEquals(i, e.object.index);
			assertEquals(original.get(i).x, e.object.x);
			assertEquals(original.get(i).y, e.object.y);
		}
	}

	private static void contains(FastAccess<ThreeIndexes> list, int idx0, int idx1, int idx2) {
		for (int i = 0; i < list.size; i++) {
			ThreeIndexes found = list.get(i);
			if (found.idx0==idx0 && found.idx1==idx1 && found.idx2==idx2) {
				return;
			}
		}
		fail("Not found: "+idx0+" "+idx1+" "+idx2);
	}
}
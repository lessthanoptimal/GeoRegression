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

import org.ddogleg.struct.DogLinkedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
@SuppressWarnings("ConstantConditions")
public class TestCyclicalLinkedList {
	@Test void pushHead() {
		var alg = new CyclicalLinkedList<Integer>();
		alg.pushHead(0);
		assertEquals(0, alg.getHead().object);
		assertEquals(1, alg.size());

		alg.pushHead(1);
		assertEquals(2, alg.size());
		DogLinkedList.Element<Integer> e = alg.getHead();
		assertEquals(1, e.object);
		assertEquals(0, e.next.object);

		assertSame(e, e.next.next);
		assertSame(e, e.prev.prev);
		assertSame(e.next, alg.getTail());
	}

	@Test void pushTail() {
		var alg = new CyclicalLinkedList<Integer>();
		alg.pushTail(0);
		assertEquals(0, alg.getHead().object);
		assertEquals(1, alg.size());

		alg.pushTail(1);
		assertEquals(2, alg.size());
		DogLinkedList.Element<Integer> e = alg.getHead();
		assertEquals(0, e.object);
		assertEquals(1, e.next.object);

		assertSame(e, e.next.next);
		assertSame(e, e.prev.prev);
		assertSame(e.next, alg.getTail());
	}

	@Test void remove() {
		var alg = new CyclicalLinkedList<Integer>();

		// see if it handles removing the last object correctly
		alg.pushHead(0);
		alg.remove(alg.getHead());
		assertEquals(0, alg.size());
		assertNull(alg.getHead());
		assertNull(alg.getTail());

		// Remove an object in the middle
		alg.pushHead(0);
		alg.pushHead(1);
		alg.pushHead(2);

		DogLinkedList.Element<Integer> e = alg.getHead();
		alg.remove(e.next);
		assertEquals(2,alg.size());
		assertSame(e,alg.getHead());
		assertSame(e.next,alg.getTail());
		assertSame(e, e.next.next);
		assertSame(e, e.prev.prev);

		// Remove an object at the head. make it size=3 to be more interesting
		alg.pushHead(3);
		assertEquals(3, alg.size());
		alg.remove(alg.getHead());
		assertEquals(2, alg.size());
		assertSame(e,alg.getHead());
		assertSame(e.prev, alg.getTail());
		assertSame(e.next, alg.getTail());
	}
}
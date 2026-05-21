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

package georegression.struct.arraylike;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CheckArrayLike_F64 {
	public abstract ArrayLike_F64 create();

	/// Call to run all tests
	public void all() {
		accessors();
		toArray();
		toArray_order();
		fromArray();
		fromArray_offset();
		isNaN();
		isInfinite();
	}

	@Test public void accessors() {
		ArrayLike_F64 a = create();
		assertTrue(a.length() > 0);

		for (int i = 0; i < a.length(); i++) {
			a.set(i, -i - 1);
		}

		for (int i = 0; i < a.length(); i++) {
			assertEquals(-i - 1, a.get(i));
		}
	}

	@Test public void toArray() {
		ArrayLike_F64 a = create();
		for (int i = 0; i < a.length(); i++) {
			a.set(i, -i - 1);
		}

		double[] found = a.toArray();
		for (int i = 0; i < a.length(); i++) {
			assertEquals(-i - 1, found[i]);
		}
	}

	@Test public void toArray_order() {
		ArrayLike_F64 a = create();
		for (int i = 0; i < a.length(); i++) {
			a.set(i, -i - 1);
		}

		var order = new int[a.length()];
		for (int i = 0; i < a.length(); i++) {
			order[i] = (i + 1)%a.length();
		}
		double[] found = a.toArray(order);
		for (int i = 0; i < a.length(); i++) {
			assertEquals(a.get(order[i]), found[i]);
		}
	}

	@Test public void fromArray() {
		ArrayLike_F64 a = create();
		var src = new double[a.length()];
		for (int i = 0; i < src.length; i++) {
			src[i] = i + 2;
		}
		a.fromArray(src);

		for (int i = 0; i < a.length(); i++) {
			assertEquals(i + 2, a.get(i));
		}
	}

	@Test public void fromArray_offset() {
		ArrayLike_F64 a = create();
		var src = new double[a.length()*2];
		for (int i = 0; i < src.length; i++) {
			src[i] = i + 2;
		}
		a.fromArray(src, a.length());

		for (int i = 0; i < a.length(); i++) {
			assertEquals(i + 2 + a.length(), a.get(i));
		}
	}

	@Test public void isNaN() {
		ArrayLike_F64 a = create();
		for (int i = 0; i < a.length(); i++) {
			a.set(i, i + 2);
		}
		assertFalse(a.isNaN());

		for (int i = 0; i < a.length(); i++) {
			a.set(i, Double.NaN);
			assertTrue(a.isNaN());
			a.set(i, i + 2);
		}
	}

	@Test public void isInfinite() {
		ArrayLike_F64 a = create();
		for (int i = 0; i < a.length(); i++) {
			a.set(i, i + 2);
		}
		assertFalse(a.isInfinite());

		for (int i = 0; i < a.length(); i++) {
			a.set(i, Double.POSITIVE_INFINITY);
			assertTrue(a.isInfinite());
			a.set(i, i + 2);
		}
	}
}

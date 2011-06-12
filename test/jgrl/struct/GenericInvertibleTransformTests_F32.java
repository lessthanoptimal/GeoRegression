/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct;

import jgrl.autocode.JgrlConstants;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public abstract class GenericInvertibleTransformTests_F32<T extends GeoTuple_F32> {

	public abstract T createRandomPoint();

	public abstract InvertibleTransform createRandomTransform();

	public abstract T apply(InvertibleTransform se, T point, T result);

	/**
	 * Makes sure that after reset is called the transform applies no transform
	 */
	@Test
	public void testReset() {
		InvertibleTransform tran1 = createRandomTransform();

		T orig = createRandomPoint();
		T before = (T) orig.createNewInstance();

		// it should modify the point
		apply(tran1, orig, before);
		assertFalse(orig.isIdentical(before, JgrlConstants.FLOAT_TEST_TOL));

		// after reset it shouldn't modify the point
		tran1.reset();
		apply(tran1, orig, before);
		assertTrue(orig.isIdentical(before, JgrlConstants.FLOAT_TEST_TOL));
	}

	/**
	 * See if applying the two transforms is the same as applying the concat of those
	 * two transforms once.
	 */
	@Test
	public void testConcat() {
		InvertibleTransform tran1 = createRandomTransform();
		InvertibleTransform tran2 = createRandomTransform();

		InvertibleTransform tran12 = tran1.concat(tran2, null);

		T orig = createRandomPoint();
		T expected = apply(tran1, orig, null);
		expected = apply(tran2, expected, expected);

		T found = apply(tran12, orig, null);

		assertTrue(found.isIdentical(expected, JgrlConstants.FLOAT_TEST_TOL));

		// do the same, but providing a place for it to write the result
		tran12 = tran1.concat(tran2, createRandomTransform());
		found = apply(tran12, orig, null);
		assertTrue(found.isIdentical(expected, JgrlConstants.FLOAT_TEST_TOL));

	}

	/**
	 * Sees if inverting a transform produces the same solution as the point's
	 * original location
	 */
	@Test
	public void testInvert() {
		InvertibleTransform a = createRandomTransform();
		T orig = createRandomPoint();
		T tran = apply(a, orig, null);

		InvertibleTransform aInv = a.invert(null);
		T found = apply(aInv, tran, null);

		assertTrue(found.isIdentical(orig, JgrlConstants.FLOAT_TEST_TOL));

		// do the same, but providing a place for it to write the result
		aInv = a.invert(createRandomTransform());

		found = apply(aInv, tran, null);

		assertTrue(found.isIdentical(orig, JgrlConstants.FLOAT_TEST_TOL));
	}
}

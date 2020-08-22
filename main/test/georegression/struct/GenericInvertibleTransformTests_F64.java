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

package georegression.struct;

import georegression.misc.GrlConstants;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public abstract class GenericInvertibleTransformTests_F64 <T extends GeoTuple_F64> {

	public abstract T createRandomPoint();

	public abstract InvertibleTransform createRandomTransform();

	public abstract T apply( InvertibleTransform se, T point, @Nullable T result );

	/**
	 * Makes sure that after reset is called the transform applies no transform
	 */
	@Test
	void testReset() {
		InvertibleTransform tran1 = createRandomTransform();

		T orig = createRandomPoint();
		T before = (T) orig.createNewInstance();

		// it should modify the point
		apply( tran1, orig, before );
		assertFalse( orig.isIdentical( before, GrlConstants.TEST_F64) );

		// after reset it shouldn't modify the point
		tran1.reset();
		apply( tran1, orig, before );
		assertTrue( orig.isIdentical( before, GrlConstants.TEST_F64) );
	}

	/**
	 * See if applying the two transforms is the same as applying the concat of those
	 * two transforms once.
	 */
	@Test
	void testConcat() {
		InvertibleTransform tran1 = createRandomTransform();
		InvertibleTransform tran2 = createRandomTransform();

		InvertibleTransform tran12 = tran1.concat( tran2, null );

		T orig = createRandomPoint();
		T expected = apply( tran1, orig, null );
		expected = apply( tran2, expected, expected );

		T found = apply( tran12, orig, null );

		assertTrue( found.isIdentical( expected, GrlConstants.TEST_F64) );

		// do the same, but providing a place for it to write the result
		tran12 = tran1.concat( tran2, createRandomTransform() );
		found = apply( tran12, orig, null );
		assertTrue( found.isIdentical( expected, GrlConstants.TEST_F64) );

	}

	/**
	 * Sees if inverting a transform produces the same solution as the point's
	 * original location
	 */
	@Test
	void testInvert() {
		InvertibleTransform a = createRandomTransform();
		T orig = createRandomPoint();
		T tran = apply( a, orig, null );

		InvertibleTransform aInv = a.invert( null );
		T found = apply( aInv, tran, null );

		assertTrue( found.isIdentical( orig, GrlConstants.TEST_F64) );

		// do the same, but providing a place for it to write the result
		aInv = a.invert( createRandomTransform() );

		found = apply( aInv, tran, null );

		assertTrue( found.isIdentical( orig, GrlConstants.TEST_F64) );
	}

	/**
	 * Makes sure it uses the storage correctlyt
	 */
	@Test
	void testInvert_input() {
		InvertibleTransform aInv = createRandomTransform();

		InvertibleTransform a = createRandomTransform();
		T orig = createRandomPoint();
		T tran = apply( a, orig, null );

		assertSame(aInv, a.invert(aInv));
		T found = apply( aInv, tran, null );

		assertTrue( found.isIdentical( orig, GrlConstants.TEST_F64) );
	}
}

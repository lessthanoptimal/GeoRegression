/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

package georegression.geometry;

import georegression.struct.curve.ConicGeneral_F32;
import georegression.struct.curve.ParabolaGeneral_F32;
import org.ejml.UtilEjml;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilCurves_F32 {
	/**
	 * The conic is a parabola This is an easy case
	 */
	@Test
	public void conic_to_parabola_4AC_is_B() {
		float A = 1.5f,C=0.9f;
		float B = (float)Math.sqrt(4*A*C);
		ConicGeneral_F32 conic = new ConicGeneral_F32(1.5f,B,0.9f,3,2,-328.6895003862225f);
		// sanity check
		assertEquals(0,conic.evaluate(10,5), UtilEjml.TEST_F32);

		ParabolaGeneral_F32 parabola = new ParabolaGeneral_F32();
		UtilCurves_F32.convert(conic,parabola);
		assertEquals(0,conic.evaluate(10,5), UtilEjml.TEST_F32);
		// make sure it's not all zeros
		assertTrue(conic.F != 0 );

		// go the reverse direction now
		ConicGeneral_F32 reversed = new ConicGeneral_F32();
		UtilCurves_F32.convert(parabola,reversed);

		assertEquals(conic.A,reversed.A, UtilEjml.TEST_F32);
		assertEquals(conic.B,reversed.B, UtilEjml.TEST_F32);
		assertEquals(conic.C,reversed.C, UtilEjml.TEST_F32);
		assertEquals(conic.D,reversed.D, UtilEjml.TEST_F32);
		assertEquals(conic.E,reversed.E, UtilEjml.TEST_F32);
		assertEquals(conic.F,reversed.F, UtilEjml.TEST_F32);
	}

	/**
	 * B is not zero in this case. When converted it will be forced ot be zero
	 */
	@Test
	public void conic_to_parabola_4AC_Not_B() {
		ConicGeneral_F32 conic = new ConicGeneral_F32(1.5f,2,0.9f,3,2,-212.5f);
		ParabolaGeneral_F32 parabola = new ParabolaGeneral_F32();
		UtilCurves_F32.convert(conic,parabola);

		assertEquals(Math.sqrt(conic.A),parabola.A, UtilEjml.TEST_F32);
		assertEquals(Math.sqrt(conic.C),parabola.C, UtilEjml.TEST_F32);
		assertEquals(conic.D,parabola.D, UtilEjml.TEST_F32);
		assertEquals(conic.E,parabola.E, UtilEjml.TEST_F32);
		assertEquals(conic.F,parabola.F, UtilEjml.TEST_F32);
	}
}
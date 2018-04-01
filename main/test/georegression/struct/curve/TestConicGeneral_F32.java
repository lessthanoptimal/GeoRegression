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

package georegression.struct.curve;

import georegression.misc.GrlConstants;
import org.ejml.UtilEjml;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestConicGeneral_F32 {
	@Test
	public void evaluate() {
		// arbitrary but hand constructed conic
		ConicGeneral_F32 conic = new ConicGeneral_F32(1.5f,0.1f,0.9f,3,2,-217.5f);
		assertEquals(0,conic.evaluate(10,5), UtilEjml.TEST_F32);
	}

	@Test
	public void isEllipse() {
		float A = 1.5f, C = 0.9f;
		float B = (float)Math.sqrt(4*A*C)+0.01f;
		ConicGeneral_F32 conic = new ConicGeneral_F32(A,B,C,3,2,-217.5f);
		assertFalse(conic.isEllipse( GrlConstants.F_EPS ));
		conic.B -= 0.02f;
		assertTrue(conic.isEllipse( GrlConstants.F_EPS ));
	}

	@Test
	public void isHyperbola() {
		float A = 1.5f, C = 0.9f;
		float B = (float)Math.sqrt(4*A*C)-0.01f;
		ConicGeneral_F32 conic = new ConicGeneral_F32(A,B,C,3,2,-217.5f);
		assertFalse(conic.isHyperbola (GrlConstants.F_EPS ));
		conic.B += 0.02f;
		assertTrue(conic.isHyperbola( GrlConstants.F_EPS ));
	}

	@Test
	public void isParabola() {
		float A = 1.5f, C = 0.9f;
		float B = (float)Math.sqrt(4*A*C)+0.01f;
		ConicGeneral_F32 conic = new ConicGeneral_F32(A,B,C,3,2,-217.5f);
		assertFalse(conic.isParabola( GrlConstants.F_EPS ));
		conic.B -= 0.01f;
		assertTrue(conic.isParabola (GrlConstants.F_EPS ));
	}
}

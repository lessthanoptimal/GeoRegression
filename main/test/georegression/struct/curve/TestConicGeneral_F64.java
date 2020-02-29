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

package georegression.struct.curve;

import georegression.misc.GrlConstants;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestConicGeneral_F64 {
	@Test
	void evaluate() {
		// arbitrary but hand constructed conic
		ConicGeneral_F64 conic = new ConicGeneral_F64(1.5,0.1,0.9,3,2,-217.5);
		assertEquals(0,conic.evaluate(10,5), UtilEjml.TEST_F64);
	}

	@Test
	void isEllipse() {
		double A = 1.5, C = 0.9;
		double B = Math.sqrt(4*A*C)+0.01;
		ConicGeneral_F64 conic = new ConicGeneral_F64(A,B,C,3,2,-217.5);
		assertFalse(conic.isEllipse( GrlConstants.EPS ));
		conic.B -= 0.02;
		assertTrue(conic.isEllipse( GrlConstants.EPS ));
	}

	@Test
	void isHyperbola() {
		double A = 1.5, C = 0.9;
		double B = Math.sqrt(4*A*C)-0.01;
		ConicGeneral_F64 conic = new ConicGeneral_F64(A,B,C,3,2,-217.5);
		assertFalse(conic.isHyperbola (GrlConstants.EPS ));
		conic.B += 0.02;
		assertTrue(conic.isHyperbola( GrlConstants.EPS ));
	}

	@Test
	void isParabola() {
		double A = 1.5, C = 0.9;
		double B = Math.sqrt(4*A*C)+0.01;
		ConicGeneral_F64 conic = new ConicGeneral_F64(A,B,C,3,2,-217.5);
		assertFalse(conic.isParabola( GrlConstants.EPS ));
		conic.B -= 0.01;
		assertTrue(conic.isParabola (GrlConstants.EPS ));
	}
}

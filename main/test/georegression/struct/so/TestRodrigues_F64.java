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

package georegression.struct.so;

import georegression.misc.GrlConstants;
import georegression.struct.point.Vector3D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestRodrigues_F64 {

	@Test
	void setParamVector() {
		Vector3D_F64 v = new Vector3D_F64(1,2,3);
		double theta = v.norm();
		v.normalize();

		Rodrigues_F64 a = new Rodrigues_F64(theta,v.x,v.y,v.z);
		Rodrigues_F64 b = new Rodrigues_F64();
		b.setParamVector(v.x*theta,v.y*theta,v.z*theta);
		
		assertEquals(a.theta,b.theta, GrlConstants.TEST_F64);
		assertEquals(a.unitAxisRotation.x,b.unitAxisRotation.x, GrlConstants.TEST_F64);
		assertEquals(a.unitAxisRotation.y,b.unitAxisRotation.y, GrlConstants.TEST_F64);
		assertEquals(a.unitAxisRotation.z,b.unitAxisRotation.z, GrlConstants.TEST_F64);
	}
}

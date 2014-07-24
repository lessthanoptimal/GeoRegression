/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.sphere;

import georegression.misc.GrlConstants;
import georegression.struct.shapes.Sphere3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestCodecSphere3D_F32 {

	@Test
	public void basicCheck() {
		Sphere3D_F32 sphere = new Sphere3D_F32(1,2,3,4);
		Sphere3D_F32 found = new Sphere3D_F32();
		/**/double param[] = new /**/double[ 4 ];

		CodecSphere3D_F32 alg = new CodecSphere3D_F32();

		alg.encode(sphere,param);
		alg.decode(param,found);

		assertEquals(0, sphere.center.distance(found.center), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(sphere.radius,found.radius, GrlConstants.FLOAT_TEST_TOL);
	}

}

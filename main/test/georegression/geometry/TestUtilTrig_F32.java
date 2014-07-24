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

package georegression.geometry;

import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilTrig_F32 {

	@Test
	public void dot_2d() {
		Point2D_F32 a = new Point2D_F32(-1,2);
		Point2D_F32 b = new Point2D_F32(2,7);
		
		float found = UtilTrig_F32.dot(a,b);
		assertEquals(-2+14,found,1e-8);
	}

	@Test
	public void dot_3d() {
		Point3D_F32 a = new Point3D_F32(-1,2,3);
		Point3D_F32 b = new Point3D_F32(2,7,0.5f);

		float found = UtilTrig_F32.dot(a,b);
		assertEquals(-2+14+1.5f,found,1e-8);
	}
}

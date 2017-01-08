/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.point.Vector3D_F32;
import org.ejml.data.RowMatrix_F32;
import org.junit.Test;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestConvertCoordinates3D_F32 {
	Random rand = new Random(234);

	/**
	 * perform the equivalent operation in Euler coordinates and see if it gets the expected results
	 */
	@Test
	public void latlonToUnitVector() {

		// check a few specific cases
		latlonToUnitVector(0,0);
		latlonToUnitVector(0.0f, GrlConstants.F_PId2 );
		latlonToUnitVector(0.0f, -GrlConstants.F_PId2 );
		latlonToUnitVector(0.0f, GrlConstants.F_PI );
		latlonToUnitVector(0.0f, -GrlConstants.F_PI );
		latlonToUnitVector( GrlConstants.F_PId2 ,0);
		latlonToUnitVector( -GrlConstants.F_PId2 ,0);
		latlonToUnitVector(0.0f, GrlConstants.F_PId2 );


		// random cases now
		for (int i = 0; i < 100; i++) {
			float lat = rand.nextFloat()*GrlConstants.F_PI - GrlConstants.F_PId2;
			float lon = rand.nextFloat()*GrlConstants.F_PI2 - GrlConstants.F_PI;

			latlonToUnitVector(lat, lon);
		}
	}

	private void latlonToUnitVector(float lat, float lon) {
		RowMatrix_F32 M = ConvertRotation3D_F32.eulerToMatrix(EulerType.YXZ,lat,0,lon,null);

		Vector3D_F32 expected = new Vector3D_F32();
		GeometryMath_F32.mult(M,new Vector3D_F32(1,0,0),expected);

		Vector3D_F32 found = ConvertCoordinates3D_F32.latlonToUnitVector(lat,lon,(Vector3D_F32)null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.TEST_F32);
	}
}

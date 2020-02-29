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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.point.Vector3D_F64;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestConvertCoordinates3D_F64 {
	Random rand = new Random(234);

	/**
	 * perform the equivalent operation in Euler coordinates and see if it gets the expected results
	 */
	@Test
	void latlonToUnitVector() {

		// check a few specific cases
		latlonToUnitVector(0,0);
		latlonToUnitVector(0.0, GrlConstants.PId2 );
		latlonToUnitVector(0.0, -GrlConstants.PId2 );
		latlonToUnitVector(0.0, GrlConstants.PI );
		latlonToUnitVector(0.0, -GrlConstants.PI );
		latlonToUnitVector( GrlConstants.PId2 ,0);
		latlonToUnitVector( -GrlConstants.PId2 ,0);
		latlonToUnitVector(0.0, GrlConstants.PId2 );


		// random cases now
		for (int i = 0; i < 100; i++) {
			double lat = rand.nextDouble()*GrlConstants.PI - GrlConstants.PId2;
			double lon = rand.nextDouble()*GrlConstants.PI2 - GrlConstants.PI;

			latlonToUnitVector(lat, lon);
		}
	}

	private void latlonToUnitVector(double lat, double lon) {
		DMatrixRMaj M = ConvertRotation3D_F64.eulerToMatrix(EulerType.YXZ,lat,0,lon,null);

		Vector3D_F64 expected = new Vector3D_F64();
		GeometryMath_F64.mult(M,new Vector3D_F64(1,0,0),expected);

		Vector3D_F64 found = ConvertCoordinates3D_F64.latlonToUnitVector(lat,lon,(Vector3D_F64)null);

		GeometryUnitTest.assertEquals(expected, found, GrlConstants.TEST_F64);
	}
}

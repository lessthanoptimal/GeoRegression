/*
 * Copyright (C)  2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.points;

import georegression.geometry.UtilPoint2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.se.SePointOps_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public abstract class GeneralCloudToCloudChecksSe2 {

	Random rand = new Random(234);

	public void all() {
		identical_translate();
		identical_rotate();
	}

	public abstract MatchCloudToCloud<Se2_F64, Point2D_F64> create();

	@Test void identical_translate() {
		identical(new Se2_F64(0.1, -0.05, 0), 0.01, 0.01);
	}

	@Test void identical_rotate() {
		identical(new Se2_F64(0, 0, 0.1), 0.01, 0.01);
	}

	private void identical(Se2_F64 expected, double tolTran, double tolAngle) {
		List<Point2D_F64> src = UtilPoint2D_F64.random(0, 1, 500, rand);

		List<Point2D_F64> dst = new ArrayList<Point2D_F64>();
		for (int i = 0; i < src.size(); i++) {
			Point2D_F64 s = src.get(i);
			Point2D_F64 d = SePointOps_F64.transform(expected, s, null);
			dst.add(d);
		}

		MatchCloudToCloud<Se2_F64, Point2D_F64> alg = create();

		alg.setSource(src);
		alg.setDestination(dst);
		assertTrue(alg.compute());

		Se2_F64 found = alg.getSourceToDestination();

		assertEquals(expected.T.x, found.T.x, tolTran);
		assertEquals(expected.T.y, found.T.y, tolTran);
		assertEquals(expected.getYaw(), found.getYaw(), tolAngle);
	}
}

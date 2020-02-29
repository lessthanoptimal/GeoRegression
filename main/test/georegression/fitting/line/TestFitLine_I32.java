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

package georegression.fitting.line;

import georegression.metric.Distance2D_F32;
import georegression.metric.Distance2D_F64;
import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.line.LinePolar2D_F64;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestFitLine_I32 {
	@Test
	void polar_F64() {
		List<Point2D_I32> pts = new ArrayList<>();

		pts.add(new Point2D_I32(100,200)); // add an outlier that should be ignored
		for( int i = 0; i < 20; i++ ) {
			Point2D_I32 p = new Point2D_I32();
			p.x = i;
			p.y = 5-i*3;
			pts.add(p);
		}

		LineGeneral2D_F64 general = new LineGeneral2D_F64(3,1,-5);
		double distanceFromZero = Distance2D_F64.distance(general,new Point2D_F64(0,0));
		double angle = Math.atan(1.0/3.0);

		LinePolar2D_F64 found = FitLine_I32.polar(pts,1,20,(LinePolar2D_F64)null);

		assertEquals(distanceFromZero,found.distance, GrlConstants.TEST_F64);
		assertTrue(UtilAngle.dist(angle, found.angle) <= GrlConstants.TEST_F64);
	}

	@Test
	void polar_F32() {
		List<Point2D_I32> pts = new ArrayList<>();

		pts.add(new Point2D_I32(100,200)); // add an outlier that should be ignored
		for( int i = 0; i < 20; i++ ) {
			Point2D_I32 p = new Point2D_I32();
			p.x = i;
			p.y = 5-i*3;
			pts.add(p);
		}

		LineGeneral2D_F32 general = new LineGeneral2D_F32(3,1,-5);
		float distanceFromZero = Distance2D_F32.distance(general,new Point2D_F32(0,0));
		float angle = (float)Math.atan(1.0/3.0);

		LinePolar2D_F32 found = FitLine_I32.polar(pts,1,20,(LinePolar2D_F32)null);

		assertEquals(distanceFromZero,found.distance, GrlConstants.TEST_F32);
		assertTrue(UtilAngle.dist(angle, found.angle) <= GrlConstants.TEST_F32);
	}
}

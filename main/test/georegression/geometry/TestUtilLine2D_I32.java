/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.line.LineSegment2D_I32;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_I32 {

	@Test
	void acuteAngle() {
		LineSegment2D_I32 line0 = new LineSegment2D_I32(0,0,1,0);
		LineSegment2D_I32 line1 = new LineSegment2D_I32(0,0,0,1);

		assertEquals(Math.PI/2,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.TEST_F64);

		line0.setTo(2,2,3,2);
		line1.setTo(3,2,2,2);

		assertEquals(Math.PI, UtilLine2D_I32.acuteAngle(line0, line1), GrlConstants.TEST_F64);

		line0.setTo(2,2,1,2);
		line1.setTo(3,2,4,2);

		assertEquals(Math.PI,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.TEST_F64);

		line0.setTo(2,2,3,2);
		line1.setTo(3,2,4,2);

		assertEquals(0,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.TEST_F64);
	}

}

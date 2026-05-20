/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

package georegression.struct.packed;

import georegression.struct.point.Point2D_I16;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestPackedArrayPoint2D_I16 extends GenericPackedArrayChecks<Point2D_I16> {

	@Override protected PackedArray<Point2D_I16> createAlg() {
		return new PackedArrayPoint2D_I16();
	}

	@Override protected Point2D_I16 createRandomPoint() {
		var point = new Point2D_I16();
		point.x = (short)(rand.nextInt(100) - 50);
		point.y = (short)(rand.nextInt(100) - 50);
		return point;
	}

	@Override protected void checkEquals( Point2D_I16 a, Point2D_I16 b ) {
		assertEquals(0.0, a.distance(b));
	}

	@Override protected void checkNotEquals( Point2D_I16 a, Point2D_I16 b ) {
		assertNotEquals(0.0, a.distance(b));
	}

	@Test void appendValues() {
		var alg = new PackedArrayPoint2D_I16();
		assertEquals(0, alg.size());
		alg.append(1, 2);

		assertEquals(1, alg.size());

		var p = alg.getTemp(0);
		assertEquals(1, p.x);
		assertEquals(2, p.y);
	}

	@Test public void setTo() {
		var src = new PackedArrayPoint2D_I16();
		src.append(1, 2);
		src.append(2, 3);

		var dst = new PackedArrayPoint2D_I16();
		dst.append(4, 5);

		dst.setTo(src);
		assertEquals(2, dst.size());
		src.forIdx(0, 2, ( idx, a ) -> assertEquals(0.0, a.distance(dst.getTemp(idx))));
	}

	@Test void format_Matrix() {
		var alg = new PackedArrayPoint2D_I16();
		alg.append(1, 3);
		alg.append(2, -4);
		assertEquals("[{1, 3},\n{2, -4}]", alg.format(new MatrixPrintFormat()));
	}

	@Test void formatMap() {
		var alg = new PackedArrayPoint2D_I16();
		alg.append(1, 3);
		alg.append(2, -4);
		assertEquals("[{x: 1, y: 3},\n{x: 2, y: -4}]", alg.formatMap(new MapPrintFormat()));
	}
}

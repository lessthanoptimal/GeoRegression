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

package georegression.struct.se;

import georegression.struct.GenericInvertibleTransformTests_F64;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.so.Quaternion_F64;
import georegression.transform.se.SePointOps_F64;
import org.ejml.MapPrintFormat;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestQuatPose_F64 extends GenericInvertibleTransformTests_F64<Point3D_F64> {
	@Override public Point3D_F64 createRandomPoint() {
		return new Point3D_F64(rand.nextGaussian()*3, rand.nextGaussian()*3, rand.nextGaussian()*3);
	}

	@Override public InvertibleTransform<?> createRandomTransform() {
		var pose = new  QuatPose_F64();
		pose.t.setTo(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
		pose.ori.setTo(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
		pose.ori.normalize();

		return pose;
	}

	@Override public Point3D_F64 apply( InvertibleTransform<?> se, Point3D_F64 point, @Nullable Point3D_F64 result ) {
		return SePointOps_F64.transform((QuatPose_F64)se, point, result);
	}

	@Test void formatMap() {
		var a = new QuatPose_F64().setTo(new Quaternion_F64(2,3.12346,-3,-8), new Vector3D_F64(0, 3, 2));
		String found = a.formatMap(new MapPrintFormat().withPrecision(2));
		assertEquals("{ori: {w: 2, x: 3.12, y: -3, z: -8}, t: {x: 0, y: 3, z: 2}}", found);
	}

	@Override public void setTo() {}
}
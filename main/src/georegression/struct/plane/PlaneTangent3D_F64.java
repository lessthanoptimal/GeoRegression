/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.struct.plane;

import georegression.struct.point.Point3D_F64;
import org.ejml.FancyPrint;

import java.io.Serializable;

/**
 * Specifies a plane using the closest point on the plane to the origin. This can also be viewed as specifying
 * vector which is tangent to the plane with a magnitude equal to the distance of the closest point. This formulation
 * cannot describe a plane which intersects with the origin.
 *
 * @author Peter Abeles
 */
public class PlaneTangent3D_F64 extends Point3D_F64 implements Serializable {

	public PlaneTangent3D_F64(double x, double y, double z) {
		super(x, y, z);
	}

	public PlaneTangent3D_F64() {}

	@Override
	public String toString() {
		FancyPrint fancy = new FancyPrint();
		return getClass().getSimpleName()+"{" +
				"x=" + fancy.s(x) +
				", y=" + fancy.s(y) +
				", z=" + fancy.s(z) +
				'}';
	}
}

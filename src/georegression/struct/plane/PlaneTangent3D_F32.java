/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.plane;

import georegression.struct.point.Point3D_F32;

/**
 * Specifies a plane using the closest point on the plane to the origin.  This can also be viewed as specifying
 * vector which is tangent to the plane with a magnitude equal to the distance of the closest point. This formulation
 * cannot describe a plane which intersects with the origin.
 *
 * @author Peter Abeles
 */
public class PlaneTangent3D_F32 extends Point3D_F32 {

	public PlaneTangent3D_F32(float x, float y, float z) {
		super(x, y, z);
	}

	public PlaneTangent3D_F32() {
	}
}

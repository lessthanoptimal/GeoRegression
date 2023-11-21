/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.cylinder;

import georegression.metric.Distance3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.ddogleg.fitting.modelset.DistanceFromModel;

import java.util.List;

/**
 * Implementation of {@link DistanceFromModel} for {@link Cylinder3D_F64} and {@link PlaneNormal3D_F64}. It
 * returns the distance the point is from the cylinder's surface. The normal vector is ignored.
 *
 * @author Peter Abeles
 */
public class PointNormalDistanceFromCylinder_F64 implements DistanceFromModel<Cylinder3D_F64, PlaneNormal3D_F64> {
	Cylinder3D_F64 cylinder = new Cylinder3D_F64();

	@Override
	public void setModel( Cylinder3D_F64 plane ) {
		this.cylinder.setTo(plane);
	}

	@Override
	public /**/double distance( PlaneNormal3D_F64 point ) {
		return Math.abs(Distance3D_F64.distanceSigned(cylinder, point.p));
	}

	@Override
	public void distances( List<PlaneNormal3D_F64> list, /**/double[] errors ) {
		for (int i = 0; i < list.size(); i++) {
			errors[i] = Math.abs(Distance3D_F64.distanceSigned(cylinder, list.get(i).p));
		}
	}

	@Override
	public Class<PlaneNormal3D_F64> getPointType() {
		return PlaneNormal3D_F64.class;
	}

	@Override
	public Class<Cylinder3D_F64> getModelType() {
		return Cylinder3D_F64.class;
	}
}

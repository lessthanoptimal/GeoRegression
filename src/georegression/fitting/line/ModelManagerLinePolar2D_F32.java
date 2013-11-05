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

package georegression.fitting.line;

import georegression.struct.line.LinePolar2D_F32;
import org.ddogleg.fitting.modelset.ModelManager;

/**
 * Manages {@link georegression.struct.line.LinePolar2D_F32}.
 *
 * @author Peter Abeles
 */
public class ModelManagerLinePolar2D_F32 implements ModelManager<LinePolar2D_F32> {
	@Override
	public LinePolar2D_F32 createModelInstance() {
		return new LinePolar2D_F32();
	}

	@Override
	public void copyModel(LinePolar2D_F32 src, LinePolar2D_F32 dst) {
		dst.angle = src.angle;
		dst.distance = src.distance;
	}
}
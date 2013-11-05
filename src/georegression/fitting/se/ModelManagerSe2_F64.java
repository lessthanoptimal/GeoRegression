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

package georegression.fitting.se;

import georegression.struct.se.Se2_F64;
import org.ddogleg.fitting.modelset.ModelManager;

/**
 * Manages {@link georegression.struct.se.Se2_F64}.
 *
 * @author Peter Abeles
 */
public class ModelManagerSe2_F64 implements ModelManager<Se2_F64> {
	@Override
	public Se2_F64 createModelInstance() {
		return new Se2_F64();
	}

	@Override
	public void copyModel(Se2_F64 src, Se2_F64 dst) {
		dst.set(src);
	}
}
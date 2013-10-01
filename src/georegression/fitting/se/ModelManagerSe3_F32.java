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

import georegression.struct.se.Se3_F32;
import org.ddogleg.fitting.modelset.ModelManager;

/**
 * Manages {@link Se3_F32}.
 *
 * @author Peter Abeles
 */
public class ModelManagerSe3_F32 implements ModelManager<Se3_F32> {
	@Override
	public Se3_F32 createModelInstance() {
		return new Se3_F32();
	}

	@Override
	public void copyModel(Se3_F32 src, Se3_F32 dst) {
		dst.set(src);
	}
}

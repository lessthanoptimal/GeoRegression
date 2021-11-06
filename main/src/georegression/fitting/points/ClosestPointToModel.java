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

package georegression.fitting.points;

import georegression.struct.GeoTuple;
import org.jetbrains.annotations.Nullable;

/**
 * An interface where the closest point to a model is looked up.
 *
 * @author Peter Abeles
 */
public interface ClosestPointToModel<T extends GeoTuple> {

	/**
	 * Searches the model for the closest point to 'target' and if one exists it
	 * is returned.
	 *
	 * @param target The point where the closest point on the model is being searched for
	 * @return Closest model point, if any has been found. null otherwise. The returned point should not
	 * be modified or saved since if the model is updated its value can change.
	 */
	@Nullable T findClosestPoint(T target);
}

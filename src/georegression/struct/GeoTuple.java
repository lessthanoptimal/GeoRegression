/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.struct;

import java.io.Serializable;

/**
 * Describes geometric objects that are composed of N double values.  Where N is the dimension
 * of space the object is contained in.  Points and vectors are two examples of a GeoTuple.  Each
 * value is the value of the object along a dimension in the space it occupies.
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple<T extends GeoTuple> implements Serializable {

	/**
	 * The dimensionality of space the tuple is contained in and the number of values it has.
	 *
	 * @return Dimensionality of the object.
	 */
	public abstract int getDimension();

	/**
	 * <p>
	 * Creates a new tuple instance of the same type.
	 * </p>
	 *
	 * @return new tuple instance.
	 */
	public abstract T createNewInstance();
}

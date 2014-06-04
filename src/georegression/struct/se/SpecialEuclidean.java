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

package georegression.struct.se;

import georegression.struct.InvertibleTransform;

import java.io.Serializable;

/**
 * <p>
 * Special Euclidean or rigid body motion is a transform that preserves the norm and cross product
 * between any two vectors.
 * </p>
 * <p>
 * <UL>
 * <LI> norm: ||se(v)|| = ||v||
 * <LI> cross product: se(u) x se(v) = se(u x v)
 * </UL>
 * where se(.) is a special euclidean transform, and u and v are real vectors of appropriate dimension.
 * </p>
 *
 * @author Peter Abeles
 */
public interface SpecialEuclidean<T extends SpecialEuclidean> extends InvertibleTransform<T> , Serializable {


}

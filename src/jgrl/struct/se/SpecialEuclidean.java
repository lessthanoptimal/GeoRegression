/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.se;

import jgrl.struct.InvertibleTransform;

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
public interface SpecialEuclidean<T extends SpecialEuclidean> extends InvertibleTransform<T> {


}

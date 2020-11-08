/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.geometry.polygon;

/**
 * Three integers that represents indexes in an array;
 *
 * @author Peter Abeles
 */
public class ThreeIndexes {
	/** Indexes in an array */
	public int idx0,idx1,idx2;

	public void set( int idx0, int idx1, int idx2 ) {
		this.idx0 = idx0;
		this.idx1 = idx1;
		this.idx2 = idx2;
	}

	public void reset() {
		idx0 = -1;
		idx1 = -1;
		idx2 = -1;
	}
}
